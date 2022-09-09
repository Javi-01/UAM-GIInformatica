from .users_managment import user_get_token

from . import api_endopints as api, encryption as ec
from routes import (FILES_ROOT, REMOTE_SIGNNED, REMOTE_UPLOADS,
                    REMOTE_DOWNLOADS_ENC, REMOTE_DOWNLOADS_DEC)

#####
# FUNCIÓN: file_get_name(user_token: str, file_id: str)
# ARGS_IN: user_token - el id de autorizacion del cliente
#          file_id - el id del fichero a buscar
# DESCRIPCIÓN: Funcion que realiza una busqueda del nombre de los ficheros
#              de un cliente
# ARGS_OUT: None si no encuentra el nombre y el filename en caso de encontrarlo
#####


def file_get_name(user_token: str, file_id: str):
    res = api.list_file(user_token)
    res_headers = res.json()

    for file in res_headers['files_list']:
        if file['fileID'] == file_id:
            return file['fileName']

    return None


#####
# FUNCIÓN: def file_upload(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la firma,
#              encriptacion y subida del fichero a securebox
# ARGS_OUT:
#####

def file_upload(args):
    # 1. Se comprueban los parametros de entrada
    if args.upload is None:
        print("Error, debe especificar --upload para indicar el fichero a subir")
        return

    # 2. Se obtiene el token de autorizacion del usuario
    user_token = user_get_token()
    print("Solicitado envio de fichero a SecureBox")

    # 3. Se hace una llamada a la api para obtener la clave publica del usuario
    # destino
    print(f"-> Recuperando clave pública de ID {args.user_id}...", end="")
    res = api.get_publicKey_user(user_token, args.user_id)
    res_headers = res.json()

    if res.status_code == 200:
        print("OK")
        # 4. Se cifra el fichero leyendo el fichero firmado
        encrypted_file_path = ec.sign_and_encrypt(
            FILES_ROOT, args.upload, res_headers['publicKey'],
            REMOTE_SIGNNED, REMOTE_UPLOADS)
        if encrypted_file_path is None:
            return

        # 5. Se sube finalemente el fichero cifrado
        print("-> Subiendo fichero al servidor...", end="")
        res = api.upload_file(user_token, encrypted_file_path)
        res_headers = res.json()

        if res.status_code == 200:
            print("OK")
            print(
                f"Subida realizada correctamente, ID del fichero: {res_headers['file_id']}")

        else:
            print("Error")
            print(
                f"{res_headers['http_error_code']} {res_headers['error_code']} "
                f"- {res_headers['description']}"
            )
    else:
        print("Error")
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )


#####
# FUNCIÓN: file_download(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la descarga
#              de un fichero de SecureBox
# ARGS_OUT:
#####

def file_download(args):

    if args.source_id is None:
        print("Error, debe especificar --source_id para verificar la firma del remitente")
        return
    # 1.Se obtiene el token
    user_token = user_get_token()
    if user_token is None:
        return
    print("Descargando fichero de SecureBox...", end="")

    # 2.Se hace una llamada a la api para obtener el fichero del servidor
    res = api.download_file(user_token, args.file_id)

    if res.status_code == 200:
        # 3.Se guarda el fichero bajado
        if ec.create_files(REMOTE_DOWNLOADS_ENC, args.file_id, res.content) is None:
            print("Error")
            return
        print("OK")

        # 4. Se manda a descifrar el fichero
        print("-> Descifrando el fichero...", end="")
        decrypted_file_name = ec.decrypt_file(REMOTE_DOWNLOADS_ENC + args.file_id,
                                              REMOTE_DOWNLOADS_DEC)
        if decrypted_file_name is None:
            print("Error")
            return
        print("OK")
        # 5. Se recupera la clave publica del usuario, para validar que es quien
        # dice ser
        print(
            f"-> Recuperando clave pública de ID {args.source_id}...", end="")
        res = api.get_publicKey_user(user_token, args.source_id)
        res_headers = res.json()

        if res.status_code == 200:
            print("OK")
            # 6. Se valida la firma digital
            print("-> Verificando firma...", end="")
            if ec.confirm_sign(REMOTE_DOWNLOADS_DEC + args.file_id,
                               res_headers['publicKey']) == -1:
                print("Error")
                return
            print("OK")

            # 7. Finalmente quitamos la firma de la cabecera del fichero para que
            # quede el contenido que queremos

            try:
                with open(REMOTE_DOWNLOADS_DEC + args.file_id, 'rb') as file:
                    content = file.read()
            except FileNotFoundError:
                print("Error leyendo el fichero...")
                return

            if ec.create_files(REMOTE_DOWNLOADS_DEC, args.file_id, content[256:]) is None:
                print("Error creando el fichero final...")

            print(
                f"Fichero '{args.file_id}' descargado y verificado correctamente")

        else:
            print("Error")
            print(
                f"{res_headers['http_error_code']} {res_headers['error_code']} "
                f"- {res_headers['description']}"
            )
    else:
        print("Error")
        res_headers = res.json()
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )

#####
# FUNCIÓN: def file_list(args=None)
# ARGS_IN: args = None - no recibe argumentos esta regla
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para el listado de
#              los ficheros de un usuario en Securebox
# ARGS_OUT:
#####


def file_list(args=None):
    # 1.Se obtiene el token
    user_token = user_get_token()
    if user_token is None:
        return
    # 2.Se realiza una llamada a la api para obtener al lista de ficheros
    print("Obtienedo los ficheros del servidor...", end="")
    res = api.list_file(user_token)
    res_headers = res.json()

    # 3.Se imprime el mensaje formateado
    if res.status_code == 200:
        print("OK")
        print(f"Numero de ficheros {res_headers['num_files']}")
        [print(f"Id: {file['fileID']} - Name: {file['fileName']}")
         for file in res_headers['files_list']]

    else:
        print("Error")
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )


#####
# FUNCIÓN: def file_delete(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la eliminacion
#              de un fichero en el servidor de SecureBox
# ARGS_OUT:
#####


def file_delete(args):
    # 1.Se obtiene el token
    user_token = user_get_token()
    if user_token is None:
        return
    # 2.Se realiza una llamada a la api para borrar el fichero
    print("Borrando el fichero del servidor...", end="")
    res = api.delete_file(user_token, args.file_id)
    res_headers = res.json()

    # 3.Se imprime el mensaje formateado
    if res.status_code == 200:
        print("OK")
        print(
            f"Fichero con ID#{res_headers['file_id']} eliminado correctamente")

    else:
        print("Error")
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )
