import os
import sys
import traceback
from flask import session
from sqlalchemy import create_engine, MetaData

# configurar el motor de sqlalchemy
db_engine = create_engine("postgresql://alumnodb:alumnodb@localhost/si1", echo=False)
db_meta = MetaData(bind=db_engine)

def db_exception(db_conn):
    if db_conn is not None:
        db_conn.close()
        traceback.print_exc(file=sys.stderr)

# FUNCIONES RELACIONADAS CON LA INFORMACIÓN DE LOS USUARIOS
def db_login(username : str):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        db_result = db_conn.execute(f"""Select password from customers
                                    where username = '{username}'""")
        db_conn.close()

        return list(db_result)
    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_registred(username : str, password : str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""Select username, "password"
                                    from customers
                                    where username = '{username}'
                                    and "password" = '{password}'""")
        db_conn.close()

        return list(db_result)

    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el registro'


def db_crear_cuenta(
    firstname : str, lastname : str, username : str, password : str,
    email : str, creditcardtype : str, creditcard : str,
    creditcardexpiration : str, address1 : str, country : str,
    region : str, city : str, balance : int
    ):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        # Obtenemos el id del último customer registrado
        db_id = list(db_conn.execute("select customerid from customers order by customerid desc limit 1"))[0][0]
        db_id += 1

        # Insertamos el usuario en la BBDD
        db_result = db_conn.execute(f'''Insert into customers
                                    (firstname, lastname, username, "password", email, creditcardtype,
                                    creditcard, creditcardexpiration, address1, country, region, city,
                                    balance, customerid)
                                    values (lower('{firstname}'), lower('{lastname}'), '{username}', '{password}',
                                    lower('{email}'), lower('{creditcardtype}'), '{creditcard}', lower('{creditcardexpiration}'),
                                    lower('{address1}'), lower('{country}'), lower('{region}'), lower('{city}'), {balance}, {db_id})''')

        db_conn.close()
        return

    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con crear una cuenta'


def db_user_change_password(username : str, email : str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""Select username, "password"
                                    from customers
                                    where username = '{username}'
                                    and email = lower('{email}') """)
        db_conn.close()
        return list(db_result)

    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_customer_ID(username : str, password : str, email : str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()
        db_result = db_conn.execute(f"""select c.customerid
                                      from customers c
                                      where c.username = '{username}'
                                      and c."password" = '{password}'
                                      and c.email = lower('{email}')""")

        customer_id = list(db_result)[0][0]

        db_conn.close()
        return customer_id
    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_update_password(username : str, new_password : str, email : str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""Update customers
                                    set "password" = '{new_password}'
                                    where username = lower('{username}')
                                    and email = lower('{email}') """)

        db_conn.close()
        return

    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_email(username : str, password : str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""select email from customers
                                    where username = '{username}'
                                    and "password" = '{password}' """)

        db_conn.close()
        return list(db_result)
    except:
        db_exception(db_conn)
        return 'Something is broken'

# FUNCIONES RELACIONADAS CON EL HOME Y EL DETAIL DE LAS PELICULAS

def db_load_movies():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        db_movies = db_conn.execute("Select * from imdb_movies limit 10")
        db_conn.close()

        peliculas = []
        for reg in db_movies:
            pelicula = {}
            pelicula['id'] = int(reg[0])
            pelicula['titulo'] = str(reg[1])
            pelicula['poster'] = 'img/base_movie.jpg'
            peliculas.append(pelicula)
        return peliculas

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_load_categories():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        db_categories = db_conn.execute("Select g.genre from genres g")
        db_conn.close()

        categories = []
        for reg in db_categories:
            categories.append(str(reg[0]))
        return categories

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_movies_by_category_and_search(category: str, search: str):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()
        if search == '':
            db_movies_by_category = db_conn.execute(f"""Select m.movieid,
                                                    m.movietitle from imdb_movies m
                                                    join imdb_moviegenres mg
                                                    on m.movieid = mg.movieid
                                                    join genres g on
                                                    g.genre_id  = mg.genre_id
                                                    where g.genre = '{category}'
                                                    limit 10""")
        else:
            db_movies_by_category = db_conn.execute(f"""Select m.movieid,
                                                    m.movietitle from imdb_movies m
                                                    join imdb_moviegenres mg
                                                    on m.movieid = mg.movieid
                                                    join genres g on
                                                    g.genre_id  = mg.genre_id
                                                    where Lower(m.movietitle)
                                                    like lower('%%{search}%%')
                                                    and g.genre = '{category}'
                                                    limit 10""")
        db_conn.close()

        peliculas = []
        for reg in db_movies_by_category:
            pelicula = {}
            pelicula['id'] = int(reg[0])
            pelicula['titulo'] = str(reg[1])
            pelicula['poster'] = 'img/base_movie.jpg'
            peliculas.append(pelicula)
        return peliculas

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_load_movie_by_id(movieid : int):
    try:
        # Se obtienen primero los campos de la pelicula
        db_conn = None
        db_conn = db_engine.connect()
        # Se obtiene la pelicula con ese id
        db_movies = list(db_conn.execute(f"Select m.movieid, m.movietitle, m.year from imdb_movies m where m.movieid = %d"%movieid))[0]
        # Se obtienen los actores de la pelicula
        db_actors = db_conn.execute(f"Select a.actorname, a.gender, ma.character from imdb_movies m join imdb_actormovies ma on m.movieid = ma.movieid join imdb_actors a on a.actorid  = ma.actorid where m.movieid = %d"%movieid)
        # Se obtienen las ediciones de la pelicula con sus precios
        db_products = db_conn.execute(f"Select p.price, p.description from imdb_movies m join products p on m.movieid = p.movieid where m.movieid = %d"%movieid)
        # Se obtienen los generos de la pelicula
        db_genres = db_conn.execute(f"Select g.genre from imdb_movies m join imdb_moviegenres mg on m.movieid = mg.movieid join genres g on g.genre_id  = mg.genre_id where m.movieid = %d"%movieid)
        # Se obtienen los idiomas disponibles de la pelicula
        db_lenguajes = db_conn.execute(f"Select l.language from imdb_movies m join imdb_movielanguages ml on m.movieid = ml.movieid join languages l on l.language_id  = ml.language_id where m.movieid = %d"%movieid)
        #Se obtienen los directores de la pelicula
        db_directors = db_conn.execute(f"Select d.directorname from imdb_movies m join imdb_directormovies id on m.movieid = id.movieid join imdb_directors d on id.directorid = d.directorid where m.movieid = %d"%movieid)
        db_conn.close()

        pelicula = {
        'id' : int(db_movies[0]),
        'anno' : str(db_movies[2]),
        'poster' : 'img/base_movie.jpg',
        'actores' : [],
        'ediciones' : [],
        'titulo' : str(db_movies[1])
        }

        for reg in db_actors:
            actor = {}
            actor['nombre'] = str(reg[0])
            actor['genero'] = str(reg[1])
            actor['personaje'] = str(reg[2])
            actor['foto'] = 'img/character.png'
            pelicula['actores'].append(actor)

        for reg in db_products:
            edicion = {}
            edicion['precio'] = int(reg[0])
            edicion['tipo'] = str(reg[1])
            pelicula['ediciones'].append(edicion)

        genres = []
        for reg in db_genres:
            genres.append(str(reg[0]))
        pelicula['generos'] = genres

        lenguajes = []
        for reg in db_lenguajes:
            lenguajes.append(str(reg[0]))
        pelicula['lenguajes'] = lenguajes

        directors = []
        for reg in db_directors:
            directors.append(str(reg[0]))
        pelicula['directores'] = directors

        return pelicula

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_top_actors(genre : str, num_actors : int):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        if genre == '' and num_actors == 0:
            db_result = list(db_conn.execute(f"""select * from
                                            getTopActors()"""))

        if genre != '' and num_actors != 0:
            db_result = list(db_conn.execute(f"""select * from
                                            getTopActors('{genre}', {num_actors})"""))

        if genre != '' and num_actors == 0:
            db_result = list(db_conn.execute(f"""select * from
                                            getTopActors('{genre}')"""))

        resultados =  []
        for row in db_result:
            update = False
            for actor in range(0, len(resultados)):
                if resultados[actor]['nombre'] == row[0]:
                    update = True
                    pos = actor
                    break
            if update:
                resultados[actor]['director'].append(row[4])
            else:
                resultados.append({
                    'nombre' : row[0],
                    'num_actuaciones' : row[1],
                    'debut' : row[2],
                    'pelicula_debut' : row[3],
                    'director' : [row[4]],
                    'id' : row[5]
                })

        db_conn.close()
        return resultados

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_product_by_movieid_and_edition(movieid : int, edition : str):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        db_price_movie = list(db_conn.execute(f"Select p.prod_id, p.price from products p join imdb_movies m on m.movieid = p.movieid where m.movieid = {movieid} and p.description = '{edition}'"))[0]
        db_conn.close()

        product = {
            'id' : int(db_price_movie[0]),
            'price' : float(db_price_movie[1])
        }
        return product

    except:
        db_exception(db_conn)
        return 'Something is broken'


#FUNCIONES RELACIONADAS CON EL CARRITO Y LA VENTA
def db_user_get_cart(customerid : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        #Si el usuario no tiene un carrito, se le crea uno
        db_order = list(db_conn.execute(f"Select o.orderid from orders o join customers c on o.customerid = c.customerid where c.customerid = {customerid} and o.status is NULL"))
        orderdetails = []
        if len(db_order) is 0:
            db_id = list(db_conn.execute("select orderid from orders order by orderid desc limit 1"))[0][0]
            db_id += 1
            session['orderid'] = db_id
            db_conn.execute(f"Insert into orders values ({db_id}, now(), {customerid}, 0, 21, 0, NULL)")
        #Si tiene carrito cogemos ese id y lo metemos en la sesion
        else:
            db_order = db_order[0][0]
            session['orderid'] = db_order
            #Se obtiene el todos los datos relevantes para el carrito
            db_orderdetail = db_conn.execute(f"""Select im.movieid, im.movietitle, p.prod_id, p.description, od.price, od.quantity from orders o 
                                                join orderdetail od on o.orderid = od.orderid
                                                join products p on p.prod_id = od.prod_id
                                                join imdb_movies im on im.movieid = p.movieid
                                                    where o.orderid = {db_order}""")

            for reg in db_orderdetail:
                flag = False
                #SI esa pelicula no esta en la compra, la introducimos de 0
                if len(orderdetails) > 0:
                    for i in range(0, len(orderdetails)):
                        if orderdetails[i]['pelicula']['id'] == int(reg[0]):
                            flag = True
                            index = i
                            break
                if flag is False:
                    orderdetail = {}
                    #Añadimos datos relevantes de la pelicula
                    orderdetail['pelicula'] = {
                        'id' : int(reg[0]),
                        'titulo' : str(reg[1]),
                        'poster' : 'img/base_movie.jpg'
                    }
                    orderdetail['precio_total'] = (float(reg[4]) * int(reg[5]))
                    orderdetail['productos'] = []
                    orderdetail['productos'].append({
                        'id' : int(reg[2]),
                        'edicion' : str(reg[3]),
                        'precio' : float(reg[4]),
                        'cantidad' : int(reg[5])
                    })
                    orderdetails.append(orderdetail)
                #SI la pelicula esta, actualizamos los campos
                else:
                    orderdetails[index]['precio_total'] += (float(reg[4]) * int(reg[5]))
                    orderdetails[index]['productos'].append({
                        'id' : int(reg[2]),
                        'edicion' : str(reg[3]),
                        'precio' : float(reg[4]),
                        'cantidad' : int(reg[5])
                    })
                session['total_precio'] += float(reg[4]) * int(reg[5])
                session['total_cantidad'] += int(reg[5])
        db_conn.close()
        return orderdetails

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_update_order_cart_data():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        if 'orderid' in session:
            orderid = session['orderid']
            db_order = list(db_conn.execute(f"Select o.netamount, sum(od.quantity) as sum from orderdetail od join orders o on od.orderid = o.orderid where o.orderid  = {orderid} group by o.netamount"))
            db_conn.close()
            if len(db_order) is 0:
                session['total_precio'] = 0
                session['total_cantidad'] = 0
            else:
                session['total_precio'] = float(db_order[0][0])
                session['total_cantidad'] = db_order[0][1]
        return
    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_orderdetail_cart_session(products : list):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Analizamos los productos del carrito y dependiendo de si estan o no
        # realizaremos una consulta
        if 'orderid' in session:
            orderid = session['orderid']
            db_products = db_conn.execute(f"Select od.prod_id from orderdetail od join orders o on o.orderid = od.orderid where o.orderid = {orderid}")
            products_to_update = []
            products_to_insert = []

            for prod in range(0, len(products)):
                flag = False
                for prod_id in db_products:
                    if products[prod]['id'] == int(prod_id[0]):
                        products_to_update.append(products[prod])
                        flag = True
                        break
                if flag is False:
                    products_to_insert.append(products[prod])

            db_conn.close()
            if len(products_to_update) > 0:
                db_update_cart_products(products_to_update)
            if len(products_to_insert) > 0:
                db_insert_products_into_cart(products_to_insert)

        return

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_insert_products_into_cart(products : list):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se insertan tantos elementos al carrito como productos nuevos haya
        if 'orderid' in session:
            orderid = session['orderid']
            consulta = "Insert into orderdetail values "
            i = 0
            for prod in products:
                consulta += f"({orderid}, {prod['id']}, {prod['precio']}, {prod['cantidad']})"
                if i != len(products) - 1:
                    consulta += ", "
                i+=1

            db_products = db_conn.execute(consulta)
            db_conn.close()
        return

    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_update_cart_products(products : list):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se insertan tantos elementos al carrito como productos nuevos haya
        if 'orderid' in session:
            orderid = session['orderid']
            for prod in products:
                db_conn.execute(f"""Update orderdetail od
                set quantity = quantity + {prod['cantidad']}
                from (select o.orderid from orders o where o.orderid = {orderid}) as N
                where od.orderid = N.orderid and od.prod_id = {prod['id']}""")

            db_conn.close()
        return
    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_delete_cart_products(products : list):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se eliminan los items pasados en la lista de productos
        if 'orderid' in session:
            orderid = session['orderid']

            for prod in products:
                db_conn.execute(f"""Delete from orderdetail
                where orderid = {orderid} and prod_id = {prod['id']}""")

            db_conn.close()
        return
    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_get_buy_summary(customer_id : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se eliminan los items pasados en la lista de productos
        if 'orderid' in session and 'usuario' in session:
            orderid = session['orderid']
            db_sum = list(db_conn.execute(f"""Select o.netamount, o.totalamount, c.balance, c.loyalty
                                             from orders o join customers c on o.customerid = c.customerid
                                             where o.orderid = {orderid} and
                                             c.customerid = {customer_id}"""))[0]
            db_conn.close()
            summary = {
                'precio' : float(db_sum[0]),
                'precio_iva' : float(db_sum[1]),
                'saldo' : float(db_sum[2]),
                'puntos' : float(db_sum[3])
            }
        return summary
    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_update_status_to_paid():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se eliminan los items pasados en la lista de productos
        if 'orderid' in session and 'usuario' in session:
            orderid = session['orderid']
            db_conn.execute(f"""update orders set
                                status = 'Paid'
                                where orderid = {orderid}"""
            )
            db_conn.close()
        return
    except:
        db_exception(db_conn)
        return 'Something is broken'


def db_check_status():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se eliminan los items pasados en la lista de productos
        if 'orderid' in session and 'usuario' in session:
            orderid = session['orderid']
            db_status = list(db_conn.execute(f"""select o.status from orders o
                                             where o.orderid = {orderid}"""))[0][0]
            db_conn.close()
        return db_status
    except:
        db_exception(db_conn)
        return 'Something is broken'

# FUNCIONES PARA REALIZAR CONSULTAS RELACIONADAS CON EL HISTORIAL
def db_user_update_balance(username : str, password : str, email : str, balance : int):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        old_balance = db_conn.execute(f"""select balance from customers
                                    where username = '{username}'
                                    and email = lower('{email}')
                                    and "password" = '{password}'""")

        old = list(old_balance)[0][0]
        new_balance = balance + int(old)

        db_result = db_conn.execute(f"""Update customers
                                    set balance = {new_balance}
                                    where username = '{username}'
                                    and email = lower('{email}')
                                    and "password" = '{password}'""")

        db_conn.close()
        return
    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_balance_loyalty(customer_id : int):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""Select c.loyalty, c.balance
                                        from customers c
                                        where c.customerid = {customer_id} """)

        values = list(db_result)
        print(values)
        dict = {'loyalty' : round(values[0][0], 2), 'balance' : values[0][1]}

        db_conn.close()
        return dict
    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'


def db_user_historial(customer_id : int):
    try:
        # conexion a la base de datos
        db_conn = None
        db_conn = db_engine.connect()

        db_result = db_conn.execute(f"""select o.orderid, o.status, o.orderdate, sum(o2.quantity),
                                      o.totalamount as precio_total
                                      from orders o, customers c, orderdetail o2
                                      where o.orderid = o2.orderid
                                      and c.customerid = o.customerid and c.customerid = {customer_id}
                                      and o.status is not NULL
                                      group by o.orderid,  o.totalamount order by o.orderdate desc""")
        orders_ids = []
        dict_general_compras = {}
        for orders in list(db_result):
            orders_ids.append(orders[0])
            dict_general_compras[str(orders[0])] = {
                    "status" : str(orders[1]),
                    "fecha" : str(orders[2]),
                    "num_articulos" : str(orders[3]),
                    "precio_total" : str(orders[4]),
                    "order_id" : str(orders[0]),
                }

        dict_by_individual_order = {}
        for order_id in orders_ids:
            db_result = db_conn.execute(f"""Select im.movieid, im.movietitle,
                                        o2.quantity, o2.quantity*o2.price, p.description
	                                    from orders o, customers c, orderdetail o2, products p,
                                        imdb_movies im where o.orderid = o2.orderid
	                                    and c.customerid = o.customerid and o2.prod_id = p.prod_id
                                        and p.movieid = im.movieid and o.orderid = {order_id}
	                                    group by im.movieid, o2.quantity, o2.price, p.description""")

            peliculas = []
            for movie in list(db_result):
                dict_peli = {
                    "movie_id" : str(movie[0]),
                    "movie_title" : str(movie[1]),
                    "quantity" : str(movie[2]),
                    "price" : str(movie[3]),
                    "poster" : "img/base_movie.jpg",
                    "description" : str(movie[4])
                }
                peliculas.append(dict_peli)

            dict_by_individual_order[str(order_id)] = peliculas

        db_conn.close()
        return [dict_general_compras, dict_by_individual_order]
    except:
        db_exception(db_conn)
        return 'Error en la consulta relacionada con el login'
