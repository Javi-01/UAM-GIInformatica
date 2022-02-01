from ip import *
from threading import Lock
import struct

ICMP_PROTO = 1

ICMP_ECHO_REQUEST_TYPE = 8
ICMP_ECHO_REPLY_TYPE = 0

timeLock = Lock()
icmp_send_times = {}

def process_ICMP_message(us,header,data,srcIp):
    '''
        Nombre: process_ICMP_message
        Descripción: Esta función procesa un mensaje ICMP. Esta función se ejecutará por cada datagrama IP que contenga
        un 1 en el campo protocolo de IP
        Esta función debe realizar, al menos, las siguientes tareas:
            -Calcular el checksum de ICMP:
                -Si es distinto de 0 el checksum es incorrecto y se deja de procesar el mensaje
            -Extraer campos tipo y código de la cabecera ICMP
            -Loggear (con logging.debug) el valor de tipo y código
            -Si el tipo es ICMP_ECHO_REQUEST_TYPE:
                -Generar un mensaje de tipo ICMP_ECHO_REPLY como respuesta. Este mensaje debe contener
                los datos recibidos en el ECHO_REQUEST. Es decir, "rebotamos" los datos que nos llegan.
                -Enviar el mensaje usando la función sendICMPMessage
            -Si el tipo es ICMP_ECHO_REPLY_TYPE:
                -Extraer del diccionario icmp_send_times el valor de tiempo de envío usando como clave los campos srcIP e icmp_id e icmp_seqnum
                contenidos en el mensaje ICMP. Restar el tiempo de envio extraído con el tiempo de recepción (contenido en la estructura pcap_pkthdr)
                -Se debe proteger el acceso al diccionario de tiempos usando la variable timeLock
                -Mostrar por pantalla la resta. Este valor será una estimación del RTT
            -Si es otro tipo:
                -No hacer nada

        Argumentos:
            -us: son los datos de usuarios pasados por pcap_loop (en nuestro caso este valor será siempre None)
            -header: estructura pcap_pkthdr que contiene los campos len, caplen y ts.
            -data: array de bytes con el conenido del mensaje ICMP
            -srcIP: dirección IP que ha enviado el datagrama actual.
        Retorno: Ninguno
    '''

    # Comprovamos el valor del checksumn para saber si descartar o no el mensaje
    checksum_value = chksum(data)
    if checksum_value != 0:
        return

    # Extraemos los campos type y code de la cabecera
    # y los loggeamos
    type = data[0]
    code = data[1]

    logging.debug(f"Loggeo del valor del type de la cabecera ICMP: {type}")
    logging.debug(f"Loggeo del valor del code de la cabecera ICMP: {code}")

    if type == ICMP_ECHO_REQUEST_TYPE:
        # Enviamos el mensaje de tipo ICMP_ECHO_REPLY
        sendICMPMessage(data[8:], ICMP_ECHO_REPLY_TYPE, code,
                        struct.unpack("!H", data[4:6])[0],
                        struct.unpack("!H", data[6:8])[0],
                        struct.unpack("!I", srcIp)[0])

    elif type == ICMP_ECHO_REPLY_TYPE:
        # Creamos la clave del diccionario icmp_send_times
        key = struct.unpack("!I", srcIp)[0]
        key += struct.unpack("!H", data[4:6])[0]
        key += struct.unpack("!H", data[6:8])[0]

        # Extrameos el tiempo de envío usando semáforos
        # para proteger las variables
        timeLock.acquire()
        send_time = icmp_send_times[key]
        timeLock.release()

        # Obtenemos el tiempo de recepción y le restamos el tiempo de envio
        extimated_rtt = header.ts.tv_sec - send_time
        logging.debug(f"Loggeo del valor estimado del rtt: {extimated_rtt}")

    return

def sendICMPMessage(data,type,code,icmp_id,icmp_seqnum,dstIP):
    '''
        Nombre: sendICMPMessage
        Descripción: Esta función construye un mensaje ICMP y lo envía.
        Esta función debe realizar, al menos, las siguientes tareas:
            -Si el campo type es ICMP_ECHO_REQUEST_TYPE o ICMP_ECHO_REPLY_TYPE:
                -Construir la cabecera ICMP
                -Añadir los datos al mensaje ICMP
                -Calcular el checksum y añadirlo al mensaje donde corresponda
                -Si type es ICMP_ECHO_REQUEST_TYPE
                    -Guardar el tiempo de envío (llamando a time.time()) en el diccionario icmp_send_times
                    usando como clave el valor de dstIp+icmp_id+icmp_seqnum
                    -Se debe proteger al acceso al diccionario usando la variable timeLock

                -Llamar a sendIPDatagram para enviar el mensaje ICMP

            -Si no:
                -Tipo no soportado. Se devuelve False

        Argumentos:
            -data: array de bytes con los datos a incluir como payload en el mensaje ICMP
            -type: valor del campo tipo de ICMP
            -code: valor del campo code de ICMP
            -icmp_id: entero que contiene el valor del campo ID de ICMP a enviar
            -icmp_seqnum: entero que contiene el valor del campo Seqnum de ICMP a enviar
            -dstIP: entero de 32 bits con la IP destino del mensaje ICMP
        Retorno: True o False en función de si se ha enviado el mensaje correctamente o no

    '''
    message = bytes()

    # Comprobamos el valor del type recibido
    if type == ICMP_ECHO_REQUEST_TYPE or type == ICMP_ECHO_REPLY_TYPE:
        ICMP_header = bytes()
        ICMP_header += bytes(type)
        ICMP_header += bytes(code)
        ICMP_header += bytes([0x0]*2)
        ICMP_header += struct.pack("!H", icmp_id)
        ICMP_header += struct.pack("!H", icmp_seqnum)

        # Comprobaciones del campo checksum
        if (len(ICMP_header) + len(data)) % 2 != 0:
            ICMP_header += bytes(0)

        message = ICMP_header
        message += data
        message[2:4] = struct.pack('<H', chksum(ICMP_header))

        # Guardamos el tiempo de envio en el diccionario icmp_send_times
        if type == ICMP_ECHO_REQUEST_TYPE:
            # Creamos la clave del diccionario icmp_send_times
            key = dstIP
            key += icmp_id
            key += icmp_seqnum

            # Protegemos el acceso al diccionario
            timeLock.acquire()
            icmp_send_times[key] = time.time()
            timeLock.release()

        # Enviamos el mensaje ICMP llamando a sendIPDatagram
        ret = sendIPDatagram(dstIP, message, ICMP_PROTO)
        if ret == True:
            return True

        return False

    return False

def initICMP():
    '''
        Nombre: initICMP
        Descripción: Esta función inicializa el nivel ICMP
        Esta función debe realizar, al menos, las siguientes tareas:
            -Registrar (llamando a registerIPProtocol) la función process_ICMP_message con el valor de protocolo 1

        Argumentos:
            -Ninguno
        Retorno: Ninguno
    '''

    registerIPProtocol(process_ICMP_message, ICMP_PROTO)
    return