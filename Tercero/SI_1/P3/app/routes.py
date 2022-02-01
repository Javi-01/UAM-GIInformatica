#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from app import app
from app import database
from flask import render_template, request

@app.route('/', methods=['POST','GET'])
@app.route('/index', methods=['POST','GET'])
def index():
    return render_template('index.html')


@app.route('/borraCiudad', methods=['POST','GET'])
def borraCiudad():
    if 'city' in request.form:
        city    = request.form["city"]
        bSQL    = request.form["txnSQL"]
        bCommit = "bCommit" in request.form
        bFallo  = "bFallo"  in request.form
        duerme  = request.form["duerme"]
        dbr = database.delCity(city, bFallo, bSQL=='1', int(duerme), bCommit)
        return render_template('borraCiudad.html', dbr=dbr)
    else:
        return render_template('borraCiudad.html')


@app.route('/topUK', methods=['POST','GET'])
def topUK():
    # TODO: consultas a MongoDB ...
    movies=[[],[],[]]
    movies[0] = database.get_SciFi_movies_between_1994_and_1998()
    movies[1] = database.get_Drama_movies_on_1998()
    movies[2] = database.get_movies_by_Julia_and_Alec()
    return render_template('topUK.html', movies=movies)