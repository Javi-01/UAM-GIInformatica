# practica3

## Introducción
En esta practica, se ha realizado una aplicacion P2P en python3, donde varios clientes pueden intercambiar señal de video a traves de la webcam o un video previamente descargado.
Para que estos clientes puedan dar comienzo a la llamada, deben haberse registrado al servidor de descubrimiento proporcionado para dicha practica, y de esta manera conocerse (direccion y puerto), y dar comienzo a la conexión.

## Problemas encontrados
La practica tiene algun problema, entre ellos, cuando se realiza la pausa de transmision por un par, pasamos a otro frame, donde, a pantalla dividida, nuestro marco se encuentra con un gif pausado, pero el del usuario destino se sigue retransmitiendo (porque este no ha pausado), pero por alguna razon que no hemos podido solucionar, recibe muy pocos frames, y se ve entre cortes.
Hay algun tipo de ejecucion, que no termina en estado consistente, entendemos que por culpa de algun thread o alguna variable que no se cierra bien en algun caso, pero en una ejecucion normal, si va correcto.

## Control de FPS
Para la transmision de un paquete, se espera 1/FPS elegidos por el usuario, de manera que entre frame y frame hay ese tiempo.
Para la compresion de la imagen, siempre es al 50%, no hemos tocado ese parametro.
Para el control de congestion, lo que se realiza, es en la recepcion, se intenta reproducir a un ritmo 1/FPS del paquete de llegada; pero si el buffer se vacia mas rapido de lo que se llena, entonces se aumenta el tiempo entre FPS a 1/(FPS*0.5) para que de tiempo a los frames a llegar, y la cola no se quede vacia.
Finalmente, comentar que se comienza con 2 segundos de retardo, para dejar tiempo a empezar a llenar el buffer, de manera que en la mayoria de casos la restranmision ira con 2 segundos de retardo.
