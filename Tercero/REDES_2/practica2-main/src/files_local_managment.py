from . import encryption as ec
import filecmp
from routes import (FILES_ROOT, LOCAL_SIGNNED,
                    LOCAL_DECRYPTED, LOCAL_ENCRYPTED)


#####
# FUNCIÓN: def encrypt_local(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la encriptacion
#              en local
# ARGS_OUT:
#####
def encrypt_local(args):
    # 1. Se obtiene la clave publica
    try:
        with open("keys/public_key.pem", 'rb') as file:
            public_key = file.read()
    except FileNotFoundError:
        print("Clave publica no creada")

    # 2. Se manda a encriptar el fichero
    print("-> Cifrando fichero...", end="")
    signned_file_path = ec.encrypt_file(
        FILES_ROOT + args.file_name, LOCAL_ENCRYPTED, public_key)

    if signned_file_path is None:
        print("Error")
        return
    print("OK")

    print("Fichero subido cifrado localmente en: ", LOCAL_ENCRYPTED)


#####
# FUNCIÓN: def sign_local(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la firma
#              en local
# ARGS_OUT:
#####
def sign_local(args):

    # 1. Se manda a firmar el fichero
    print("-> Firmando fichero...", end="")
    signned_file_path = ec.sign_message(
        FILES_ROOT + args.file_name, LOCAL_SIGNNED)

    if signned_file_path is None:
        print("Error")
        return
    print("OK")

    print("Fichero subido firmado localmente en: ", LOCAL_SIGNNED)


#####
# FUNCIÓN: def encrypt_sign_local(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para la firma
#              y posterior encriptacion en local
# ARGS_OUT:
#####
def encrypt_sign_local(args):
    # 1. Se obtiene la clave publica del usuario
    try:
        with open("keys/public_key.pem", 'rb') as file:
            public_key = file.read()
    except FileNotFoundError:
        print("El usuario no dispone de clave publica")
        return

    # 2. Se manda a firmar y encriptar el fichero
    encrypted_file_path = ec.sign_and_encrypt(
        FILES_ROOT, args.file_name, public_key,
        LOCAL_SIGNNED, LOCAL_ENCRYPTED)

    if encrypted_file_path is None:
        return
    print("Fichero subido localmente en: ", LOCAL_ENCRYPTED)


#####
# FUNCIÓN: def decrypt_local(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria para el descifrado
#              en local
# ARGS_OUT:
#####
def decrypt_local(args):

    # 1. Se descifra en primer lugar el fichero
    print("-> Decifrando fichero...", end="")

    decrypted_file_path = ec.decrypt_file(
        LOCAL_ENCRYPTED + args.file_name, LOCAL_DECRYPTED)
    if decrypted_file_path is None:
        print("Error")
        return
    print("OK")

    # 2. Se comprueba si ese fichero estaba firmado, y en caso afirmativo se compara
    # si el fichero ya desencriptado es igual que el de la firma (lo deberia ser si exsite)
    try:
        with open(LOCAL_SIGNNED + args.file_name, 'rb'):
            pass

        # 3. filecmp.cmp compara dos ficheros
        if filecmp.cmp(LOCAL_SIGNNED + args.file_name, LOCAL_DECRYPTED + args.file_name) is False:
            return

        # 4. Se valida la firma digital
        print("-> Verificando firma...", end="")
        if ec.confirm_sign(LOCAL_DECRYPTED + args.file_name,
                           open("keys/public_key.pem").read()) == -1:
            print("Error")
            return
        print("OK")

        # 5. Se elimina la parte de la firma digital una vez ha sido verificada
        # que sabemos que son los primeros 256 bits correspondientes al hash
        try:
            with open(LOCAL_DECRYPTED + args.file_name, 'rb') as file:
                content = file.read()
        except FileNotFoundError:
            print("Error leyendo el fichero...")
            return

        if ec.create_files(LOCAL_DECRYPTED, args.file_name, content[256:]) is None:
            print("Error creando el fichero final...")

        print("Fichero descifrado localmente en: ", LOCAL_DECRYPTED)

    except FileNotFoundError:
        pass


#####
# FUNCIÓN: def unsign_local(args)
# ARGS_IN: args - los argumentos que ha recibido y parseado de la linea de comandos
# DESCRIPCIÓN: Funcion que realiza la funcionalidad necesaria comprobar la firma
# ARGS_OUT:
#####
def unsign_local(args):

    # 1. Se valida la firma digital
    print("-> Verificando firma...", end="")
    if ec.confirm_sign(LOCAL_SIGNNED + args.file_name,
                       open("keys/public_key.pem").read()) == -1:
        print("Error")
        return
    print("OK")
