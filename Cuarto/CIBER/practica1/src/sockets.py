import socket as s

LISTEN_SIZE = 15


#####
# FUNCIÓN: def socket_create
# ARGS_IN: port - El puerto al que hacer bind si es TCP el socket
#          ip - LA ip al que hacer bind si es TCP el socket
# DESCRIPCIÓN: Funcion que se encarga de crear un socket TCP
# ARGS_OUT: socket - es el socket creado
#####
def socket_create(port: int = None, ip: str = None):

    try:
        # Socket TCP
        socket = s.socket(s.AF_INET, s.SOCK_STREAM)
        if ip is not None:

            # Registra las direcciones
            socket.bind((ip, port))

            # Establece la cola de espera
            socket.listen(LISTEN_SIZE)

    except OSError:
        socket.close()
        return None
    return socket


#####
# FUNCIÓN: def socket_connect
# ARGS_IN: socket - El socket con el que nos debemos conectar
#          dst_ip - La direccion del peer destino
#          dst_port - El puerto al que nos deseamos conectar
# DESCRIPCIÓN: Funcion que se encarga de conectar el socket al peer destino
# ARGS_OUT: sock - es el socket utilizado
#####
def socket_connect(socket: s, dst_ip: str, dst_port: int):
    try:
        socket.connect((dst_ip, dst_port))
    except (ConnectionRefusedError, TimeoutError):
        socket.close()
        return None

    return socket


#####
# FUNCIÓN: def socket_accept
# ARGS_IN: socket - El socket
# DESCRIPCIÓN: Funcion que se encarga de aceptar peticiones
# ARGS_OUT: la informacion que devuelve al aceptar
#####
def socket_accept(socket: s):
    return socket.accept()


#####
# FUNCIÓN: def socket_send
# ARGS_IN: socket - El socket
#          data - los datos a enviar
# DESCRIPCIÓN: Funcion que se encarga de enviar un paquete TCP al destinatario
# ARGS_OUT: data - el paquete enviado
#####
def socket_send(socket: s, data: str):
    try:
        socket.send(data.encode())

    except Exception as e:
        return None

    except s.error.errno as e:
        return None

    return None


#####
# FUNCIÓN: def socket_recv
# ARGS_IN: socket - El socket
# DESCRIPCIÓN: Funcion que se encarga de recibir un mensaje TCP. Hemos comprobado que
#              el buffer solo se recibe de 1360 en 1360 bytes, por lo que hay
#              que hacer un bucle para conecatenar los datos
# ARGS_OUT: data - el paquete enviado
#####
def socket_recv(socket: s):
    data = ""

    try:
        while True:
            received_data = socket.recv(1024)
            data += received_data.decode()

            # Se recibieron todos los datos
            if len(received_data) < 1024:
                break

    except BlockingIOError:
        pass

    except Exception as e:
        return "ERROR"

    except s.error.errno as e:
        return "ERROR"

    return data

#####
# FUNCIÓN: def socket_disconnect
# ARGS_IN: socket - El socket
# DESCRIPCIÓN: Funcion que se encarga de cerrar el socket
# ARGS_OUT:
#####


def socket_disconnect(socket: s):
    socket.close()
