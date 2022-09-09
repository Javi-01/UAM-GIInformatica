# Practica2 Redes 2 - Seguridad y Criptografia
## Introducción
En esta segunda practica se ha realizado un cliente que consusume una API Rest del servidor Securebox proporcionado. Ademas, como se trata de un servicio seguro, habra que aplicar los conceptos de criptografia y seguridad aprendidos en la asignatura, como firma digital, cifrado simetrico y asimetrico (con par de claves priavada y publica).

## Preparar la ejecución
Para comenzar a hacer uso del servidor, debemos acceder en primer lugar al servidor para obtener nuestro token de autorización que seranecesario a la hora de consumir la API. Para ello:
1. Accede a *http://vega.ii.uam.es:8080/* conectado a la VPN de la UAM.
2. Pulsa en *Tokens de autenticación*
3. Adjunta el NIA y el correo de la UAM para finalmente obtener un token

Por otro lado, es necesario instalar *argparse* para el manejo de la linea de comandos:

    pip install argparse

Y la Instalacion de *pycryptodomex* para manejo del cifrado, firma y descifrado de los ficheros:

    sudo apt-get install build-essential python3-dev
    pip install pycryptodomex
    pip install pycryptodome-test-vectors
    python3 -m Cryptodome.SelfTest

## Cliente por linea de comandos
Como se ha mencionado, el cliente que consume la API se realiza a traves de la linea de comandos, y por ello, vamos a adjuntar las posibles opciones que se tienen, asi como la explicacion de cada una:


**create_user *token_de_autorizacion*** 

Se encarga de crear un fichero que guarda ese ID de autorizacion para todas las peticiones a la API.

  *Nota: Esta funcion debe ser la primera en ejecutar, porque sin el token de autorizacion no se puede hacer llamadas a la API*
    
**create_id *name* *email***

Crea una identidad con ese "nombre" e "email" en el servidor, crea un par de claves RSA publica y
privada, y añade al fichero el ID que nos proporciona la API para nuestro usuario.

  *Nota: Esta funcion debe ser la segunda en ejecutar, porque si un usuario registrado en securebox no se puede acceder al resto de funcionalidad*


**search_id *data***

Busca en el servidor las coincidencias tanto en el campo "nombre" como en el campo "email" que se 
encuentran en Securebox y devuelve el ID nombre y email de cada uno de ellos.

**delete_id *user_id***

Nos permite borrar el usuario y el token de autorizacion del Securebox, pudiendo crear otro (esta accion
solo es compatible con nuestro usuario)

**dest_id *user_id* --upload *file_name***    

Esta accion nos permite subir un fichero *file_name* a Securebox encriptado con la clave publica de *user_id*,
de manera que tan solo este podra descifrarlo con su clave privada, cumpliendo asi la propiedad de integridad.
Esta peticion nos porporciona un identificador de fichero.

**download *file_id* --source_id *user_id***

Esta accion nos permite bajar el fichero con id *file_id*, de manera que si este era para nosotros, lo podremos
descifrar con nuestra clave privada. Adicionalmente, se debe verificar que el que firma el fichero, es quien
dice ser, en otras palabras, autenticar el emisor, por ello el parametros *user_id*.

**delete_file *file_id***

Esta accion permite borrar un fichero del cual somos propiedad del servidor de Securebox.

**list_files**

Esta accion permite listar los ficheros de los cuales somos propiedad.

**encrypt *file_name***

Esta propiedad permite tan solo hacer la accion de encriptar el fichero *file_name* por separado en local, ya
que si un fichero se quiere subir a securebox, es necesario el cumplimiento del esquema hibrido.

**sign *file_name***

Esta propiedad permite tan solo hacer la accion de firmar el fichero *file_name* por separado en local, de
manera que el mensaje estara concatenado en plano (es legible), pero el fichero tendra en su parte alta el 
hash con la firma digital.

**enc_sign *file_name***

Esta propiedad, permite hacer la accion de firmar y posteriormente cifrar el fichero *file_name*, pero en local,
con lo cual, realiza las mismas operaciones que subir un fichero a securebox, pero sin subirlo.

**decrypt *file_name***

Una vez que tenemos cifrado algun fichero localmente con nombre *file_name*, existe la posibilidad de
descifrarlo usando este comando. Es capaz de reconocer si el fichero estaba tan solo cifrado, o lo estaba 
tanto cifrado como firmado, y descifrarlo.

