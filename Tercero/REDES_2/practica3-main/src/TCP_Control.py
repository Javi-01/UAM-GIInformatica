from . import sockets as s, UDP_Transmition as tr
import threading

#####
# CLASS: class TCPControl
# ARGS_CLASS: src_peer - los datos del usuario para el que se ha creado esta instancia
#             app - la interfaz grafica
#             ds_server - la instancia del servidor de descubrimiento
# DESCRIPCIÓN: Clase que contempla la funcionalidad de la conexion de control
#              siguiendo el protocolo descrito en la practica para mandar comandos
#####


class TCPControl:

    tcp_conn = False

    server_socket = None
    client_socket = None
    socket_conn = None

    src_peer = {}
    dst_peer = {}

    ds_server = None
    udp_transmition = None
    app = None

    def __init__(self, src_peer, app, ds_server):
        # Se guardan los campos del peer
        self.ctrl_set_peer(src_peer)

        self.app = app
        self.ds_server = ds_server

        self.server_socket = s.socket_create(self.src_peer["port"])
        if self.server_socket is None:
            return

        # Se crea un hilo que se mantenga a la espera de llamadas
        thread_recv = threading.Thread(
            target=self.ctrl_recv_TCP_connection)
        thread_recv.setDaemon(True)
        thread_recv.start()

    #####
    # FUNCIÓN: def ctrl_call_peer
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de llamar al usuario y quedarse a la
    #              espera de la respuesta, y mandar a procesar dicha respuesta
    # ARGS_OUT:
    #####
    def ctrl_call_peer(self):

        # Se manda el mensaje de llamada, y se espera respuesta
        response = s.socket_comunicate(self.socket_conn, "CALLING " +
                                       self.src_peer["username"] + " " + self.src_peer["port"])
        # Cuando hay respuesta, se manda a analizar dicha
        if response:
            self.ctrl_process_call(response)
        else:
            self.app.frame_list_users(self.src_peer["username"])

    #####
    # FUNCIÓN: def ctrl_call_hold
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de enviar que el usuario desea pausar
    #              la reproduccion de video
    # ARGS_OUT:
    #####
    def ctrl_call_hold(self):
        # Si no hay canal de transmision, no se envia el comando
        if self.udp_transmition:
            if self.udp_transmition.call_pause.is_set() is False:
                s.socket_send(self.socket_conn, "CALL_HOLD " +
                              self.src_peer["username"])
                self.udp_transmition.call_pause.set()

    #####
    # FUNCIÓN: def ctrl_call_resume
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de enviar que el usuario que desea
    #              reanudar la reproduccion de video
    # ARGS_OUT:
    #####

    def ctrl_call_resume(self):
        # Si no hay canal de transmision, no se envia el comando
        if self.udp_transmition:
            if self.udp_transmition.call_pause.is_set():
                s.socket_send(self.socket_conn, "CALL_RESUME " +
                              self.src_peer["username"])
                self.udp_transmition.call_pause.clear()

    #####
    # FUNCIÓN: def ctrl_call_end
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de enviar que el usuario que desea
    #              finalizar la transmision de video
    # ARGS_OUT:
    #####

    def ctrl_call_end(self):
        # Si no hay canal de transmision, no se envia el comando
        if self.udp_transmition:
            if self.udp_transmition.call_end.is_set() is False:
                s.socket_send(self.socket_conn, "CALL_END " +
                              self.src_peer["username"])
                self.udp_transmition.call_end.set()

    #####
    # FUNCIÓN: def ctrl_call_accept
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de enviar la aceptacion de la llamada
    # ARGS_OUT:
    #####

    def ctrl_call_accept(self):
        s.socket_send(self.socket_conn, "CALL_ACCEPTED " +
                      self.src_peer["username"] + " " + self.src_peer["port"])

    #####
    # FUNCIÓN: def ctrl_call_deny
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de enviar un rechazo de la llamada
    # ARGS_OUT:
    #####

    def ctrl_call_deny(self):
        s.socket_send(self.socket_conn, "CALL_DENY " +
                      self.src_peer["username"])

    #####
    # FUNCIÓN: def ctrl_call_busy
    # ARGS_IN: socket - el socket al usuario al que se debe enviar que estamos
    #          ocupados, ya que este todavia no ha sido guardado como socket de
    #          conexion, porque ya tenemos una llamada activa
    # DESCRIPCIÓN: Funcion que se encarga de enviar que el usuario esta ocupado
    # ARGS_OUT:
    #####

    def ctrl_call_busy(self, socket):
        s.socket_send(socket, "CALL_BUSY")

    #####
    # FUNCIÓN: def ctrl_process_request
    # ARGS_IN: request - la peticion del usuario que recibimos
    #          socket - el socket de conexion
    # DESCRIPCIÓN: Funcion que se encarga de procesar la peticion del usuario
    #              y dependiendo de lo que desee hacer ante nosotros,
    #              actuamos de una manera u otra
    # ARGS_OUT:
    #####

    def ctrl_process_request(self, request: str, socket):
        request = request.split(" ")

        if request[0] == "CALLING":
            # Si recibo una llamada, compruebo si ya tengo una conexion (llamada) establecida
            if self.socket_conn is None:
                # En el caso de no tener llamada, se guarda el socket y se manda a
                # contruir la llamada
                self.socket_conn = socket
                self.app.frame_entry_call(request[1], request[2])
            else:
                # En caso de si tener una llamada, se envia un ocupado
                self.ctrl_call_busy(socket)

        elif request[0] == "CALL_HOLD":
            # Se manda a contruir el frame de llamada parada si el usuario es el
            # que dice ser
            if request[1] == self.dst_peer["username"]:
                # Se cambia la variable de pausa
                pass
        elif request[0] == "CALL_RESUME":
            # Se manda a contruir el frame de llamada normal si el usuario es el
            # que dice ser
            if request[1] == self.dst_peer["username"]:
                # Se cambia la variable de pausa
                pass
        elif request[0] == "CALL_END":
            # Se manda a contruir el frame de home si el usuario es el
            # que dice ser
            if request[1] == self.dst_peer["username"]:
                # Es estblece el evento de parada a True
                self.udp_transmition.call_end.set()

                # Se muestra un mensaje de llamada finalizada y se devuelve
                # al home page
                self.app.frame_home_msg_dialog(
                    "Advertencia", "La llamada ha sido finalizada")
                self.app.frame_list_users(
                    self.ctrl_get_src_peer_field("username"))
        else:
            print("[ERROR] Petición desconocida - TCP Control")

    #####
    # FUNCIÓN: def ctrl_process_call
    # ARGS_IN: response - la respuesta a la llamada
    # DESCRIPCIÓN: Funcion que se encarga de procesar la respuesta recibida del
    #              usuario para ver que tiene que hacer tras haber llamado a otro
    #              usuario
    # ARGS_OUT:
    #####
    def ctrl_process_call(self, response: str):
        response = response.split(" ")
        if response[0] == "CALL_ACCEPTED":
            # Se lanza un thread para el manejo de recepcion de mensajes de control
            tcp_thread = threading.Thread(target=self.ctrl_TCP_comunicate)
            tcp_thread.start()
            # Se lanza la interfaz de llamada
            self.app.frame_active_call(self.ctrl_get_src_peer_field(
                "username"), self.ctrl_get_dst_peer_field("username"))
            # Se inicializa la transmision UDP
            self.udp_transmition = None
            self.udp_transmition = tr.UDPTransmition(
                self.app, self, int(response[2]), int(self.app.frame_get_FPS()))

        elif response[0] == "CALL_DENY":
            # Se manda un mensaje de llamada rechazada
            self.app.frame_home_msg_dialog(
                "Advertencia", "El usuario " + response[1] + " ha rechazado la llamada")
            # Se borran los datos del usuario destino, y el socket de conexion
            self.dst_peer = {}
            s.socket_disconnect(self.socket_conn)
            self.socket_conn = None
            # Se vuelve al home
            self.app.frame_list_users(self.src_peer["username"])

        elif response[0] == "CALL_BUSY":
            # Se manda un mensaje de usuario ocupado
            self.app.frame_home_msg_dialog(
                "Advertencia", "El usuario que desea llamar se encuentra ocupado")
            # Se borran los datos del usuario destino, y el socket de conexion
            self.dst_peer = {}
            s.socket_disconnect(self.socket_conn)
            self.socket_conn = None
            # Se vuelve al home
            self.app.frame_list_users(self.src_peer["username"])

        else:
            self.app.frame_list_users(self.src_peer["username"])

    #####
    # FUNCIÓN: def ctrl_recv_TCP_conection
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de conexiones TCP de otros clientes, de manera
    #              que cuando acepta una peticion, espera a recibir el mensaje
    #              del peer
    # ARGS_OUT:
    #####

    def ctrl_recv_TCP_connection(self):

        while self:
            socket_conn, _ = s.socket_accept(self.server_socket)
            data = s.socket_recv(socket_conn)
            if data:
                print("[OK] Conexion recibida - TCP Control", data)

                # Se manda a procesar la peticion
                self.ctrl_process_request(data, socket_conn)

        s.socket_disconnect(self.server_socket)

    #####
    # FUNCIÓN: def ctrl_TCP_comunicate
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes de control del otro peer para ver
    #              y procesar los comandos de control
    # ARGS_OUT:
    #####

    def ctrl_TCP_comunicate(self):
        # Me mantengo a la espera de mensajes de control
        while self.socket_conn:
            # Me mantengo a la espera de mensajes de control
            data = s.socket_recv(self.socket_conn)
            if data:
                # Se manda a procesar la peticion
                self.ctrl_process_request(data, self.socket_conn)
        return

    #####
    # FUNCIÓN: def ctrl_TCP_connect
    # ARGS_IN: dst_peer - los datos del usuario destino
    # DESCRIPCIÓN: Funcion que se encarga de conectar a otro par del cual sabe su
    #              IP y puerto, abriendo un socket y conectando, de manera que
    #              guarda hasta su respuesta los campos en el dst_peer
    # ARGS_OUT: None en caso de que no se pueda conectar
    #####

    def ctrl_TCP_connect(self, dst_peer):
        # Se crea un socket de cliente y se conecta al otro peer, se establce un
        # timeout de 10 segundos para que coja la llamada
        if dst_peer is None:
            return

        client_socket = s.socket_create()
        client_socket.settimeout(10)
        print(dst_peer)

        if s.socket_connect(client_socket, dst_peer["address"], dst_peer["port"]) is None:
            s.socket_disconnect(client_socket)
            return None
        print("[OK] Conexion establecida - TCP Control", dst_peer["port"])
        # Se guarda como socket de conexion el usado por el cliente
        self.socket_conn = client_socket
        # Se guardan los datos de manera temporal del usuario destino de la conexion
        self.ctrl_set_peer(dst_peer, dst=True)
        return dst_peer

    #####
    # FUNCIÓN: def ctrl_set_peer
    # ARGS_IN: user - los datos del usuario
    #          dst - flag para saber si se guardan en el usuario dst o src
    # DESCRIPCIÓN: Funcion que se encarga de gurdar los campos del usuario al
    #              formato establecido en nuestra variable de clase peer
    # ARGS_OUT:
    #####
    def ctrl_set_peer(self, user, dst=False):
        if dst is True:
            self.dst_peer["username"] = user["username"]
            self.dst_peer["address"] = user["address"]
            self.dst_peer["port"] = user["port"]
            self.dst_peer["version"] = user["version"]
        else:
            self.src_peer["username"] = user["username"]
            self.src_peer["address"] = user["address"]
            self.src_peer["port"] = user["port"]
            self.src_peer["version"] = user["version"]

    #####
    # FUNCIÓN: def ctrl_get_dst_peer_field
    # ARGS_IN: field - el campo a buscar
    # DESCRIPCIÓN: Funcion que se encarga de obtener uno de los campos del usuario
    #              destino de solicitado por el usuario
    # ARGS_OUT: None en caso de no haber o el nombre del campo
    #####
    def ctrl_get_dst_peer_field(self, field):
        return self.dst_peer[field] if field in self.dst_peer else None

    #####
    # FUNCIÓN: def ctrl_get_src_peer_field
    # ARGS_IN: field - el campo a buscar
    # DESCRIPCIÓN: Funcion que se encarga de obtener uno de los campos del usuario
    #              emisor de solicitado por el usuario
    # ARGS_OUT: None en caso de no haber o el nombre del campo
    #####
    def ctrl_get_src_peer_field(self, field):
        return self.src_peer[field] if field in self.src_peer else None
