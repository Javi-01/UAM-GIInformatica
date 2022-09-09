import collections
from . import sockets as s
from PIL import Image, ImageTk
import cv2
import threading
import time
import numpy as np

SCALE = (320, 240)

#####
# CLASS: class UDPTransmition
# ARGS_CLASS: app - Interfaz de usuario
#             tcp_control - Instancia del modulo de la comunicacion TCP
#             UDPPort - puerto del destinatario que desea recibir los paquetes
#             fps - los fps a los que desea el usuario emitir
# DESCRIPCIÓN: Clase que contempla la funcionalidad de la transmision y recepcion de
#              envio de video UDP
#####


class UDPTransmition:

    capture = None
    capture_video = None
    frame_number = 0
    FPS = int()
    buffer = None

    app = None
    tcp_control = None

    call_pause = False
    call_end = False

    sender_socket = None
    receiver_socket = None

    dst_image = None
    src_image = None

    udp_dst_port = None

    def __init__(self, app, tcp_control, UDPPort, fps=20):

        self.app = app
        self.FPS = fps
        self.tcp_control = tcp_control
        self.udp_dst_port = UDPPort

        # Se crean los sockets de envio y de recepcion del usuario
        self.receiver_socket = s.socket_create(
            self.tcp_control.ctrl_get_src_peer_field("port"), UDP=True)
        self.sender_socket = s.socket_create(UDP=True)

        # Si los sokcets no se crean correctamente, se deshace el modulo
        if self.sender_socket is None or self.receiver_socket is None:

            self.app.frame_home_msg_dialog(
                "Error", "Error al comenzar la comunicacion con el usuario")
            self.app.frame_list_users(
                self.tcp_control.ctrl_get_src_peer_field("username"))
            return

        # Creacion del buffer, que esta formado por el numero de FPS a los que
        # se quiere tranmitir * 2 para tener un margen de slots
        self.buffer = collections.deque(maxlen=self.FPS * 2)

        # Creacion de hilos de enviar, recibir(y meter en cola), y procesar
        self.trans_create_threads()

    #####
    # FUNCIÓN: def trans_create_frame
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de capturar el frame, esperar debido a
    #              la tasa de envio que se requiere, y codificarlo al formato binario
    #              para el posterior envio
    # ARGS_OUT: frame - el frame de video que se quiere enviar codificado
    #####

    def trans_create_frame(self):
        # Capturamos un frame de la cámara o del vídeo, y si la variable ret
        # nos devuelve false si no recibe
        ret, cap_frame = self.capture.read()
        if cap_frame is None or ret is False:
            self.call_end.set()
            return None

        # Como se quiere transmitir a x FPS, significa que estoy mandando x frames
        # por segundo, lo que quiere decir que 1 frame se envia cada 1/x segundos,
        # y eso es lo que habria que esperar, como los FPS cambian, el tiempo
        # tambien cambiara de manera dinamica
        time.sleep(float(1/self.FPS))
        self.app.update_status_bar(str(self.FPS))

        # Se envian los paquetes a
        # Redimensionamos el frame a escala que queremos verlo en la gui
        src_frame = cv2.resize(cap_frame, SCALE)
        cv2_im = cv2.cvtColor(src_frame, cv2.COLOR_BGR2RGB)

        self.app.update_src_call_frame(
            ImageTk.PhotoImage(Image.fromarray(cv2_im)))

        # Realizamos la compresion y la dejamos preparada para el posterior envio
        dst_frame = cv2.resize(cap_frame, SCALE)
        result, encimg = cv2.imencode(
            '.jpg', dst_frame, [cv2.IMWRITE_JPEG_QUALITY, 50])
        if result == False:
            print('[Error] - UDP - Codificar imagen')
            return None

        return encimg.tobytes()

    #####
    # FUNCIÓN: def trans_send_frame
    # ARGS_IN: video_frame - frame de video que se quiere enviar
    # DESCRIPCIÓN: Funcion que se encarga de enviar el frame al formato requerido
    #              a traves del socket que envia
    # ARGS_OUT:
    #####
    def trans_send_frame(self, video_frame):
        # Se crea el paquete "Número de orden#Timestamp#Resolución video#FPS#Datos"
        if video_frame:
            data = bytearray(
                f"{self.frame_number}#{time.time()}#{SCALE[0]}x{SCALE[1]}#{self.FPS}#".encode()) + video_frame

            self.frame_number += 1
            # Se envia el datagrama al cliente TCP destino
            s.socket_send_to(self.sender_socket, data, self.tcp_control.ctrl_get_dst_peer_field(
                "address"), self.udp_dst_port)

    #####
    # FUNCIÓN: def trans_process_frame
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de procesar el frame, esto consiste en
    #              sacarlo de la cola, parsearlo y descomprimirlo para sustituirlo
    #              por el frame de imagen de la interfaz
    # ARGS_OUT:
    #####
    def trans_process_frame(self):
        # Si la cola esta vacia, se espera un poco hasta que se llene, normalemente
        # para sirve de 2 a 5 segundos para absorver el posible jitter
        if len(self.buffer) == 0:
            time.sleep(2)
        # Se obtiene el primer elemento por la izquierda
        try:
            frame = self.buffer.popleft()
        except IndexError:
            return None
        # De hace el parseo del datagrama
        data = frame.split(b"#", 4)
        # Se guarda la resolucion
        resolution = data[2].split(b"x")
        resolution = (int(resolution[0]), int(resolution[1]))
        send_time = float(data[1])
        # Se guardan los FPS
        fps = float(data[3])

        # Finalmente el frame del video
        compress_frame = data[4]

        # Se descomprime el frame de video
        descompress_frame = cv2.imdecode(
            np.frombuffer(compress_frame, np.uint8), 1)
        # Se convierte el frame al formato que es enseñable en la gui
        # Redimensionamos primero el frame a escala que queremos verlo en la gui

        dst_frame = cv2.resize(descompress_frame, resolution)
        cv2_im = cv2.cvtColor(dst_frame, cv2.COLOR_BGR2RGB)

        # Guardamos el frame que nos llego al formato del frame del receptor
        # en una variable de clase
        if self.call_pause.is_set():
            self.app.update_dst_call_frame_onPause(
                ImageTk.PhotoImage(Image.fromarray(cv2_im)))
        else:
            self.app.update_dst_call_frame(
                ImageTk.PhotoImage(Image.fromarray(cv2_im)))

        # Si el buffer se vacia mas rapido de lo que se vacia, tenemos un pequeño
        # control de congestion, de manera que si llega a menos del 50%, el tiempo
        # entre frames se ve aumentado para que de tiempo a recuperar
        if len(self.buffer) < self.FPS:
            time.sleep(float(1/(fps * 0.5)))
            self.app.update_status_bar(str(fps * 0.5))
        else:
            time.sleep(float(1/fps))
            self.app.update_status_bar(str(fps))

    #####
    # FUNCIÓN: def trans_video_recv_handler
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga en un thread de recibir en un bucle,
    #              quedarse bloqueado a la espera de un frame, e insertarlo
    #              posteriormente en el buffer
    # ARGS_OUT:
    #####
    def trans_video_recv_handler(self):
        # Mientras que la llamada no haya finalizado
        while self.call_end.is_set() is False:
            # Se reciben paquetes UDP del peer destino
            frame, dst_addr = s.socket_recv_from(self.receiver_socket)
            # Comprobamos que el emisor es el que tenemos registrado, y si no lo es
            # rechazamos el paquete
            if dst_addr[0] != self.tcp_control.ctrl_get_dst_peer_field("address"):
                return None
            # # Si el buffer circular no se encuentra lleno, ponemos el elemento
            if len(self.buffer) < self.buffer.maxlen:
                # Se coge la posicion del frame y se coloca en la misma, de manera
                # que se puedan reproducir de manera ordenada
                frame_num = frame.split(b"#")[0]
                try:
                    self.buffer.insert(int(frame_num), frame)
                except IndexError:
                    # Si no se puede colocar en dicha posicion, perdemos el frame
                    pass

        return

    #####
    # FUNCIÓN: def trans_process_frame_handler
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga en un thread de procesar el frame para
    #              sacarlo del buffer e imprimirlo por pantalla
    # ARGS_OUT:
    #####
    def trans_process_frame_handler(self):
        # Mientras la llamada no se de por finalizada, procesamos los frames que
        # recibimos por otro hilo
        while self.call_end.is_set() is False:
            self.trans_process_frame()
        return

    #####
    # FUNCIÓN: def trans_video_send_handler
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga en un thread de capturar la camara en un
    #              bucle mientras que la llamada no haya finalizado, ademas, si
    #              se selecciona capturar un video, comenzara automaticamente a
    #              mandar el video. Ademas, tambien para el envio si se pausa por
    #              el usuario
    # ARGS_OUT:
    #####
    def trans_video_send_handler(self):
        release_cap = False
        # Se va a capturar la webcam
        self.capture = cv2.VideoCapture(0)
        # Mientras la llamada no se de por finalizada, creo el frame de video
        # y lo envio
        while self.call_end.is_set() is False:
            # Si el usuario selecciona en algun momento mandar un video, se
            # cierra la camara y se empieza a emitir el videos
            if self.capture_video and release_cap is False:
                release_cap = True
                self.capture.release()
                self.capture = cv2.VideoCapture(self.capture_video)

            # Se manda a capturar el frame y enviar mediante UDP al receptor
            video_frame = self.trans_create_frame()
            self.trans_send_frame(video_frame)
            # Si la llamada se pone en pausa, ejecutamos un bucle infinito
            # mientras esta siga en pausa.
            if self.call_pause.is_set():
                while self.call_pause.is_set():
                    # Adicionalmente, si se para mientras esta en pausa,
                    # salimos del bucle y posteriormente terminara el segundo bucle
                    if self.call_end.is_set():
                        break
        # Se eliminan todos los datos TCP y UDP sobre la conexion
        self.trans_video_finalize()

    #####
    # FUNCIÓN: def trans_video_finalize
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de finalizar todas las variables de
    #              la llamada, sockets UDP y captura de video
    # ARGS_OUT:
    #####
    def trans_video_finalize(self):
        # Cierre de sockets UDP
        s.socket_disconnect(self.sender_socket)
        s.socket_disconnect(self.receiver_socket)
        self.sender_socket = None
        self.receiver_socket = None
        # Cierre de socket TCP
        s.socket_disconnect(self.tcp_control.socket_conn)
        self.tcp_control.socket_conn = None
        # Peer destino borrado
        self.tcp_control.dst_peer = {}
        # Se libera la captura de video
        if self.capture:
            self.capture.release()

    #####
    # FUNCIÓN: def trans_create_threads
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de crear los hilos de captura y envio,
    #              recepcion y procesamiento
    # ARGS_OUT:
    #####
    def trans_create_threads(self):
        # Variables para manejar eventos de pausa y finalizacion de llamada, de
        # manera que estos funcionan con una variable flag interna que para y
        # habilita los threads que estan siendo ejecutados.
        self.call_pause = threading.Event()
        self.call_end = threading.Event()

        self.thread_sender = threading.Thread(
            target=self.trans_video_send_handler)
        self.thread_sender.start()
        self.thread_receiver = threading.Thread(
            target=self.trans_video_recv_handler)
        self.thread_receiver.start()
        self.thread_process = threading.Thread(
            target=self.trans_process_frame_handler)
        self.thread_process.start()