**unsign *file_name***

Esta funcion se encarga de realizar de manera local, la validacion de la firma del fichero *file_name*

## Gestion de Usuarios - *users_manegment.py*

### Crear un usuario Local
En primer lugar, si queremos usar la aplicacion, es necesario añadir el token de autorizacion al cliente, para hacer las posteriores peticiones en la aplicacion, pudiendo renovarlo en cualquier momento.
De ello se encarga la funcion de *user_create*, escibiendo el token en el fichero.

### Registrar un usuario en Securebox
A continuacion, el siguiente paso seria registrar el usuario en el servidor de Securebox, para ello, la funcion *user_register*, genera el par de claves publica y privada necesarias para poder hacer la subida y bajada de ficheros de manera segura, las guarda en un fichero, y hace una llamada a la API para registrar el usuario pasando el token de Autorizacion, el nombre e email especificado, y la clave publica que la API guardara a este usuario. Si la respuesta tiene exito, devuelve el ID creado por Securebox para ese usuario, el cual guardaremos tambien en el fichero junto al token por si no es de utilidad.

### Listar Usuarios
Ademas de esto, el usuario puede buscar, para ello, simplemente se hace una llamada a la API con la cadena de texto que se desea buscar, y esta nos devuelve los resultados, los cuales son mostrados por pantalla.

### Eliminar Usuario de Securebox
Finalmente, se puede borrar una identadad, el cual ademas de eliminar el usuario con ese ID del Securebox, elimina tambien el token de Autorizacion, el cual habra que generar de nuevo. Para ello, se hace de nuevo la correspondiente llamada a la API, y esta nos devuelve el ID eliminado.

## Gestion de Ficheros en Remoto - *files_remote_managment.py*

### Subir un fichero
La primera funcionalidad es la de subir un fichero con la funcion *file_upload* a Securebox, para ello, primero se obtiene mediante una llamada a la API, la clave publica del usuario cuyo ID (especificado por parametros) queremos enviar el fichero.
Esto nos permitira cifrar el fichero asegurandonos que solo va a poder ser descifrado por dicho usuario, a continuacion, llamamos a la funcion de firmar y encriptar que explicaremos en su seccion mas abajo, y esta se encarga de crear el fichero firmado y encriptado que sera subido a la API, finalmente hacemos la llamada a la API para que el fichero sea subido, y este se guarda en la lista de ficheros subidos por mí.

### Bajar un fichero
Para bajar un fichero, con la funcion *file_download*, el proceso es hacer la llamada a la API con el ID del fichero el cual queremos bajar, una vez lo bajamos,
guardamos este fichero encriptado directamente de cara al sigueinte paso, el cual es llamar a la funcion de desencriptar que comentarmeos tambien mas abajo. Si se desencripta correctamente, estamos listos para validar la autenticidad del emisor, para ello, primero obtenemos la clave publica del emisor con otra llamada a la API, y llamamos a la funcion que valida la firma, guardando el fichero desencriptado en local.

Hay que comentar que en este punto hay un par de problemas:
1. Como hemos comentado, necesitas el ID del fichero, lo cual es entendible, pero si es otro usuario que no seas tú el que "te ha subido el fichero", no tienes ninguna manera de encontrarlo que no sea "diciendote el ID del fichero a boca o mandandote un mensaje", ya que la funcion que comentaremos a continuacion de listar ficheros solo nos permite listar aquellos de los que somos propiedad, y no pudiendo buscar los de otros usuarios (al menos que nos hayan subido a nosotros).
2. Cuando el fichero se descarga, la API tan solo nos proporciona de respuesta el ID del fichero, pero no sabemos el nombre con el que se subio, lo cual es menos preocupante, ya que le hemos asignado directamente el ID del fichero como nombre, pero para tipos de archivos como .jpg al descargarse sin extension, no hemos podido abrirlo sin que sea "poner forzadamente" el .jpg en el fichero descargado (con eso si funcionaba).

### Listar ficheros
La funcion *file_list*, la cual no recibe argumentos, y mediante una llamada a la API nos proporciona la lista de nuestros ficheros, con los campos de ID y nombre del fichero que mostamos por pantalla.

### Borrar fichero
La funcion *file_delete*, recibe el ID del fichero a borrar, y mediante una llamada a la API, elimina el fichero de nuestro registro de ficheros.

## Gestion de Ficheros en Remoto - *files_remote_managment.py*

