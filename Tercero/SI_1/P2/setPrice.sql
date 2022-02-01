update orderdetail od
set price = N.new_price
from (select o.orderid, od.prod_id, round(p.price * power(1.02, DATE_PART('year', AGE(now(), o.orderdate)))::numeric, 2) as new_price
from orders o join orderdetail od on od.orderid = o.orderid join products p on od.prod_id = p.prod_id) as N
where od.orderid = N.orderid and od.prod_id = N.prod_id