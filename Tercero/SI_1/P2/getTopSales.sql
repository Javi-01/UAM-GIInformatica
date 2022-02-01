create or replace function getTopSales(IN year1 INT, IN year2 INT)
	returns table (Year int, Film varCHAR, sales bigint) as $$
declare
begin
	loop
		exit when year1 > year2;

		return query select m.year::integer, m.movietitle, sum(i.sales)
		from imdb_movies m
			join products p on m.movieid = p.movieid
			join inventory i on p.prod_id = i.prod_id
		where m.year = year1::text
		group by (m.movieid) order by sum(i.sales) desc limit 1;

		year1 := year1 + 1;
	 end loop;
end; $$
language 'plpgsql';
