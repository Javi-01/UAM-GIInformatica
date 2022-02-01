from django.db import models
from django.core.validators import RegexValidator
from catalog.models import Book

'''
    @function: Order
    @brief: Order Model class
    @author: Javier Fraile Iglesias
'''


class Order(models.Model):

    first_name = models.CharField(max_length=255)
    last_name = models.CharField(max_length=255)
    email = models.EmailField()
    address = models.CharField(max_length=50)
    postal_code = models.CharField(max_length=5,
                                   validators=[RegexValidator(r'^\d{1,10}$')]
                                   )
    city = models.CharField(max_length=50)
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    paid = models.BooleanField(default=False)

    class Meta:

        ordering = ["updated"]

    def __str__(self) -> str:
        return self.first_name + self.last_name + str(self.id)

    '''
        @function: get_total_cost
        @brief: Function in charge of calculating
            the total price of a purchase
        @author: Iván Fernández París
    '''
    def get_total_cost(self):
        total_price = 0.0
        for orderItem in list(self.items.all()):
            total_price += float(orderItem.price * orderItem.quantity)

        return total_price


'''
    @function: OrderItem
    @brief: OrderItem Model class
    @author: Javier Fraile Iglesias
'''


class OrderItem(models.Model):

    order = models.ForeignKey(
        Order, on_delete=models.CASCADE, related_name="items"
        )
    book = models.ForeignKey(
        Book, on_delete=models.CASCADE, related_name="book"
        )
    price = models.DecimalField(max_digits=10, decimal_places=2)
    quantity = models.IntegerField(default=1)

    def __str__(self) -> str:
        return str(self.id) + " " + self.book.title
