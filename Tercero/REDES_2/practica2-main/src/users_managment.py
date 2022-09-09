from . import encryption as ec, api_endopints as api
import re
import os

#####
# FUNCIÓN: def user_get_token()
# ARGS_IN:
# DESCRIPCIÓN: Obtiene el token de autorizacion de nuestro perfil desde un fichero
# ARGS_OUT: str - el token del usuario, si se ha escrito a posteriori el parametro
#               de userID, se elimina el \n del final
#####


def user_get_token():
    try:
        with open("user_data.conf", "r") as file:
            lines = file.readlines()
    except FileNotFoundError:
        print("El usuario no tiene un token creado")
        return None

    return lines[0].split(": ")[1][:-1] if len(lines) > 1 else lines[0].split(": ")[1]


#####
# FUNCIÓN: def user_get_id()
# ARGS_IN:
# DESCRIPCIÓN: Obtiene el id del usuario el cual se ha añadido con anterioridad
# ARGS_OUT: str - el id del usuario
#####
def user_get_id():
    try:
        with open("user_data.conf", "r") as file:
            lines = file.readlines()
    except:
        print("El usuario no tiene la clave publica creada")
        return None

    return lines.pop().split(": ")[1]


#####
# FUNCIÓN: def user_validate_email(email : str)
# ARGS_IN: str email - el email que se quiere validar
# DESCRIPCIÓN: Valida si el email comple con las condiciones de los emails
# ARGS_OUT: boolean - true si es un email valido, false en caso contrario
#####
def user_validate_email(email: str):
    return (
        True
        if re.match(
            "^[(a-z0-9\_\-\.)]+@[(a-z0-9\_\-\.)]+\.[(a-z)]{2,15}$", email.lower(
            )
        )
        else False
    )


#####
# FUNCIÓN: def user_register(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza l afuncionalidad necesaria para el registro
#              de un usuario de la UAM en el servidor de SecureBox
# ARGS_OUT:
#####
def user_create(args):
    print("Creando un fichero de configuracion de usuario...", end="")

    with open("user_data.conf", "w") as file:
        file.write(f"Authorization: {args.token}")
    print("OK")


#####
# FUNCIÓN: def user_register(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza l afuncionalidad necesaria para el registro
#              de un usuario de la UAM en el servidor de SecureBox
# ARGS_OUT:
#####
def user_register(args):

    # 1. Se comprueba la validez del correo
    if not user_validate_email(args.email):
        print("Error, el correo electronico es incorrecto")
        return

    # 2. Se ontiene el token que llevara las cabeceras de la API
    user_token = user_get_token()
    if user_token is None:
        return
    # 3. Se genera el par de claves publica y privada
    print("Generando par de claves RSA de 2048 bits...", end="")
    public_key, private_key = ec.generate_user_key()
    print("OK")
    # 4. Se crea un directorio y dos ficheros con las respectivas claves
    try:
        os.mkdir("keys")
    except:
        pass

    with open("keys/private_key.pem", "wb") as file:
        file.write(private_key)
        file.close()

    with open("keys/public_key.pem", "wb") as file:
        file.write(public_key)
        file.close()
    # 5. Se hace la llamada a la API para registrar el usuario
    res = api.register_user(user_token, args.name, args.email, public_key)
    res_headers = res.json()

    if res.status_code == 200:
        print(
            f"Identidad con ID #{res_headers['userID']} creada correctamente")

        # 6. Se guarda ese id del usuario para que este lo tenga accesible
        if user_get_id() != res_headers["userID"]:
            with open("user_data.conf", "a") as file:
                file.write(f"\nUser_ID: {res_headers['userID']}")
    else:
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )


#####
# FUNCIÓN: def user_search(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad de obtener las coincidencias
#              de usuarios en el servidor de SecureBox
# ARGS_OUT:
#####
def user_search(args):

    print(f"Buscando usuario '{args.data}' en el servidor...")
    # 1. Se ontiene el token que llevara las cabeceras de la API
    user_token = user_get_token()
    if user_token is None:
        return
    # 2. Se hace una llamada a la API para buscar los usuarios
    res = api.search_user(user_token, args.data)
    res_headers = res.json()

    # 3. Se muestra al usuario el resultado de la busqueda
    if res.status_code == 200:
        print(f"{len(res_headers)} usuarios encontrados:")
        [
            print(f"{user['nombre']}, {user['email']}, ID: {user['userID']}")
            for user in res_headers
        ]
    else:
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )


#####
# FUNCIÓN: def user_delete(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad de eliminar un usuario
#              de usuarios en el servidor de SecureBox
# ARGS_OUT:
#####
def user_delete(args):

    # 1. Se ontiene el token que llevara las cabeceras de la API
    user_token = user_get_token()
    if user_token is None:
        return
    # 2. Se hace una llamada a la API para eliminar el usuario los usuarios
    print(f"Solicitando borrado de la identidad #{args.user_id}...", end="")
    res = api.delete_user(user_token, args.user_id)
    res_headers = res.json()
    print("OK")
    # 3. Se muestra al usuario el resultado de la eliminacion
    if res.status_code == 200:
        user_id = user_get_id()
        if user_id is None:
            return
        if user_id == res_headers["userID"]:
            with open("user_data.conf", "w") as file:
                file.write(f"Authorization: {user_token}")
        print(
            f"Identidad con ID#{res_headers['userID']} borrada correctamente")
    else:
        print(
            f"{res_headers['http_error_code']} {res_headers['error_code']} "
            f"- {res_headers['description']}"
        )