### Cifrar fichero 
Esta recogida en la funcion de *encrypt_local*, como estas funciones se realiza en local, no sera necesaria las llamadas a la API, por ello, simplemente cogemos nuestra clave publica *la cual debemos tener creada*, ya que nosotros mismos seremos los receptores del mensaje (lo encriptaremos con nuestra propia clave publica) y el fichero a encriptar. Finalmente se encripta y se guara en local.

### Firmar en Local
Esta fincionalidad esta recogida en la funcion *sign_local*, y permite crear una firma (hash), y concatenarle el texto en plano, simplemente para hacer la prueba de autenticidad, para ello, simplemente se llama a la funcion de firma del fichero con el fichero especificado.

### Firmar y cifrar en local
Esta engloba las dos anteriores en una, con la funcion *encrypt_sign_local*, llama a la misma funcion que llama la de subir ficheros a al servidor ya que en realidad se realiza el mismo procedimiento, pero esta vez en local y con nuestra clave publica como destinatario.

### Descifrar en local
Esta funcion *decrypt_local*, se encarga de descifrar, y en caso de ser un fichero adicionalmente firmado, verifica la firma.
Esto se consigue primero descifrando en ambos casos, y luego se comparan los ficheros, si el que acabamos de descifrar es el mismo que el que fichero que tuvimos que firmar antes de encriptar (esto se produce cuando primero se firma, y nos da este fichero que posteriormente se encripta, es el mismo que el que desencripto ya que me queda tan solo con la firma), significa que es el mismo entonces solo en este caso verificamos la firma y guardamos en local.

### Verificar la firma
Esta funcion *unsign_local* tan solo valida la firma a partir de un fichero solo firmado.

## Funciones de Cifrado - *encryption.py*

### Generar par de claves
La funcion que genera el par de claves publica y privada *generate_user_key*, genera par de claves RSA de 2048 bits publica y privada

### Firmar un mensaje
Esta funcion *sign_message* recibe el path donde esta el fichero que quiere firmar, y el path donde quiere que se guarde el fichero, este diseño se ha adoptado asi en todas las funciones, ya que queriamos distinguir entre ficheros que se cifran y/o firman en local y en remoto (esta distribucion de rutas que hemos elegido lo explicaremos mas adelante).
Esta funcion obtiene la clave privada guardada localmente, lee el fichero (mensaje) que se quiere firmar, realiza la firma digital, que basicamente es firmar con nuestra clave privada el hash del mensaje. Esto se realiza del hash en vez del mensaje entero porque el hash siempre tiene un tamaño fijo (256 bits en este caso), y el texto plano podria ser muy amplio, y cifrarlo con la clave privada podria tener unos costes computacionalmete muy caros.
Finalmente, se concatena a esa firma digital el mensaje en plano y se manda a crear un fichero firmado en el path destino.

### Verificar la firma
Esta funcion *confirm_sign*, la cual obtiene el file_path del fichero, y la clave publica con la que queremos verificar la firma. Se abre el fichero en primer lugar y se obtiene el la firma digital (hash 256 bits), y el mensaje concatenado en plano. Esto pasa porque recordemos que esta funcion solo va a verificar que el emisor cuya clave publica es la especificada, es quien dice ser, pero en este punto siempre supondremos que se habra decodificado el fichero anteriormente.
Entonces se verifica que (sabiendo que la firma del mensaje son los primeros 256 bits), los primeros 256 bits tienen que ser iguales que los bits del 257 al final pasados por el hash, esta validacion se debe hacer con la clave publica del emisor ya que recordemos que este firmo el fichero con su clave privada, y la unica manera de validarlo es mediante la clave publica.

