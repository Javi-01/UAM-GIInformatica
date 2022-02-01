#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from app import app
from flask import render_template, request, url_for, redirect, session, json
from random import randint, randrange
from datetime import datetime
from hashlib import blake2b
import os

data = open(
    os.path.join(
        app.root_path, 'peliculas/peliculas.json'
    ), encoding="utf-8"
).read()

movies = json.loads(data)


def get_movie_by_id(pelicula_id):
    for movie in movies['peliculas']:
        if movie['id'] == pelicula_id:
            return movie


def is_movie_in_session_cart(movie):
    for m in session['carrito']:
        if movie == m['pelicula']:
            return True
    return False


def get_index_of_session_cart_element(movie):
    for i in range(0, len(session['carrito'])):
        if session['carrito'][i]['pelicula'] == movie:
            return i
    return None


@app.route('/', methods=['GET', 'POST'])
@app.route('/home', methods=['GET', 'POST'])
def home():

    if request.method == 'POST':
        # Se obtienen los campos del formulario recibido
        # mediante el metodo POST
        search_field = request.form['search']
        category = request.form['categories']
        # Se devuelve ese mismo formulario con las
        # peliculas que cumplen las condiciones del formulario
        return render_template('home.html', s=search_field, movies=[
            movie for movie in movies['peliculas']
            if (
                movie['categoria'] == category
                and
                search_field in movie['titulo']
                )],
            categories=set(movie['categoria']
                           for movie in movies['peliculas']),
            selected_category=category
        )
    else:
        # En caso contrario estamos ante un
        # cuestionario nuevo mediante el metodo GET
        return render_template('home.html', movies=movies['peliculas'],
                               categories=set(movie['categoria']
                                              for movie in movies['peliculas'])
                               )


@app.route('/create_user', methods=['GET', 'POST'])
def crear_cuenta():
    mensaje = ""
    if request.method == 'POST':
        try:
            # Creando el directorio para ese usuario
            user = request.form['usuario']
            os.mkdir(f"usuarios/{user}")
            # Creacion del fichero data.dat
            data_dat = open(f'usuarios/{user}/data.dat', 'w')
            # Ciframos la contraseña
            hash = blake2b(
                (request.form['contraseña1']).encode('utf-8')).hexdigest()
            user_lines = [
                user + '\n',
                hash + '\n',
                request.form['correo'] + '\n',
                request.form['tarjeta'] + '\n',
                str(randrange(1, 100)) + '\n',
                str(0) + '\n',
            ]
            data_dat.writelines(user_lines)
            data_dat.close()

            # Creacion del fichero historial.json
            historial = {}
            historial['compras'] = []

            historial_json = open(f'usuarios/{user}/historial.json', 'w')
            json.dump(historial, historial_json, indent=4)
            historial_json.close()

            # Se guarda en la sesion el usuario que acaba de iniciarse
            session['usuario'] = user
            session.modified = True
        except OSError:
            mensaje = "El usuario ya ha sido registrado"
            return render_template('registro.html', mensaje=mensaje)

        return redirect(url_for('home'))

    return render_template('registro.html')


