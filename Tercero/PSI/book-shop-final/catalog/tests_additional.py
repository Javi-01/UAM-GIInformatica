from django.test import TestCase, Client
from .management.commands.populate import Command
from django.urls import reverse
from decimal import Decimal

from .models import Author as Author
from .models import Book as Book

DETAIL_SERVICE = "detail"
SEARCH_SERVICE = "search"
SEARCH_TITLE = "Search"

SERVICE_DEF = {DETAIL_SERVICE: {
        "title": "",
    },
    SEARCH_SERVICE: {
        "title": SEARCH_TITLE,
    },
}


class ServiceBaseTest(TestCase):

    def setUp(self):
        self.client1 = self.client
        self.client2 = Client()
        self.client3 = Client()
        self.populate = Command()
        self.populate.handle()

    def tearDown(self):
        self.populate.cleanDataBase()

    @classmethod
    def decode(cls, txt):
        return txt.decode("utf-8")


'''
    @function: CatalogAdditionalTests
    @brief: Class with additional quizzes to complete
        the coverage of the models and views modules.
    @author: Iván Fernández París
'''


class CatalogAdditionalTests(ServiceBaseTest):

    author1Dict = {
        "first_name": 'Charles',
        "last_name": 'Dickens',
    }

    author2Dict = {
        "first_name": 'Mary',
        "last_name": 'Shelley',
    }

    bookDict = {
        "isbn": '1234567890123',
        "title": 'title_2',
        "price": Decimal(14.32),
        "path_to_cover_image": 'oporto.jpg',
        "number_copies_stock": 14,
        "score": 5,
    }

    userDict = {
        "username": 'Oporto',
        "password": 'F.C',
        "first_name": 'Pedro',
        "last_name": 'Porro',
        "email": 'pedro.porro@oporto.com',
    }

    def create_check(self, dictionary, ObjectClass):
        """ create an object of the class 'ObjectClass'
        using the dictionary. Then,
        check that all key-values in the
        dictionary are attributes in the object.
        return created object of class Object
        """
        # check that str function exists
        self.assertTrue(ObjectClass.__str__ is not object.__str__)
        # create object
        item = ObjectClass.objects.create(**dictionary)
        for key, value in dictionary.items():
            self.assertEqual(getattr(item, key), value)
        # execute __str__() so all the code in models.py is checked
        item.__str__()
        return item

    '''
        @function: test00_home
        @brief: Test to check that the highest rated and most
            recent books are correctly obtained.
        @author: Iván Fernández París
    '''
    def test00_home(self):
        """check that the 5 best scored books and
        the most recent are passed to the home"""
        # mayor date and score
        mayor = Book.objects.all().order_by('-score')[:5]
        recent = Book.objects.all().order_by('-date')[:5]

        response = self.client1.get(
                reverse("home", kwargs={}),
                follow=True)

        response_txt = self.decode(response.content)

        for b1 in mayor:
            self.assertFalse(response_txt.find(b1.title) == -1)

        for b2 in recent:
            self.assertFalse(response_txt.find(b2.title) == -1)

    '''
        @function: test01_empty_string
        @brief: Test to check the search in case of
            filtering by an empty string
        @author: Iván Fernández París
    '''
    def test01_empty_string(self):
        """Check what happens if we search without filtering any string"""

        searchString = ''

        response = self.client1.get(
            reverse(SEARCH_SERVICE) + '?q=%s' % searchString, follow=True)
        response_txt = self.decode(response.content)
        for i in range(int(response.context['paginator'].num_pages)+1):
            response = self.client1.get(
                reverse(SEARCH_SERVICE) + '?q=%s&page=%d' % (searchString, i),
                follow=True)
            response_txt += self.decode(response.content)

        for book in Book.objects.all():
            self.assertIn(book.title, response_txt)

    '''
        @function: test02_book
        @brief: Test to check the functionality of
            the display_authors and get_absolute_url
            methods of the Book model.
        @author: Iván Fernández París
    '''
    def test02_book(self):
        "Test book model with no authors"
        book = self.create_check(self.bookDict, Book)

        # Test book model with authors
        a1 = self.create_check(self.author1Dict, Author)
        a2 = self.create_check(self.author2Dict, Author)
        book.author.add(a1)
        book.author.add(a2)
        book.save()

        bookA1 = a1.book_set.all()[0]

        # Comparamos que imprima en el orden
        # establecido los nombres de los autores
        self.assertEqual(bookA1.display_authors(),
                         "Charles Dickens; Mary Shelley")

        # Comparamos que el metodo get_absolute_url
        # devuelve la url correcta
        self.assertEqual(bookA1.get_absolute_url(),
                         "/home/detail/" + bookA1.slug)
