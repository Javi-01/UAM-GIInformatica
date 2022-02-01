--------------------
--IMDB_ACTORMOVIES--
--------------------
alter table imdb_actormovies
    add foreign key(movieid) references
    imdb_movies (movieid)
    on update cascade
    on delete cascade;

alter table imdb_actormovies
    add foreign key(actorid) references
    imdb_actors (actorid)
    on update cascade
    on delete cascade;

alter table imdb_actormovies
    add primary key(actorid, movieid);


----------
--ALERTS--
----------
create table alerts(
	prod_id serial4,
	sold_out_time timestamp,
	primary key(prod_id),
	foreign key(prod_id) references inventory (prod_id)
);

------------
--CUSTOMERS--
-------------
alter table customers
    add column loyalty decimal default 0,
    add column balance BIGINT;

create or replace function setCustomerBalance(IN initialBalance bigint)
    returns void as $$
begin
		update customers
			set balance = floor(random() * initialBalance + 0);
end;
$$
language 'plpgsql';

select setCustomerBalance(100);


---------------
--ORDERDETAIL--
---------------
alter table orderdetail
    add foreign key(prod_id) references
    products (prod_id)
    on update cascade
    on delete cascade;

alter table orderdetail
    add foreign key(orderid) references
    orders (orderid)
    on update cascade
    on delete cascade;


----------
--ORDERS--
----------
alter table orders
    add foreign key(customerid) references
    customers (customerid)
    on update cascade
    on delete cascade;


-------------
--INVENTORY--
-------------
alter table inventory
    add foreign key(prod_id) references
    products (prod_id)
    on update cascade
    on delete cascade;


-------------
--LANGUAGES--
-------------
--La nueva relacion languages tiene como objetivo establecer los lenguages en una
--relacion aparte para eliminar los atributos multivalor de imdb_movielanguage

--Se crea una secuencia que se incremente en 1 unidad sin limites minimo ni maximo
create sequence language_sequence
	start with 1
	increment by 1
	no minvalue
	no maxvalue;

--Se crea la nueva relacion languages con un id el cual se ira autoincrementando
--cuando introduzcamos posteriomente las lenguages, de manera que nextval obtiene
--el siguiente valor de la secuencia creada
create table languages (
	language_id int not null default nextval('language_sequence'),
	language varchar(50) not null,
	primary key (language_id)
);

--le asignamos a ese id la secuencia
alter sequence language_sequence
	owned by languages.language_id;

--se ingresa en la tabla de lenguages, todos los idiomas que contiene imdb_movielanguages
insert into languages (language)
	select distinct language from imdb_movielanguages;


-----------------------
--IMDB_MOVIELANGUAGES--
-----------------------
--Comenzamos introduciendo la nueva columna con el id creado en la tabla language
alter table imdb_movielanguages
	add column language_id int not null default 1;

--Referenciamos dicho atributo con el de la tabla language
alter table imdb_movielanguages
	add foreign key (language_id)
	references languages (language_id)
	on update cascade
	on delete cascade;

--actualizamos la nueva tabla estableciendo los valores del id en funcion de su
--correspondencia con el lenguage
update imdb_movielanguages im set
	language_id = l.language_id
	from languages l
	where l.language = im.language;

--En este momento ya no necesitamos la columna de los lenguages porque ya tenemos
--la relacion creada
alter table imdb_movielanguages drop column language;

--Se añade la tupla como clave primaria
alter table imdb_movielanguages
	add primary key (movieid, language_id);

--Finalmente actualizamos la clave foranea actualizando los valores en cascada
alter table imdb_movielanguages
	drop constraint imdb_movielanguages_movieid_fkey;

alter table imdb_movielanguages
	add foreign key (movieid) references
	imdb_movies (movieid)
	on update cascade
	on delete cascade;


----------
--GENRES--
----------
--La nueva relacion genres tiene como objetivo establecer los genres en una
--relacion aparte para eliminar los atributos multivalor de imdb_moviegenres

create sequence genre_sequence
	start with 1
	increment by 1
	no minvalue
	no maxvalue;

create table genres (
	genre_id int not null default nextval('genre_sequence'),
	genre varchar(50) not null,
	primary key (genre_id)
);

alter sequence genre_sequence
	owned by genres.genre_id;

insert into genres (genre)
	select distinct genre from imdb_moviegenres;


--------------------
--IMDB_MOVIEGENRES--
--------------------
alter table imdb_moviegenres
	add column genre_id int not null default 1;

alter table imdb_moviegenres
	add foreign key (genre_id)
	references genres (genre_id)
	on update cascade
	on delete cascade;

update imdb_moviegenres im set
	genre_id = l.genre_id
	from genres l
	where l.genre = im.genre;

alter table imdb_moviegenres drop column genre;

alter table imdb_moviegenres
	add primary key (movieid, genre_id);

alter table imdb_moviegenres
	drop constraint imdb_moviegenres_movieid_fkey;

alter table imdb_moviegenres
	add foreign key (movieid) references
	imdb_movies (movieid)
	on update cascade
	on delete cascade;


-------------
--COUNTRIES--
-------------
--La nueva relacion countries tiene como objetivo establecer los countries en una
--relacion aparte para eliminar los atributos multivalor de imdb_moviecountries

create sequence country_sequence
	start with 1
	increment by 1
	no minvalue
	no maxvalue;

create table countries (
	country_id int not null default nextval('country_sequence'),
	country varchar(50) not null,
	primary key (country_id)
);

alter sequence country_sequence
	owned by countries.country_id;

insert into countries (country)
	select distinct country from imdb_moviecountries;


-----------------------
--IMDB_MOVIECOUNTRIES--
-----------------------
alter table imdb_moviecountries
	add column country_id int not null default 1;

alter table imdb_moviecountries
	add foreign key (country_id)
	references countries (country_id)
	on update cascade
	on delete cascade;

update imdb_moviecountries im set
	country_id = l.country_id
	from countries l
	where l.country = im.country;

alter table imdb_moviecountries drop column country;

alter table imdb_moviecountries
	add primary key (movieid, country_id);

alter table imdb_moviecountries
	drop constraint imdb_moviecountries_movieid_fkey;

alter table imdb_moviecountries
	add foreign key (movieid) references
	imdb_movies (movieid)
	on update cascade
	on delete cascade;
