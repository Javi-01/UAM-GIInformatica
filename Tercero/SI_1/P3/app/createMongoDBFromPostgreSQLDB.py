import sys
import time
import traceback
import pymongo
from sqlalchemy import create_engine, MetaData

start_time = time.time()

# Iniciar la conexion a la base de datos de postgresql
db_engine = create_engine("postgresql://alumnodb:alumnodb@localhost/si1", echo=False)
db_meta = MetaData(bind=db_engine)

#Funciones para obtener datos de postgresql
def db_exception(db_conn):
    if db_conn is not None:
        db_conn.close()
        traceback.print_exc(file=sys.stderr)


def _get_movies():
    try:
        db_conn = None
        db_conn = db_engine.connect()
        #Primero se limpia del nombre el año
        db_conn.execute(f"""Update imdb_movies m set
                                 movietitle = substring(m.movietitle from 0 for length(m.movietitle) - length(m.year) - 2)""")

        # A continuacion se hace la consulta de las peliculas mas actuales en ingles (400)
        db_movies = db_conn.execute("""Select distinct im.movieid, im.movietitle, max(im."year") from imdb_movies im join imdb_moviecountries imc
                                    on im.movieid = imc.movieid
                                    where imc.country = 'UK'
                                    group by (im.movieid, im.movietitle)
                                    order by max(im."year") desc limit 400""")
        db_conn.close()

        movies = [(movie[0], movie[1], movie[2]) for movie in db_movies]
        return movies

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de las peliculas'


