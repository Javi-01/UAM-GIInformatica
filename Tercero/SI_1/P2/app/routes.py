#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from app import app
import database
from flask import render_template, request, url_for, redirect, session, json
from random import randint, randrange
from datetime import datetime
from hashlib import blake2b
import os

def is_movie_in_session_cart(movie):
    for i in range(0, len(session['carrito'])):
        if session['carrito'][i]['pelicula']['id'] == movie['id']:
            return True
    return False


def get_index_of_session_cart_element(movie):
    for i in range(0, len(session['carrito'])):
        if session['carrito'][i]['pelicula']['id'] == movie['id']:
            return i
    return None

def get_index_of_session_cart_element_by_edition(movie, edition):
    for i in range(0, len(session['carrito'])):
        if session['carrito'][i]['pelicula']['id'] == movie['id']:
            for prod in range(0, len(session['carrito'][i]['productos'])):
                if session['carrito'][i]['productos'][prod]['edicion'] == edition:
                    return prod
    return None


def get_session_products(productos : list):
    for i in range(0, len(session['carrito'])):
        for j in range(0, len(session['carrito'][i]['productos'])):
            productos.append(session['carrito'][i]['productos'][j])


def get_movie_index_by_product_id(prod_id):
    for i in range(0, len(session['carrito'])):
        for prod in session['carrito'][i]['productos']:
            if prod['id'] == prod_id:
                return i
    return None


def get_prod_index_by_movie_and_prod_id(prod_id, movie_index):
    for prod in range(0, len(session['carrito'][movie_index]['productos'])):
        if session['carrito'][movie_index]['productos'][prod]['id'] == prod_id:
            return prod


def set_session_cart():
    if 'usuario' in session:
        customerid = database.db_user_customer_ID(
            session['usuario'],
            session['password'],
            session['email']
        )
        cart = database.db_user_get_cart(int(customerid))
        if 'carrito' in session:
            products = []
            get_session_products(products)
            database.db_orderdetail_cart_session(products)
            session['carrito'] = database.db_user_get_cart(int(customerid))
        else:
            # La variable del carrito solo se inicializa si tiene articulos,
            # si no simplemente se crea un registro NULL al usuario para empezar
            # a llenar su carrito
            if len(cart) > 0:
                session['carrito'] = cart
        database.db_update_order_cart_data()


@app.route('/', methods=['GET', 'POST'])
@app.route('/home', methods=['GET', 'POST'])
def home():
    if 'total_cantidad' not in session and 'total_precio' not in session:
        session['total_cantidad'] = 0
        session['total_precio'] = 0
    if request.method == 'POST':
        # Se obtienen los campos del formulario recibido
        # mediante el metodo POST
        search_field = request.form['search']
        category = request.form['categories']

        # Si no se indican el numero de actores por
        # defecto se mostrarán 10
        if request.form['num_actors'] == '':
            num_actores = 0
        else:
            num_actores = int(request.form['num_actors'])

        top_actors = database.db_top_actors(str(category), num_actores)

        # Se devuelve ese mismo formulario con las
        # peliculas que cumplen las condiciones del formulario
        return render_template(
            'home.html',
            s=search_field,
            movies = database.db_movies_by_category_and_search(
                category,
                search_field
            ),
            categories=database.db_load_categories(),
            category=category,
            selected_category=category,
            top_actors = top_actors
        )
    else:
        # En caso contrario estamos ante un
        # cuestionario nuevo mediante el metodo GET
        # Si no se ha filtrado categoría en especifico,
        # se filtrará por defecto por el género 'Action'
        # y se mostrarán 10 actores
        return render_template('home.html',
                               movies=database.db_load_movies(),
                               categories=database.db_load_categories(),
                               top_actors = database.db_top_actors('', 0),
                               category='Action',
                               selected_category='Action'
                               )


@app.route('/create_user', methods=['GET', 'POST'])
def crear_cuenta():
    mensaje = ""
    if request.method == 'POST':
        user = request.form['usuario']
        password = request.form['contraseña1']
        db_user_registred = database.db_user_registred(
            user, password)

        if len(db_user_registred) == 0: # Si es igual a 0 es porque no hay ningun user registrado
                                        # con esas credenciales
            balance = randrange(1, 100)
            db_crear_cuenta = database.db_crear_cuenta(request.form['firstname'], request.form['lastname'],
                                           user, password, request.form['correo'],
                                           request.form['credittype'], request.form['tarjeta'],
                                           request.form['creditdate'], request.form['country'],
                                           request.form['region'], request.form['city'],
                                           request.form['direccion'], balance)

            if db_crear_cuenta != None:
                mensaje = "Error al actualizar la BBDD"
                return render_template('registro.html', mensaje=mensaje)
            # Se guarda en la sesion el usuario que acaba de iniciarse
            session['usuario'] = user
            session['password'] = password
            session['email'] = request.form['correo']
            session.modified = True
            set_session_cart()
            session.modified = True
            return redirect(url_for('home'))

        else:
            mensaje = "Credenciales de usuario y contraseña ya regsitradas"
            return render_template('registro.html', mensaje=mensaje)

    return render_template('registro.html')


