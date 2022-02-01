* Datos administrador: 
- Usuario: Administrador
- Contraseña: teatro 

* Para cambiar entre sesiones, habrá que cerrar la aplicación.

* Modo sesión invitado: 
- La primera pestaña que se muestra es la de inicio, donde tienes la opción de registrarse o inciar sesión. 
- En caso de no inicar sesión/registrarte, contamos con dos opciones más (dos pestañas más).
- La primera que es Buscar evento, donde puedes filtrar por un nombre introducido o en caso de dar al botón de +
buscar sin introducir ningún de evento se mostrarán todos los eventos que haya en el teatro.
- Y la siguiente pestaña que es la de Buscar Representaciones sigue la misma dinámica para filtrar y buscar representaciones 
por dicho filtro, pero en este caso los filtros son por "danza, teatro o concierto" y se mostrarán aquellas representaciones
que pertenezcan a un evento de ese tipo de evento filtrado. 

* Modo usuario registrado: 
- En la primera pestaña que se muestra, habrá que iniciar sesion (introduciendo los datos en los campos de arriba) o 
registrarse (que habrá que rellenar los campos de la ventana flotante que sale), y si se introducen bien o mal los datos, habra que dar al 
botón de "OK" que se muestra por pantalla.
- Una vez iniciada la sesión, encontraremos 5 pestañas, siendo estas y por orden: "Buscar Evento, Buscar Ciclos, Comprar Entradas, Comprobar Abono, 
Notificaciones y Mis Compras".
- En la pestaña "Buscar Evento", se buscan eventos de la misma manera que en el modo de sesión como invitado.
- En la pestña "Buscar Ciclos", se sigue la misma dinámica que buscar eventos, si se pincha en el botón buscar sin indicar un nombre, se mostrarán todos los
ciclos que hayan en el sistema o se puede filtar por nombre. 
- En la pestaña "Compras Entradas", se filtrarán las representaciones por los tres tipos de eventos y dependiendo del tipo elegido, se mostrarán aquellas 
representaciones que no hayan finalziado y que sean de ese tipo, pinchando en una de ellas, se cambiará de ventana a otra en la que se mostrará información
acerca de las zonas de la representación indicando nombre, tipo y aforo restante (en caso de ser una zona compuesta,no se mostrará el aforo, si no que 
pinchando en ella se verá otra pestaña en la que se verán las subzonas que contiene con la misma información que se mostraba previametente). Una vez se pinche
en una zona simple, si es de Pie se mostrará una ventana con un slider indicando el númeor de entradas que se queiren comprar y en caso de ser Sentado, 
además se podrá elegir una sugerencia de como se quiere la disposición (en caso de haber sugerencias en una ventana auxiliar, dará la opción de o bien comprar
las entradas sugeridas o de introducir las entradas que quieres (con espacios entre los identificadores que se introduzcan) entre las disponibles 
(dependiendo de si son sugeridas, libres u ocupadas, saldrán en un color u otro) y una vez elgidas, tanto si son entradas de Pie o Sentado, se pasará a la ventana
del carrito donde se mostrará un resumen de la compra y podrás o bien cancelar la compra (con el botón de voler), reservar o confirmarla (y en caso de tener que pagar con tarjeta se pedirá que se 
introduzca el número de la tarjeta, número de 16 dígitos). 
- En la pestaña "Comprar Abonos", se podrán buscar abonos o ciclos y dependiendo que se filtre se mostrarán todos los de ese tipo de filtro y pinchando en uno de ellos
se pasara al carrito de compra (el mismo que para comprar entradas) donde se mostrará un resumen de la compra y dando a confirmar pedirá también un número de tarjeta.
- En la pestaña "Notifiaciones" donde se mostrará una tabla con las notificaciones no leidas por el usuario y además se mostrará información de la notifiación, si se pincha en una de las notificaciones
se mostrará una ventana po rpantalla y se actualizará la tabla. 
- En la pestaña "Mis Compras" donde puedes filtrar por entradas compradas y reservadas y en la tabla de entradas reservadas, pinchando en una de ellas, se tendrá 
la opción de cancelar la reserva o de comprarla (de forma individual) introduciendo el numero de cuenta en la ventan aque aparece.

* Modo administrador: 
- En la primera pestaña que se muestra, habrá que iniciar sesion con los datos indicados al inicio del fichero y si se introducen bien o mal los datos, habra que dar al 
botón de "OK" que se muestra por pantalla.
- Una vez iniciada la sesión, encontraremos 5 pestañas, siendo estas y por orden: "Crear Evento, Crear Abonos, Gestion Representaciones, Zonas y Aforo 
y por último Estadísticas"
- En la pestaña "Crear Evento" podemos filtrar por el tipo de evento a crear mediante un desplegable y dependiendo del tipo de evento, se mostrarán
unos campos u otros a rellenar, una vez rellenados los campos (deberán estar todos rellenados y la duración deberá ser un int ) al darle al botón siguiente
se pedirá que se rellenen los precios para cada zona del teatro que tendrá el evento creado (en caso de haber zonas creadas, en caso contrario dara error)
y una vez rellenados los datos al darle al botón crear evento pues se creará y en caso de darle a cancelar se vuelve a la pestaña de inicio de "Crear Evento"
- En la siguiente pesataña, "Crear Abono" podemos filtar por crear abonos anuales o ciclos, en caso de seleccionar abonos anuales, se pedirá rellenar el precio 
que tendrá el abono y el nombre de la zona a la que se asignará el abono, una vez rellenados los campos, al darle al botón de crear abono, si no han surgido problemas
el abono será creado. Si en caso contrario, se elige crear un ciclo, se pedirá rellenar otros campos, estos serían, el nombre del ciclo, el porcentaje de reducción 
y la zona, además se odrán añadir eventos o ciclos (previamente creados), para añadir los eventos/ciclos, se pincha en el botón correspondiente y se mostrará una tabla 
con los eventos/ciclos ya creados y pinchando el deseado se volverá a la pestaña y aparecerá en una tabla de previsualización, y como en el caso de abonos, dando al botón 
de crear si no han ocurridos problemas, dicho ciclo se creará. 
- En la pestaña "Gestión Representaciones", habrá tres opciones, "añadir, cancelar o posponer". En añadir, se busca el evento entre los ya creados y se introduce la 
fecha siguiendo el formato indicado (tiene autorellenado, es decir, si introduces el año lo siguiente que introduzcas se añadirá al mes y así...). En cancelar, se busca un evento
con el botón buscar evento y se selecciona una de las representaciones que haya del evento a cancelar. Por último, en posponer, sigue la misma dinámica, buscamos el evento, 
la representacion y se introduce la misma fecha (de la misma manera que en añadiir) y en los tres casos, dando al botón de confirmar se aplican los cambios si no 
han ocurrido errores. 
- En la siguiente pestaña, "Zonas y Aforos", se pueden filtrar 4 opciones, "Crear Zonas, Reducir aforo, Deshabilitar butacas y Entradas a comprar" (cambios que pùede relaizar
el admin sobre campos del teatro). En crear zonas, se filtar por los 3 tipos de zonas a poder crear, y se estabelce el nombre de la zona, aforo (en caso de ser de pie el aforo total
y en caso de ser sentado se indica las filas y columnas) y si se queire crear una zona compuesta, se eligen las zonas que estarán en la zona compuesta (de forma parecida 
a como se buscaban representaciones/eventos) y en todos los casos, con el botón aceptar, si no han ocurrido problemas, se crearán las zonas (no se puede crear zonas si ya hay eventos creados).
Reducir aforo, se busca el evento del que se quiera reducir el aforo, se establece el porcentaje de reducción y con el boton confirmar se aplican los cambios.
Deshabilitar butaca, donde siguiendo la misma dinámica de búsquedas, se busca la zona y en dicha zona se busca la butaca, se indica el motivo, fecha de inicio y fin 
de la deshabilitación y con confirmar se aplican los cambios. Por último, entradas a comprar (número máximo de entradas que puede comprar de seguido un usuario), se 
elige el número con el deslizable y con el botón aceptar se aplican los cambios.
- Como última pestaña, "Estadisticas", donde hay dos tipos de filtros, por recaudación/ocupacion y por evento/zonas, se puede introducir el nombre (nombre exacto) del evento
o de la zona y para dicho evento/zona se pueden elegir si se queiren ver las estadísticas por ocupación o por recaudación. Si no se indica el evento/zona como filtro
pero si recaudación/ocupación, se mostrarán las estadisticas totales de todos los eventos, es decir, si se indica por ocupacion, para cada evento se mostrará que 
ocupación total ha tenido. 
