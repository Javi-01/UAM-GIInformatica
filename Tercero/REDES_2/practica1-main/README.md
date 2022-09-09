# Practica 1 Redes 2 - Servidor WEB
# Introducción
En esta práctica, se ha implementado un servidor local que acepta, procesa y responde peticiones concurrentes siguiente el protocolo de comunicación de HTTP.
La comunicación entre clientes y servidor se ha realizado mediante sockets TCP, orientados a la conexión.
Los verbos soportados por el servidor son el GET, POST y OPTIONS.
El servidor tambien soporta la ejecución de scripts python y php

## Preparación del servidor
El servidor dispone de un fichero de configuracion del servidor *server.conf* con los siguientes parámetros:

    server_root = "./www"
    max_clients = 5
    listen_port = 7001
    server_signature = "Pareja1_server"

Donde *server_root*, indica donde se encuentran los recursos que va a porporcionar el servidor, *max_clients* es el máximo de clientes por los que va a escuchar el servidor,
*listen_port* es el puerto del servidor, y *server_signature* es el campo del "server" que vendrá en la cabecera de las respuestas del servidor.

## Ejecución
En primer lugar, comentar que debido a que se ha hecho uso de la libreria de *libconfuse* para parsear el fichero de *server.conf*, es necesario instalar dicho comando:

    sudo apt-get update
    sudo apt-get install libconfuse-dev

A continuación, configuramos el *server.conf* y nos colocamos en la raíz del proyecto ejectando **make**, y obteniendo un fichero *server*.
Finalemnte, ejecutamos **./server**, y de esta manera, el servidor ya ha iniciado.

## Funcionamiento
Necesitamos un cliente, que envie peticiones al servidor, nosotros hemos empleado herramientas como *curl* o *wget*, pero el cliente más común es el *navegador web*.

### Abrimos un navegador Web
Debemos indicar en primer lugar, el uso del protocolo http (esto es opcional porque el navegador lo utiliza de manera predetermianda) *http://*, seguido de la direccion local *localhost* o *127.0.0.1*, y después de los *:* el puerto que pusimos al servidor para manetenerse a la escucha de peticiones, en nuestro caso *7001*, y seguido de un */* la url del recurso:

    **http://localhost:7001/index.html**