@app.route('/change_pasword', methods=['GET', 'POST'])
def cambiar_contraseña():
    mensaje = ""
    if request.method == 'POST':
        user = request.form['usuarioChange']
        email = request.form['correo']
        db_user_change_password = database.db_user_change_password(
            user, email)

        if len(db_user_change_password) != 0:
            # Cambiamos las credenciales
            password = request.form['contraseña']
            db_user_update_password = database.db_user_update_password(user, password, email)
            if db_user_update_password != None:
                mensaje = "Error al actualizar la BBDD"
                return render_template('cambiar_contraseña.html',
                                   mensaje=mensaje)

            session['usuario'] = user
            session['password'] = password
            session['email'] = email
            session.modified = True
            set_session_cart()
            session.modified = True
            return redirect(url_for('home'))
        else:
            mensaje = f"El correo no esta registrado para {user}"
            return render_template('cambiar_contraseña.html',
                                   mensaje=mensaje)

    # aqui se le puede pasar como argumento un mensaje de login invalido
    return render_template('cambiar_contraseña.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    mensaje = ""
    msg_error = 'Error en la consulta relacionada con el login'
    if request.method == "POST":
        user = request.form['usuarioLogin']
        ret_db_login = database.db_login(user)

        if ret_db_login != msg_error:
            if len(ret_db_login) > 0:
                passwords = []
                for password in ret_db_login:
                    if password[0] not in passwords:
                        passwords.append(password[0])

                if request.form['contraseñaLogin'] in passwords:
                    # En caso afirmativo se guarda la session y se envia a la pagina
                    # principal
                    email = database.db_user_email(user, request.form['contraseñaLogin'])[0][0]
                    session['usuario'] = user
                    session['password'] = request.form['contraseñaLogin']
                    session['email'] = email
                    session.modified = True
                    set_session_cart()
                    session.modified = True
                    return redirect(url_for('home'))
                else:
                    # aqui se le puede pasar como argumento un mensaje de login
                    mensaje = "La contraseña no es correcta"
                    return render_template('login.html', mensaje=mensaje)
            else:
                mensaje = "Nombre de usuario no registrado"
                return render_template('login.html', mensaje=mensaje)
        else:
                mensaje = "Error relacionado con la BBDD"
                return render_template('login.html', mensaje=mensaje)

    return render_template('login.html')


@app.route('/logout', methods=['GET', 'POST'])
def logout():
    if 'usuario' in session:
        session.pop('usuario', None)
        session.pop('orderid', None)
        session.pop('password', None)
        session.pop('email', None)
        session.pop('carrito', None)
        session['total_precio'] = 0
        session['total_cantidad'] = 0
    return redirect(url_for('home'))


@app.route('/pelicula/<int:pelicula_id>', methods=['GET', 'POST'])
def pelicula(pelicula_id):
    # Se obtiene la pelicula mediante ese id
    movie = database.db_load_movie_by_id(pelicula_id)

    if request.method == "POST":
        #Si el usuario no esta en la sesion
        #Saber que edicion ha elegido y coger el precio de dicha edicion
        edition = request.form.get("submit_edition")
        product = database.db_product_by_movieid_and_edition(
                        movie['id'],
                        edition
                    )

        #Se coloca el producto en la sesion de manera que la queremos utilizar
        info_session = {
            'pelicula': {
                'id' : movie['id'],
                'titulo' : movie['titulo'],
                'poster' : 'img/base_movie.jpg'
                },
            'precio_total' : product['price'],
            'productos': []
        }
        info_session['productos'].append({
            'id' : product['id'],
            'edicion' : edition,
            'precio' : product['price'],
            'cantidad' : 1
        })

        # Si ya hay carrito y no esta la pelicula, la metemos como nueva
        if 'carrito' in session:
            if not is_movie_in_session_cart(movie):
                session['carrito'].append(info_session)
                # Si hay usuario en la sesion tambien cambiamos la db metiendo
                # un nuevo registro en ella
                if 'usuario' in session:
                    database.db_insert_products_into_cart(
                        info_session['productos']
                        )
                    database.db_update_order_cart_data()
                else:
                    session['total_precio'] += product['price']
                    session['total_cantidad'] += 1
            #Si no hay que ver que pelicula es para añadir la edicion o para
            # introducirla como nueva edicion dentro de una pelicula
            else:
                index_movie = get_index_of_session_cart_element(movie)
                index_edition = get_index_of_session_cart_element_by_edition(
                    movie,
                    edition
                )
                # Si la edicion no se encuentra en el carrito se añade
                if index_movie is not None and index_edition is None:
                    info_session = {
                        'id' : product['id'],
                        'edicion' : edition,
                        'precio' : product['price'],
                        'cantidad' : 1
                    }
                    session['carrito'][index_movie]['productos'].append(info_session)
                    # Si hay usuario en la sesion y la edicion no esta le pasamos esa
                    # edicion de la pelicula para que inserte ese producto en la db
                    if 'usuario' in session:
                        database.db_insert_products_into_cart(
                            [info_session]
                            )
                        database.db_update_order_cart_data()
                    else:
                        session['total_precio'] += product['price']
                        session['total_cantidad'] += 1
                # Si el producto si que esta se aumenta la cantidad y precio
                elif index_movie is not None and index_edition is not None:
                    session['carrito'][index_movie]['productos'][index_edition]['cantidad'] += 1
                    session['carrito'][index_movie]['productos'][index_edition]['precio'] += product['price']
                    session['total_precio'] += product['price']
                    session['total_cantidad'] += 1
                    # Si hay usuario en la sesion y la edicion si esta le pasamos esa
                    # edicion de la pelicula para que la aumente en 1 unidad + precio
                    if 'usuario' in session:
                        database.db_update_cart_products(
                            [{
                                'id' : session['carrito'][index_movie]['productos'][index_edition]['id'],
                                'edicion' : edition,
                                'precio' : product['price'],
                                'cantidad' : 1,
                            }]
                            )
                        database.db_update_order_cart_data()
                    else:
                        session['total_precio'] += product['price']
                        session['total_cantidad'] += 1
                #Para ambos casos se suma la cantidad total
                session['carrito'][index_movie]['precio_total'] += product['price']


        else:
            session['carrito'] = [info_session]
            if 'usuario' in session:
                database.db_insert_products_into_cart(
                    info_session['productos']
                    )
                database.db_update_order_cart_data()
            else:
                session['total_precio'] += product['price']
                session['total_cantidad'] += 1
        # Por ultimo se guardan los cambios y se redirecciona al carrito
        session.modified = True
        return redirect(url_for('carrito'))

    return render_template('pelicula.html', movie=movie)


@app.route('/carrito', methods=['GET', 'POST'])
def carrito():
    if request.method == 'POST':
        if 'usuario' in session and 'carrito' in session:
            return redirect(url_for('confirmar_compra'))
        else:
            return redirect(url_for('login'))
    return render_template('carrito.html')


@app.route('/carrito/confirmar_compra', methods=['GET', 'POST'])
def confirmar_compra():

    if request.method == "POST":
        database.db_update_status_to_paid()
        st = database.db_check_status()
        if st == "Paid":
            pagado = "Enhorabuena!! La compra se ha realizado correctamente   "
            session['total_precio'] = 0
            session['total_cantidad'] = 0
            session.pop('carrito', None)
            session.modified = True
            set_session_cart()
            return render_template('confirmar_compra.html', pagado=pagado)
        else:
            error = "Error al realizar la compra, puede que no haya stock de los productos"
            return render_template('confirmar_compra.html', error=error)
    else:
        customerid = database.db_user_customer_ID(
            session['usuario'],
            session['password'],
            session['email']
        )
        summary = database.db_get_buy_summary(customerid)
        return render_template('confirmar_compra.html', summary=summary)

@app.route('/carrito/aumentar_item/<int:product_id>', methods=['GET', 'POST'])
def aumentar_item_carrito(product_id):

    if 'carrito' in session:
        movie_idx = get_movie_index_by_product_id(product_id)
        if movie_idx is not None:
            prod_idx = get_prod_index_by_movie_and_prod_id(product_id, movie_idx)
        # Si el producto esta
        if prod_idx is not None:
        # Si el usuario esta en sesion se acrualiza la base de datos
            if 'usuario' in session:
                prod_item = {
                    'id' : session['carrito'][movie_idx]['productos'][prod_idx]['id'],
                    'edicion' : session['carrito'][movie_idx]['productos'][prod_idx]['edicion'],
                    'cantidad' : 1,
                    'precio' : session['carrito'][movie_idx]['productos'][prod_idx]['precio']
                }
                database.db_update_cart_products([prod_item])
                database.db_update_order_cart_data()
            else:
                session['total_precio'] += session['carrito'][movie_idx]['productos'][prod_idx]['precio']
                session['total_cantidad'] += 1
            # A parte se incrementa en la variable de sesion la cantidad en 1
            # y el precio total para esa pelicula
            session['carrito'][movie_idx]['productos'][prod_idx]['cantidad'] += 1
            session['carrito'][movie_idx]['precio_total'] += session['carrito'][movie_idx]['productos'][prod_idx]['precio']
            session.modified = True
    return redirect(url_for('carrito'))


@app.route('/carrito/reducir_item/<int:product_id>', methods=['GET', 'POST'])
def reducir_item_carrito(product_id):

    if 'carrito' in session:
        movie_idx = get_movie_index_by_product_id(product_id)
        if movie_idx is not None:
            prod_idx = get_prod_index_by_movie_and_prod_id(product_id, movie_idx)
        # Si el producto esta
        if prod_idx is not None:
        # Si el usuario esta en sesion se acrualiza la base de datos
            prod_item = {
                'id' : session['carrito'][movie_idx]['productos'][prod_idx]['id'],
                'edicion' : session['carrito'][movie_idx]['productos'][prod_idx]['edicion'],
                'cantidad' : -1,
                'precio' : session['carrito'][movie_idx]['productos'][prod_idx]['precio']
            }
            if 'usuario' in session:
                # Si la cantidad de ese articulo es 1, se elimina, si no se actualiza
                if session['carrito'][movie_idx]['productos'][prod_idx]['cantidad'] > 1:
                    database.db_update_cart_products([prod_item])
                else:
                    database.db_delete_cart_products([prod_item])
                database.db_update_order_cart_data()
            else:
                session['total_precio'] -= session['carrito'][movie_idx]['productos'][prod_idx]['precio']
                session['total_cantidad'] -= 1
            # A parte, se reducen los productos en 1 unidad y el precio
            # Si queda un solo producto se elimina de la lista y si quedan mas
            # tan solo se disminuyen
            session['carrito'][movie_idx]['productos'][prod_idx]['cantidad'] -= 1
            session['carrito'][movie_idx]['precio_total'] -= prod_item['precio']
            if session['carrito'][movie_idx]['productos'][prod_idx]['cantidad'] == 0:
                # Si se va a eliminar, hay que ver que queden mas productos
                # Si no quedan se elimina la pelicula porque esta no tiene productos
                session['carrito'][movie_idx]['productos'].remove(
                    session['carrito'][movie_idx]['productos'][prod_idx]
                )
                if len(session['carrito'][movie_idx]['productos']) == 0:
                    session['carrito'].remove(
                    session['carrito'][movie_idx]
                )

        session.modified = True
    return redirect(url_for('carrito'))


@app.route('/carrito/limpiar_carrito')
def limpiar_carrito():
    # Se hace un pop del elemento de carrito de la sesion para limpiar el
    # carrito
    if 'carrito' in session:
        if 'usuario' in session:
            prods = []
            get_session_products(prods)
            database.db_delete_cart_products(prods)
            database.db_update_order_cart_data()
        session['total_precio'] = 0
        session['total_cantidad'] = 0
        session.pop('carrito', None)
        session.modified = True
    return redirect(url_for('carrito'))


@app.route('/historial')
def historial():
    precios = {}
    peli = {}

    try:

        # Accedemos al historial del usuario loggeado
        user = session['usuario']
        password = session['password']
        email = session['email']

        customer_id = database.db_user_customer_ID(
            user, password, email
        )

        # Consultamos la información del historial de compras
        # asociado al customer que tiene la sesion iniciada
        dict_balance_loyalty = database.db_user_balance_loyalty(customer_id)
        datos_historial = database.db_user_historial(customer_id)

        return render_template("historial.html", puntos=dict_balance_loyalty['loyalty'],
                               saldo=dict_balance_loyalty['balance'],
                               info_general=datos_historial[0],
                               info_individual=datos_historial[1])
    except FileNotFoundError:
        return render_template("historial.html")

# Revisar cuando este hecho la parte del historial
@app.route('/historial/saldo', methods=['GET', 'POST'])
def añadir_saldo():

    if request.method == "POST":

        saldo_introducido = int(request.form['saldo'])

        user = session['usuario']
        password = session['password']
        email = session['email']
        db_user_update_balance = database.db_user_update_balance(user, password, email, saldo_introducido)

    return redirect(url_for('historial'))


@app.route('/num_random', methods=['POST'])
def num_random():
    if request.method == "POST":
        valor = randint(1, 100)
        response = {"num": valor}
        return response
