create or replace function updInventoryAndCustomers() returns trigger as $$
declare f record;
begin
	--Se comprueba que el status haya pasado a pagado
	if old.status is NULL and new.status = 'Paid' then
	--Se comprueba que haya stock de dichos productos
		for f in select i.stock, od.quantity
				from orderdetail od join inventory i on od.prod_id = i.prod_id
					where new.orderid = od.orderid
		loop
			if f.quantity > f.stock then
				update orders set status = null where orderid = old.orderid;
				return old;
			end if;
		end loop;

	--Se comprueba que el usuario tenga saldo suficiente
		if (select sum(c.balance + c.loyalty) from customers c where c.customerid = new.customerid) < new.totalamount then
			update orders set status = null where orderid = old.orderid;
			return old;
		else
		--Si puede comprarlo, primero se mira si puede pagarlo entero con puntos, con lo cual se les descuenta y aplica la bonificacion de puntos
			if (select c.loyalty from customers c where c.customerid = new.customerid) > new.totalamount then 
				update customers c set
					loyalty = loyalty - new.totalamount + (new.totalamount * 0.05)
				where c.customerid = new.customerid;
			else
			--Se actualiza el customer, restandole al saldo el precio de la compra, y se le añaden los puntos de fidelidad
				update customers c set
					balance = loyalty + balance - new.totalamount,
					loyalty = new.totalamount * 0.05
				where c.customerid = new.customerid;
			end if;
		end if;

		--Se actualiza el stock del producto, restando las cantidades compradas y se aumentan las ventas del producto
		update inventory i set
			stock = stock - N.quantity,
			sales = sales + N.quantity
		from (select od.prod_id, od.quantity from orderdetail od join orders on new.orderid = od.orderid) as N
			where N.prod_id = i.prod_id;

		update orders set orderdate = now() where orderid = new.orderid;
		--Se crean alertas para todos aquellos productos cuyo stock se ha quedado a 0
		insert into alerts
			select i.prod_id, now() from inventory i join orderdetail od on od.prod_id = i.prod_id where od.orderid = new.orderid
			and i.stock = 0;
		return new;
	end if;
	return old;
end;
$$
language 'plpgsql';

create trigger upd_inventoryAndCustomer
after update of status on orders
for each row
execute procedure updInventoryAndCustomers();