@app.route('/change_pasword', methods=['GET', 'POST'])
def cambiar_contraseña():
    mensaje = ""
    if request.method == "POST":
        # Comprobar que el usuario introducido se encuentra registrado
        try:
            user = request.form['usuarioChange']
            data_dat = open(f'usuarios/{user}/data.dat')
            data = data_dat.read()
            # Se guardan los elementos que se introdujeron mediante espacios en
            # una lista
            user_data = data.split('\n')
            data_dat.close()
        except FileNotFoundError:
            mensaje = "El nombre de usuario no existe"
            return render_template('cambiar_contraseña.html',
                                   mensaje=mensaje)
        # Se comprueba que el correo corresponde a dicho usuario
        if request.form['correo'] == user_data[2]:
            try:
                # Se guardan los elementos que se introdujeron mediante
                # espacios en una lista
                # Se actualizan los campos de dicho usuario
                data_dat = open(f'usuarios/{user}/data.dat', 'w')
                # Creamos un nuevo hash para la contraseña nueva y
                # actualizamos los datos del usuario
                hash = blake2b(
                    (request.form['contraseña'])
                    .encode('utf-8')).hexdigest()
                data_dat.write(user + "\n" +
                               hash + "\n" +
                               request.form['correo'] + "\n" +
                               user_data[3] + "\n" +
                               user_data[4] + "\n" +
                               user_data[5]
                               )
                data_dat.close()
                session['usuario'] = user
                session.modified = True
                return redirect(url_for('home'))
            except OSError:
                return render_template('cambiar_contraseña.html')
        else:
            mensaje = f"El correo no esta registrado para {user}"
            return render_template('cambiar_contraseña.html',
                                   mensaje=mensaje)
    # aqui se le puede pasar como argumento un mensaje de login invalido
    return render_template('cambiar_contraseña.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    mensaje = ""
    if request.method == "POST":
        # Rescatamos el usuario del formulario y buscamos el fichero data.dat
        # del directorio con su nombre
        try:
            user = request.form['usuarioLogin']
            data_dat = open(f'usuarios/{user}/data.dat')
            data = data_dat.readlines()
            password = data[1].replace('\n', '')
            # Se guardan los elementos que se introdujeron mediante espacios en
            # una lista
            data_dat.close()
        except FileNotFoundError:
            mensaje = "El nombre de usuario no existe"
            return render_template('login.html', mensaje=mensaje)

        # Se comprueba que la contraseña sea correcta comprobando el cifrado
        # simple
        hash_introduced_password = blake2b(
            (request.form['contraseñaLogin']).encode('utf-8')).hexdigest()
        if hash_introduced_password == password:
            # En caso afirmativo se guarda la session y se envia a la pagina
            # principal
            session['usuario'] = user
            session.modified = True
            return redirect(url_for('home'))
        else:
            # aqui se le puede pasar como argumento un mensaje de login
            mensaje = "La contraseña no es correcta"
            return render_template('login.html', mensaje=mensaje)
    return render_template('login.html')


@app.route('/logout', methods=['GET', 'POST'])
def logout():
    if 'usuario' in session:
        session.pop('usuario', None)
        session.pop('carrito', None)
    return redirect(url_for('home'))


@app.route('/pelicula/<int:pelicula_id>', methods=['GET', 'POST'])
def pelicula(pelicula_id):
    # Se obtiene la pelicula mediante ese id
    movie = get_movie_by_id(pelicula_id)
    if request.method == "POST":
        info_session = {
            'pelicula': movie,
            'cantidad': 1
        }

        if 'carrito' in session:
            # Si no tenemos esa pelicula en la sesion, introducimos el id de la
            # pelicula y la cantidad inicial de copias a 1
            if not is_movie_in_session_cart(movie):
                session['carrito'].append(info_session)
            else:
                # Si tenemos la pelicula, se actualiza la cantidad del elemento
                # incrementando en 1
                index = get_index_of_session_cart_element(movie)
                if index is not None:
                    session['carrito'][index]['cantidad'] += 1
            # Si no tenemos carrito, creamos el carrito introduciendo la
            # pelicula
        else:
            session['carrito'] = [info_session]

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
    precio = 0
    num_articulos = 0

    user = session['usuario']
    try:
        with open(f'usuarios/{user}/data.dat') as data_dat:
            data = data_dat.readlines()
            data_dat.close()

        saldo = data[4].replace('\n', '')
        puntos = data[5].replace('\n', '')

    except FileNotFoundError:
        return redirect(url_for('carrito'))

    if 'carrito' in session and 'usuario' in session:
        for m in session['carrito']:
            precio += m['pelicula']['precio'] * m['cantidad']
            num_articulos += m['cantidad']

    if request.method == 'POST':
        usar_puntos = request.form.get('usar_puntos')
        puntos = float(puntos)
        saldo = float(saldo)

        if usar_puntos is not None:
            descuento = puntos * (1/100)
            if descuento >= precio:
                precio = 0
            else:
                precio = precio - descuento
            if saldo >= precio:
                saldo -= precio
                puntos += precio * (5/100)

        else:
            if saldo >= precio:
                saldo -= precio
                puntos += precio * (5/100)

        try:
            saldo = round(saldo, 2)
            puntos = round(puntos, 2)
            precio = round(precio, 2)
            data[4] = str(saldo) + '\n'
            data[5] = str(puntos) + '\n'
            with open(f'usuarios/{user}/data.dat', 'w') as data_dat:
                data_dat.writelines(data)
                data_dat.close()

            with open(f'usuarios/{user}/historial.json') as historial_json:
                historial = json.load(historial_json)
                historial_json.close()
            compra = {
                "id_compra": len(historial['compras']) + 1,
                "fecha_compra": datetime.today().strftime('%Y-%m-%d'),
                "cantidad_articulos": str(num_articulos),
                "precio_total": str(precio),
                "carrito": session['carrito'],
            }
            historial['compras'].append(compra)
            with open(
                f'usuarios/{user}/historial.json', 'w'
                    ) as historial_json:
                json.dump(historial, historial_json, indent=4)
                historial_json.close()
        except FileNotFoundError:
            return redirect(url_for('carrito'))
        session.pop('carrito', None)
        session.modified = True
        return redirect(url_for('historial'))

    return render_template('confirmar_compra.html', precio=precio, saldo=saldo,
                           puntos=puntos)


@app.route('/carrito/borrar_item/<int:pelicula_id>', methods=['GET', 'POST'])
def borrar_item_carrito(pelicula_id):

    if 'carrito' in session:
        movie = get_movie_by_id(pelicula_id)
        # Si existe ese elemento en el carrito
        index = get_index_of_session_cart_element(movie)
        if index is not None:
            # Se elimina por completo todas las copias de esa pelicula de
            # carrito
            session['carrito'].remove(session['carrito'][index])
            session.modified = True
    return redirect(url_for('carrito'))


@app.route('/carrito/aumentar_item/<int:pelicula_id>', methods=['GET', 'POST'])
def aumentar_item_carrito(pelicula_id):

    if 'carrito' in session:
        movie = get_movie_by_id(pelicula_id)
        # Si existe el elemento en el carrito
        index = get_index_of_session_cart_element(movie)
        if index is not None:
            session['carrito'][index]['cantidad'] += 1
            session.modified = True
    return redirect(url_for('carrito'))


@app.route('/carrito/reducir_item/<int:pelicula_id>', methods=['GET', 'POST'])
def reducir_item_carrito(pelicula_id):

    if 'carrito' in session:
        movie = get_movie_by_id(pelicula_id)
        # Si existe el elemento en el carrito
        index = get_index_of_session_cart_element(movie)
        if index is not None:
            # Mientras la cantidad de ese articulo se mayor que 1 se disminuye
            # su cantidad
            if session['carrito'][index]['cantidad'] > 1:
                session['carrito'][index]['cantidad'] -= 1
            # Si la cantidad es 1, se elimina el elemento ya que seria reducir
            # a 0
            elif session['carrito'][index]['cantidad'] == 1:
                session['carrito'].remove(session['carrito'][index])

            session.modified = True
    return redirect(url_for('carrito'))


@app.route('/carrito/limpiar_carrito')
def limpiar_carrito():
    # Se hace un pop del elemento de carrito de la sesion para limpiar el
    # carrito
    if 'carrito' in session:
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

        with open(f'usuarios/{user}/historial.json') as historial_json:
            historial = json.load(historial_json)
            historial_json.close()

        with open(f'usuarios/{user}/data.dat') as data_dat:
            data = data_dat.readlines()
            data_dat.close()

        # Generamos un diccionario para por cada compra incluir el precio
        # individual gastado por cada pelicula
        for compra in historial['compras']:
            for pelicula in compra['carrito']:
                valor = pelicula['pelicula']['precio'] * pelicula['cantidad']
                valor = round(valor, 2)
                peli[pelicula['pelicula']['id']] = valor
            precios[compra['id_compra']] = peli

        return render_template("historial.html", puntos=data[5], saldo=data[4],
                               compras=historial['compras'],
                               historial_precio=precios)
    except FileNotFoundError:
        return render_template("historial.html")


@app.route('/historial/saldo', methods=['GET', 'POST'])
def añadir_saldo():

    if request.method == "POST":

        try:
            # Recogemos el saldo que el usuario quiere añadir
            saldo_introducido = request.form['saldo']
            if int(saldo_introducido) <= 0:
                return redirect(url_for('historial'))

            user = session['usuario']

            with open(f'usuarios/{user}/data.dat') as data_dat:
                data = data_dat.readlines()
                data_dat.close()

            nuevo_saldo = float(saldo_introducido) + \
                float(data[4].replace('\n', ''))
            nuevo_saldo = round(nuevo_saldo, 2)
            data[4] = str(nuevo_saldo) + '\n'

            # actualizamos los datos

            with open(f'usuarios/{user}/data.dat', 'w') as data_dat:
                data_dat.writelines(data)
                data_dat.close()
        except FileNotFoundError:
            return redirect(url_for('historial'))

    return redirect(url_for('historial'))


@app.route('/num_random', methods=['POST'])
def num_random():
    if request.method == "POST":
        valor = randint(1, 100)
        response = {"num": valor}
        return response