def _get_movie_genres(movieid : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se otienen los generos de cada pelicula
        db_genres = db_conn.execute(f"""Select distinct img.genre from imdb_movies im join imdb_moviegenres img
                                    on im.movieid = img.movieid
                                    where im.movieid = {movieid}""")

        db_conn.close()
        genres = [genre[0] for genre in db_genres]

        return genres

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de los generos de las peliculas'


def _get_movie_directors(movieid : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se otienen los directores de cada pelicula
        db_directors = db_conn.execute(f"""Select distinct id.directorname from imdb_movies im join imdb_directormovies imd
                                    on imd.movieid = im.movieid join imdb_directors id
                                    on id.directorid = imd.directorid
                                    where im.movieid = {movieid}""")

        db_conn.close()
        directors = [director[0] for director in db_directors]

        return directors

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de los directores de las peliculas'


def _get_movie_actors(movieid : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se otienen los actores de cada pelicula
        db_actors = db_conn.execute(f"""Select distinct ia.actorname from imdb_movies im join imdb_actormovies ima
                                    on ima.movieid = im.movieid join imdb_actors ia
                                    on ia.actorid = ima.actorid
                                    where im.movieid = {movieid}""")

        db_conn.close()
        actors = [actor[0] for actor in db_actors]

        return actors

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de los actores de las peliculas'


def _get_most_related_movies(movieid : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        # Se realiza mediante una funcion las peliculas con el 100% de los generos iguales
        db_conn.execute(f"""create or replace function getMostRelatedMovies(IN id INT)
                            returns table (movieid int, movietitle varchar, anno text) as $$
                        declare
                            film record;
                            films cursor for select distinct im.movieid, im.movietitle, max(im."year") from imdb_movies im join imdb_moviecountries imc
                                    on im.movieid = imc.movieid
                                    where imc.country = 'UK'
                                    group by (im.movieid, im.movietitle)
                                    order by max(im."year") desc limit 400;
                        begin
                            open films;
                            loop
                                fetch films into film;
                                exit when not found;
                                if ((select distinct count(*) from imdb_movies im1 join imdb_moviegenres img1
                                                                on im1.movieid = img1.movieid
                                                                    where im1.movieid = id) =
                                    (Select distinct count(*) from imdb_movies im2 join imdb_moviegenres img2
                                                                on im2.movieid = img2.movieid
                                                                    where im2.movieid = film.movieid)) then
                                    if ((select count(C) from ((Select distinct img1.genre from imdb_movies im1 join imdb_moviegenres img1
                                                                on im1.movieid = img1.movieid
                                                                    where im1.movieid = id) except
                                    (Select distinct img2.genre from imdb_movies im2 join imdb_moviegenres img2
                                                                    on im2.movieid = img2.movieid
                                                                    where im2.movieid = film.movieid)) as C) = 0) then
                                            return query select im.movieid, im.movietitle, im."year" from imdb_movies im
                                                        where film.movieid = im.movieid and im.movieid <> id;
                                    end if;
                                end if;
                            end loop;
                            close films;
                        end;
                        $$
                        language 'plpgsql';""")

        db_most_related_movies = db_conn.execute(f"select * from getMostRelatedMovies({movieid}) limit 10")
        db_conn.close()
        related_movies = [{"title": movie[1], "year": movie[2]} for movie in db_most_related_movies]

        return related_movies

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de las peliculas mas relacionadas de las peliculas'


def _get_related_movies(movieid : int, num_genres : int):
    try:
        db_conn = None
        db_conn = db_engine.connect()
        related_movies = []
        #Primero se comprueba que el numero de generos sea mayor que 1
        if num_genres > 1:
            # Se realiza mediante una funcion las peliculas con el 50% de los generos iguales
            db_conn.execute(f"""create or replace function getRelatedMovies(IN id INT)
                                returns table (movieid int, movietitle varchar, anno text) as $$
                            declare
                                film record;
                                films cursor for select distinct im.movieid, im.movietitle, max(im."year") from imdb_movies im join imdb_moviecountries imc
                                        on im.movieid = imc.movieid
                                        where imc.country = 'UK'
                                        group by (im.movieid, im.movietitle)
                                        order by max(im."year") desc limit 400;
                            begin
                                open films;
                                loop
                                    fetch films into film;
                                    exit when not found;
                                    if ((select count(C) from ((Select distinct img1.genre from imdb_movies im1 join imdb_moviegenres img1
                                                                on im1.movieid = img1.movieid
                                                                    where im1.movieid = id) except
                                    (Select distinct img2.genre from imdb_movies im2 join imdb_moviegenres img2
                                                                    on im2.movieid = img2.movieid
                                                                    where im2.movieid = film.movieid)) as C) =
                                    (select distinct count(*)/2 from imdb_movies im1 join imdb_moviegenres img1
                                                                    on im1.movieid = img1.movieid
                                                                    where im1.movieid = id)) then
                                            return query select im.movieid, im.movietitle, im."year" from imdb_movies im
                                                        where film.movieid = im.movieid and im.movieid <> id;
                                    end if;
                                end loop;
                                close films;
                            end;
                            $$
                            language 'plpgsql';""")

            db_related_movies = db_conn.execute(f"select * from getRelatedMovies({movieid}) limit 10")
            db_conn.close()
            related_movies = [{"title": movie[1], "year": movie[2]} for movie in db_related_movies]

        return related_movies

    except:
        db_exception(db_conn)
        return 'Error en la obtencion de las peliculas mas relacionadas de las peliculas'


def _create_mongodb_collection():
    psql_movies = _get_movies()

    mongo_db_collection = []
    # Se recogen los campos de la base de datos postgre
    for movieid, movietitle, movieyear in psql_movies:
        mongo_db_document = {}
        mongo_db_document['title'] = movietitle
        mongo_db_document['genres'] = _get_movie_genres(movieid)
        mongo_db_document['year'] = int(movieyear)
        mongo_db_document['directors'] = _get_movie_directors(movieid)
        mongo_db_document['actors'] = _get_movie_actors(movieid)
        mongo_db_document['most_related_movies'] = _get_most_related_movies(movieid)
        mongo_db_document['related_movies'] = _get_related_movies(
            movieid,
            len(mongo_db_document['genres'])
            )

        mongo_db_collection.append(mongo_db_document)

    return mongo_db_collection


#Iniciar la conexion a la base de datos mongodb
mongo_client = pymongo.MongoClient("mongodb://localhost:27017/")
mongo_db = mongo_client["si1"]
mongo_db_collection = mongo_db['topUK']
mongo_db_collection.insert_many(_create_mongodb_collection())

print("Creacion de la BBDD de mongo terminada")