### Cifrar el fichero
Esta funcion *encrypt_file*, recibe las rutas donde se encuentra el fichero a cifrar, y la ruta donde queremos guardar el fichero cifrado, ademas de la clave publica del receptor con la que cifraremos el fichero para asegurar la confidencialidad.
En primer lugar, abrimos el fichero, y dependiendo de la funcion de donde se llame este puede ser un fichero previamente firmado o siemplemente en plano, pero eso no altera el procedimiento de cifrado.
Generamos un vector de inicializacion de 16 bytes y una clave simetrica de 32 bytes como se indica en el enunciado, ya que vamos a usar un cifrado con clave simetrica y modo de cifrado de bloque CBC visto en teoria.
Encriptamos con la clave simetrica el mensaje leido del fichero, cabe destacar que se hace un cifrado simetrico del mensaje porque el coste computacional de un cifrado asimetrico con clave publica seria mucho mas alto que los cifrados simetricos.
Pero como el cifrado simetrico supondria tener que compartir con el receptor por otro canal esta clave, se va a realizar un cifrado con la clave publica del receptor esa clave simetrica, que es mas corta que el mensaje entero (32 bytes), y de esta manera nos aseguramos que el receptor va a poder con su clave privada descifrar la clave asimetrica, y posteriormente con esa clave simetrica desencriptar el mensaje.
Finalmente, se compone el mensaje final que seria (vector de inicializacion, clave simetrica encriptada con clave publica del receptor, mensaje firmado y encriptado con dicha clave simetrica), y se crea el fichero en la ruta de destino.

### Descifrar el fichero
Para desencriptar un fichero se usa la funcion *decrypt_file*, la cual recibe el par de rutas de donde se encuntra el fichero a desencriptar, y donde se quiere guardar el fichero desencriptado.
Este proceso es el opuesto al anterior, suponiendo que nosotros somos el receptor, leemos el fichero encriptado y lo guardamos. Dividimos el fichero de la manera estandar que se ha propuesto (vector de inicializacion, clave simetrica cifrada, mensaje cifrado), y lo que hacemos es a partir de nuestra clave privada, desencriptamos la clave simetrica que se supone que debia estar cifrada con nuestra clave publica, si eso no es asi, el mensaje no era para nosotros y debe saltar excepcion. A continuacion, una vez tenemos la clave simetrica, lo que hacemos es descifrar el mensaje encriptado con la clave simetrica y el vector de inicializacion en el modo CBC con el que fue cifrado obteniendo asi el mensaje en plano.
Finalmente se crea un fichero desencriptado en la ruta de destino.

*Hay que comentar que en este punto el fichero que se tiene es el que tiene La firma digital + el mensaje en plano, cuando este sea validado, se creara el fichero final*

## Arbol/Organizacion de directorios del Proyecto - **routes.py**

Las rutas del proyecto donde est eva a almacenar los ficheros viene descrito en el fichero de *routes.py*, pero el arbol quedaria de la siguiente manera:
    
    |-- resources                   -> Carpeta principal 
    |   |-- files                   -> Carpeta donde se encuntran los ficheros con los que vamos a interctuar
    |   |-- local                   -> Carpeta destinada a las operaciones en local
    |   |   |-- signned              -> Mensajes firmados con el comando de firmar en local
    |   |   |-- encrypted            -> Ficheros encriptados o encriptados y firmados con los comandos en local
    |   |   |-- decrypted            -> Ficheros desencriptados en local
    |   |--remote                   -> Carpeta destinada a las operaciones en remoto
    |   |   |-- signned              -> Ficheros firmados listos para ser subidos remotamente  
    |   |   |-- uploads              -> Directorio especifico de los ficheros que he subido al servidor
    |   |   |-- downloads            -> Directorio especifico para las operaciones de descargas del servidor
    |   |   |   |-- encrypted         -> Ficheros que han sido bajados del servidor que siguen encriptados
    |   |   |   |-- decrypted         -> Ficheros que han sido bajados del servidor y desencriptados (resultado final de los ficheros bajados)


## Dificultades encontrados

Como problemas encontrados, al hacer el *file_download* del servidor, al principio pensamos que el contenido cifrado del fichero se encontraba en la variable *response.text*, y efectivamente cuando imprimíamos esto nos daba un texto cifrado, pero cuando veiamos el tamaño del fichero nos dabamos cuenta que era menor del que habiamos subido al servidor (como que se perdian bytes por el camino), pero finalmente encontramos que la variable ue contenia el texto cifrado era *response.content*.
Otra dificultad, era cuando nos decia que cuando subiamos un fichero a la API, habia tener en cuanta que no era un json lo que se mandaba en el request, ya que habia que subirlo como un formulario, por eso pusimos *files*.

## Conclusiones
Esta segunda practica nos ha servido para obtener los cocimientos a alto nivel (mediante librerias de python3) de Criptografia vistos en teoria, y precisamente eso ha sido quizás lo más desafiante, ya que tuvimos que entender bien mas o menos como se utilizaba la libreria a la hora de cifrar y firmar.
Además, tambien aprendimos el modulo de python de requests, para consumir como cliente una API.
