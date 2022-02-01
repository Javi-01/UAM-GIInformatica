create or replace function updOrders() returns trigger as $$
begin
		if TG_OP = 'DELETE' then
			update orders o set
			netamount = N.netamount_price,
			totalamount =  round(N.netamount_price *(1 + o.tax/100), 2)
			from (select od.orderid, sum(od.price * od.quantity) as netamount_price
			from orderdetail od where od.orderid = old.orderid
			group by od.orderid) as N where N.orderid = o.orderid;
			return old;
		elsif TG_OP = 'UPDATE' or TG_OP = 'INSERT' then
			update orders o set
			netamount = N.netamount_price,
			totalamount =  round(N.netamount_price *(1 + o.tax/100), 2)
			from (select od.orderid, sum(od.price * od.quantity) as netamount_price
			from orderdetail od where od.orderid = new.orderid
			group by od.orderid) as N where N.orderid = o.orderid;
			return new;
		end if;
end;
$$
language 'plpgsql';

create trigger upd_orders
after update or delete or insert on orderdetail
for each row
execute procedure updOrders();
