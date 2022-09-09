import socket
import random


LISTEN_SIZE = 5

#####
# FUNCIÓN: def socket_get_config
# ARGS_IN:
# DESCRIPCIÓN: Funcion que se encarga de obtener nuestra IP, conectandose primero
#              mediante un socket UDP, a cualquier direccion de Internet, de manera
#              que este socket nos devuelve la IP que ha cogido de nuestro sistema
#              para realizar esa conexion.
#              En caso contrario, nos devuelve el localhost y un puerto random
# ARGS_OUT: IP, port - la IP y el puerto utilizados
#####


def socket_get_config():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.settimeout(0)

    try:
        # Me intento conectar a una red (no tiene por que existir)
        s.connect(('10.0.0.0', 1))
        # Devuelvo la IP que ha utilizado para dicha conexion
        (ip, port) = s.getsockname()
    except:
        # Si no puedo, devuelvo la IP local
        ip = socket.gethostbyname(socket.gethostname())
        port = random.randint(2000, 10000)
    finally:
        s.close()
    return ip, port

#####
# FUNCIÓN: def socket_create
# ARGS_IN: src_port - El puerto al que hacer bind si es TCP el socket
#          UDP - flag si el socket a crear es UDP
# DESCRIPCIÓN: Funcion que se encarga de generar cualquier tipo de socket TCP o
#              UDP u tanto para socket cliente como servidor
# ARGS_OUT: socket - es el socket creado
#####


def socket_create(src_port=None, UDP=False):

    try:
        # Socket UDP
        if UDP is True:
            sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        # Socket TCP
        else:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # Socket receptor(servidor)
        if src_port is not None:
            sock.bind(("", int(src_port)))
            # Solo establece una cola el socket TCP
            if UDP is False:
                sock.listen(LISTEN_SIZE)

    except OSError:
        print("[ERROR] Creacion del socket ")
        sock.close()
        return None
    return sock

#####
# FUNCIÓN: def socket_connect
# ARGS_IN: sock - El socket con el que nos debemos conectar
#          dst_addr - La direccion del peer destino
#          dst_port - El puerto al que nos deseamos conectar
# DESCRIPCIÓN: Funcion que se encarga de conectar el socket al peer destino
# ARGS_OUT: sock - es el socket utilizado
#####


def socket_connect(sock, dst_addr, dst_port):
    try:
        sock.connect((dst_addr, int(dst_port)))
    except (ConnectionRefusedError, TimeoutError):
        print("[ERROR] Conexion con el socket rechazada")
        sock.close()
        return None

    return sock

#####
# FUNCIÓN: def socket_accept
# ARGS_IN: sock - El socket
# DESCRIPCIÓN: Funcion que se encarga de aceptar peticiones
# ARGS_OUT: la informacion que devuelve al aceptar
#####


def socket_accept(sock):
    return sock.accept()

#####
# FUNCIÓN: def socket_send_to
# ARGS_IN: sock - El socket
#          data - datos que quieres enviar
#          dst_addr - la direccion IP del destino
#          dst_port - el puerto destino
# DESCRIPCIÓN: Funcion que se encarga de enviar el paquete UDP al destinatario
# ARGS_OUT:
#####


def socket_send_to(sock, data, dst_addr, dst_port):
    try:
        sock.sendto(bytes(data), (dst_addr, int(dst_port)))
    except OSError:
        print("[ERROR] Mandar datos UDP")

#####
# FUNCIÓN: def socket_recv_from
# ARGS_IN: sock - El socket
# DESCRIPCIÓN: Funcion que se encarga de recibir el paquete UDP al destinatario
# ARGS_OUT: data, dst_addr - los datos recibidos junto la IP y puerto destino
#####


def socket_recv_from(sock):
    try:
        data, dst_addr = sock.recvfrom(20000)
    except OSError:
        print("[ERROR] Mandar datos UDP")
        return None
    return data, dst_addr

#####
# FUNCIÓN: def socket_send
# ARGS_IN: sock - El socket
#          data - los datos a enviar
# DESCRIPCIÓN: Funcion que se encarga de enviar un paquete TCP al destinatario
# ARGS_OUT: data - el paquete enviado
#####


def socket_send(sock, data):
    try:
        sock.send(bytes(data.encode()))
    except OSError:
        print("[ERROR] Mandar datos TCP")
        return None
    return data

#####
# FUNCIÓN: def socket_recv
# ARGS_IN: sock - El socket
# DESCRIPCIÓN: Funcion que se encarga de recibir un paquete TCP. Hemos comprobado que
#              el buffer solo se recibe de 1360 en 1360 bytes, por lo que hay
#              que hacer un bucle para conecatenar los datos
# ARGS_OUT: data - el paquete enviado
#####


def socket_recv(sock):
    empty = False
    data = ""
    try:
        while empty is False:
            packet = sock.recv(1360).decode()
            if len(packet) < 1360:
                empty = True
            data = data + packet

    except OSError:
        print("[ERROR] Recibir datos TCP")
        return None
    return data

#####
# FUNCIÓN: def socket_disconnect
# ARGS_IN: sock - El socket
# DESCRIPCIÓN: Funcion que se encarga de cerrar el socket
# ARGS_OUT:
#####


def socket_disconnect(sock):
    sock.close()

#####
# FUNCIÓN: def socket_comunicate
# ARGS_IN: sock - El socket
#          data - los datos a comunicar
# DESCRIPCIÓN: Funcion que se encarga de enviar y recibir, las dos acciones TCP
#              en una misma
# ARGS_OUT: data recibida o None en caso de error
#####


def socket_comunicate(socket, data):
    if socket_send(socket, data):
        return socket_recv(socket)
    return None
