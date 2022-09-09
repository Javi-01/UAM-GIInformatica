# TEST ADICIONALES PARA LA COVERTURA DE LOS MODULOS IMPLEMENTADOS

from django.test import TestCase
from django.urls import reverse
from django.test import Client
from catalog.models import Book
from django.conf import settings
from .cart import Cart
from catalog.management.commands.populate import Command

ORDERSERVICE = 'order_create'


'''
    @function: CartTest
    @brief: Class with additional quizzes to complete
        the coverage of the models and views modules.
    @author: Iván Fernández París
'''


class CartTest(TestCase):
    def setUp(self):
        # fill database
        self.populate = Command()
        self.populate.handle()
        self._client = Client()

    @classmethod
    def decode(cls, txt):
        return txt.decode("utf-8")

    '''
        @function: test00_CartTotalPrice
        @brief: We check that if a book that is in the cart changes its
            value, the total price of the cart changes as well.
        @author: Iván Fernández París
    '''
    def test00_CartTotalPrice(self):
        # CREATE SESSION
        # create session so we can store the cart in it
        response = self._client.get(reverse('home'))
        request = response.wsgi_request

        # get book
        books = Book.objects.all()
        # create cart
        UNITS5 = 5
        UNITS3 = 5
        cart = Cart(request)

        # add items to the cart
        book0 = books.first()
        book1 = books[1]
        cart.add(book0, quantity=UNITS5)
        cart.add(book1, quantity=UNITS3)

        totalPrice = cart.get_total_price()
        quantity0 = request.session[settings.CART_SESSION_ID][
            str(book0.id)]['quantity']
        quantity1 = request.session[settings.CART_SESSION_ID][
            str(book1.id)]['quantity']
        _totalPrice = book0.price * quantity0 + book1.price * quantity1
        self.assertEqual(totalPrice, _totalPrice)

        ''' We change the price of the book so that
        the total price of the cart is updated '''
        book0.price = float("5.9")
        book0.save()
        totalPrice = cart.get_total_price()
        self.assertNotEqual(totalPrice, _totalPrice)
