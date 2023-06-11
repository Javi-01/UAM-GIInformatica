from src import api_endpoints as ds_api
from src import encryption as ec
from src import sockets as s
import threading
import pickle
import time
import os
import signal


class ET:

    logged_in = False
    running = True
    threads = []

    server_socket = None
    dst_conn_peers = []

    et_instance = {}
    aes_key = None

    # Campos temporales
    public_key = None
    msg = None
    filename = None

    def __init__(self):
        pass

    # -------------------- FUNCIONES DE MENÚ --------------------

    #####
    # FUNCIÓN: def et_link()
    # ARGS_IN: et_name - nombre de la ET
    #          et_password - contraseña de la ET
    #          listen_port - puerto servidor de la ET
    #          ip - ip de la ET
    #          public_key - clave publica de la ET
    # DESCRIPCIÓN: Registra una ET en la BO y lanza un puerto a la escucha de peticiones
    # ARGS_OUT:
    #####
    def et_link(self, et_name: str, et_password: str):

        # Generamos las claves de la ET
        self.public_key, private_key, secret_code = ec.generate_RSAkeys()

        res = ds_api.get_bo_info()
        bo = res.json()

        if res.status_code in [200]:

            # Guardar la instancia del dron en la clase
            self.et_set_fields(
                et_name,
                et_password,
                private_key,
                secret_code
            )

            bo["type"] = "BO"

            # Me conecto a la bo
            if self._et_to_bo_or_et(bo) == False:
                print("\nLa BO no se encuentra operativa\n")
                del self.et_instance["name"]
                os.kill(os.getpid(), signal.SIGINT)

        else:
            print("\nERROR: La BO no existe\n")

    #####
    # FUNCIÓN: def et_login()
    # ARGS_IN: et_name - nombre de la ET
    #          et_password - contraseña de la ET
    # DESCRIPCIÓN: Logea una ET en la BO y lanza un puerto a la escucha de peticiones
    # ARGS_OUT:
    #####
    def et_login(self, et_name: str, et_password: str):

        # Comprobamos si dicha et existe
        if os.path.exists(f"src/pickles/{et_name}.pickle") == False:
            print("\nLa ET {et_name} no se encuentra registrada\n")
            return

        # Recoger la instancia del pickle
        picke_filename = f'src/pickles/{et_name}.pickle'

        # Deserializamos el objeto desde el archivo
        with open(picke_filename, "rb") as file:
            data = pickle.load(file)

        et_instance_data = data

        if et_instance_data['password'] != et_password:
            print("\nContraseña incorrecta para la ET", et_name)
            return

        # Guardar la instancia del ET en la clase
        self.et_set_fields(
            et_instance_data['name'],
            et_instance_data['password'],
            et_instance_data['private_key'],
            et_instance_data['secret_code'],
            et_instance_data['ip'],
            et_instance_data['listen_port']
        )

        print(f"\nBienvenido de nuevo {self.et_instance['name']}\n")

        res = ds_api.get_bo_info()
        bo = res.json()

        if res.status_code in [200]:
            self.logged_in = True

            bo["type"] = "BO"

            # Me conecto a la bo
            if self._et_to_bo_or_et(bo) == False:
                print("\nLa BO no se encuentra operativa\n")
                del self.et_instance["name"]
                os.kill(os.getpid(), signal.SIGINT)

            # Lanzamos un hilo de recepcion de mensajes
            self._et_create_server_socket()

        else:
            print("\nERROR: La BO no existe\n")

    #####
    # FUNCIÓN: def dr_logout()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Cierra la conexion y guarda la instancia de una ET
    # ARGS_OUT:
    #####
    def et_logout(self):

        if 'name' in self.et_instance.keys():
            print("\nGuardando la instancia de la ET",
                  self.et_instance['name'], "\n")

            # Creamos una tupla con los datos a guardar
            data = (self.et_instance)

            picke_filename = f'src/pickles/{self.et_instance["name"]}.pickle'

            # Serializamos el objeto y lo guardamos en un archivo
            with open(picke_filename, "wb") as file:
                pickle.dump(data, file)

            for peer in self.dst_conn_peers:
                self._et_disconnect_handler(peer)

            if self.server_socket is not None:
                s.socket_disconnect(self.server_socket)

        return

    #####
    # FUNCIÓN: def et_unlink_bo()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Elimina el link de una ET a la BO
    # ARGS_OUT:
    #####
    def et_unlink_bo(self):
        # Comprueba que haya un dron en esta instancia de clase
        peer = self.et_get_peer_byName("bo")

        if peer is not None:
            self._et_unlink_handler(
                peer["socket"], peer["aes_key"], "ET", self.et_instance["name"])

    #####
    # FUNCIÓN: def et_fly_drone()
    # ARGS_IN: dr_name - nombre del dron que queremos mandar a volar
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar volar al dron con un dron
    #              de los que estamos conectados
    # ARGS_OUT:
    #####
    def et_fly_drone(self, dr_name: str):

        peer = self.et_get_peer_byName(dr_name)

        # Se debe comprobar que estamos en conexion con un dron, y que este
        # se encuentra aterrizado
        if peer is None:
            print(f"\nNo estas conectado al dron {dr_name}\n")
            return

        # Si el dron esta volando, no se puede mandar a volar
        if peer['fly_status'] == True:
            print(f"\nEl dron {dr_name} ya se encuentra en el aire\n")
            return

        # Ciframos el comando de FLY junto con el mensaje
        # y obtenemos el nonce de cifrado
        msg_enc, nonce = ec.prepare_msg_to_sent(
            peer["aes_key"], ["FLY", self.et_instance['name']])

        # Enviamos mensaje de FLY cifrado a la ET
        # indicandole que tiene un mensaje
        s.socket_send(peer["socket"],
                      f"{msg_enc} {nonce}")

    #####
    # FUNCIÓN: def et_land_drone()
    # ARGS_IN: dr_name - nombre del dron a aterrizar
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar aterrizar al dron con el cual estamos
    #              conectados
    # ARGS_OUT:
    #####
    def et_land_drone(self, dr_name: str):

        peer = self.et_get_peer_byName(dr_name)

        # Se debe comprobar que estamos en conexion con un dron, y que este
        # se encuentra volando
        if peer is None:
            print(f"\nNo estas conectado al dron {dr_name}\n")
            return

        # Si el dron esta en el suelo, no se manda a aterrizar
        if peer['fly_status'] == False:
            print(f"\nEl dron {dr_name} ya se encuentra en el suelo\n")
            return

        # Ciframos el comando de LAND junto con el mensaje
        # y obtenemos el nonce de cifrado
        msg_enc, nonce = ec.prepare_msg_to_sent(
            peer["aes_key"], ["LAND", self.et_instance['name']])

        # Enviamos mensaje de LAND cifrado a la ET
        # indicandole que tiene un mensaje
        s.socket_send(peer["socket"],
                      f"{msg_enc} {nonce}")

    #####
    # FUNCIÓN: def et_send_msg()
    # ARGS_IN: name - nombre de la ET o BO
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar mensaje a una ET o BO
    # ARGS_OUT:
    #####
    def et_send_msg(self, name: str, type: str, msg: str):

        # Comprobamos si la entidad receptora no es ella misma,
        # en caso de serlo no dejaremos enviar el mensaje
        if name == self.et_instance['name']:
            print(f"\nNo es posible enviarse un mensaje a si mismo\n")
            return

        peer = self.et_get_peer_byName(name)

        # Si se quiere enviar a la BO...
        if type == "2":
            if peer is None:
                print(
                    f"\nLa BO no está operativa\n")
                return

            self._et_msg_handler(peer, name, msg)

        # En caso de querer enviar a una ET...
        else:
            # Comprobamos si ya estamos conecatdos a dicha ET
            if peer is None:
                self.msg = msg

                # Pedirle a la BO la info de dicha ET y establecer luego la conex
                peer = self.et_get_peer_byName("bo")
                self._et_who_et_handler(peer, name, "MSG")
            else:
                self._et_msg_handler(peer, name, msg)

    #####
    # FUNCIÓN: def et_send_msg()
    # ARGS_IN: name - nombre de la ET o BO
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar un file a una ET o BO
    # ARGS_OUT:
    #####
    def et_send_file(self, name: str, type: str, filename: str):

        # Comprobamos si la entidad receptora no es ella misma,
        # en caso de serlo no dejaremos enviar el fichero
        if name == self.et_instance['name']:
            print(f"\nNo es posible enviarse un fichero a si mismo\n")
            return

        # Comprobamos si el fichero existe
        if os.path.exists(f"documents/{filename}") == False:
            print("\nEl archivo no existe\n")
            return

        peer = self.et_get_peer_byName(name)

        # Si se quiere enviar a la BO...
        if type == "2":

            if peer is None:
                print(
                    f"\nLa BO no está operativa\n")
                return

            self._et_file_handler(peer, name, filename)

        # En caso de querer enviar a una ET...
        else:

            # Comprobamos si ya estamos conecatdos a dicha ET
            if peer is None:
                self.filename = filename

                # Pedirle a la BO la info de dicha ET y establecer luego la conex
                peer = self.et_get_peer_byName("bo")
                self._et_who_et_handler(peer, name, "FILE")
            else:
                self._et_file_handler(peer, name, filename)

    # -------------------- FUNCIONES COMO PEER DE CONEXIÓN --------------------

    #####
    # FUNCIÓN: def _et_create_server_socket()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Crear un socket de escucha y lanza en un hilo aparte una cola
    #              de recepcion
    # ARGS_OUT:
    #####
    def _et_create_server_socket(self):

        # Comprueba que haya una et en esta instancia de clase
        if 'listen_port' in self.et_instance.keys():
            # Creamos el server socket con IP y puerto y creamos la cola (Listen)
            self.server_socket = s.socket_create(
                self.et_instance['listen_port'],
                self.et_instance['ip']
            )
            if self.server_socket is None:
                return
            else:
                # Se lanza el Thread de recpcion en un hilo secundario
                thread_rcv = threading.Thread(
                    target=self._et_recv_connection_handler)
                thread_rcv.setDaemon(True)
                thread_rcv.start()
        else:
            print("\nTodavia no estas registrado o logueado\n")

    #####
    # FUNCIÓN: def _et_recv_connection_handler()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de recibir peticiones de los Drones que quieren
    #              conectarse, o de la BO
    # ARGS_OUT:
    #####
    def _et_recv_connection_handler(self):

        while self:
            # Aceptamos las peticiones y recibimos mensajes (El accept crea un nuevo socket)
            socket_conn, _ = s.socket_accept(self.server_socket)
            data = s.socket_recv(socket_conn)

            # Si hay mensaje, se manda a procesar
            if data:
                self._et_process_request(data, socket_conn)

    #####
    # FUNCIÓN: def _et_comunicate
    # ARGS_IN: scket - el socket de comunicacion para un peer
    #
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes del otro peer con el cual tiene una
    #              conexion establecida
    # ARGS_OUT:
    #####
    def _et_comunicate(self, socket, aes_key):

        # Se establece un tiempo de espera infinito
        socket.settimeout(None)

        # Me mantengo a la espera de mensajes en el socket de conexion
        while self.running:
            data = s.socket_recv(socket)
            # Si recibimos error cerramos la conexion
            if data == "ERROR":
                socket = None
            elif data:
                # Se manda a procesar la peticion
                self._et_process_request(data, socket, aes_key)
        return

    #####
    # FUNCIÓN: def _et_process_request()
    # ARGS_IN: data - cadena con el comando recibido
    #          socket - el socket de conexion temporal creado para la conexion
    #
    # DESCRIPCIÓN: Funcion que se encarga de actuar en consecuencia del comando
    #              recibido
    # ARGS_OUT:
    #####
    def _et_process_request(self, data: str, socket, aes_key=None):
        # Parseo el mensaje
        request = data.split(" ")

        # Si el comando no esta cifrado quiere decir que es una peticion de hello
        # inicial donde debo obtener la simetrica de sesion con mi privada
        if 'HELLO_BY' in data:
            new_dst_peer = {}
            # Me quedo con el nombre de la entidad que me manda el hello
            type = request[0].split(":")[1]

            if "ET" in type:
                aes_key = ec.prepare_aes_key_recived(
                    self.et_instance['private_key'], self.et_instance['secret_code'], request[2])

                new_dst_peer = {
                    'name': request[1],
                    'socket': socket,
                    'aes_key': aes_key
                }
                self.dst_conn_peers.append(new_dst_peer)

            else:
                # Se comprueba si ya tenemos una instancia de ese dron
                drone = self.et_get_peer_byName(request[1])
                # Se crea una clave de sesion y el mensaje cifrado con la
                # publica del dron
                aes_key, msg_encrypted = ec.prepare_aes_key_to_sent(request[2])
                if drone is None:

                    new_dst_peer = {
                        'name': request[1],
                        'socket': socket,
                        'aes_key': aes_key
                    }
                    self.dst_conn_peers.append(new_dst_peer)

                else:
                    # Si hay dron, lo que hacemos es cambiar el socket y la clave
                    # de sesion
                    self.et_set_peer_field_byName(
                        drone['name'], 'socket', socket)
                    self.et_set_peer_field_byName(
                        drone['name'], 'aes_key', aes_key)
                    new_dst_peer = drone

            self._et_accept_conn(new_dst_peer)

            # Si el hello lo envia una ET
            if "ET" in type:
                split = type.split("-")
                self._et_hello_ok_et_handler(new_dst_peer, split[1])
            else:
                # Enviamos hello OK al dron
                self._et_hello_ok_dr_handler(
                    new_dst_peer, msg_encrypted)

        elif 'HELLO_OK' in data:

            # HELLO OK eniado por la BO
            if self.public_key is not None:
                self._et_link_handler(
                    socket, aes_key, "ET", self.et_instance["name"], self.et_instance["password"], self.public_key)
                self.public_key = None

            # HELLO OK eniado por otra ET
            elif len(request) > 1:

                type = request[1]

                # Mandamos MSG
                peer = self.et_get_peer_byName(request[2])
                if type == "MSG":
                    self._et_msg_handler(peer, request[2], self.msg)
                    self.msg = None

                # Mandamos un file
                else:
                    self._et_file_handler(peer, request[2], self.filename)
                    self.msg = None

        # con la clave simetrica de sesion generada previcamente en el hello
        # En caso de que el comando recibido este cifrado deberá de descrifrarlo
        else:
            # Obtenemos el comando y la info restante del mensaje
            command, info = ec.prepare_msg_recived(
                aes_key, request[1], request[0])
            try:
                command, type = command.split(":")
            except Exception:
                pass

            # Si recibe un LINK del dron, lo manda a la BO
            if 'LINK' == command:
                # Obtenemos la informacion a partir del nombre del dron
                peer = self.et_get_peer_byName("bo")

                # Se manda ahora un Link a la BO
                self._et_link_handler(
                    peer['socket'], peer['aes_key'], "DR", info[0], info[1], info[2]
                )

            if 'LINK_OK' == command:

                if type == 'ET':
                    self.logged_in = True

                    self.et_instance['ip'] = info[1]
                    self.et_instance['listen_port'] = int(info[2])

                    # Lanzamos un hilo de recepcion de mensajes
                    self._et_create_server_socket()

                if type == 'DR':

                    # Se obtiene el peer a partir del nombre
                    peer = self.et_get_peer_byName(info[0])
                    # Se manda a enviar el link OK al dron con ese nombre, puerto e ip
                    # generados por la BO
                    self._et_link_ok_dr_handler(peer, info[1], info[2])

            elif 'LINK_ERR' == command:

                if info[0] == "ET":

                    print(
                        f'\nNo ha sido posible linkear a la ET {self.et_instance["name"]} con la BO puesto que ya estaba linkeada\n')

                    for peer in self.dst_conn_peers:
                        self._et_disconnect_handler(peer)

                    if self.server_socket is not None:
                        s.socket_disconnect(self.server_socket)

                    del self.et_instance["name"]
                    os.kill(os.getpid(), signal.SIGINT)

                else:
                    peer = self.et_get_peer_byName(info[1])
                    self._et_link_err_dr_handler(peer)

            elif 'UNLINK' == command:
                peer = self.et_get_peer_byName("bo")
                if peer:
                    self._et_unlink_handler(
                        peer['socket'], peer['aes_key'], "DR", info[0])

            elif 'UNLINK_OK' == command:

                if type == "ET":
                    print(f"\nTe has unlikeado de la BO\n")

                    for peer in self.dst_conn_peers:
                        self._et_disconnect_handler(peer)

                    if self.server_socket is not None:
                        s.socket_disconnect(self.server_socket)

                    del self.et_instance["name"]
                    os.kill(os.getpid(), signal.SIGINT)

                else:
                    peer = self.et_get_peer_byName(info[0])
                    if peer:
                        self._et_unlink_ok_dr_handler(peer)

            elif "DISCONNECT" == command:
                peer = self.et_get_peer_byName(info[0])

                if peer:

                    # Desconecta el socket
                    self._et_disconnect_socket(peer)

                    # Si ademas, se recibe el ET a desconectar en el comando, significa
                    # que el dron es el que ha cortado la conexion, y hay que borrarla
                    if self.et_instance['name'] in info:
                        # Envia el disconnect a la BO para borrar el registro de la conexion
                        bo = self.et_get_peer_byName("bo")
                        if bo:
                            self._et_remove_dr_connection_handler(
                                bo['socket'], bo['aes_key'], info[0])

             # Si el comando recibido es un connect de un Dron
            elif "CLOSE" == command:
                peer = self.et_get_peer_byName(info[0])
                self._et_disconnect_socket(peer)
                os.kill(os.getpid(), signal.SIGINT)

            # Si el comando recibido es un connect de un Dron
            elif 'CONNECT' == command:
                # Se obtiene el dron
                peer = self.et_get_peer_byName("bo")
                if peer:
                    # Se manda ahora un connect a la BO para que este cree la conexion
                    self._et_connect_handler(
                        peer['socket'], peer['aes_key'], info[0]
                    )

            elif 'CONNECT_OK' == command:
                # Obtenemos el peer
                drone = self.et_get_peer_byName(info[0])
                # Enviamos la confirmacion de Conexion para que el dron comience
                # a mandar la telemetria
                self._et_connect_ok_dr_handler(drone)

            elif 'CONNECT_ERR' == command:
                # Obtenemos el peer
                drone = self.et_get_peer_byName(info[0])
                # Enviamos la confirmacion de Conexion para que el dron comience
                # a mandar la telemetria
                self._et_connect_ok_dr_handler(drone)

            # Si el comando recibido por un dron es su status
            elif 'STATUS' == command:

                # Se guarda en la informacion de vuelo, True o False dependiendo de lo
                # recibido y se muestra por pantalla
                drone = self.et_get_peer_byName(info[0])

                if drone is not None:

                    drone['fly_status'] = True if info[2].lower(
                    ) == 'true' else False
                    drone['battery'] = info[4]

                    battery = float(info[4])
                    battery = round(battery, 2)

                    if drone['fly_status'] == True:
                        print(
                            f'\nDrone {drone["name"]} se encuentra volando con {battery}% de bateria\n')
                    else:
                        print(
                            f'\nDrone {drone["name"]} se encuentra en tierra con {battery}% de bateria\n')

                    # Le pasamos la info a la BO
                    self._et_status_bo_handler(drone["name"], info[2], info[4])

            elif 'FLY' == command:
                # Le indicamos al dron que esta conectado a esta et que vuele
                self.et_fly_drone(info[0])

            elif 'LAND' == command:
                # Le indicamos al dron que esta conectado a esta et que aterrice
                self.et_land_drone(info[0])

            elif 'ET_INFO_OK' == command:
                print(
                    f'\nInfo de la ET {info[0]} recibida y comenzando comunicación con ella...\n')

                et = {
                    "name": info[0],
                    "ip": info[1],
                    "listen_port": int(info[2]),
                    "public_key": info[3],
                    "type": f"ET-{info[4]}"
                }

                self._et_to_bo_or_et(et)

            elif 'ET_INFO_ERR' == command:
                print(
                    f'\nLa ET {info[0]} no se encuentra linkeada u operativa\n')

            elif 'MSG' == command:
                print(
                    f'\nHas recibido el siguiente mensaje por parte de {info[0]}: {" ".join(info[1:])}\n')

            elif 'FILE' == command:
                # Si recibo FILE como comando tengo que descifrar
                # el contenido en bytes del archivo

                file_content = ec.prepare_file_recived(
                    aes_key, request[3], request[2])

                original_name = info[1]
                filename = info[1]
                counter = 1
                while os.path.exists(f"documents/recived/{filename}"):
                    file, extension = original_name.split('.')
                    filename = f"{file}({counter}).{extension}"
                    counter += 1

                # Escribir los bytes recibidos en el archivo
                with open(f"documents/recived/{filename}", "wb") as f:
                    f.write(file_content)

                print(
                    f'\nHas recibido un fichero por parte de {info[0]}, puedes encontrarlo en la ruta documents/recived/{filename}\n')

            elif 'SHUTDOWN' == command:
                # Le indicamos al dron que esta conectado a esta et que aterrice y despues se desconecte
                self.et_land_drone(info[0])
                self.et_disconnect_drone(info[0])

    #####
    # FUNCIÓN: def _et_accept_conn
    # ARGS_IN: dst_peer - el peer del cual queremos aceptar la conexion
    #
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes del otro peer con el cual tiene una
    #              conexion establecida
    # ARGS_OUT:
    #####
    def _et_accept_conn(self, dst_peer: dict):

        # Se compruba que tengamos un socket de conexion para ese peer

        if dst_peer['socket'] is not None:

            print(f"\nAceptando la conexion de {dst_peer['name']}...\n")

            thread_rcv = threading.Thread(
                target=self._et_comunicate, args=(dst_peer['socket'], dst_peer['aes_key']))
            thread_rcv.start()

            self.threads.append(thread_rcv)

    #####
    # FUNCIÓN: def _et_reject_drone_conn
    # ARGS_IN: socket - el socket temporal de conexion que se usa solo para responder
    #          msg - mensaje de respuesta al dron, con el motivo de rechazo
    #
    # DESCRIPCIÓN: Funcion que se encarga de enviar mensaje de rechazo respuesta
    #              al dron que se intenta conectar a nosotros
    # ARGS_OUT:
    #####
    def _et_reject_drone_conn(self, socket, msg):
        # Se rechaza la llamada
        if socket is not None:
            if s.socket_send(socket, "CONNECT_ERR "
                             f"{msg}") is None:
                s.socket_disconnect(socket)

    #####
    # FUNCIÓN: def _et_reject_bo_conn
    # ARGS_IN: socket - el socket temporal de conexion que se usa solo para responder
    #          msg - mensaje de respuesta al dron, con el motivo de rechazo
    #
    # DESCRIPCIÓN: Funcion que se encarga de enviar mensaje de rechazo respuesta
    #              al dron que se intenta conectar a nosotros
    # ARGS_OUT:
    #####
    def _et_reject_bo_conn(self, socket, msg):
        # Se rechaza la llamada
        if socket is not None:
            if s.socket_send(socket, "CONNECT_ERR "
                             f"{msg}") is None:
                s.socket_disconnect(socket)

    #####
    # FUNCIÓN: def et_disconnect_drone()
    # ARGS_IN: dr_name - nombre del dron que nos queremos deconectar
    #
    # DESCRIPCIÓN: Funcion que se encarga de desconectar la ET del dron con
    #              la cual tiene una conexion activa en ese momento
    # ARGS_OUT:
    #####
    def et_disconnect_drone(self, dr_name: str):
        # Se comprueba que haya Instancia del ET
        if 'name' in self.et_instance.keys():
            drone = self.et_get_peer_byName(dr_name)
            # Se comprueba el socket de conexion
            if drone is not None:

                print(f'\nIndicando al DR {dr_name} que se desconecte\n')

                # Se envia el disconnect al Dron
                self._et_remove_dr_connection_handler(
                    drone['socket'], drone['aes_key'], dr_name)
                time.sleep(1)
                s.socket_disconnect(drone['socket'])
                drone['socket'] = None
                self.dst_conn_peers.remove(drone)

                # Se envia el disconnect a la BO
                bo = self.et_get_peer_byName("bo")
                if bo:
                    self._et_remove_dr_connection_handler(
                        bo['socket'], bo['aes_key'], dr_name)

            else:
                print("\nNo estas conectado a ninguna Dron\n")
        else:
            print("\nDebes estar registrado y logueado\n")

    #####
    # FUNCIÓN: def _et_disconnect_socket()
    # ARGS_IN: peer - peer de conex
    #
    # DESCRIPCIÓN: Busca el peer de conexion, cierra el socket y elimina el peer
    # ARGS_OUT:
    #####
    def _et_disconnect_socket(self, peer):

        # Se comprueba el socket de conexion
        if peer["socket"] != None:
            print(f"\nCerrando conexion con {peer['name']}")

            s.socket_disconnect(peer["socket"])

        else:
            print("\nNo hace falta cerrar ninguna conexión...\n")

    # -------------------- FUNCIONES HANDLER --------------------

    #####
    # FUNCIÓN: def _et_link_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          type - tipo de entidad que envia el comando
    #          name - nombre de la ET
    #          password - password de la ET
    #          public_key - clave publica de la ET
    #
    # DESCRIPCIÓN: Comprueba
    # ARGS_OUT:
    #####
    def _et_link_handler(self, socket, aes_key, type, name, password, public_key):

        if socket is not None:

            # Ciframos el comando CONNECT_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f'LINK:{type}', name, password, public_key])

            # Enviamos mensaje de CONNECT_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el mensaje de link a {name}\n')

    #####
    # FUNCIÓN: def _et_unlink_handler()
    # ARGS_IN: peer - peer de conexion
    #          type - tipo de entidad que pide la desconexion
    #          name - nombre de la entidad
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje y cierra el socket
    # ARGS_OUT:
    #####
    def _et_unlink_handler(self, socket, aes_key, type, name):

        if socket is not None:

            # Ciframos el comando CONNECT_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f'UNLINK:{type}', name, self.et_instance["name"]])

            # Enviamos mensaje de CONNECT_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible enviar el mensaje de unlink a {name}\n')

    #####
    # FUNCIÓN: def _et_connect_handler()
    # ARGS_IN: socket - socket de conexion con la BO
    #          aes_key - clave de sesion con la BO
    #          name - nombre del dron a conectar
    #
    # DESCRIPCIÓN: Envia un mensaje de connect con el dron a la BO
    # ARGS_OUT:
    #####

    def _et_connect_handler(self, socket, aes_key, name):

        if socket is not None:

            # Ciframos el comando CONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f'CONNECT', name, self.et_instance['name']])

            # Enviamos mensaje de CONNECT_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el mensaje a la BO\n')

    #####
    # FUNCIÓN: def _et_remove_dr_connection_handler()
    # ARGS_IN: socket - socket de conexion con la BO
    #          aes_key - clave de sesion con la BO
    #          name - nombre del dron a conectar
    #
    # DESCRIPCIÓN: Envia un mensaje para que la bo borre la conexion del dron con la et
    # ARGS_OUT:
    #####
    def _et_remove_dr_connection_handler(self, socket, aes_key, name):

        if socket is not None:

            # Ciframos el comando DISCONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"DISCONNECT", name, self.et_instance['name']])

            # Enviamos mensaje de DISCONNECT cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el mensaje a la BO\n')

    #####
    # FUNCIÓN: def _et_hello_ok_et_handler()
    # ARGS_IN: peer - peer de conexion temporal creado para la conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de HELLO OK a la et
    # ARGS_OUT:
    #####

    def _et_hello_ok_et_handler(self, peer, type):

        if peer['socket'] is not None:

            # Enviamos mensaje de HELLO a la et junto
            # con la simetrica codificada en hexadecimal
            s.socket_send(
                peer['socket'], f"HELLO_OK {type} {self.et_instance['name']}")

        else:
            print(
                f'\nNo ha sido posible mandar un HELLO OK a la ET {peer["name"]}\n')

    #####
    # FUNCIÓN: def _et_hello_ok_dr_handler()
    # ARGS_IN: peer - peer de conexion temporal creado para la conexion
    #          msg - clave de sesion encriptada con la publica del dron
    #
    # DESCRIPCIÓN: Envia el mensaje de HELLO OK al dron
    # ARGS_OUT:
    #####
    def _et_hello_ok_dr_handler(self, peer, msg):

        if peer['socket'] is not None:

            # Enviamos mensaje de HELLO al dron
            s.socket_send(
                peer['socket'], f'HELLO_OK {self.et_instance["name"]} {msg}')

        else:
            print(
                f'\nNo ha sido posible mandar la clave de sesion al dron {peer["name"]}\n')

    #####
    # FUNCIÓN: def _et_link_ok_et_handler()
    # ARGS_IN: peer - peer que queremos aceptar el linkeo
    #          port - puerto del dron generado por la BO
    #          ip - ip del dron generado por la BO
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK OK al dron
    # ARGS_OUT:
    #####
    def _et_link_ok_dr_handler(self, peer, ip, port):

        if peer['socket'] is not None:

            print(f'\nEnviando confirmacion de LINK al dron {peer["name"]}\n')

            # Ciframos el comando LINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer['aes_key'], [f'LINK_OK', peer['name'], ip, port])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el LINK_OK al Dron\n')

    #####
    # FUNCIÓN: def _et_unlink_ok_dr_handler()
    # ARGS_IN: peer - peer que queremos aceptar el unlinkear
    #
    # DESCRIPCIÓN: Envia el mensaje de UNLINK OK al dron
    # ARGS_OUT:
    #####
    def _et_unlink_ok_dr_handler(self, peer):

        if peer['socket'] is not None:

            print(
                f'\nEnviando confirmacion de UNLINK al dron {peer["name"]}\n')

            # Ciframos el comando UNLINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer['aes_key'], [f'UNLINK_OK', peer['name'], self.et_instance['name']])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")

            # Se elimina el peer de nuestra lista
            self.dst_conn_peers.remove(peer)

        else:
            print(f'\nNo ha sido posible enviar el UNLINK_OK al Dron\n')

    #####
    # FUNCIÓN: def _et_connect_ok_dr_handler()
    # ARGS_IN: peer - peer que queremos aceptar el la conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de CONNETC_OK al dron
    # ARGS_OUT:
    #####

    def _et_connect_ok_dr_handler(self, peer):

        if peer['socket'] is not None:

            print(
                f'\nEnviando confirmacion de CONNECT al dron {peer["name"]}\n')

            # Ciframos el comando LINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer['aes_key'], [f'CONNECT_OK', peer['name'], self.et_instance['name']])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el CONNECT_OK al Dron\n')

    #####
    # FUNCIÓN: def _et_link_err_dr_handler()
    # ARGS_IN: peer - peer de conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK ERR a la entidada que se ha linkeado
    # ARGS_OUT:
    #####

    def _et_link_err_dr_handler(self, peer):

        if peer is not None:

            # Ciframos el comando LINK_ERR junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer['aes_key'], [f"LINK_ERR", peer['name']])

            # Enviamos mensaje de LINK_ERR cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")
            time.sleep(1)
            s.socket_disconnect(peer['socket'])
            self.dst_conn_peers.remove(peer)

        else:
            print(
                f'\nCerrando conexion con la BO\n')

    #####
    # FUNCIÓN: def _et_connect_err_dr_handler()
    # ARGS_IN: peer - peer de conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de CONNECT ERR al dron conectado
    # ARGS_OUT:
    #####

    def _et_connect_err_dr_handler(self, peer):

        if peer['socket'] is not None:

            # Ciframos el comando CONNECT_ERR junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer['aes_key'], [f"CONNECT_ERR", peer['name'], self.et_instance['name']])

            # Enviamos mensaje de CONNECT_ERR cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")
            time.sleep(1)
            s.socket_disconnect(peer['socket'])
            self.dst_conn_peers.remove(peer)

        else:
            print(
                f'\nERROR al mandar el mensaje de CONNECT ERR\n')

    #####
    # FUNCIÓN: def _et_status_bo_handler()
    # ARGS_IN: dr_name - nombre del drone
    #          et_name - nombre de la et
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje de LAND a la et y cierra el socket
    # ARGS_OUT:
    #####
    def _et_status_bo_handler(self, dr_name: str, fly_status: str, battery_status: str):
        # Se hacen comprobaciones de que haya socket creado y de que
        # se haya logeado o registrado un dron
        peer = self.et_get_peer_byName("bo")

        if peer["socket"] is not None:

            # Ciframos el comando STATUS junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], [f'STATUS {dr_name} {fly_status} {battery_status}'])

            # Enviamos mensaje de DISCONNECT
            s.socket_send(
                peer["socket"], f"{msg_enc} {nonce}")

        else:
            print("\nNo ha sido posible mandar aterrizar al dron...\n")

    #####
    # FUNCIÓN: def _et_disconnect_handler()
    # ARGS_IN: peer - peer de conexion
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje de LAND a la et y cierra el socket
    # ARGS_OUT:
    #####
    def _et_disconnect_handler(self, peer):

        if peer["socket"] is not None:

            # Ciframos el comando DISCONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], [f'DISCONNECT {self.et_instance["name"]}'])

            # Enviamos mensaje de DISCONNECT
            s.socket_send(
                peer["socket"], f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible mandar el mensaje de DISCONNECT al drone {peer["name"]}\n')

    #####
    # FUNCIÓN: def _et_msg_handler()
    # ARGS_IN: name - nombre de quien recibira el mensaje
    #          msg - mensaje a enviar
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje y cierra el socket
    # ARGS_OUT:
    #####
    def _et_msg_handler(self, peer, name: str, msg: str):

        if peer["socket"] is not None:

            print(f'\nEnviando mensaje a {name}\n')

            # Ciframos el comando MSG junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["MSG", self.et_instance["name"], msg])

            # Enviamos mensaje de MSG cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido posible enviar el mensaje a {name}\n')

    #####
    # FUNCIÓN: def _et_file_handler()
    # ARGS_IN: peer - peer de conexion
    #          name - nombre de la entidad
    #          filename - nombre del fichero a enviar a la et
    #
    # DESCRIPCIÓN: Envia el contenido del fichero cifrado
    #              a la entidada que corrsponda
    # ARGS_OUT:
    #####
    def _et_file_handler(self, peer, name: str, filename: str):

        if peer["socket"] is not None:

            print(f'\nEnviando fichero a {name}\n')

            # Abrir el archivo y leer su contenido
            with open(f"documents/{filename}", 'rb') as f:
                file_contents = f.read()

            # Ciframos el comando de FILE junto con el nombre del fichero
            msg_enc, msg_nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["FILE", self.et_instance["name"], filename])

            # Ciframos el contenido en bytes del file leido
            file_enc, file_nonce = ec.prepare_file_to_sent(
                peer["aes_key"], file_contents)

            # Enviamos mensaje de MSG_BY:BO cifrado a la ET
            # indicandole que tiene un mensaje
            s.socket_send(peer["socket"],
                          f"{msg_enc} {msg_nonce} {file_enc} {file_nonce}")

        else:
            print(
                f'\nNo ha sido posible enviar el fichero a {peer["name"]}\n')

    #####
    # FUNCIÓN: def _et_who_et_handler()
    # ARGS_IN: name - nombre de quien recibira el mensaje
    #          msg - mensaje a enviar
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje y cierra el socket
    # ARGS_OUT:
    #####
    def _et_who_et_handler(self, peer, name, type):

        if peer["socket"] is not None:

            print(f'\nPidiendo info de la ET {name} a la BO')

            # Ciframos el comando MSG junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["ET_INFO", name, type])

            # Enviamos mensaje de MSG cifrado a la ET
            s.socket_send(peer['socket'],
                          f"{msg_enc} {nonce}")

        else:
            print(f'\nNo ha sido pedir la info de la ET {name} a la BO\n')

    # -------------------- FUNCIONES AUXILIARES --------------------

    #####
    # FUNCIÓN: def et_set_fields()
    # ARGS_IN: _id - identificador de la ET
    #          name - nombre de la ET
    #          password - contraseña de la ET
    #          private_key - clave privada de la ET
    #          secret_code - codigo secreto de la ET
    #
    # DESCRIPCIÓN: Establece los parametros en un diccionario de la instancia de clase
    # ARGS_OUT:
    #####
    def et_set_fields(self, name: str, password: str, private_key: str, secret_code: str, ip: str = None, listen_port: int = None):
        self.et_instance['name'] = name
        self.et_instance['password'] = password
        self.et_instance['private_key'] = private_key
        self.et_instance['secret_code'] = secret_code
        self.et_instance['ip'] = ip
        self.et_instance['listen_port'] = listen_port

    #####
    # FUNCIÓN: def et_set_peer_field_byName()
    # ARGS_IN: name - nombre que identifica el otro extremo del peer
    #          field - campo a actualizar
    #          field_value - valor de field
    # DESCRIPCIÓN: Actualiza un campo del peer y lo devuelve actualizado
    # ARGS_OUT: El peer actualizado
    #####

    def et_set_peer_field_byName(self, name: str, field: str, field_value):
        for i, peer in enumerate(self.dst_conn_peers):
            if peer['name'] == name:
                self.dst_conn_peers[i][field] = field_value
                return self.dst_conn_peers[i]

        return None

    #####
    # FUNCIÓN: def et_get_peer_byName()
    # ARGS_IN: name - nombre que identifica el otro extremo del peer
    #
    # DESCRIPCIÓN: Obtiene la instancia de un peer a partir de su nombre
    # ARGS_OUT: El peer dado un name
    #####

    def et_get_peer_byName(self, name: str):
        for peer in self.dst_conn_peers:
            if peer['name'] == name:
                return peer

        return None

    #####
    # FUNCIÓN: def _et_to_bo_or_et()
    # ARGS_IN: ent - dict con los datos de la entidad con la que conectarse
    #
    # DESCRIPCIÓN: Funcion que se encarga de desconectar la ET del dron con
    #              la cual tiene una conexion activa en ese momento
    # ARGS_OUT:
    #####
    def _et_to_bo_or_et(self, ent):

        # Socket cliente creado, el cual no hace bind
        # (Sirve solo para intentar establecer conexion)
        client_socket = s.socket_create()
        # Se establece un timeout de 10 segundos
        client_socket.settimeout(10)

        # Se intenta establecer conexion
        if s.socket_connect(client_socket, ent["ip"], ent["listen_port"]) is None:
            # Si la conexion es fallida, se cierra
            s.socket_disconnect(client_socket)

        else:
            # Creamos una clave de sesion simetrica y se la enviamos al servidor
            aes_key = ec.generate_AESkey()

            aes_key, msg_encrypted = ec.prepare_aes_key_to_sent(
                ent["public_key"])

            self._et_send_aes_key(
                client_socket, msg_encrypted, ent["type"], ent["name"])

            # Si la conexion es exitosa, se crea el socket de conexion con el
            # cual nos comunicaremos con la ET
            new_dst_peer = {
                'listen_port': ent["listen_port"],
                'ip': ent["ip"],
                'name': ent["name"],
                'socket': client_socket,
                'aes_key': aes_key
            }

            self.dst_conn_peers.append(new_dst_peer)
            self._et_accept_conn(new_dst_peer)

            return True

        return False

    #####
    # FUNCIÓN: def _et_send_aes_key()
    # ARGS_IN: msg - clave de sesion cifrada
    #          socket - el socket de conexion temporal creado para la conexion
    #          type - tipo de entidad que envia el mensaje
    #          name - nombre de la entidad que recibira el mensaje
    #
    # DESCRIPCIÓN: Envia el mensaje de HELLO a la et junto con la clave de sesion
    # ARGS_OUT:
    #####
    def _et_send_aes_key(self, socket, msg, type, name):

        if socket is not None:
            print(f'\nEnviando la clave de sesion a {name}\n')

            # Enviamos mensaje de HELLO a la et junto
            # con la simetrica codificada en hexadecimal
            s.socket_send(
                socket, f'HELLO_BY:{type} {self.et_instance["name"]} {msg}')

        else:
            print(
                f'\nNo ha sido posible mandar la clave de sesion a {name}\n')

    @classmethod
    def close_threads(cls):

        cls.running = False

        for thread in cls.threads:
            if thread.is_alive():
                thread.join()
