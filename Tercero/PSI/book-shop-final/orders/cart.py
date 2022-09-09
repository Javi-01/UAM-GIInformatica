from django . conf import settings
from catalog . models import Book


class Cart(object):

    def __init__(self, request):
        """
        Initialize the cart .
        if request . session [ settings . CART_SESSION_ID ]
        does not exist create one
        Important : Make a copy of request . session [
        settings . CART_SESSION_ID ]
        do not manipulate it directly
        request . session is not a proper
        dictionary and
        direct manipulation will produce
        weird results
        """

        # request session is the only variable
        # that persists between two http accesses
        # from the same client

        self.session = request.session
        cart = self.session.get(settings.CART_SESSION_ID)
        if not cart:
            # If there is no cart create an empty one
            # and save it in the session
            cart = self.session[settings.CART_SESSION_ID] = {}
        self.cart = cart

    def add(self, book, quantity=1, update_quantity=False):
        """
        Add a book to the cart or update quantity if
        book exists . Note , use strings as keys of the
        dictionary since session use JSON module
        to serialize dictionaries and JSON
        many not supports keys that are not strings ,
        For each book store only the ID ( as key )
        and a dictionary
        with the price and quantity as value
        { ’ quantity ’: 0 ,
        ’ price ’: str ( book . price ) }
        Store ’ price ’ as a string because a Decimal
        object may not be properlly serialized
        """
        book_id = str(book.id)

        if book_id not in self.cart.keys():
            self.cart[book_id] = {
                'quantity': 0,
                'price': str(book.price)}

        self.cart[book_id]['quantity'] += quantity

        # end of your code goes here
        self.save()

    def save(self):
        # update the session cart
        self.session[settings.CART_SESSION_ID] = self.cart
        # mark the session as " modified "
        # to make sure it is saved
        # django saves the session if
        # new pairs key : value are
        # added or deleted but what is
        # modified is inside self . cart
        # and django does not realize
        # that it has been modified .
        self.session.modified = True

    def remove(self, book):
        """
        Remove a book from the cart .
        """
        # your code goes here
        book_id = str(book.id)
        if book_id in self.cart.keys():
            del self.cart[book_id]

        self.save()
        # endyourcode

    def __iter__(self):
        """
        Iterate over the items ( books IDs ) in the cart
        and get the book

        from the database . This function is used by the
        for loop .
        for item in self . cart :
        Note : here we add to each item saved in self .
        cart and object
        of type book . This will help us to create
        templates showing
        the book title . We can not add a book object in
        the method
        " add " becose self . cart is saved at the end of
        the function and a session variable cannot
        be complex , it can only store numbers and
        strings but not object with pointers .
        """
        book_ids = self.cart.keys()
        # get the book objects and add them to the cart
        books = Book.objects.filter(id__in=book_ids)
        for book in books:

            # Si el precio del libro ha cambiado se actualiza
            if str(book.price) != self.cart[str(book.id)]['price']:
                self.cart[str(book.id)]['price'] = str(book.price)

            self.cart[str(book.id)]['book_title'] = book.title
            self.cart[str(book.id)]['book_slug'] = book.slug

        for item in self.cart.values():
            # since ’ price ’ is stored as string cast it
            # to ’ decimal ’

            item['price'] = float(item['price'])
            item['total_price'] = item['price']*item['quantity']

            yield item

    def __len__(self):
        """
        return the number of items in the cart . That is
        , the sum of
        the quantities of each book in the cart . If the
        user
        wants to buy 2 copies of book with id =1 and 4
        copies of
        book with id =2
        __len__ () should return 6
        """
        # your code goes here
        quantity = 0
        for v in self.cart.values():
            quantity += v['quantity']

        self.save()
        return quantity
        # endyourcode

    def get_total_price(self):
        """
        returns total amount to be paid for all items
        in the cart
        """
        # your code goes here
        total_price = 0.0
        for k, v in self.cart.items():
            book = Book.objects.get(id=k)
            if float(book.price) != float(self.cart[k]['price']):
                self.cart[k]['price'] = str(book.price)
            total_price += (v['quantity']*float(v['price']))

        self.save()
        return total_price
        # endyourcode

    def clear(self):
        # remove cart from session
        del self.session[settings.CART_SESSION_ID]
        self.session.modified = True
