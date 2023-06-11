from src.peers import BO as bp
import signal
import sys


def signal_handler(sig, frame):
    print('\nSaliendo...')
    bo.bo_logout()
    bo.close_threads()
    sys.exit(0)


def main():

    # instancia vacia del ET
    global bo
    bo = bp.BO()

    # Capturando la señal de Ctrl+C
    signal.signal(signal.SIGINT, signal_handler)
    # Capturando la señal de Ctrl+Z
    signal.signal(signal.SIGTSTP, signal_handler)

    try:
        while True:

            print(end="\n\n")
            print('send', "-> Enviar archivo o mensaje")
            print('fly', "-> Mandar volar a un dron")
            print('land', "-> Manda aterrizar a un dron")
            print('status', "-> Obtener el estado de las ETs y los DRs")
            print('shutdown', "-> Manda aterrizar y desconectar a todos los drones")
            print('exit', "-> Salir del programa")
            print(end="\n\n")

            choice = input("Escribe el comando que desea ejecutar:")

            if choice == 'send':
                et_name = input("Escribe el nombre de la ET: ")
                type = input(
                    "Desea enviar un archivo (1), una cadena (2) o cancelar (introduce cualquier cosa): ")

                if type == '1':
                    file_name = input("Escribe el nombre del archivo: ")
                    bo.bo_send_file(et_name, file_name)
                elif type == '2':
                    msg = input("Escribe el mensaje: ")
                    bo.bo_send_msg(et_name, msg)
                else:
                    print("Cancelando envio de archivo o mensaje")

            elif choice == 'fly':
                dron = input("Introduce el nombre del dron: ")
                bo.bo_fly_drone(dron)

            elif choice == 'land':
                dron = input("Introduce el nombre del dron: ")
                bo.bo_land_drone(dron)

            elif choice == 'status':
                bo.get_ets_status()
                bo.get_drones_status()

            elif choice == 'shutdown':
                bo.bo_shutdown()

            elif choice == 'exit':
                print('\nSaliendo...')
                bo.bo_logout()
                bo.close_threads()
                sys.exit(0)

            else:
                print("\nOpcion Incorrecta, Intentalo de nuevo\n")

            input("\n(ENTER) para continuar\n")

    finally:
        exit()


if __name__ == '__main__':
    main()
