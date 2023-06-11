import base64
from Cryptodome.PublicKey import RSA
from Cryptodome.Cipher import AES, PKCS1_OAEP
import os
import secrets
import string
import base64
import binascii

#####
# FUNCIÓN: def generate_passphrase()
# ARGS_IN: length - longitud del passphrase a generar
# DESCRIPCIÓN: Genera una cadena de longitud indicada por parámetros
# ARGS_OUT: str - passphrase
#####


def generate_passphrase(length: int):

    alphabet = string.ascii_letters + string.digits
    passphrase = ''.join(secrets.choice(alphabet) for _ in range(length))
    return passphrase

#####
# FUNCIÓN: def generate_RSAkeys()
# ARGS_IN:
# DESCRIPCIÓN: Genera una pareja de claves RSA de 2048 bits
# ARGS_OUT: list - lista como primer argumento la public key, como segundo la private key
#               y como tercero el secret_code utilizado para generar las claves
#####


def generate_RSAkeys():

    # Generar pareja de claves RSA de 2048 bits de longitud
    key = RSA.generate(2048)

    # Passphrase para encriptar la clave privada
    secret_code = generate_passphrase(10)

    # Obtenemos la clave privada
    private_key = key.export_key(passphrase=secret_code)

    # Obtenemos la clave pública
    public_key = key.publickey().export_key()
    public_key_str = base64.b64encode(public_key).decode('utf-8')

    return public_key_str, private_key, secret_code

#####
# FUNCIÓN: def generate_AESkey()
# ARGS_IN:
# DESCRIPCIÓN: Genera una clave AES de 512 bits
# ARGS_OUT: str - cadena hexadecimal aleatoria de 512 bits que actuará como clave simétrica
#####


def generate_AESkey():

    # Generar una cadena hexadecimal aleatoria de 32 bytes (32 bits)
    return os.urandom(32)


#####
# FUNCIÓN: def encode_AESkey()
# ARGS_IN: aes_key - clave AES a encriptar
#          public_key - public_key con la que se encriptará la clave AES
# DESCRIPCIÓN: Encripta la clave AES usando la clave publica
# ARGS_OUT: clave AES encriptada
#####

def encrypt_AESkey(aes_key, public_key: str):

    # Crear una instancia del cifrador simétrico basado en RSA con la clave publica
    cipher = PKCS1_OAEP.new(RSA.import_key(public_key))

    # Cifrar la clave simetrica
    cipher_key = cipher.encrypt(aes_key)

    return cipher_key

#####
# FUNCIÓN: def decode_AESkey()
# ARGS_IN: aes_key_encrypted - clave AES a encriptada
#          private_key - public_key con la que se encriptará la clave AES
#          passphrase - passphrase utilizado para generar el par de claves RSA
# DESCRIPCIÓN: Desencripta la clave AES usando la clave privada
# ARGS_OUT: objeto AES - clave simétrica desencriptada
#####


def decrypt_AESkey(aes_key_encrypted, private_key: bytes, passphrase: str):

    # Crear una instancia del cifrador simétrico basado en RSA con la clave privada
    cipher = PKCS1_OAEP.new(RSA.import_key(private_key, passphrase=passphrase))

    # Descifrar la clave simetrica
    cipher_key = cipher.decrypt(aes_key_encrypted)

    return cipher_key

#####
# FUNCIÓN: def encrypt_msg()
# ARGS_IN: key - clave AES
#          msg - mensaje a cifrar
# DESCRIPCIÓN: Encriptar un mensaje con la clave simetrica en modo CTR
# ARGS_OUT: list - lista con el nonce generado aleatoriamente y el contenido cifrado
#####


def encrypt_msg(key: bytes, msg: str):

    # Generar una secuencia aleatoria de 12 bytes como nonce
    nonce = os.urandom(12)

    # Crear una instancia del cifrador AES en modo CTR
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)

    # Encriptar el mensaje
    ciphertext = cipher.encrypt(msg.encode('utf-8'))

    return nonce, ciphertext

#####
# FUNCIÓN: def decrypt_msg()
# ARGS_IN: key - clave AES para descrifrar el mansaje
#          nonce - valor aleatorio que se combina con una cadena de datos
#                antes de ser ser sometida a una función de cifrado
#          ciphertext - mensaje cifrado a descrifrar
# DESCRIPCIÓN: Desencriptar el mensaje cifrado con el modo CTR
# ARGS_OUT: str - mensaje descrifrado
#####


def decrypt_msg(key: bytes, nonce: bytes, ciphertext):

    # Crear una instancia del cifrador AES en modo CTR
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)

    # Descifrar el mensaje
    plaintext = cipher.decrypt(ciphertext)

    return plaintext.decode('utf-8')

#####
# FUNCIÓN: def encrypt_file()
# ARGS_IN: key - clave AES
#          file_content - bytes del archivo a cifrar
# DESCRIPCIÓN: Encriptar los bytes de un fichero con la clave simetrica en modo CTR
# ARGS_OUT: list - lista con el nonce generado aleatoriamente y el contenido cifrado
#####


def encrypt_file(key: bytes, file_content: bytes):

    # Generar una secuencia aleatoria de 12 bytes como nonce
    nonce = os.urandom(12)

    # Crear una instancia del cifrador AES en modo CTR
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)

    # Encriptar el mensaje
    ciphertext = cipher.encrypt(file_content)

    return nonce, ciphertext

#####
# FUNCIÓN: def decrypt_file()
# ARGS_IN: key - clave AES para descrifrar el mansaje
#          nonce - valor aleatorio que se combina con una cadena de datos
#                antes de ser ser sometida a una función de cifrado
#          ciphercontent - contenido cifrado a descifrar
# DESCRIPCIÓN: Desencriptar el contenido cifrado con el modo CTR
# ARGS_OUT: bytes - contenido descrifrado
#####


