from appJar import gui
from src import discovery_server as ds, TCP_Control as tc, sockets as s, UDP_Transmition as tr
import threading
import signal
import sys

#####
# CLASS: class Frame
# ARGS_CLASS: windows_size - La instancia de la interfaz gui
#             ds_server - La instancia de la clase del servidor de descubrimiento
#             tcp_control - La instancia de la clase del TCP_control
# DESCRIPCIÓN: Clase que contiene la logica de la aplicacion con la interfaz grafica
#####


class Frame:

    app = None
    ds_server = None
    tcp_control = None

    caller_user = None
    caller_port = None

    def __init__(self, window_size, ds_server, ds_port):
        # Se crea la ventana general
        self.app = gui("Practica 3 - Redes2", window_size)
        self.app.setFont(size=16)
        self.ds_server = ds.DiscoveryServer(
            server_addr=ds_server, server_port=ds_port)
        # Se crea el frame al estilo card-layout
        self.frame_create()
        signal.signal(signal.SIGINT, self.frame_signal_handler)

    #####
    # FUNCIÓN: def frame_start
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de lanzar la aplicacion con su primera
    #               pantalla de login
    # ARGS_OUT:
    #####
    def frame_start(self):
        self.frame_register_user()
        self.app.go()

    #####
    # FUNCIÓN: def frame_signal_handler
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de cerrar los elementos de manera
    #              correcta en caso de CTRL + C
    # ARGS_OUT:
    #####
    def frame_signal_handler(self, sig, any):
        # Se para la transmision UDP en caso de tenerla abierta

        try:
            self.tcp_control.ctrl_call_end()
        except AttributeError:
            pass
        s.socket_disconnect(self.tcp_control.server_socket)
        self.tcp_control = None
        self.app.stop()
        sys.exit()

    #####
    # FUNCIÓN: def frame_create
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de crear los distintos frames que tiene
    #              la aplicacion, en forma de cardLayout
    # ARGS_OUT:
    #####
    def frame_create(self):
        # Card de Inicio de Sesion
        self.app.addStatusbar(fields=1)
        with self.app.frame("Inicio de Sesion", 0, 0):
            self.app.addLabel("Inicio de Sesion", row=0, colspan=3)
            self.app.addLabelEntry("Usuario", row=1, colspan=3)
            self.app.addLabel("", row=2, colspan=3)
            self.app.addLabelSecretEntry("Contraseña", row=3, colspan=3)
            self.app.addLabel(" ", row=4, colspan=3)
            self.app.addLabel("       ", row=5, colspan=3)
            self.app.setFocus("Usuario")

            self.app.addButtons(["Iniciar Sesion", "Salir"],
                                funcs=self.listener_register_user, row=6, colspan=3)
        # Card de Pagina Principal
        with self.app.frame("Pagina Principal", 0, 0):
            self.app.addLabel(title="username", row=0, colspan=4)
            self.app.addListBox("Usuarios", [], row=1, colspan=4)
            self.app.addLabel("  ", row=2)
            self.app.addLabelEntry(
                "Buscar", row=3, colspan=4)
            self.app.addLabel("   ", row=4)
            self.app.addRadioButton("FPS", "10 FPS", row=5, column=0)
            self.app.addRadioButton("FPS", "15 FPS", row=5, column=1)
            self.app.addRadioButton("FPS", "20 FPS", row=5, column=2)
            self.app.addRadioButton("FPS", "24 FPS", row=5, column=3)

            self.app.addButtons(["Llamar", "Volver"],
                                funcs=self.listener_select_user, row=6, colspan=4)

        # Card de llamada entrante
        with self.app.frame("Llamada Entrante", 0, 0):
            self.app.addLabel(title="caller")
            self.app.addButtons(["Aceptar", "Rechazar"],
                                funcs=self.listener_process_call, row=2, colspan=4)

        with self.app.frame("Llamando Usuario", 0, 0):
            self.app.addLabel(title="called")

        # Card de interfaz de llamada
        with self.app.frame("Pagina Llamada", 0, 0):
            self.app.addImage("video_src", "media/cargando.gif",
                              row=0, column=0, rowspan=2, colspan=2)
            self.app.addImage("video_dst", "media/cargando.gif",
                              row=0, column=2, rowspan=2, colspan=2)
            self.app.addLabel("src_user", row=3)
            self.app.addLabel("dst_user", row=3, column=2)
            self.app.addLabel("           ", row=4, colspan=4)
            self.app.addButtons(
                ["Mandar Video", "Pausar", "Colgar"], funcs=self.listener_active_call, row=5, colspan=4)

        # Card de interfaz de llamada pausada
        with self.app.frame("Pagina Llamada Pausada", 0, 0):
            self.app.addImage("paused_src", "media/pause.gif",
                              row=0, column=0, rowspan=2, colspan=2)
            self.app.addImage("paused_dst", "media/cargando.gif",
                              row=0, column=2, rowspan=2, colspan=2)
            self.app.addLabel("src_paused_user", row=3)
            self.app.addLabel("dst_paused_user", row=3, column=2)
            self.app.addLabel("        ", row=4, colspan=4)
            self.app.addButtons(
                ["Reanudar", "Colgar "], funcs=self.listener_paused_call, row=5, colspan=4)

    #####
    # FUNCIÓN: def frame_register_user
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el frame del registro
    # ARGS_OUT:
    #####
    def frame_register_user(self):
        self.app.raiseFrame("Inicio de Sesion")

    #####
    # FUNCIÓN: def frame_register_user_error
    # ARGS_IN: message=None - se establece si se quiere añadir mensaje
    # DESCRIPCIÓN: Funcion que se encarga de mostrar mensaje de error y limpiar
    #               los campos del login
    # ARGS_OUT:
    #####
    def frame_register_user_error(self, message=None):
        if message:
            self.app.errorBox("Error", message)
        self.app.clearEntry("Usuario")
        self.app.clearEntry("Contraseña")
        self.app.setFocus("Usuario")

    #####
    # FUNCIÓN: def frame_list_users
    # ARGS_IN: user_nick - el usuario que inicia la aplicacion
    # DESCRIPCIÓN: Funcion que se encarga de añadir los usuarios en una lista
    #              desplegable y mostrar el menu principal
    # ARGS_OUT:
    #####
    def frame_list_users(self, user_nick):

        self.app.setLabel(name="username", text="Bienvenido " + user_nick)
        self.app.clearEntry("Buscar")
        self.app.clearListBox(title="Usuarios", callFunction=False)

        list_users = self.ds_server.ds_list_users()
        if list_users is None:
            self.frame_register_user_error(
                "Se ha producido un error al cargar el menu, intentalo mas tarde")
            return

        self.app.addListItems(title="Usuarios", items=[
                              user["username"] for user in list_users if user["username"] != user_nick])

        self.app.raiseFrame("Pagina Principal")

    #####
    # FUNCIÓN: def frame_home_msg_dialog
    # ARGS_IN: header - la cabecera del pop up
    #          message - mensaje que se envia con la informacion necesaria
    # DESCRIPCIÓN: Funcion que se encarga de mostrar un pop up flotante en el
    #              home page
    # ARGS_OUT:
    #####
    def frame_home_msg_dialog(self, header, message):
        self.app.errorBox(header, message)

    #####
    # FUNCIÓN: def frame_entry_call
    # ARGS_IN: dst_user - el nick del usuario que me llama
    #          dst_port - el puerto del usuario que me llama
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el frame de llamada entrante
    # ARGS_OUT:
    #####
    def frame_entry_call(self, dst_user, dst_port):

        self.app.setLabel(
            name="caller", text="Llamada entrante... " + dst_user)

        self.caller_port = dst_port
        self.caller_user = dst_user
        self.app.raiseFrame("Llamada Entrante")

    #####
    # FUNCIÓN: def frame_active_call
    # ARGS_IN: user_nick - nick del usuario al que se llama
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el frame de llamando a usuario
    # ARGS_OUT:
    #####
    def frame_calling_user(self, user_nick):
        self.app.setLabel(
            name="called", text="Llamando usuario " + user_nick + " ...")
        self.app.raiseFrame("Llamando Usuario")

    #####
    # FUNCIÓN: def frame_active_call
    # ARGS_IN: src_user - los datos del peer llamador
    #          dst_user - los datos del peer destino
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el frame de llamada activa
    # ARGS_OUT:
    #####
    def frame_active_call(self, src_user, dst_user):
        self.app.setLabel(
            name="src_user", text=src_user)
        self.app.setLabel(
            name="dst_user", text=dst_user)
        self.app.raiseFrame("Pagina Llamada")

    #####
    # FUNCIÓN: def frame_paused_call
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el frame de llamada pausada
    # ARGS_OUT:
    #####
    def frame_paused_call(self, src_user, dst_user):
        self.app.setLabel(
            name="src_paused_user", text=src_user)
        self.app.setLabel(
            name="dst_paused_user", text=dst_user)
        self.app.raiseFrame("Pagina Llamada Pausada")

    #####
    # FUNCIÓN: def update_src_call_frame
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de establecer el fotograma de video
    #              emitido por el usuario
    # ARGS_OUT:
    #####
    def update_src_call_frame(self, frame):
        self.app.setImageData("video_src", frame, fmt="PhotoImage")

    #####
    # FUNCIÓN: def update_dst_call_frame
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el fotograma del usuario destino
    # ARGS_OUT:
    #####
    def update_dst_call_frame(self, frame):
        self.app.setImageData("video_dst", frame, fmt="PhotoImage")

    #####
    # FUNCIÓN: def update_dst_call_frame_onPause
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de mostrar el fotograma del usuario destino
    #              cuando yo estoy en pausa
    # ARGS_OUT:
    #####
    def update_dst_call_frame_onPause(self, frame):
        self.app.setImageData("paused_dst", frame, fmt="PhotoImage")

    #####
    # FUNCIÓN: def frame_get_FPS
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de obtener los FPS de la seleccion del usuario
    # ARGS_OUT:
    #####
    def frame_get_FPS(self):
        return self.app.getRadioButton("FPS").split(" ")[0]

    #####
    # FUNCIÓN: def update_status_bar
    # ARGS_IN:
    # DESCRIPCIÓN: Funcion que se encarga de actualizar la barra de estado
    # ARGS_OUT:
    #####
    def update_status_bar(self, FPS):
        self.app.setStatusbar("FPS: " + FPS, 0)
    #####
    # FUNCIÓN: def listener_active_call
    # ARGS_IN: btn - el boton por el cual se ha accionado el listener
    # DESCRIPCIÓN: Funcion que se encarga de aplicar la logica de procesar si el
    #              usuario ha reanudado la llamada, o ha colgado la misma
    # ARGS_OUT:
    #####

    def listener_active_call(self, btn):
        if btn == "Mandar Video":
            # Se enseña una caja para seleccionar un video
            video_path = self.app.openBox(title="mandar video",
                                          fileTypes=[("videos", "*.mp4"),
                                                     ("videos", "*.mpeg")]
                                          )
            if video_path != "":
                # Se guarda en la variable de video para que se empiece a enviar
                self.tcp_control.udp_transmition.capture_video = video_path

        elif btn == "Pausar":
            # Se comunica por el canal de control que se pausa la llamada
            self.tcp_control.ctrl_call_hold()
            # Se coloca el frame de llamada pausada
            self.frame_paused_call(self.tcp_control.ctrl_get_src_peer_field(
                "username"), self.tcp_control.ctrl_get_dst_peer_field("username"))

        elif btn == "Colgar":
            # Se comunica por el canal de control que se termina la llamada
            self.tcp_control.ctrl_call_end()

            # Se coloca el frame del home
            self.frame_list_users(
                self.tcp_control.ctrl_get_src_peer_field("username"))
    #####
    # FUNCIÓN: def listener_paused_call
    # ARGS_IN: btn - el boton por el cual se ha accionado el listener
    # DESCRIPCIÓN: Funcion que se encarga de aplicar la logica de procesar si el
    #              usuario ha reanudado la llamada, o ha colgado la misma
    # ARGS_OUT:
    #####

    def listener_paused_call(self, btn):
        if btn == "Reanudar":
            # Se comunica por el canal de control que se reanuda la llamada
            self.tcp_control.ctrl_call_resume()
            # Se coloca el frame de llamada pausada
            self.frame_active_call(self.tcp_control.ctrl_get_src_peer_field(
                "username"), self.tcp_control.ctrl_get_dst_peer_field("username"))

        elif btn == "Colgar ":
            # Se comunica por el canal de control que se termina la llamada
            self.tcp_control.ctrl_call_end()
            # Se coloca el frame de llamada pausada
            self.frame_list_users(
                self.tcp_control.ctrl_get_src_peer_field("username"))
    #####
    # FUNCIÓN: def listener_process_call
    # ARGS_IN: btn - el boton por el cual se ha accionado el listener
    # DESCRIPCIÓN: Funcion que se encarga de aplicar la logica de procesar una llamada
    #              en funcion de si el usuario ha aceptado o rechazado la misma
    # ARGS_OUT:
    #####

    def listener_process_call(self, btn):

        # Si se acepta la llamada, se manda un accept
        if btn == "Aceptar":
            # Se envia la aceptacion de la llamada
            self.tcp_control.ctrl_call_accept()
            # Guarda los datos del usuario destino
            user = self.ds_server.ds_query_user(self.caller_user)
            print(user, self.caller_port)
            self.tcp_control.ctrl_set_peer(
                self.ds_server.ds_query_user(self.caller_user), dst=True)
            # Se establece un hilo de comunicacion que recibe mensajes de control
            threading.Thread(
                target=self.tcp_control.ctrl_TCP_comunicate).start()
            # Se crea la instancia de envio y recepcion de paquetes UDP en este caso
            # en el lado del receptor de la llamada, que es aquel que la acepta
            self.tcp_control.udp_transmition = None
            udp_transmition = tr.UDPTransmition(
                self, self.tcp_control, int(self.caller_port))
            self.tcp_control.udp_transmition = udp_transmition
            # Se pasa al frame de llamada
            self.frame_active_call(self.tcp_control.ctrl_get_src_peer_field(
                "username"), self.tcp_control.ctrl_get_dst_peer_field("username"))
            return

        elif btn == "Rechazar":
            # Si el usuario rechaza, envia el deny y reestablece las variables auxiliares
            # del usuario
            self.tcp_control.ctrl_call_deny()
            self.caller_port = None
            self.caller_user = None
            # Se contruye el frame del home
            self.frame_list_users(
                self.tcp_control.ctrl_get_src_peer_field("username"))

    #####
    # FUNCIÓN: def listener_select_user
    # ARGS_IN: btn - el boton por el cual se ha accionado el listener
    # DESCRIPCIÓN: Funcion que se encarga de aplicar la logica de llamar a alguien
    #              en funcion de la eleccion del usuario a llamar
    # ARGS_OUT:
    #####
    def listener_select_user(self, btn):
        if btn == "Llamar":
            # Se coge el usuario de la caja de busqueda
            if self.app.getEntry("Buscar") != "":
                user = self.ds_server.ds_query_user(
                    self.app.getEntry("Buscar"))
            # SI no ha introducido nada, se coge el de menu de opciones
            else:
                user = self.ds_server.ds_query_user(
                    self.app.getListBox("Usuarios")[0])

            # Se establece la conexion TCP
            if self.tcp_control.ctrl_TCP_connect(user) is None:
                self.frame_home_msg_dialog(
                    "Error", "El usuario no se encuentra conectado")
                return
            # Una vez nos conectamos,se llama al usuario mientras se le
            # enseña la interfaz de estar llamando
            threading.Thread(
                target=self.tcp_control.ctrl_call_peer).start()

            self.frame_calling_user(user["username"])

        elif btn == "Volver":
            s.socket_disconnect(self.tcp_control.server_socket)
            self.tcp_control.ds_server = None
            self.frame_register_user_error()
            self.app.raiseFrame("Inicio de Sesion")

    #####
    # FUNCIÓN: def listener_register_user
    # ARGS_IN: btn - el boton por el cual se ha accionado el listener
    # DESCRIPCIÓN: Funcion que se encarga de aplicar la logica de registrar un
    #              usuario en el servidor segun los campos introducidos
    # ARGS_OUT:
    #####
    def listener_register_user(self, btn):
        if btn == "Iniciar Sesion":
            # Se cogen los campos de usuario y contraseña
            user_nick = self.app.getEntry("Usuario")
            user_password = self.app.getEntry("Contraseña")

            # Se crea el nuevo usuario si los campos son validos
            if user_nick != "" and user_password != "":
                config = s.socket_get_config()
                print(config)
                if self.ds_server.ds_register_user(
                        user_nick, config[0],
                        config[1], user_password, "V0") is None:
                    self.frame_register_user_error(
                        "El nombre de usuario ya esta seleccionado")
                    return

                # Se inicia la conexion de recepcion de llamadas, para ello, primero
                # me busco a mi mismo en el servidor de descubrimiento
                user = self.ds_server.ds_query_user(user_nick)
                if user is None:
                    self.frame_register_user_error(
                        "Se ha producido un error iniciando sesion")
                    return
                self.tcp_control = tc.TCPControl(user, self, self.ds_server)

                # Se manda al siguiente template del home
                self.frame_list_users(user["username"])
            else:
                self.frame_register_user_error(
                    "Los campos de registro no pueden estar vacios")

        elif btn == "Salir":
            self.app.stop()
