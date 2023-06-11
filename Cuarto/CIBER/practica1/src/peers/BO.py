from src import encryption as ec
from src import sockets as s
from src import api_endpoints as ds_api
import threading
import pickle
import os


class BO:

    running = True
    threads = []

    server_socket = None
    dst_conn_peers = []

    bo_instance = {}
    aes_key = None

    def __init__(self) -> None:

        # Comprobamos si existe ya una instancia de la BO y en caso de que no exista
        # se crea una nueva instancia
        try:
            picke_filename = 'src/pickles/bo.pickle'

            # Deserializamos el objeto desde el archivo
            with open(picke_filename, "rb") as file:
                data = pickle.load(file)

            bo_instance_data = data

            # Guardar la instancia del ET en la clase
            self.bo_set_fields(
                bo_instance_data['name'],
                ds_api.regenerate_port().json(),
                bo_instance_data['ip'],
                bo_instance_data['private_key'],
                bo_instance_data['secret_code']
            )

        except FileNotFoundError:
            # Si no existe la instancia de la bo, la creamos
            self.bo_register()

        # Lanzamos un hilo de recepcion de mensajes
        self._bo_create_server_socket()

    # -------------------- FUNCIONES DE MENÚ --------------------

    #####
    # FUNCIÓN: def bo_register()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Si no hay una instancia de la BO, se crea una nueva instancia
    #              en caso contrario se carga la instancia existente
    # ARGS_OUT:
    #####
    def bo_register(self):

        # Generamos las claves de la ET
        public_key, private_key, secret_code = ec.generate_RSAkeys()

        res = ds_api.bo_register("bo", 10000, "localhost", str(public_key))

        res_data = res.json()

        if res.status_code in [200]:
            self.logged_in = True

            # Guardar la instancia del dron en la clase
            self.bo_set_fields(
                res_data['name'],
                ds_api.regenerate_port().json(),
                res_data['ip'],
                private_key,
                secret_code
            )

        else:
            print("\nError registro", res_data['error'])

    #####
    # FUNCIÓN: def bo_logout()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Cierra la conexion y guarda la instancia de la BO
    # ARGS_OUT:
    #####
    def bo_logout(self):

        # Borramos todas las conexiones existentes
        ds_api.delete_connections()

        # Cerrar la conexión de la BO
        print("\nGuardando la instancia de la BO\n")

        # Creamos una tupla con los datos a guardar
        data = (self.bo_instance)

        picke_filename = 'src/pickles/bo.pickle'

        # Serializamos el objeto y lo guardamos en un archivo
        with open(picke_filename, "wb") as file:
            pickle.dump(data, file)

        # Desconectamos todos los peers existentes
        for peer in self.dst_conn_peers:
            self._bo_disconnect_handler(peer)

        if self.server_socket is not None:
            s.socket_disconnect(self.server_socket)

        return

    #####
    # FUNCIÓN: def send_msg()
    # ARGS_IN: et_name - nombre de la et a la que se quiere enviar el archivo
    #          msg - mensaje a enviar
    #
    # DESCRIPCIÓN: Envia un mensaje a la ET que se indique
    # ARGS_OUT:
    #####
    def bo_send_msg(self, et_name: str, msg: str):

        peer = self.bo_get_peer_byName(et_name)
        if peer is None:
            print(f"\nLa ET {et_name} esta linkeada pero no está operativa\n")
            return

        self._bo_msg_et_handler(peer, et_name, msg)

    #####
    # FUNCIÓN: def send_file()
    # ARGS_IN: et_name - nombre de la et a la que se quiere enviar el archivo
    #          file_name - nombre del archivo a enviar
    #
    # DESCRIPCIÓN: Envia un archivo a la ET que se indique
    # ARGS_OUT:
    #####
    def bo_send_file(self, et_name: str, filename: str):

        # Comprobamos si el fichero existe
        if os.path.exists(f"documents/{filename}") == False:
            print("\nEl archivo no existe\n")
            return

        peer = self.bo_get_peer_byName(et_name)
        if peer is None:
            print(f"\nLa ET {et_name} esta linkeada pero no está operativa\n")
            return

        self._bo_file_et_handler(peer, filename)

    #####
    # FUNCIÓN: def bo_fly_drone()
    # ARGS_IN: dr_name - nombre del drone que se quiere poner a volar
    #
    # DESCRIPCIÓN: Busca a que et esta conectado el drone y le dice que vuele
    # ARGS_OUT:
    #####
    def bo_fly_drone(self, dr_name: str):

        res = ds_api.get_drone(dr_name)

        if res.status_code in [200]:
            dr_info = res.json()

            res = ds_api.bo_get_dr_connection(dr_info["_id"])

            if res.status_code in [200]:
                et = res.json()

                peer = self.bo_get_peer_byName(et["name"])

                if peer is None:
                    print(
                        f'\nLa ET {et["name"]} esta linkeada pero no está operativa por lo que no se puede indicar al drone {dr_name}\n')
                    return

                self._bo_fly_et_handler(peer, dr_name, et["name"])
            else:
                print(
                    f'\nEl drone {dr_name} no se encuentra conectado\n')

        else:
            print(f'\nEl Dron con nombre {dr_name} no esta registrado\nsss')

        return

    #####
    # FUNCIÓN: def bo_land_drone()
    # ARGS_IN: dr_name - nombre del drone que se quiere aterrizar
    #
    # DESCRIPCIÓN: Busca a que et esta conectado el drone y le dice que aterrice
    # ARGS_OUT:
    #####
    def bo_land_drone(self, dr_name: str):

        res = ds_api.get_drone(dr_name)

        if res.status_code in [200]:
            dr_info = res.json()

            res = ds_api.bo_get_dr_connection(dr_info["_id"])

            if res.status_code in [200]:
                et = res.json()

                peer = self.bo_get_peer_byName(et["name"])

                if peer is None:
                    print(
                        f'\nLa ET {et["name"]} esta linkeada pero no está operativa por lo que no se puede indicar al drone {dr_name}\n')
                    return

                self._bo_land_et_handler(peer, dr_name, et["name"])

            else:
                print(
                    f'\nEl drone {dr_name} no se encuentra conectado\n')

        else:
            print(f'\nEl Dron con nombre {dr_name} no esta registrado\n')

    #####
    # FUNCIÓN: def get_ets_status()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Busca a que ets estan linkeadas y les pide su estado
    # ARGS_OUT:
    #####
    def get_ets_status(self):

        # Obtengo las ets linkedas
        res = ds_api.get_ets()

        if res.status_code in [200] and len(res.json()) > 0:
            ets = res.json()

            # Recorro las ets
            for et in ets:

                peer = self.bo_get_peer_byName(et["name"])
                if peer is None:
                    print(
                        f'\nLa ET {et["name"]} con IP {et["ip"]} y PUERTO {et["listen_port"]} no se encuentra operativa\n')
                else:
                    print(
                        f'\nLa ET {et["name"]} con IP {et["ip"]} y PUERTO {et["listen_port"]} se encuentra operativa\n')
        else:
            print(f'\nNo hay ninguna et linkeada\n')

    #####
    # FUNCIÓN: def get_drones_status()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Busca a que drone estan linkeados/conectados a alguna et y les pide su estado
    # ARGS_OUT:
    #####
    def get_drones_status(self):

        ets = ds_api.get_ets()
        if ets.status_code in [200] and len(ets.json()) > 0:

            # Obtengo los drones linkedos y los conectados
            connected = ds_api.bo_get_connected_drs().json()
            non_connected = ds_api.bo_get_nonconnected_drs().json()

            # Recorremos los drones no conectados
            if len(non_connected) != 0:
                for dr in non_connected:
                    battery = float(dr["battery_porcentage"])
                    battery = round(battery, 2)

                    print(
                        f'\nEl DR {dr["name"]} con IP {dr["ip"]} y PUERTO {dr["listen_port"]} se encuentra en tierra, con {battery}% de bateria y no conectado\n')

            # Recorremos los drones conectados
            if len(connected) != 0:
                for dr in connected:
                    battery = float(dr["battery_porcentage"])
                    battery = round(battery, 2)

                    status = True if dr['fly_status'].lower(
                    ) == 'true' else False

                    if status == True:
                        print(
                            f'\nEl DR {dr["name"]} con IP {dr["ip"]} y PUERTO {dr["listen_port"]} se encuentra volando, con {battery}% de bateria y conectado\n')
                    else:
                        print(
                            f'\nEl DR {dr["name"]} con IP {dr["ip"]} y PUERTO {dr["listen_port"]} se encuentra en tierra, con {battery}% de bateria y conectado\n')
        else:
            print(f'\nNo hay ningún dr linkeado\n')

    #####
    # FUNCIÓN: def bo_shutdown()
    # ARGS_IN:
    #
    # DESCRIPCIÓN: Busca a que drone estan conectados a alguna et, los manda a aterrizar
    #              y los desconecta de sus ets
    # ARGS_OUT:
    #####
    def bo_shutdown(self):

        ets = ds_api.get_ets()

        if ets.status_code in [200] and len(ets.json()) > 0:

            # Obtengo los drones conectados
            connected = ds_api.bo_get_connected_drs().json()

            # Recorremos los drones conectados
            if len(connected) != 0:
                for dr in connected:
                    et = ds_api.bo_get_dr_connection(dr["_id"]).json()

                    peer = self.bo_get_peer_byName(et["name"])
                    if peer is None:
                        print(
                            f"\nLa ET {et['name']} esta linkeada pero no está operativa\n")
                        return

                    self._bo_shutdown_et_handler(peer, dr["name"], et["name"])
            else:
                print(f'\nNo hay ningún dr conectado\n')
        else:
            print(f'\nNo hay ninguna et registrada\n')

    # -------------------- FUNCIONES COMO PEER DE CONEXIÓN --------------------

    #####
    # FUNCIÓN: def _bo_create_server_socket()
    # ARGS_IN:
    # DESCRIPCIÓN: Crear un socket de escucha y lanza en un hilo aparte una cola
    #              de recepcion
    # ARGS_OUT:
    #####
    def _bo_create_server_socket(self):
        # Comprueba que haya una bo en esta instancia de clase
        if 'listen_port' in self.bo_instance.keys():
            # Creamos el server socket con IP y puerto y creamos la cola (Listen)
            self.server_socket = s.socket_create(
                self.bo_instance['listen_port'],
                self.bo_instance['ip']
            )
            if self.server_socket is None:
                return
            else:
                print("\nEscuchando...")
                # Se lanza el Thread de recpcion en un hilo secundario
                thread_rcv = threading.Thread(
                    target=self._bo_recv_connection_handler)
                thread_rcv.setDaemon(True)
                thread_rcv.start()
        else:
            print("\nTodavia no estas registrado o logueado\n")

    #####
    # FUNCIÓN: def _bo_recv_connection_handler()
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de recibir peticiones de las ETs que quieren
    #              linkearse o que quieran mandar un mensaje o fichero
    # ARGS_OUT:
    #####
    def _bo_recv_connection_handler(self):

        while self:
            # Aceptamos las peticiones y recibimos mensajes (El accept crea un nuevo socket)
            socket_conn, _ = s.socket_accept(self.server_socket)
            data = s.socket_recv(socket_conn)

            # Si hay mensaje, se manda a procesar
            if data:
                self._bo_process_request(data, socket_conn)

    #####
    # FUNCIÓN: def _bo_comunicate
    # ARGS_IN: socket - socket de comunicación
    #          aes_key - clave de sesión con el otro extremos del peer
    #
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes del otro peer con el cual tiene una
    #              conexion establecida
    # ARGS_OUT:
    #####

    def _bo_comunicate(self, socket, aes_key):

        # Se establece un tiempo de espera infinito
        socket.settimeout(0)

        # Me mantengo a la espera de mensajes en el socket de conexion
        while self.running:
            data = s.socket_recv(socket)
            # Si recibimos error cerramos la conexion
            if data == "ERROR":
                socket = None
            elif data:
                # Se manda a procesar la peticion
                self._bo_process_request(data, socket, aes_key)

        return

    #####
    # FUNCIÓN: def _bo_process_request()
    # ARGS_IN: data - cadena con el comando recibido
    #          socket - socket de comunicación
    #          aes_key - clave de sesión con el otro extremos del peer o None
    #
    # DESCRIPCIÓN: Funcion que se encarga de actuar en consecuencia del comando
    #              recibido
    # ARGS_OUT:
    #####
    def _bo_process_request(self, data: str, socket, aes_key=None):

        # Parseo el mensaje
        request = data.split(" ")

        # Si el comando recibido es un connect de un Dron
        if 'HELLO_BY' in data:

            aes_key = ec.prepare_aes_key_recived(
                self.bo_instance['private_key'], self.bo_instance['secret_code'], request[2])

            new_dst_peer = {
                'name': request[1],
                'socket': socket,
                'aes_key': aes_key
            }

            self.dst_conn_peers.append(new_dst_peer)
            self._bo_accept_et_conn(new_dst_peer)
            self._bo_hello_ok_et_handler(new_dst_peer)

        # En caso de que el comando recibido este cifrado deberá de descrifrarlo
        # con la clave simetrica de sesion generada previcamente en el hello
        else:
            # Obtenemos el comando y la info restante del mensaje
            command, info = ec.prepare_msg_recived(
                aes_key, request[1], request[0])

            try:
                command, type = command.split(":")
            except Exception:
                pass

            if 'LINK' == command:

                if type == 'ET':
                    res = ds_api.et_register(
                        info[0], info[1], info[2])
                if type == 'DR':
                    res = ds_api.dr_register(
                        info[0], info[1], info[2])

                    peer = self.bo_get_peer_byKey(aes_key)
                    dr = ds_api.get_drone(info[0]).json()
                    links = ds_api.dr_links(info[0]).json()

                    linked = False
                    for link in links:
                        if link["name"] == peer["name"]:
                            linked = True

                    if linked == False:
                        print(
                            f"\nLinkeado {type} con nombre {info[0]} a la ET {peer['name']}\n")

                        ds_api.dr_link_et(info[0], peer["name"])
                        self._bo_link_ok_et_handler(
                            socket, aes_key, type, info[0], dr["ip"], str(dr["listen_port"]))
                        return

                if res.status_code == 200:
                    new_ent = res.json()

                    print(
                        f"\nLinkeado {type} con nombre {info[0]}\n")

                    self._bo_link_ok_et_handler(
                        socket, aes_key, type, info[0], new_ent["ip"], str(new_ent["listen_port"]))
                else:
                    print(
                        f"\nError al linkear {type} con nombre {info[0]} puesto que ya linkeado\n")

                    self._bo_link_err_et_handler(
                        socket, aes_key, type, info[0])

            elif "UNLINK" == command:

                if type == 'ET':
                    # Eliminamos la ET y los links de los drones
                    res = ds_api.bo_unlink_et(
                        info[0])
                if type == 'DR':
                    # Eliminamos el link del drone a la ET
                    res = ds_api.dr_unlink_et(
                        info[0], info[1])

                if res.status_code == 200:
                    # Se elimina conexion activa, si la hay
                    ds_api.bo_del_connection(info[1], info[0])
                    # Se manda respuesta de confirmacion
                    self._bo_unlink_ok_et_handler(
                        socket, aes_key, type, info[0])
                else:
                    print(f"\nError al unlinkear el {type} {info[0]}\n")

            elif 'CONNECT' == command:
                peer = self.bo_get_peer_byName(info[1])
                if peer:
                    # Se crea la conexion en la base de datos
                    res = ds_api.bo_add_connection(info[1], info[0])
                    if res.status_code in [200]:
                        # Mandar el mensaje de confirmacion
                        self._bo_connect_ok_handler(
                            socket, aes_key, info[0], info[1])

                    else:
                        print(f"\nLa conexion entre {info[0]} y {info[1]}\
                            no se ha podido establecer\n")
                        # Mandar el mensaje de que no ha sido posible
                        self._bo_connect_err_et_handler(
                            socket, aes_key, info[0], info[1])

            elif "DISCONNECT" == command:

                # si tengo otro peer en la peticion significa que es una peticion
                # de borrado de conexion entre Dron y et
                if len(info) == 2:

                    # Se llama a la api para borrar la conexion
                    res = ds_api.bo_del_connection(info[1], info[0])
                    if res.status_code in [200]:
                        print(
                            f"\nConexion entre {info[0]} e {info[1]} finalizada\n")

                        # Actualizamos el estado del drone en la base de datos
                        res = ds_api.dr_update_status(
                            info[0], "False", "100.0")
                        if res.status_code not in [200]:
                            print(
                                f"\nError al actualiza el status del DR {info[0]}\n")

                else:
                    peer = self.bo_get_peer_byKey(aes_key)
                    self._bo_disconnect_socket(peer)

            elif 'STATUS' == command:

                # Actualizamos el estado del drone en la base de datos
                res = ds_api.dr_update_status(info[0], info[1], info[2])
                if res.status_code not in [200]:
                    print(f"\nError al actualiza el status del DR {info[0]}\n")

            elif 'ET_INFO' == command:

                # Buscamos la info de la ET solicitada
                res = ds_api.get_et(info[0])
                if res.status_code == 200 and self.bo_get_peer_byName(info[0]) is not None:
                    self._bo_et_info_ok_handler(socket, aes_key, info[0],
                                                res.json()["ip"], str(res.json()["listen_port"]), res.json()["public_key"], info[1])
                else:
                    self._bo_et_info_err_handler(socket, aes_key, info[0])

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

    #####
    # FUNCIÓN: def _bo_accept_et_conn()
    # ARGS_IN: dst_peer - el peer del cual queremos aceptar la conexion
    #
    # DESCRIPCIÓN: Funcion que se encarga de mantenerse (en un hilo apartado),
    #              a la espera de mensajes del otro peer con el cual tiene una
    #              conexion establecida
    # ARGS_OUT:
    #####
    def _bo_accept_et_conn(self, dst_peer: dict):
        # Se compruba que tengamos un socket de conexion para ese peer
        if dst_peer['socket'] is not None:

            print(f"\nAceptando la conexion de {dst_peer['name']}...\n")

            thread_rcv = threading.Thread(
                target=self._bo_comunicate, args=(dst_peer['socket'], dst_peer['aes_key']))
            thread_rcv.start()

            self.threads.append(thread_rcv)

    #####
    # FUNCIÓN: def _bo_disconnect_socket()
    # ARGS_IN: peer - peer de conexion
    #
    # DESCRIPCIÓN: Busca el peer de conexion, cierra el socket y elimina el perr
    # ARGS_OUT:
    #####

    def _bo_disconnect_socket(self, peer):

        # Se comprueba el socket de conexion
        if peer["socket"] != None:
            print(f"\nCerrando conexion con {peer['name']}\n")
            s.socket_disconnect(peer["socket"])
            peer["socket"] = None
            self.dst_conn_peers.remove(peer)

        else:
            print("\nNo hace falta cerrar ninguna conexión...\n")

    # -------------------- FUNCIONES HANDLER --------------------

    #####
    # FUNCIÓN: def _bo_hello_ok_et_handler()
    # ARGS_IN: peer - peer de conexion temporal creado para la conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de HELLO OK a la et
    # ARGS_OUT:
    #####
    def _bo_hello_ok_et_handler(self, peer):

        if peer['socket'] is not None:

            # Enviamos mensaje de HELLO a la et junto
            # con la simetrica codificada en hexadecimal
            s.socket_send(
                peer['socket'], f"HELLO_OK")

        else:
            print(
                f'\nNo ha sido posible mandar la clave de sesion a la ET {peer["name"]}\n')

    #####
    # FUNCIÓN: def _bo_link_ok_et_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          type - tipo de entidad que envia el comando
    #          name - nombre de la entidad creada
    #          ip - ip de la entidad creada
    #          listen_port - puerto de la entidad creada
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK OK a la entidada que se ha linkeado
    # ARGS_OUT:
    #####
    def _bo_link_ok_et_handler(self, socket, aes_key, type, name, ip, listen_port):

        if socket is not None:

            # Ciframos el comando LINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"LINK_OK:{type}", name, ip, listen_port])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nERROR al mandar el mensaje de LINK OK\n')

    #####
    # FUNCIÓN: def _bo_connect_ok_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          dr_name - nombre del dron de la conexion creada
    #          et_name - nombre de la ET de la conexion creada
    # DESCRIPCIÓN: Envia el mensaje de CONNECT OK a la ET
    # ARGS_OUT:
    #####
    def _bo_connect_ok_handler(self, socket, aes_key, dr_name, et_name):

        if socket is not None:

            # Ciframos el comando CONNECT_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"CONNECT_OK", dr_name, et_name])

            # Enviamos mensaje de CONNECT_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nERROR al mandar el mensaje de CONNECT OK\n')

    #####
    # FUNCIÓN: def _bo_link_err_et_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          name - nombre de la entidad no creada
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK ERR a la entidada que se ha linkeado
    # ARGS_OUT:
    #####
    def _bo_link_err_et_handler(self, socket, aes_key, type, name):

        if socket is not None:

            # Ciframos el comando LINK_ERR junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"LINK_ERR", type, name])

            # Enviamos mensaje de LINK_ERR cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nERROR al mandar el mensaje de LINK ERR\n')

    #####
    # FUNCIÓN: def _bo_connect_err_et_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          name - nombre de la entidad no creada
    #
    # DESCRIPCIÓN: Envia el mensaje de CONNECT ERR a la ET de la conexion
    # ARGS_OUT:
    #####
    def _bo_connect_err_et_handler(self, socket, aes_key, dr_name, et_name):

        if socket is not None:

            # Ciframos el comando CONNECT_ERR junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"CONNECT_ERR", dr_name, et_name])

            # Enviamos mensaje de CONNECT_ERR cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f"\nERROR al mandar el mensaje de CONNECT ERR\n")

    #####
    # FUNCIÓN: def _bo_unlink_ok_et_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          type - tipo de entidad que envia el comando
    #          name - nombre del elemento a unlinkear
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK OK a la entidada que se ha linkeado
    # ARGS_OUT:
    #####
    def _bo_unlink_ok_et_handler(self, socket, aes_key, type, name):

        if socket is not None:

            # Ciframos el comando UNLINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, [f"UNLINK_OK:{type} {name}"])

            # Enviamos mensaje de UNLINK_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nERROR al mandar el mensaje de UNLINK OK\n')

    #####
    # FUNCIÓN: def _bo_disconnect_handler()
    # ARGS_IN: peer - peer de conexion
    #
    # DESCRIPCIÓN: Envia el mensaje de DISCONNECT a la et y cierra el socket
    # ARGS_OUT:
    #####
    def _bo_disconnect_handler(self, peer):

        if peer["socket"] is not None:

            print(f"\nCerrando conexion con {peer['name']}\n")

            # Ciframos el comando DISCONNECT junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], [f'CLOSE {self.bo_instance["name"]}'])

            # Enviamos mensaje de DISCONNECT
            s.socket_send(
                peer["socket"], f"{msg_enc} {nonce}")

        else:
            print(
                f"\nError al mandar el mensaje de DISCONNECT a {peer['name']}\n")

    #####
    # FUNCIÓN: def _bo_et_info_ok_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          name - nombre de la et solicitada
    #          ip - ip de la ET
    #          listen_port - puerto de la ET
    #          public_key - Public key de la ET
    #          type - Si se pide conexion para mandar msg o file
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK OK a la entidada que se ha linkeado
    # ARGS_OUT:
    #####
    def _bo_et_info_ok_handler(self, socket, aes_key, name, ip, listen_port, public_key, type):

        if socket is not None:

            print(f'\nEnviando la info solicitada de la ET {name}\n')
            # Ciframos el comando LINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, ["ET_INFO_OK", name, ip, listen_port, public_key, type])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible la info solicitada de la ET {name}\n')

    #####
    # FUNCIÓN: def _bo_et_info_err_handler()
    # ARGS_IN: socket - socket de conexion
    #          aes_key - clave de sesion
    #          name - nombre de la ET solicitada
    #
    # DESCRIPCIÓN: Envia el mensaje de LINK OK a la entidada que se ha linkeado
    # ARGS_OUT:
    #####
    def _bo_et_info_err_handler(self, socket, aes_key, name):

        if socket is not None:

            # Ciframos el comando LINK_OK junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                aes_key, ["ET_INFO_ERR", name])

            # Enviamos mensaje de LINK_OK cifrado a la ET
            s.socket_send(socket,
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nERROR al enviar la info de la ET solicitada\n')

    #####
    # FUNCIÓN: def _bo_msg_et_handler()
    # ARGS_IN: peer - peer de conexion
    #          et_name - nombre del drone
    #          msg - mensaje a enviar a la ET
    #
    # DESCRIPCIÓN: Envia el mensaje a la ET y cierra el socket
    # ARGS_OUT:
    #####
    def _bo_msg_et_handler(self, peer, et_name: str, msg: str):

        if peer["socket"] is not None:

            print(f'\nEnviando mensaje a la ET {et_name}\n')

            # Ciframos el comando de MSG junto con el mensaje
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["MSG", "BO", msg])

            # Enviamos mensaje de MSG_BY:BO cifrado a la ET
            # indicandole que tiene un mensaje
            s.socket_send(peer["socket"],
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible enviar el mensaje a la ET {et_name}\n')

    #####
    # FUNCIÓN: def _bo_file_et_handler()
    # ARGS_IN: peer - peer de conexion
    #          et_name - nombre de la ET
    #          filename - nombre del fichero a enviar a la et
    #
    # DESCRIPCIÓN: Envia el contenido del fichero a la et
    #              y cierra el socket
    # ARGS_OUT:
    #####
    def _bo_file_et_handler(self, peer, filename: str):

        if peer["socket"] is not None:

            print(f'\nEnviando fichero a la ET {peer["name"]}\n')

            # Abrir el archivo y leer su contenido
            with open(f"documents/{filename}", 'rb') as f:
                file_contents = f.read()

            # Ciframos el comando de FILE junto con el nombre del fichero
            msg_enc, msg_nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["FILE", "BO", filename])

            # Ciframos el contenido en bytes del file leido
            file_enc, file_nonce = ec.prepare_file_to_sent(
                peer["aes_key"], file_contents)

            # Enviamos mensaje de MSG_BY:BO cifrado a la ET
            # indicandole que tiene un mensaje
            s.socket_send(peer["socket"],
                          f"{msg_enc} {msg_nonce} {file_enc} {file_nonce}")

        else:
            print(
                f'\nNo ha sido posible enviar el fichero a la ET {peer["name"]}\n')

    #####
    # FUNCIÓN: def _bo_fly_et_handler()
    # ARGS_IN: peer - peer de conexion
    #          dr_name - nombre del drone
    #          et_name - nombre de la ET
    #
    # DESCRIPCIÓN: Envia el mensaje de FLY a la ET y cierra el socket
    # ARGS_OUT:
    #####

    def _bo_fly_et_handler(self, peer, dr_name: str, et_name: str):

        if peer["socket"] is not None:

            print(
                f'\nPidiendo a la ET {et_name} que indique volar al DR {dr_name}\n')

            # Ciframos el comando FLY junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["FLY", dr_name])

            # Enviamos mensaje de FLY cifrado a la ET
            s.socket_send(peer["socket"],
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible comunicarle a la ET {et_name} que indique volar al DR {dr_name}\n')

    #####
    # FUNCIÓN: def _bo_land_et_handler()
    # ARGS_IN: peer - peer de conexion
    #          dr_name - nombre del drone
    #          et_name - nombre de la ET
    #
    # DESCRIPCIÓN: Envia el mensaje de LAND a la ET y cierra el socket
    # ARGS_OUT:
    #####
    def _bo_land_et_handler(self, peer, dr_name: str, et_name: str):

        if peer["socket"] is not None:

            print(
                f'\nPidiendo a la ET {et_name} que mande aterrizar al dron {dr_name}\n')

            # Ciframos el comando LAND junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["LAND", dr_name])

            # Enviamos mensaje de LAND cifrado a la ET
            s.socket_send(peer["socket"],
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible indicarle a la ET {et_name} que mandar aterrizar al dron {dr_name}\n')

    #####
    # FUNCIÓN: def _bo_shutdown_et_handler()
    # ARGS_IN: peer - peer de conexion
    #          dr_name - nombre del drone
    #          et_name - nombre de la ET
    #
    # DESCRIPCIÓN: Envia el mensaje de shutdown a la ET y cierra el socket
    # ARGS_OUT:
    #####
    def _bo_shutdown_et_handler(self, peer, dr_name: str, et_name: str):

        if peer["socket"] is not None:

            print(
                f'\nPidiendo a la ET {et_name} que indique aterrizar y desconectar al DR {dr_name}\n')

            # Ciframos el comando SHUTDOWN junto con el resto de info
            # y obtenemos el nonce de cifrado
            msg_enc, nonce = ec.prepare_msg_to_sent(
                peer["aes_key"], ["SHUTDOWN", dr_name])

            # Enviamos mensaje de SHUTDOWN cifrado a la ET
            s.socket_send(peer["socket"],
                          f"{msg_enc} {nonce}")

        else:
            print(
                f'\nNo ha sido posible comunicarle a la ET {et_name} que indique aterrizar y desconectar al DR {dr_name}\n')

# -------------------- FUNCIONES AUXILIARES --------------------

#####
    # FUNCIÓN: def bo_set_fields()
    # ARGS_IN: listen_port - puerto servidor de la BO
    #          ip - ip de la BO
    #          private_key - clave privada de la BO
    #          secret_code - codigo secreto de la BO
    #
    # DESCRIPCIÓN: Establece los parametros en un diccionario de la instancia de clase
    # ARGS_OUT:
    #####
    def bo_set_fields(self, name: str, listen_port: int, ip: str, private_key: str, secret_code: str):
        self.bo_instance['name'] = name
        self.bo_instance['listen_port'] = listen_port
        self.bo_instance['ip'] = ip
        self.bo_instance['private_key'] = private_key
        self.bo_instance['secret_code'] = secret_code

    #####
    # FUNCIÓN: def bo_get_peer_byName()
    # ARGS_IN: name - nombre que identifica el otro extremo del peer
    #
    # DESCRIPCIÓN: Obtiene la instancia de un peer a partir de su nombre
    # ARGS_OUT: El peer dado un name
    #####
    def bo_get_peer_byName(self, name: str):
        for peer in self.dst_conn_peers:
            if peer['name'] == name:
                return peer

        return None

    #####
    # FUNCIÓN: def bo_get_peer_byKey()
    # ARGS_IN: name - nombre que identifica el otro extremo del peer
    #
    # DESCRIPCIÓN: Obtiene la instancia de un peer a partir de su aes_key
    # ARGS_OUT: El peer dado un name
    #####
    def bo_get_peer_byKey(self, key):
        for peer in self.dst_conn_peers:
            if peer['aes_key'] == key:
                return peer

        return None

    @classmethod
    def close_threads(cls):
        cls.running = False

        for thread in cls.threads:
            if thread.is_alive():
                thread.join()