De esta manera, lo que estamos indicando es que dentro de nuestro *server_root*, que era *./www*, siendo *./* el root del proyecto, vamos a buscar un fichero que es el *index.html*:

    **./www/index.html**

Y este procedimiento es el que se haría para todo tipo de fichero que contenga nuestro servidor web, de manera que si el recurso no es encontrado devuelve el conocido *error 404*.

# Desarrollo técnico
### Socktes y Paralelización
Para empezar a desarrollar esta práctica, creamos el fichero de *sockets.c*, con la finalidad de encapsular el funcionamiento de la API de sockets que nos proporciona c.
En este módulo, se crean todas las funciones necesarias para empezar a utilizar los sockets, así como el control de errores de estos.
Una vez finalizada la programación de las funciones de los sockets, se crea un nuevo fichero *server.c*, que va a ser el main del programa, en el cual se realizaron pruebas de creación de sockets TCP y envió y recepción de mensajes simples.
Cuando esa parte era funcional, creamos un fichero *server_socket_conf.c*, el cual es muy sencillo, contiene una función que llama a su vez a las tres funciones necesarias para hacer el “setup” del servidor (socket_create, socket_bind y socket_listen), así de esta manera reducimos del main el tener que estar llamando a esas tres funciones por separado.
Posteriormente, paralelizamos le servidor realizando un *fork* en el momento posterior al de aceptar un cliente, así las peticiones se atienden de manera paralela. Por otro lado, se crea el manejador de las señales, de manera que si ejecutamos *ctrl + C* finalizamos la ejecución de manera consistente cerrando las conexiones de sockets necesarias.

### Servidor HTTP
Una vez el servidor funcionaba correctamente a cadenas de "texto" sencillas, ahora habia que establecer el protocolo de comunicación HTTP cliente-servidor.
Para ello en primer lugar, creamos le fichero de configuracion del servidor *server.conf*, e implementamos el parseo del mismo en el main de la aplicación, siguiendo la API de *libconfuse* permitida.
Desde el main es donde mandamos a procesar las peticiones desde la funcion *process_http_request*, pasandole el buffer de lectura del socket, y la configuracion del servidor, tenemos todo lo necesario para empezar a procesar las peticiones HTTP.

Para ello, creamos el modulo *process_http.c*, el cual sera el encargado de realizar toda la funcionalidad de parseo, procesado y respuesta de las peticiones HTTP.
En primer lugar, al recibir el buffer de lectura, se ha empleado la herramienta de parseo HTTP *picohttpparser*, la cual nos proporciona la URL, los "headers" HTTP, la version de HTTP, el verbo utilizado (GET, POST, OPTIONS).
Una vez tenemos toda esa infromacion almanecanda, tenemos tres funciones que procesan por separado dependiendo del verbo que ha solicitado:
1. **process_http_GET_response**

    Esta funcion recibe la URI del recurso, y demas infromacion relevante para crear la respuesta.
    Para ello, en primer lugar intenta abrir el archivo segun la URI, en formato binario, de manera que si no lo puede abrir   
    llamara a *process_404_error_response* para mostrar el error de no encontrado.
    Si lo encuentra, comenzamos a crear los headers de la respuesta, en nuestro caso los obligatorios eran:

        Date: Fecha Actual de la peticion
        Last-Modified: Fecha de ultima modificacion del fichero
        Server: *server_signature* del *server.conf*
        Content-Length: Longitud del recurso solicitado
        Connection: close
        Content-Type: Tipo de recurso

    Para obtener estos datos, se ha creado un modulo aparte *http_utils.c*, para reducir las lineas de codigo y modularizar más el 
    proyecto.

    La *Date* se obtiene en el util con la fecha actual del sistema usando *localtime* y el posterior formateo de la misma 
    para ser entendida por http.
    El campo *Last_modified*, se obtiene de con localtime, pero empleado *stat* para obtener infromacion del fichero.
    *Content-Length* se obtiene con un *stat* tambien del fichero pero esta vez obteniendo el *size*
    Y finalente, *Content-Type*, se obtiene pasando la extension del fichero y mediante unas respuestas predefinidas se 
    devuelve la cadena correspondiente.

    Una vez obtenidos los campos de las cabeceras de respuesta, se comprueba si el fichero es un *script*, en cuyo caso se 
    procesarán los argumentos **pasados por la URL** en el caso del get llamando a *search_request_args* y posteriomente 
    *proccess_script* para obtener el script.

    El primero parsea los argumentos y los guarda en una estructura, y el segundo utiliza esos parametros como input del 
    ejecutable del script, que tras ejecutar un *popen*, se obtiene el output esperado del script para mandarlo de vuelta 
    a la funcion de procesar el GET.

    Finalmente, se concatena la cabecera de la respuesta dejando *\r\n* entre cada campo, y un *\r\n\r\n* para el cuerpo de la 
    respueta.
    El cual se hace en un bucle (debido a los tamaños varibles y en algunos casos muy amplios de algun fichero), para enviar de 
    4096 en 4096 bytes, el fichero que se quiere enviar.

2. **process_http_POST_response**

    Esta funcion es igual que la del GET, a diferencia de que los parámetros son pasados por el body de la peticion y no por la 
    URL, aunque tambien puede recibir parametros por la URL y por el cuerpo.

3. **process_http_OPTIONS_response**

    Esta ultima funcion no devuelve ningun recurso; tan solo se encarga de añadir a la respuesta la siguiente cabecera:

        Allow: GET POST OPTIONS

    De esta manera se indican los verbos soportados.

Finalmente, comentar las funciones de error, los cuales no leen ningun fichero, si no que devuelven una cadena de texto con el correspondiente error en su formato html.

**process_400_error_response**
Se ejecuta en el caso de que el verbo no sea ni GET, ni POST, ni OPTIONS.

**process_404_error_response**
El cual como ya comentamos en el GET, se ejecuta en el caso de que el recurso no se pueda encontrar.

# Dificultades encontradas
Una de los principales problemas fue en cuanto al buffer de escritura en cuanto a ficheros grandes. Hasta que no planteamos el bucle que fuera leyendo de 4096 en 4096 bytes, principalmente las imagenes no cargaban correctamente y en varias ocasiones el servidor se quedaba pillado.

# Conclusiones
En esta primera entrega, se han puesto en practica los conocimientos de manejo de Sockets vistos en teoría, además de tener que profundizar un poco más en el protocolo HTTP.
Por otro lado, al ser c, sabemos a lo que nos enfrentabamos en cuanto a problemas de memoria, pero la practica ha sido bastante entretenida.
Finalmente, comentar que creemos que se ha desarrollado un código de calidad, legible y modulado, que fue planteado antes de codificar ni tan siquiera una línea de codigo.

