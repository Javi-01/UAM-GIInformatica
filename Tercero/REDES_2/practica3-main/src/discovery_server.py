from . import sockets as s

#####
# CLASS: class DiscoveryServer
# ARGS_CLASS: ds_addr - direccion IP del servidor de descubrimiento
#             ds_port - puerto de escucha del servidor de descubrimiento
# DESCRIPCIÓN: Clase que contempla la funcionalidad del servidor de descubrimiento
#####


class DiscoveryServer:
    ds_addr = None
    ds_port = None

    def __init__(self, server_addr, server_port):
        self.ds_addr = server_addr
        self.ds_port = server_port

    #####
    # FUNCIÓN: def ds_register_user
    # ARGS_IN: user_nick - nick del usuario en el servidor de descubrimiento
    #          user_ip - ip del usuario en el servidor de descubrimiento
    #          user_port - puerto del usuario en el servidor de descubrimiento
    #          user_password - contraseña del usuario en el servidor de descubrimiento
    #          protocol_version - version de implementacion de la practica soportado
    # DESCRIPCIÓN: Funcion que registra a un usuario en el servidor de descubrimiento
    # ARGS_OUT:
    #####

    def ds_register_user(self, user_nick, user_ip, user_port, user_password, protocol_version):

        socket_ds = ds_establish_conn(self.ds_addr, self.ds_port)
        if socket_ds is None:
            print("[ERROR] Conexion - Servidor Descubrimiento")
            return None

        data = f"REGISTER {user_nick} {user_ip} \
                    {user_port} {user_password} {protocol_version}"

        res = s.socket_comunicate(socket_ds, data)
        if res.startswith("NOK WRONG_PASS"):
            print("[ERROR] Registro - Servidor Descubrimiento")
            return None

        self.ds_quit_connection(socket_ds)
        s.socket_disconnect(socket_ds)

        print("[OK] Registro - Servidor Descubrimiento -", res)
        return res

    #####
    # FUNCIÓN: def ds_query_user
    # ARGS_IN: user_nick - nick del usuario en el servidor de descubrimiento
    # DESCRIPCIÓN: Funcion que busca a un usuario en el servidor de descubrimiento
    #              por su nick
    # ARGS_OUT:
    #####

    def ds_query_user(self, user_nick: str):
        socket_ds = ds_establish_conn(self.ds_addr, self.ds_port)
        if socket_ds is None:
            print("[ERROR] Conexion - Servidor Descubrimiento")
            return None

        data = f"QUERY {user_nick}"

        res = s.socket_comunicate(socket_ds, data)
        if res.startswith("NOK USER_UNKNOWN"):
            print("[ERROR] Usuario no Encontrado - Servidor Descubrimiento")
            return None

        self.ds_quit_connection(socket_ds)
        s.socket_disconnect(socket_ds)

        print("[OK] Usuario Encontrado - Servidor Descubrimiento")
        res = res.split(" ")[2:]

        return {
            "username": res[0], "address": res[1], "port": res[2],
            "version": res[3]
        }

    #####
    # FUNCIÓN: def ds_list_users
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que busca a todos los usuario en el servidor de
    #              descubrimiento
    # ARGS_OUT:
    #####

    def ds_list_users(self):

        socket_ds = ds_establish_conn(self.ds_addr, self.ds_port)
        if socket_ds is None:
            print("[ERROR] Conexion - Servidor Descubrimiento")
            return None

        res = s.socket_comunicate(socket_ds, "LIST_USERS")
        if res.startswith("NOK USER_UNKNOWN"):
            print("[ERROR] Listar usuarios - Servidor Descubrimiento")
            return None

        self.ds_quit_connection(socket_ds)
        s.socket_disconnect(socket_ds)

        users = res[17:].split("#")[:-1]
        print("[OK] Listar Usuarios - Servidor Descubrimiento")
        return ds_dict_users(users)

    #####
    # FUNCIÓN: def ds_quit_connection
    # ARGS_IN: socket - socket de la conexion que se quiere cerrar
    # DESCRIPCIÓN: Funcion manda a cerrar la conexion con el servidor de
    #              descubrimiento
    # ARGS_OUT:
    #####
    def ds_quit_connection(self, socket):
        return s.socket_comunicate(socket, "QUIT")


def ds_establish_conn(ds_addr, ds_port):
    sock = s.socket_create()
    if sock is not None:
        sock = s.socket_connect(sock, ds_addr, ds_port)
        if sock is not None:
            return sock

    return None


def ds_dict_users(users):
    users_list = []
    for user in users:
        user = user.split(" ")
        users_list.append({
            "username": user[0], "address": user[1], "port": user[2],
        })
    return users_list
