create or replace function setOrderAmount() returns void as $$
begin
	update orders o set
		netamount = N.netamount_price,
		totalamount =  round(N.netamount_price *(1 + o.tax/100), 2)
	from (select od.orderid, sum(od.price * od.quantity) as netamount_price
		from orders o join orderdetail od on o.orderid = od.orderid
		group by od.orderid) as N
		where N.orderid = o.orderid and o.netamount is NULL and o.totalamount is NULL;
end;
$$
language 'plpgsql';

select setOrderAmount();