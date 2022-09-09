from Crypto.PublicKey import RSA
from Crypto.Hash import SHA256
from Crypto.Signature import pkcs1_15
from Crypto.Random import get_random_bytes
from Crypto.Cipher import AES, PKCS1_OAEP
from Crypto.Util.Padding import pad, unpad
import os

#####
# FUNCIÓN: def user_get_token()
# ARGS_IN:
# DESCRIPCIÓN: Obtiene el token de autorizacion de nuestro perfil desde un fichero
# ARGS_OUT: str - el token del usuario
#####


def generate_user_key():
    key = RSA.generate(2048)

    private_key = key.export_key()
    public_key = key.public_key().export_key()

    return public_key, private_key


#####
# FUNCIÓN: sign_message(file_name_path)
# ARGS_IN: file_name_path - path del fichero donde se encuentra el mensaje
#          path_to_upload - path donde se va a almacenar el fichero creado
# DESCRIPCIÓN: Firma un mensaje y lo guarda en un fichero
# ARGS_OUT: str con la ruta del fichero o None en caso de error
#####
def sign_message(file_name_path, path_to_upload):

    # Se obtiene el mensaje
    try:
        with open(file_name_path, 'rb') as file:
            message = file.read()
    except FileNotFoundError:
        return None

    # Se obtiene la clave privada
    try:
        with open("keys/private_key.pem", 'rb') as file:
            private_key = file.read()
    except FileNotFoundError:
        return None

    # Se firma el mensaje con mi clave privada
    try:
        digital_signature = pkcs1_15.new(
            RSA.import_key(private_key)).sign(SHA256.new(message))
    except (ValueError, TypeError):
        return None

    # Se construye los datos que contendra el fichero, una firma y el mensaje
    data = digital_signature + message
    # Se manda a crear un fichero con la firma y el mensaje
    signed_file_name = os.path.split(file_name_path)[1].split('.')[0]
    if create_files(path_to_upload, signed_file_name, data) is not None:
        return path_to_upload + signed_file_name

    return None


#####
# FUNCIÓN: confirm_sign(file_name_path)
# ARGS_IN: file_name_path - ruta del fichero a descifrar
#          public_key - clave publica del destinatario
# DESCRIPCIÓN: El receptor confirma la firma y autenticidad del mensaje
# ARGS_OUT: 0 si se pudo verificar la firma y auenticación o -1 en caso contrario
#####
def confirm_sign(file_name_path, public_key):

    # Se obtiene el mensaje del fichero desencriptado
    try:
        with open(file_name_path, 'rb') as file:
            dec_data = file.read()

    except FileNotFoundError:
        return None

    # Se verifica la firma digital, para ello, como sabemos que el hash tiene un
    # tamaño constante de 256 bits, y nosotros encriptamos siempre (al subir a Securebox)
    # de manera 'firma digital + mensaje', sabemos que los primeros 256 bits son la
    # firma y el resto es el mensaje

    # Con lo cual, se pasa por hash el mensaje [256:final], y se compara con los bits que
    # coresponden al hash [inicio:256]
    try:
        pkcs1_15.new(RSA.import_key(public_key)).verify(
            SHA256.new(dec_data[256:]), dec_data[:256])
    except (ValueError, TypeError):
        return -1

    return 0


#####
# FUNCIÓN: encrypt_file(file_name_path, path_to_upload, public_key)
# ARGS_IN: file_name_path - path del fichero donde se encuentra el mensaje
#          path_to_upload - path donde se va a almacenar el fichero creado
#          public_key - clave publica del destinatario
# DESCRIPCIÓN: Se encarga de encriptar el mensaje
# ARGS_OUT: 0 si se pudo verificar la firma y auenticación o -1 en caso contrario
#####
def encrypt_file(file_name_path, path_to_upload, public_key):

    # Se lee el mensaje del fichero
    if public_key is not None:
        try:
            with open(file_name_path, 'rb') as file:
                message = pad(file.read(), AES.block_size)

        except FileNotFoundError:
            return None

        # Generamos un vector de inicialización random de 16 bytes
        ini_vector = get_random_bytes(16)

        # Generamos una clave simétrica de 256 bits
        symmetric_key = get_random_bytes(32)

        # Encriptamos el mensaje con la clave simetrica
        enc_message = AES.new(symmetric_key, AES.MODE_CBC,
                              iv=ini_vector).encrypt(message)

        # Encriptamos la clave simetrica con la clave publica del receptor
        try:
            enc_key = PKCS1_OAEP.new(key=RSA.import_key(
                public_key)).encrypt(symmetric_key)
        except ValueError:
            return None

        # El mensaje final encriptado el la concatenacion del vector de inicializacion,
        # con la clave asimetrica cifrada y con el mensaje encriptado y firmado
        enc_data = ini_vector + enc_key + enc_message

        # Se manda a crear el fichero encriptado
        encrypted_file_name = os.path.split(file_name_path)[1].split('.')[0]
        if create_files(path_to_upload, encrypted_file_name, enc_data) is not None:
            return path_to_upload + encrypted_file_name

    return None


