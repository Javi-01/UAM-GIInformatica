analyze orders;
create index index_status on orders(status);

----------------------------------------------------
explain analyze select count(*)
from orders
where status is null;

select count(*)
from orders
where status is null;

----------------------------------------------------
explain analyze select count(*)
from orders
where status ='Shipped';

select count(*)
from orders
where status ='Shipped';
----------------------------------------------------

explain analyze select count(*)
from orders
where status ='Paid';

select count(*)
from orders
where status ='Paid';
----------------------------------------------------
explain analyze select count(*)
from orders
where status ='Processed';

select count(*)
from orders
where status ='Processed';