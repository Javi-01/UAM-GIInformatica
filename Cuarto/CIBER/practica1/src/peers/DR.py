from src import api_endpoints as ds_api
from src import encryption as ec
from src import sockets as s
import threading
import signal
import time
import pickle
import os


class DR:

    logged_in = False
    running = True
    threads = []

    server_socket = None
    dst_conn_peer = {}
    active_conn = False
    unlink = False
    conn_socket = None

    drone_instance = {}
    links = []
    aes_key = None
    dr_battery_status = 100.0
    dr_fly_status = False

    def __init__(self):
        pass
    # -------------------- FUNCIONES DE MENÚ --------------------

    #####
    # FUNCIÓN: def dr_link()
    # ARGS_IN: dr_name - nombre del dron
    #          dr_password - contraseña del dron
    #          et_name - nombre de la ET a la que linkearse
    #
    # DESCRIPCIÓN: Registra un dron en la BO y lanza un puerto a la escucha de peticiones
    # ARGS_OUT:
    #####

    def dr_link(self, dr_name: str, dr_password: str, et_name: str):

        # Generamos las claves del dron
        public_key, private_key, secret_code = ec.generate_RSAkeys()

        # Obtenemos el puerto y la IP de esa ET para conectarnos a él
        res = ds_api.get_et(et_name)

        if res.status_code in [200] and res.json() is not None:

            res_data = res.json()

            # Guardar la instancia del dron en la clase
            self.dr_set_fields(
                dr_name,
                dr_password,
                private_key,
                secret_code
            )

            # Guardamos la public_key para mandarla posteriormente
            self.public_key = public_key

            # Abrimos un socket a la ET para comenzar el handshake
            if self._dr_to_et(res_data) == False:
                print("\nLa ET " + et_name + " no se encuentra operativa\n")
                del self.et_instance["name"]
                os.kill(os.getpid(), signal.SIGINT)

        else:
            print("\nNo existe la ET a la que te quieres linkear\n")

    #####
    # FUNCIÓN: def dr_login()
    # ARGS_IN: dr_name - nombre del dron
    #          dr_password - contraseña del dron
    #
    # DESCRIPCIÓN: Logea un dron en la BO y lanza un puerto a la escucha de peticiones
    # ARGS_OUT:
    #####
    def dr_login(self, dr_name: str, dr_password: str):

       # Comprobamos si dicho dron existe
        if os.path.exists(f"src/pickles/{dr_name}.pickle") == False:
            print(f"\nEl Drone {dr_name} no se encuentra registrado\n")
            return

        # Recoger la instancia del pickle
        picke_filename = f'src/pickles/{dr_name}.pickle'

        # Deserializamos el objeto desde el archivo
        with open(picke_filename, "rb") as file:
            data = pickle.load(file)

        dr_instance_data = data[0]
        self.dr_fly_status = False
        self.dr_battery_status = 100.0
        self.public_key = data[3]
        self.links = data[4]

        if dr_instance_data['password'] != dr_password:
            print("\nContraseña incorrecta para el Dron", dr_name, "\n")
            return

        # Guardar la instancia del DR en la clase
        self.dr_set_fields(
            dr_instance_data['name'],
            dr_instance_data['password'],
            dr_instance_data['private_key'],
            dr_instance_data['secret_code']
        )

        self.drone_instance['ip'] = dr_instance_data['ip']
        self.drone_instance['listen_port'] = dr_instance_data['listen_port']

        # Lanzamos un hilo de recepcion de mensajes
        self._dr_create_server_socket()

        self.logged_in = True

    #####
    # FUNCIÓN: def dr_logout()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Cierra la conexion y guarda la instancia de un dron
    # ARGS_OUT:
    #####
    def dr_logout(self):

        if 'name' in self.drone_instance:
            print(
                f"\nGuardando la instancia del drone {self.drone_instance['name']}\n")

            # Creamos una tupla con los datos a guardar
            data = (self.drone_instance, False, 100.0,
                    self.public_key, self.links)

            picke_filename = f'src/pickles/{self.drone_instance["name"]}.pickle'

            # Serializamos el objeto y lo guardamos en un archivo
            with open(picke_filename, "wb") as file:
                pickle.dump(data, file)

            if self.server_socket is not None:
                s.socket_disconnect(self.server_socket)

    #####
    # FUNCIÓN: def dr_only_link()
    # ARGS_IN: et_name - nombre de la ET a la que linkearse
    #
    # DESCRIPCIÓN: Linkea al DR con una et y lanza un puerto a la escucha de peticiones
    # ARGS_OUT:
    #####
    def dr_only_link(self, et_name: str):

        # Se comprueba que no estemos linkeado a esa ET
        if self.dr_get_et_byName(et_name) is None:

            # Obtenemos el puerto y la IP de esa ET para conectarnos a él
            res = ds_api.get_et(et_name)

            if res.status_code in [200] and res.json() is not None:
                res_data = res.json()

                # Abrimos un socket a la ET para comenzar el handshake
                if self._dr_to_et(res_data) == False:
                    print("\nLa ET " + et_name + " no se encuentra operativa\n")
                    return

            else:
                print("\nNo existe la ET a la que te quieres linkear\n")
        else:
            print(f"\nYa estas linkeado a la ET {et_name}\n")

    #####
    # FUNCIÓN: def dr_unlink_et()
    # ARGS_IN: et_name - nombre de la ET de la que nos queremos desuscribir (unlinkear)
    #
    # DESCRIPCIÓN: Elimina el link un dron a una ET por su nombre
    # ARGS_OUT:
    #####
    def dr_unlink(self, et_name):
        et = self.dr_get_et_byName(et_name)
        if et is not None:
            if self._dr_to_et(et) == False:
                print("\nLa ET no se encuentra conectada\n")
            self.unlink = True
        else:
            print("\nNO estas linkeada a la ET", et_name)

    #####
    # FUNCIÓN: def dr_connect_et()
    # ARGS_IN: et_name - nombre de la ET a la que nos queremos conectar
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar el comando a la ET que
    #              esta lienkeada para conectarse
    # ARGS_OUT:
    #####
    def dr_connect_et(self, et_name: str):

        # Buscamos si el DR esta linkeado a la ET
        et = self.dr_get_et_byName(et_name)
        if et is not None:

            # Abrimos un socket a la ET para comenzar el handshake
            if self._dr_to_et(et) == False:
                print("\nLa ET " + et_name + " no se encuentra operativa\n")

        else:
            print("\nNO estas linkeado a la ET", et_name)

    #####
    # FUNCIÓN: def dr_connect_et()
    # ARGS_IN: et_name - nombre de la ET a la que nos queremos desconectar
    #
    # DESCRIPCIÓN: Funcion que se encarga de mandar el comando a la ET que
    #              esta conectada para desconectarse
    # ARGS_OUT:
    #####
    def dr_disconnect_et(self):

        # Buscamos si el DR esta linkeado a la ET
        if self.dst_conn_peer and self.conn_socket:
            self._dr_disconnect_handler(self.dst_conn_peer['name'])
        else:
            print("\nNO estas conectado a nunguna la ET\n")

    # -------------------- FUNCIONES COMO PEER DE CONEXIÓN --------------------

    #####
    # FUNCIÓN: def _dr_create_server_socket()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Crear un socket de escucha y lanza en un hilo aparte una cola
    #              de recepcion
    # ARGS_OUT:
    #####
    def _dr_create_server_socket(self):
        # Comprueba que haya un dron en esta instancia de clase
        if 'listen_port' in self.drone_instance.keys():
            # Creamos el server socket con IP y puerto y creamos la cola (Listen)
            self.server_socket = s.socket_create(
                self.drone_instance['listen_port'],
                self.drone_instance['ip']
            )
            if self.server_socket is None:
                return
            else:
                # Se lanza el Thread de recpcion en un hilo secundario
                thread_rcv = threading.Thread(
                    target=self._dr_recv_connection_handler)
                thread_rcv.setDaemon(True)
                thread_rcv.start()
        else:
            print("\nTodavia no estas registrado o logueado\n")

    #####
    # FUNCIÓN: def _dr_recv_connection_handler()
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de recibir comandos de las ETs a las que quiere
    #              establecer conexion
    # ARGS_OUT:
    #####
    def _dr_recv_connection_handler(self):

        while self:
            # Aceptamos las peticiones y recibimos mensajes (El accept crea un nuevo socket)
            socket_conn, _ = s.socket_accept(self.server_socket)
            data = s.socket_recv(socket_conn)

            # Si hay mensaje, se manda a procesar
            if data:
                self._dr_process_request(data, socket_conn)

    #####
    # FUNCIÓN: def _dr_comunicate
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes del otro peer con el cual tiene una
    #              conexion establecida
    # ARGS_OUT:
    #####
    def _dr_comunicate(self):
        # Se establece un tiempo de espera infinito
        self.conn_socket.settimeout(None)

        # Me mantengo a la espera de mensajes en el socket de conexion
        while self.running:
            data = s.socket_recv(self.conn_socket)
            # Si recibimos error cerramos la conexion
            if data == "ERROR":
                break
            elif data:
                # Se manda a procesar la peticion
                self._dr_process_request(data, self.conn_socket)
        return

    #####
    # FUNCIÓN: def _dr_process_request()
    # ARGS_IN: data - cadena con el comando recibido
    #          socket - el socket de conexion temporal creado para la conexion
    # DESCRIPCIÓN: Funcion que se encarga de actuar en consecuencia del comando
    #              recibido
    # ARGS_OUT:
    #####
    def _dr_process_request(self, data: str, socket):

        # Parseo el mensaje
        request = data.split(" ")
        if 'HELLO_OK' == request[0]:

            # Se obtiene la simetrica descifrando con la privada de este dron
            aes_key = ec.prepare_aes_key_recived(
                self.drone_instance['private_key'],
                self.drone_instance['secret_code'],
                request[2]
            )

            # Buscamos si tenemos el peer en conexion
            et = self.dr_get_et_byName(request[1])

            # Si no lo tenemos, es porque la peticion es LINK
            if et is None:
                # Se manda a hacer el link con los datos del dron
                self._dr_link_et_handler(aes_key)
            # Si lo tenemos guardado, es que la peticion es CONNECT o UNLINK
            else:
                if self.unlink:
                    self._dr_unlink_et_handler(aes_key, et)
                else:
                    self._dr_connect_handler(aes_key)

        else:
            # Obtenemos el comando y la info restante del mensaje
            command, info = ec.prepare_msg_recived(
                self.aes_key, request[1], request[0])

            # Si recibimos el link ok de la ET
            if 'LINK_OK' == command:
                self.logged_in = True

                # Se guardan los campos de ip y puerto de recepcion de mi dron
                self.drone_instance['ip'] = info[1]
                self.drone_instance['listen_port'] = int(info[2])

                # Se crea el socket de recepcion del dron
                if self.server_socket is None:
                    self._dr_create_server_socket()

                # Guardamos la info de la ET a la que se ha linkeado el DR
                self.links.append(
                    {
                        "name": self.dst_conn_peer["name"],
                        "ip": self.dst_conn_peer["ip"],
                        "listen_port": self.dst_conn_peer["listen_port"],
                    }
                )

                # Me desconecto de la ET
                self._dr_disconnect_handler()

            # SI recibimos el unlink_ok, eliminamos el link a la ET correspondiete
            elif 'UNLINK_OK' == command:
                print(f"\nTe has unlinkeado de la ET {info[1]}\n")
                # Se elimina la instancia de la lista
                self.links.remove(self.dr_get_et_byName(info[1]))
                self.unlink = False
                # Se desconecta con la ET
                self._dr_disconnect_handler()

            # Si recibimos el link err de la ET
            elif 'LINK_ERR' == command:
                self.running = False

                print(
                    f'\nNo ha sido posible linkear al DR {self.drone_instance["name"]} con la ET {self.dst_conn_peer["name"]} puesto que el drone ya estaba linkeado\n')

                # Reestablecemos el DR de cero
                self.dst_conn_peer = {}
                self.aes_key = None

                del self.drone_instance["name"]
                os.kill(os.getpid(), signal.SIGINT)

            # Si el comando recibido por la ET es un connect_OK
            elif 'CONNECT_OK' == command:
                if self.conn_socket is not None:
                    print(f"\nConexion aceptada por la ET {info[1]}\n")

                    # Cambiamos la conexion a activa
                    self.active_conn = True
                    # Lanzamos thread para enviar mensajes de Estado de Vuelo
                    t = threading.Thread(
                        target=self._dr_status_handler, args=(
                            self.dr_get_et_byName(info[1]),)).start()
                    self.threads.append(t)

            # Si el comando recibido por la ET es un connect_ERR, significa que no
            # puede establecer una conexion en ese momento
            elif 'CONNECT_ERR' == command:
                if socket is not None:
                    print(
                        f"\nNo ha sido posible conectarse a la ET {info[1]}\n")
                    # Se cierra el socket y se eliminan los campos del peer destino
                    s.socket_disconnect(socket)
                    self.dst_conn_peer = {}

            # Si el comando recibido por la ET es volar
            elif 'FLY' == command:
                # Si recibo el comando de volar, lo que se hace simplemente es
                # cambiar mi variable de estado de vuelo a volar
                if self.conn_socket is not None:
                    self.dr_fly_status = True
                    print(
                        f"\nDron {self.drone_instance['name']} mandado a volar por {info[0]}\n")

            # Si el comando recibido por la ET es aterrizar
            elif 'LAND' == command:
                # Si recibo el comando de aterrizar, se cambia la variable de estado
                # de vuelo a false
                if self.conn_socket is not None:
                    self.dr_fly_status = False
                    print(
                        f"\nDron {self.drone_instance['name']} mandado a aterrizar por {info[0]}\n")

            # Si el comando recibido por la ET es desconectarse
            elif 'DISCONNECT' == command:

                print(f"\nDesconectandose de la ET {info[1]}\n")
                self.active_conn = False
                s.socket_disconnect(self.conn_socket)

                self.dst_conn_peer = {}
                self.aes_key = None
                self.dr_battery_status = 100.0
                self.dr_fly_status = False

    # -------------------- FUNCIONES HANDLER --------------------

    #####
    # FUNCIÓN: def _dr_to_et_handler()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Envia el mensaje de HELLO y lanza el hilo de recepcion
    #              a la espera de respuesta
    # ARGS_OUT:
    #####

    def _dr_to_et_handler(self):

        # Enviamos mensaje de HELLO a la et
        s.socket_send(
            self.conn_socket, f'HELLO_BY:DR {self.drone_instance["name"]} {self.public_key}')

        # Lanzamos Thread para comunicarnos con el ET
        t = threading.Thread(target=self._dr_comunicate)
        t.start()
        self.threads.append(t)

    #####
    # FUNCIÓN: def _dr_link_et_handler()
    # ARGS_IN: aes_key - clave de sesion
    #
    # DESCRIPCIÓN: Mandar el mensaje de link de a la ET
    # ARGS_OUT:
    #####
    def _dr_link_et_handler(self, aes_key):

        self.aes_key = aes_key

        # Se encripta el mensaje de LINK
        msg_enc, nonce = ec.prepare_msg_to_sent(
            self.aes_key, ["LINK", self.drone_instance['name'], self.drone_instance['password'], self.public_key])

        # Enviamos mensaje de link con la info del dron
        s.socket_send(self.conn_socket,
                      f"{msg_enc} {nonce}")

    #####
    # FUNCIÓN: def _dr_unlink_et_handler()
    # ARGS_IN: aes_key - clave de sesion
    #          et - et a la que nos queremos unlinkear
    # DESCRIPCIÓN: Mandar el mensaje de unlink de a la ET
    # ARGS_OUT:
    #####
    def _dr_unlink_et_handler(self, aes_key, et):

        self.aes_key = aes_key

        # Se encripta el mensaje de LINK
        msg_enc, nonce = ec.prepare_msg_to_sent(
            self.aes_key, ["UNLINK", self.drone_instance['name']])

        # Enviamos mensaje de unlink con la info del dron
        s.socket_send(self.conn_socket,
                      f"{msg_enc} {nonce}")

    #####
    # FUNCIÓN: def _dr_connect_handler()
    # ARGS_IN: aes_key - clave de sesion
    #
    # DESCRIPCIÓN: Se manda el mensaje de comnezar una conexion a la ET
    # ARGS_OUT:
    #####

    def _dr_connect_handler(self, aes_key):

        self.aes_key = aes_key

        if self.conn_socket is not None:

            # Ciframos el comando CONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                self.aes_key, [f'CONNECT {self.drone_instance["name"]}'])

            # Enviamos mensaje de CONNECT
            s.socket_send(
                self.conn_socket, f"{msg_enc} {nonce}")

        else:
            print("\nNo ha sido posible conectarse con la ET\n")

    #####
    # FUNCIÓN: def _dr_status_handler()
    # ARGS_IN: et - et a la que mandar los comandos de status
    #
    # DESCRIPCIÓN: Funcion que se encarga de enviar en un hilo aparte, los paquetes
    #              del estado de vuelo a la ET
    # ARGS_OUT:
    #####

    def _dr_status_handler(self, et):
        battery_life = 60
        seconds_left = battery_life

        while 0 < seconds_left and self.active_conn is True:
            # Se reduce el porcentaje de bateria si esta en vuelo
            if self.dr_fly_status is True and self.active_conn is True:
                self.dr_battery_status = 100 * (seconds_left/battery_life)

            # Ciframos el comando STATUS junto con el resto de info
            # y obtenemos el nonce de cifrado

            if self.active_conn is True:
                msg_enc, nonce = ec.prepare_msg_to_sent(
                    self.aes_key, ["STATUS", self.drone_instance['name'], f'Volando: {self.dr_fly_status}', f'Bateria: {self.dr_battery_status}'])

            # Se envia el porcentaje de bateria y el estado de vuelo
            if self.active_conn is True:
                s.socket_send(self.conn_socket,
                              f"{msg_enc} {nonce}")

            if self.dr_fly_status is True and self.active_conn is True:
                # Se reducen los segundos y se hace parar un segundo
                seconds_left -= 1
            # Cada 2 segundos se envia
            time.sleep(2)

        if self.active_conn == True:
            print("\nDron sin bateria, aterizando...\n")
            msg_enc, nonce = ec.prepare_msg_to_sent(
                self.aes_key, ["STATUS", self.drone_instance['name'], f'Bateria: {self.dr_battery_status}', f'Bateria: {self.dr_battery_status}'])
            time.sleep(1)
            self._dr_disconnect_handler(et['name'])

            # Reestablecemos los datos de bateira y fly_status del drone
            self.dr_battery_status = 100.0
            self.dr_fly_status = False

    #####
    # FUNCIÓN: def _dr_disconnect_handler()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Busca el peer de conexion, envia el mensaje de DISCONNECT a la et y cierra el socket
    # ARGS_OUT:
    #####

    def _dr_disconnect_handler(self, et_name=None):

        if self.conn_socket is not None:

            # Ciframos el comando DISCONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            if et_name:
                # Si espeficicamos el ET, significa que es disconnect por mandato
                # del dron
                msg_enc, nonce = ec.prepare_msg_to_sent(
                    self.aes_key, [f'DISCONNECT {self.drone_instance["name"]} {et_name}'])
            else:
                msg_enc, nonce = ec.prepare_msg_to_sent(
                    self.aes_key, [f'DISCONNECT {self.drone_instance["name"]}'])

            # Enviamos mensaje de DISCONNECT
            s.socket_send(
                self.conn_socket, f"{msg_enc} {nonce}")

            self.active_conn = False
            time.sleep(1)
            s.socket_disconnect(self.conn_socket)

            self.dst_conn_peer = {}
            self.aes_key = None

        else:
            print("\nNo ha sido posible desconectarse con la ET\n")

    # -------------------- FUNCIONES AUXILIARES --------------------

    #####
    # FUNCIÓN: def dr_set_fields()
    # ARGS_IN:
    #          name - nombre del dron
    #          password - contraseña del dron
    #          private_key - clave privada del dron
    #          secret_code - codigo secreto del dron
    #
    # DESCRIPCIÓN: Establece los parametros en un diccionario de la instancia de clase
    # ARGS_OUT:
    #####

    def dr_set_fields(self, name: str, password: str,  private_key: str, secret_code: str):
        self.drone_instance['name'] = name
        self.drone_instance['password'] = password
        self.drone_instance['private_key'] = private_key
        self.drone_instance['secret_code'] = secret_code

    #####
    # FUNCIÓN: def _dr_to_et()
    # ARGS_IN: et - informacion de la et a la que nos queremos linkear
    # DESCRIPCIÓN: Crear un socket para linkearse a la ET
    # ARGS_OUT: True si la conexion fue exitosa, False en caso contrario
    #####
    def _dr_to_et(self, et):

        # Socket cliente creado, el cual no hace bind
        # (Sirve solo para intentar establecer conexion)
        client_socket = s.socket_create()
        # Se establece un timeout de 10 segundos
        client_socket.settimeout(10)

        # Se intenta establecer conexion
        if s.socket_connect(client_socket, et["ip"], et["listen_port"]) is None:
            # Si la conexion es fallida, se cierra
            s.socket_disconnect(client_socket)

        else:

            # Se guarda el socket de conexion
            self.conn_socket = client_socket

            # Se establecen los campos que de momento sabemos de la ET
            self.dst_conn_peer['name'] = et['name']
            self.dst_conn_peer['listen_port'] = et['listen_port']
            self.dst_conn_peer['ip'] = et['ip']

            # Se manda procesar el link
            self._dr_to_et_handler()
            return True

        return False

    #####
    # FUNCIÓN: def dr_get_et_byName()
    # ARGS_IN: name - nombre que identifica a la et
    #
    # DESCRIPCIÓN: Buscar entre los links del drone la ET con el nombre indicado
    # ARGS_OUT: Info de la ET linkeada o None en caso de no estar linkeado
    #####

    def dr_get_et_byName(self, name: str):
        for et in self.links:
            if et['name'] == name:
                return et

        return None

    @classmethod
    def close_threads(cls):
        cls.running = False

        for thread in cls.threads:
            if thread is not None and thread.is_alive():
                thread.join()
