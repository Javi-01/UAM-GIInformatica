# -*- coding: utf-8 -*-

import time

from sqlalchemy import create_engine
from pymongo import MongoClient

# configurar el motor de sqlalchemy
db_engine = create_engine("postgresql://alumnodb:alumnodb@localhost/si1", echo=False, execution_options={"autocommit":False})

# Crea la conexión con MongoDB
mongo_client = MongoClient("mongodb://localhost:27017/")

def getMongoCollection(mongoDB_client):
    mongo_db = mongoDB_client.si1
    return mongo_db.topUK


def get_SciFi_movies_between_1994_and_1998():
    db = getMongoCollection(mongo_client)

    db_movies = db.find(
        {
            "genres" : "Sci-Fi",
            "year" : {
                "$gte": 1994,
                "$lte":1998
             }
         }
        )
    return db_movies


def get_Drama_movies_on_1998():
    db = getMongoCollection(mongo_client)
    db_movies = db.find(
        {
            "genres" : "Drama",
            "year" : 1998,
            "title": {
                "$regex" : 'The$'
                }
            }
        )

    print(db_movies)
    return db_movies


def get_movies_by_Julia_and_Alec():
    db = getMongoCollection(mongo_client)
    db_movies = db.find({"$and": [
        {
            "actors": "Roberts, Julia"
        },
        {
            "actors": "Baldwin, Alec"
        }
        ]
    })

    return db_movies

def mongoDBCloseConnect(mongoDB_client):
    mongoDB_client.close()


def dbConnect():
    return db_engine.connect()


def dbCloseConnect(db_conn):
    db_conn.close()


def delCity(city, bFallo, bSQL, duerme, bCommit):

    # Array de trazas a mostrar en la página
    dbr=[]
    db_conn = dbConnect()

    """CONSULTAS DE BORRADO A REALIZAR"""

    orderdetail = f"""Delete from orderdetail od where od.orderid in (
                Select o.orderid from orders o join customers c on c.customerid = o.customerid
                where c.city = '{city}');"""
    orders = f"""Delete from orders o where o.customerid in (select c.customerid
            from customers c where c.city = '{city}')"""
    customers = f"""Delete from customers c where c.city = '{city}';"""


    """CONSULTAS DE COMPROBACIÓN DE BORRADO"""

    comprueba_odtails = f"""select count(*) from customers c join orders o on o.customerid = c.customerid
                join orderdetail od on od.orderid = o.orderid where c.city = '{city}';"""
    comprueba_orders = f"""Select count(*) from customers c join orders o on o.customerid = c.customerid
                where c.city = '{city}';"""
    comprueba_customers = f"""Select count(*) from customers c where c.city = '{city}';"""

    # TODO: Ejecutar consultas de borrado
    # - ordenar consultas según se desee provocar un error (bFallo True) o no
    # - ejecutar commit intermedio si bCommit es True
    # - usar sentencias SQL ('BEGIN', 'COMMIT', ...) si bSQL es True
    # - suspender la ejecución 'duerme' segundos en el punto adecuado para forzar deadlock
    # - ir guardando trazas mediante dbr.append()

    try:
        dbr.append("Begin;")
        db_conn.execute("Begin;")
        # TODO: ejecutar consultas
        if bFallo:
            """
            Orden Incorrecto = orderdetail, customers y orders
            """

            dbr.append(orderdetail)
            db_conn.execute(orderdetail)
            comp = list(db_conn.execute(comprueba_odtails))[0]
            dbr.append(f"-----Borrado en orderdetail => {comp[0]} registros")

            if bCommit:
                dbr.append("Commit;")
                db_conn.execute("Commit;")
                comp = list(db_conn.execute(comprueba_odtails))[0]
                dbr.append(f"-----Commit en orderdetail => {comp[0]} registros")
                comp = list(db_conn.execute(comprueba_customers))[0]
                dbr.append(f"-----Commit en customers => {comp[0]} registros")
                comp = list(db_conn.execute(comprueba_orders))[0]
                dbr.append(f"-----Commit en orders => {comp[0]} registros")
                dbr.append("Begin;")
                db_conn.execute("Begin;")

            dbr.append(customers)
            db_conn.execute(customers)
            comp = list(db_conn.execute(comprueba_customers))[0]
            dbr.append(f"-----Borrado en customers => {comp[0]} registros")

            dbr.append(orders)
            db_conn.execute(orders)
            comp = list(db_conn.execute(comprueba_orders))[0]
            dbr.append(f"-----Borrado en orders => {comp[0]} registros")

        else:
            """
            Orden Correcto = orderdetail, orders y customers
            """

            dbr.append(orderdetail)
            db_conn.execute(orderdetail)
            comp = list(db_conn.execute(comprueba_odtails))[0]
            dbr.append(f"-----Borrado en orderdetail => {comp[0]} registros")

            dbr.append(orders)
            db_conn.execute(orders)
            comp = list(db_conn.execute(comprueba_orders))[0]
            dbr.append(f"-----Borrado en orders => {comp[0]} registros")

            dbr.append(customers)
            db_conn.execute(customers)
            comp = list(db_conn.execute(comprueba_customers))[0]
            dbr.append(f"-----Borrado en customers => {comp[0]} registros")

            time.sleep(int(duerme))

        # TODO: confirmar cambios si todo va bien
        dbr.append("Commit;")
        db_conn.execute("Commit;")
        comp = list(db_conn.execute(comprueba_odtails))[0]
        dbr.append(f"-----Commit en orderdetail => {comp[0]} registros")
        comp = list(db_conn.execute(comprueba_customers))[0]
        dbr.append(f"-----Commit en customers => {comp[0]} registros")
        comp = list(db_conn.execute(comprueba_orders))[0]
        dbr.append(f"-----Commit en orders => {comp[0]} registros")

    except Exception as e:
        # TODO: deshacer en caso de error
        dbr.append("Rollback;")
        db_conn.execute("Rollback;")

        comp = list(db_conn.execute(comprueba_odtails))[0]
        dbr.append(f"-----Rollback en orderdetail => {comp[0]} registros")
        comp = list(db_conn.execute(comprueba_customers))[0]
        dbr.append(f"-----Rollback en customers => {comp[0]} registros")
        comp = list(db_conn.execute(comprueba_orders))[0]
        dbr.append(f"-----Rollback en orders => {comp[0]} registros")

    finally:
        dbCloseConnect(db_conn)
        return dbr