def decrypt_file(key: bytes, nonce: bytes, ciphercontent):

    # Crear una instancia del cifrador AES en modo CTR
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)

    # Descifrar el mensaje
    file_content = cipher.decrypt(ciphercontent)

    return file_content


#####
# FUNCIÓN: def prepare_msg_to_sent()
# ARGS_IN: public_key - clave publica con la que se encryptara la clave AES
# DESCRIPCIÓN: Genera una clave simetrica de sesion, la encripta con la publica
#              y la convierte en base hexadecimal
# ARGS_OUT: list - lista con el aes_key sin cifrar para el emisor
#                  y la aes_key cifrada para el receptor
#####


def prepare_aes_key_to_sent(public_key):

    # Generar una clave simetrica de sesion
    aes_key = generate_AESkey()

    public_key_bytes = base64.b64decode(public_key).decode('utf-8')

    # Cifro la clave de sesion con la publica del servidor receptor
    return aes_key, binascii.hexlify(encrypt_AESkey(aes_key, public_key_bytes)).decode()

#####
# FUNCIÓN: def prepare_aes_key_recived()
# ARGS_IN: private_key - clave privada para descrifrar el mansaje
#          passphrase - passphrase utilizado para generar el par de claves RSA
#          msg_enc_hexa - mensaje cifrado en base hexadecimal a descrifrar
# DESCRIPCIÓN: Convierte a cadena de bytes el mensaje cifrado y descifra el mensaje
# ARGS_OUT: bytes - clave de sesion semetrica descifrada
#####


def prepare_aes_key_recived(private_key: bytes, passphrase: str, msg_enc_hexa: str):

    # Obtenemos la clave simetrica codificada y convertida en cadena hexadecimal
    aes_enc = binascii.unhexlify(msg_enc_hexa.encode())

    # Desencripto la clave simetrica de sesion
    return decrypt_AESkey(aes_enc, private_key, passphrase)

#####
# FUNCIÓN: def prepare_msg_to_sent()
# ARGS_IN: key - clave AES para crifrar el mansaje
#          msg_components - lista con los elementos que
#                           tendra el mensaje a enviar
# DESCRIPCIÓN: Genera el mensaje concatenando la lista de componentes
#              encripta el mensaje con la clave simetrica y representa
#              en base hexadecimal el mensaje cifrado y el nonce
# ARGS_OUT: list - lista con el mensaje cifrado y el nonce
#####


def prepare_msg_to_sent(key: bytes, msg_components):

    msg = ""
    for component in msg_components:
        msg += component

        if msg_components.index(component) < len(msg_components) - 1:
            msg += " "

    msg_enc, nonce = encrypt_msg(key, msg)

    return binascii.hexlify(msg_enc).decode(), binascii.hexlify(nonce).decode()

#####
# FUNCIÓN: def prepare_msg_recived()
# ARGS_IN: key - clave AES para descrifrar el mansaje
#          nonce_hexa - nonce en base hexadecimal
#          msg_hexa - mensaje cifrado en base hexadecimal a descrifrar
# DESCRIPCIÓN: Convierte a cadena de bytes el mensaje cifrado y el nonce
#              descrifra el mensaje con la clave simetrica y el nonce
# ARGS_OUT: list - lista con comando y resto de info del mensaje descrifrado
#####


def prepare_msg_recived(key: bytes, nonce_hexa: str, msg_enc_hexa: str):

    # Obtenemos el mensaje cifrado y el nonce convertidos en cadena hexadecimal
    msg_enc = binascii.unhexlify(nonce_hexa.encode())
    nonce = binascii.unhexlify(msg_enc_hexa.encode())

    # Desencriptamos el mensaje con la aes_key
    split = decrypt_msg(key, nonce, msg_enc).split(" ")

    return split[0], split[1:]

#####
# FUNCIÓN: def prepare_msg_to_sent()
# ARGS_IN: key - clave AES para crifrar el mansaje
#          msg_components - lista con los elementos que
#                           tendra el mensaje a enviar
# DESCRIPCIÓN: Genera el mensaje concatenando la lista de componentes
#              encripta el mensaje con la clave simetrica y representa
#              en base hexadecimal el mensaje cifrado y el nonce
# ARGS_OUT: list - lista con el mensaje cifrado y el nonce
#####


def prepare_file_to_sent(key: bytes, file_content: bytes):

    file_enc, nonce = encrypt_file(key, file_content)

    return binascii.hexlify(file_enc).decode(), binascii.hexlify(nonce).decode()

#####
# FUNCIÓN: def prepare_file_recived()
# ARGS_IN: key - clave AES para descrifrar el mansaje
#          nonce_hexa - nonce en base hexadecimal
#          file_conent_enc_hexa - contenido del file cifrado en base hexadecimal a descrifrar
# DESCRIPCIÓN: Convierte a cadena de bytes el contenido de bytes cifrado y el nonce
#              descrifra el contenido con la clave simetrica y el nonce
# ARGS_OUT: bytes - contenido de bytes descrifrado
#####


def prepare_file_recived(key: bytes, nonce_hexa: str, file_conent_enc_hexa: str):

    # Obtenemos el mensaje cifrado y el nonce convertidos en cadena hexadecimal
    msg_enc = binascii.unhexlify(nonce_hexa.encode())
    nonce = binascii.unhexlify(file_conent_enc_hexa.encode())

    return decrypt_file(key, nonce, msg_enc)
