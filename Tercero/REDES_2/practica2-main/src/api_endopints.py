import requests

SECUREBOX = "vega.ii.uam.es:8080/api"

#####
# FUNCIÓN: def register_user(
#           user_token: str, name: str, email:str, publick_key: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str name - el nombre del usuario
#          str email - el email del usuario
#          str public_key - clave publica del usuario
# DESCRIPCIÓN: Manda una solicitud de registro al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####


def register_user(user_token: str, name: str, email: str, publick_key: str):
    url = f"http://{SECUREBOX}/users/register"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"nombre": name, "email": email, "publicKey": publick_key}

    return requests.post(url, headers=headers, json=args)


#####
# FUNCIÓN: def get_publicKey_user(user_token: str, user_id: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str user_id - el id del usuario el cual obtener la clave publica
# DESCRIPCIÓN: Manda una solicitud de obtencion de la clave publica de un
#               usuario al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def get_publicKey_user(user_token: str, user_id: str):
    url = f"http://{SECUREBOX}/users/getPublicKey"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"userID": user_id}

    return requests.post(url, headers=headers, json=args)


#####
# FUNCIÓN: def search_user(str user_token, data: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str data - la cadena de contenido la cual queremos encontrar
# DESCRIPCIÓN: Manda una solicitud de busqueda de usuarios al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def search_user(user_token: str, data: str):
    url = f"http://{SECUREBOX}/users/search"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"data_search": data}

    return requests.post(url, headers=headers, json=args)


#####
# FUNCIÓN: def delete_user(str user_token, user_id: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str user_id - id del usuario que se va a eliminar
# DESCRIPCIÓN: Manda una solicitud de eliminacion de un usuario al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def delete_user(user_token: str, user_id: str):
    url = f"http://{SECUREBOX}/users/delete"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"userID": user_id}

    return requests.post(url, headers=headers, json=args)


#####
# FUNCIÓN: def upload_file(user_token: str, file_name: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str file_name - nombre del fichero a subir
# DESCRIPCIÓN: Sube un fichero al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def upload_file(user_token: str, file_name: str):
    url = f"http://{SECUREBOX}/files/upload"
    headers = {
        "Authorization": f"Bearer {user_token}",
    }
    args = {"ufile": open(file_name, "rb")}

    return requests.post(url, headers=headers, files=args)


#####
# FUNCIÓN: def download_file(user_token: str, file_id: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str file_id - id del fichero a bajar
# DESCRIPCIÓN: Sube un fichero al servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def download_file(user_token: str, file_id: str):
    url = f"http://{SECUREBOX}/files/download"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"file_id": file_id}

    return requests.post(url, headers=headers, json=args)


#####
# FUNCIÓN: def list_file(user_token: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
# DESCRIPCIÓN: Lista los ficheros de un ID autorizacion del Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def list_file(user_token: str):
    url = f"http://{SECUREBOX}/files/list"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }

    return requests.post(url, headers=headers)


#####
# FUNCIÓN: def delete_file(user_token: str, file_id: str)
# ARGS_IN: str user_token - el token de autoriacion de la api
#          str file_id - id del fichero a borrar
# DESCRIPCIÓN: ELimina un fichero del servidor de Securebox
# ARGS_OUT: response - un objeto de respuesta de la API
#####
def delete_file(user_token: str, file_id: str):
    url = f"http://{SECUREBOX}/files/delete"
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {user_token}",
    }
    args = {"file_id": file_id}

    return requests.post(url, headers=headers, json=args)