#####
# FUNCIÓN: decrypt_file(message, public_key, filename, signature=None)
# ARGS_IN: file_name_path - ruta del fichero encriptado del servidor
#          path_to_upload - ruta donde se almacena el fichero
# DESCRIPCIÓN: Se encarga de encriptar el mensaje
# ARGS_OUT: 0 si se pudo verificar la firma y auenticación o -1 en caso contrario
#####
def decrypt_file(file_name_path, path_to_upload):

    # Se lee el fichero encriptado del fichero
    try:
        with open(file_name_path, 'rb') as file:
            enc_data = file.read()
    except FileNotFoundError:
        return None

    ini_vector = enc_data[:16]
    enc_key = enc_data[16:16+256]
    enc_message = enc_data[16+256:]

    # Se obtiene la clave privada
    try:
        with open("keys/private_key.pem", 'rb') as file:
            private_key = file.read()
    except FileNotFoundError:
        return None

    # Se obtiene la clave simetrica descifrada con nuestra clave privada
    try:
        symmetric_key = PKCS1_OAEP.new(
            RSA.import_key(private_key)).decrypt(enc_key)
    except (TypeError, ValueError):
        return None

    # Se obtiene el mensaje descifrado con la clave simetrica y el vector de inicializacion
    message = AES.new(symmetric_key, AES.MODE_CBC,
                      iv=ini_vector).decrypt(enc_message)

    # Se manda a crear el fichero desencriptado
    decrypted_file_name = os.path.split(file_name_path)[1].split('.')[0]
    if create_files(path_to_upload, decrypted_file_name,
                    unpad(message, AES.block_size)) is not None:
        return path_to_upload + decrypted_file_name

    return None


#####
# FUNCIÓN: sign_and_encrypt(path, file_name)
# ARGS_IN: path - ruta del fichero que vamos a firmar
#          file_name - nombre del fichero
#          public_key - clave publica del receptor
#          signned_path - ruta donde se crea el fichero firmado
#          path_to_upload - ruta donde se va a almacenar el fichero encriptado y firmado
# DESCRIPCIÓN: Funcion que se encarga de firmar y encriptar un fichero
# ARGS_OUT: Str con la ruta del fichero o None en caso de error
#####
def sign_and_encrypt(path, file_name, public_key, signned_path, path_to_upload):

    # Se manda a firmar el fichero
    print("-> Firmando fichero...", end="")
    signned_file_path = sign_message(
        path + file_name, signned_path)
    if signned_file_path is None:
        print("Error")
        return None
    print("OK")

    # Se manda a cifrar el fichero
    print("-> Cifrando el fichero...", end="")
    encripted_file_path = encrypt_file(
        signned_file_path, path_to_upload, public_key)
    if encripted_file_path is None:
        print("Error")
        return None
    print("OK")

    return encripted_file_path

#####
# FUNCIÓN: create_files(path, file_name, data)
# ARGS_IN: path - ruta del directorio
#          file_name - nombre del fichero
#          data - texto a escribir en el fichero
# DESCRIPCIÓN: Se encarga de crear tanto el directorio si no esta creado como el fichero
# ARGS_OUT: None en caso de Error y la ruta en caso de exito
#####


def create_files(path, file_name, data):

    # Se crean directorios recursivos si son necesarios
    os.makedirs(path, exist_ok=True)
    try:
        with open(path + file_name, 'wb') as file:
            file.write(data)
    except:
        return None

    return file_name
