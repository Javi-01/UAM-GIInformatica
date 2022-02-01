--Se crea la columna promo en la tabla customers, como decimal
alter table customers add column promo decimal(5,2) default 0;


--Triger para actualizar el precio en base al nuevo porcentaje aplicado en
--la columna promo
create or replace function updatePrice() returns trigger as $$
begin
		perform pg_sleep(5);
  		update orders o set
  			totalamount = (totalamount * ((100 - new.promo)/100))
  		where new.customerid = o.customerid and o.status is null;

  	return new;
end;
$$
language 'plpgsql';

--el trigger se ejecuta sobre la tabla customer, justo en el momento posterior
--a actualizar la columna promo

create trigger upd_price
after update of promo on customers
for each row
execute procedure updatePrice();




--Para la ejecución de la prueba del deadlock ejecutamos los siguientes comandos
update orders
set status = null
where orderid = 15236;

begin;
update customers set promo=15 where customerid=1144;
commit;
