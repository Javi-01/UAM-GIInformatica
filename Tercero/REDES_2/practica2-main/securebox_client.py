from src import (files_local_managment as flm,
                 files_remote_management as frm, users_managment as um)
import argparse
import sys


def main():
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers()

    """GESTION DE USUARIOS"""
    # Argumento relacionado con la creacion del fichero de configuracion de
    # usuario y añadir el Token de autorizacion de uso de la API
    command = subparsers.add_parser(
        "create_user", help="crear un usuario en local con token de autorizacion 'token'")
    command.add_argument("token", type=str)
    command.set_defaults(handler=um.user_create)

    # Argumento relacionado con la creacion de un usuario en la API
    command = subparsers.add_parser(
        "create_id", help="crea un usuario en el servidor con nombre 'name' e email 'email'")
    command.add_argument("name", type=str)
    command.add_argument("email", type=str)
    command.set_defaults(handler=um.user_register)

    # Argumento relacionado con la busqueda de los usuarios coincidentes
    command = subparsers.add_parser(
        "search_id", help="busca las coincidencias de usuarios con el texto 'data'")
    command.add_argument("data", type=str)
    command.set_defaults(handler=um.user_search)

    # Argumento relacionado con la eliminacion de un usuario
    command = subparsers.add_parser(
        "delete_id", help="borra el usuario con id 'user_id'")
    command.add_argument("user_id", type=str)
    command.set_defaults(handler=um.user_delete)

    """GESTION DE FICHEROS REMOTO"""
    # Argumento relacionado con la subida de un fichero a un ID destino del
    # servidor
    command = subparsers.add_parser(
        "dest_id", help="Sube en el servidor al usuario receptor con id 'user_id', el fichero con nombre '--upload'")
    command.add_argument("user_id", type=str)
    command.add_argument("--upload", type=str)
    command.set_defaults(handler=frm.file_upload)

    # Argumento relacionado con la bajada de un fichero a un ID destino del
    # servidor
    command = subparsers.add_parser(
        "download", help="Descarga el fichero del servidor con id 'file_id', del emisor con id '--source_id'")
    command.add_argument("file_id", type=str)
    command.add_argument("--source_id", type=str)
    command.set_defaults(handler=frm.file_download)

    # Argumento relacionado con borrar un fichero del servidor
    command = subparsers.add_parser(
        "delete_file", help="Borra el fichero con id 'file_id' del usuario")
    command.add_argument("file_id", type=str)
    command.set_defaults(handler=frm.file_delete)

    # Argumento relacionado con el listado de ficheros del servidor
    command = subparsers.add_parser(
        "list_files", help="Lista los ficheros del usuario")
    command.set_defaults(handler=frm.file_list)

    """GESTION DE FICHEROS LOCAL"""
    # Argumento relacionado con el cifrado de un fichero local
    command = subparsers.add_parser(
        "encrypt", help="Encripta el fichero en local con nombre 'file_name'")
    command.add_argument("file_name", type=str)
    command.set_defaults(handler=flm.encrypt_local)

    # Argumento relacionado con la firma de un fichero
    command = subparsers.add_parser(
        "sign", help="Firma un fichero en local con nombre 'file_name'")
    command.add_argument("file_name", type=str)
    command.set_defaults(handler=flm.sign_local)

    # Argumento relacionado con el cifrado de un fichero y firma de un fichero
    command = subparsers.add_parser(
        "enc_sign", help="Encripta y Firma el fichero en local con nombre 'file_name'")
    command.add_argument("file_name", type=str)
    command.set_defaults(handler=flm.encrypt_sign_local)

    # Argumento relacionado con el descifrado del fichero en local
    command = subparsers.add_parser(
        "decrypt", help="Desencripta el fichero en local con nombre 'file_name'")
    command.add_argument("file_name", type=str)
    command.set_defaults(handler=flm.decrypt_local)

    # Argumento relacionado con la validacion de la firma
    command = subparsers.add_parser(
        "unsign", help="Comprueba la firma de un fichero en local con nombre 'file_name'")
    command.add_argument("file_name", type=str)
    command.set_defaults(handler=flm.unsign_local)

    args = parser.parse_args(args=sys.argv[1:])
    args.handler(args)


if __name__ == "__main__":
    main()
