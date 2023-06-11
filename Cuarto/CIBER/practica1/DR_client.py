from src.peers import DR as dr
import getpass
import signal
import sys
import time


def signal_handler(sig, frame):
    print('\nSaliendo...')
    drone.dr_logout()
    drone.close_threads()
    sys.exit(0)


def main():

    # instancia vacia del ET
    global drone
    drone = dr.DR()

    # Capturando la señal de Ctrl+C
    signal.signal(signal.SIGINT, signal_handler)
    # Capturando la señal de Ctrl+Z
    signal.signal(signal.SIGTSTP, signal_handler)

    try:
        while True:

            print(end="\n\n")

            if drone.logged_in == False:
                print('link', "-> Linkear un nuevo Dron a una ET disponible")
                print('login', "-> Login de un Dron con usuario y contraseña")
            else:
                print(
                    'link', f"-> Linkear Dron {drone.drone_instance['name']} a una ET disponible")
                print(
                    'unlink', f"-> Unlinkear Dron {drone.drone_instance['name']} a una ET disponible")
                print('logout', "-> Logout del Dron",
                      drone.drone_instance['name'])
                if drone.active_conn == False:
                    print('connect', "-> Conectarse a un ET disponible")
                else:
                    print('disconnect',
                          "-> Se desconecta de la ET a la que esta conectada")
            print(end="\n\n")

            choice = input("Escribe el comando que desea ejecutar: ")

            if choice == 'link':
                if drone.logged_in == False:
                    dr_name = input("Introduce el nombre del nuevo Dron: ")
                    dr_password = getpass.getpass(
                        "Introduce la contraseña del nuevo Dron: ")

                et_name = input(
                    "Introduce el nombre del ET al que te quieres linkear: ")

                if drone.logged_in == False:
                    drone.dr_link(dr_name, dr_password, et_name)
                else:
                    drone.dr_only_link(et_name)

                time.sleep(3)

                continue

            elif choice == 'login':
                dr_name = input("Introduce el nombre del Dron: ")
                dr_password = getpass.getpass(
                    "Introduce la contraseña del Dron: ")
                drone.dr_login(dr_name, dr_password)

                continue

            elif choice == 'unlink':
                et_name = input(
                    "Introduce el nombre del ET al que te quieres unlinkear: ")
                drone.dr_unlink(et_name)

                continue

            elif choice == 'logout':
                print('\nSaliendo...')
                drone.dr_logout()
                drone.close_threads()
                sys.exit(0)

            elif choice == 'connect':
                et_name = input(
                    "Introduce el nombre del ET al que te quieres conectar: ")
                drone.dr_connect_et(et_name)

                continue

            elif choice == 'disconnect':
                drone.dr_disconnect_et()
                continue

            else:
                print("\nOpcion Incorrecta, Intentalo de nuevo\n")

            input("\n(ENTER) para continuar\n")

    finally:
        exit()


if __name__ == "__main__":
    main()
