drop index index_creditCardType;

create index index_creditCardType on customers(customerid, creditcardtype);

explain select count (distinct o.city)
from customers c join orders o on c.customerid = o.customerid where c.creditcardtype = 'VISA' and
extract (month from o.orderdate) = 04 and extract (year from o.orderdate) = 2016;