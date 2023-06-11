from src.peers import ET as ets
import getpass
import signal
import sys
import time


def signal_handler(sig, frame):
    print('\nSaliendo...')
    et.et_logout()
    et.close_threads()
    sys.exit(0)


def main():

    # instancia vacia del ET
    global et
    et = ets.ET()

    # Capturando la señal de Ctrl+C
    signal.signal(signal.SIGINT, signal_handler)
    # Capturando la señal de Ctrl+Z
    signal.signal(signal.SIGTSTP, signal_handler)

    try:
        while True:

            print(end="\n\n")

            if et.logged_in == False:
                print('link', "-> Linkear ET")
                print('login', "-> Login de una ET con usuario y contraseña")
            else:
                print('unlink', "-> Unlink la ET", et.et_instance['name'])
                print('logout', "-> Logout de la ET", et.et_instance['name'])
                print('fly', "-> Manda volar el dron con el cual estas conectado")
                print('land', "-> Manda aterrizar el dron con el cual estas conectado")
                print('disconnect',
                      "-> Se desconecta del Dron al que estas conectado ")
                print('send', "-> Enviar archivo o mensaje")
            print(end="\n\n")

            choice = input("Escribe el comando que desea ejecutar: ")

            if choice == 'link':
                et_name = input("Introduce el nombre de la nueva ET: ")
                et_password = getpass.getpass(
                    "Introduce la contraseña de la nueva ET: ")

                et.et_link(et_name, et_password)
                time.sleep(2)

                continue

            elif choice == 'login':
                et_name = input("Introduce el nombre de la ET: ")
                et_password = getpass.getpass(
                    "Introduce la contraseña de la ET: ")
                et.et_login(et_name, et_password)

                continue

            elif choice == 'unlink':
                et.et_unlink_bo()

                continue

            elif choice == 'logout':
                print('\nSaliendo...')
                et.et_logout()
                et.close_threads()
                sys.exit(0)

            elif choice == 'fly':
                dr_name = input("Introduce el nombre del dron a volar: ")
                et.et_fly_drone(dr_name)

                continue

            elif choice == 'land':
                dr_name = input("Introduce el nombre del dron a aterrizar: ")
                et.et_land_drone(dr_name)

                continue

            elif choice == 'disconnect':
                dr_name = input("Introduce el nombre del dron a desconectar: ")
                et.et_disconnect_drone(dr_name)

                continue

            if choice == 'send':
                rec_type = input(
                    "Desea enviar a otra ET (1), a la BO (2) o cancelar (introduce cualquier cosa): ")

                if rec_type == '1':
                    name = input("Escribe el nombre de la ET: ")
                elif rec_type == '2':
                    name = "bo"
                else:
                    print("Cancelando envio de archivo o mensaje")
                    continue

                type = input(
                    "Desea enviar un archivo (1), una cadena (2) o cancelar (introduce cualquier cosa): ")

                if type == '1':
                    file_name = input("Escribe el nombre del archivo: ")
                    et.et_send_file(name, rec_type, file_name)
                elif type == '2':
                    msg = input("Escribe el mensaje: ")
                    et.et_send_msg(name, rec_type, msg)
                else:
                    print("Cancelando envio de archivo o mensaje")

                continue

            else:
                print("\nOpcion Incorrecta, Intentalo de nuevo\n")

            input("\n(ENTER) para continuar\n")

    finally:
        exit()


if __name__ == "__main__":
    main()
