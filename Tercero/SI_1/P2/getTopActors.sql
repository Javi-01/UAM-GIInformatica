create or replace function getTopActors(genre_to_search char default 'Action', num_actores int default 10)
	returns table (Actor varchar, Num int, Debut int, Film varchar, Director varchar, Id int) as $$

declare
	Cur_actores cursor for select a.* from imdb_movies m
		join imdb_actormovies am on m.movieid = am.movieid
		join imdb_actors a on a.actorid = am.actorid
		join imdb_moviegenres im on m.movieid = im.movieid
		join genres g on im.genre_id = g.genre_id where g.genre like genre_to_search
		group by a.actorname, genre, a.actorid having count(a.actorname) > 4 order by count(a.actorname) desc limit num_actores;

begin

	for actor in Cur_actores loop

		return query select t1.nombre_actor, t1.num_actuaciones::integer, t2.year::integer, t2.title, t3.directors, t2.id_pelicula from
		(
		-- Obtiene el numero de interpretaciones de un actor por cada genero
		select a.actorname as nombre_actor, count(a.actorname) as num_actuaciones, g.genre as genre
			from imdb_movies m
			join imdb_actormovies am on m.movieid = am.movieid
			join imdb_actors a on a.actorid = am.actorid
			join imdb_moviegenres im on m.movieid = im.movieid
			join genres g on im.genre_id = g.genre_id where g.genre like genre_to_search and a.actorname = actor.actorname
			group by a.actorname, genre order by num_actuaciones desc
		) t1 inner join

			(
		-- Obtener la pelicula de debut de un actor para un cierto genero
		select a.actorname as actor, m.movietitle as title, m."year" as year, g.genre, m.movieid as id_pelicula
			from imdb_movies m
			join imdb_actormovies am on m.movieid = am.movieid
			join imdb_actors a on a.actorid = am.actorid
			join imdb_moviegenres im on m.movieid = im.movieid
			join genres g on im.genre_id = g.genre_id where g.genre like genre_to_search and a.actorname = actor.actorname
			group by a.actorname, m.movietitle, g.genre, m."year", m.movieid order by m."year" limit 1
		) t2 on t1.nombre_actor = t2.actor inner join

		(
		-- Directores de una pelicula
		select im.movietitle as title, id2.directorname as directors from imdb_movies im
			join imdb_directormovies id on im.movieid = id.movieid
			join imdb_directors id2 on id.directorid = id2.directorid where im.movietitle = (
				select m.movietitle
					from imdb_movies m
					join imdb_actormovies am on m.movieid = am.movieid
					join imdb_actors a on a.actorid = am.actorid
					join imdb_moviegenres im on m.movieid = im.movieid
					join genres g on im.genre_id = g.genre_id where g.genre like genre_to_search and a.actorname = actor.actorname
					group by a.actorname, m.movietitle, g.genre, m."year" order by m."year" limit 1)
		) t3 on t2.title = t3.title limit num_actores;
	end loop;

end;
$$
language 'plpgsql';