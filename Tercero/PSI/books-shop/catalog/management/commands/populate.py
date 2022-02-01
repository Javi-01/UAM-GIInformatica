# Populate database
# This file has to be placed within the
# catalog/management/commands directory in your project.
# If that directory doesn't exist, create it.
# The name of the script is the name of the custom command,
# that is, populate.py.
#
# execute python manage.py  populate
#
# use module Faker generator to generate data
# (https://zetcode.com/python/faker/)
import os

from django.core.management.base import BaseCommand
from catalog.models import (Author, Book, Comment)
from django.contrib.auth.models import User
from faker import Faker
# from decimal import Decimal
# define STATIC_PATH in settings.py
from bookshop.settings import STATIC_PATH
from PIL import Image, ImageDraw, ImageFont
import random
import pytz


FONTDIR = "/usr/share/fonts/truetype/freefont/FreeMono.ttf"

# The name of this class is not optional must be Command
# otherwise manage.py will not process it properly
#


class Command(BaseCommand):
    # helps and arguments shown when command python manage.py help populate
    # is executed.
    help = """populate database
           """

    # def add_arguments(self, parser):

    # handle is another compulsory name, do not change it"
    # handle function will be executed by 'manage populate'
    def handle(self, *args, **kwargs):
        # check a variable that is unlikely been set out of heroku
        # as DYNO to decide which font directory should be used.
        # Be aware that your available fonts may be different
        # from the ones defined here
        if 'DYNO' in os.environ:
            self.font = \
                "/usr/share/fonts/truetype/dejavu/DejaVuSansMono-Bold.ttf"
        else:
            self.font = \
                "/usr/share/fonts/truetype/freefont/FreeMono.ttf"

        self.NUMBERUSERS = 20
        self.NUMBERBOOKS = 30
        self.NUMBERAUTHORS = 6
        self.MAXAUTHORSPERBOOK = 3
        self.NUMBERCOMMENTS = self.NUMBERBOOKS * 5
        self.MAXCOPIESSTOCK = 30
        self.cleanDataBase()   # clean database
        # The faker.Faker() creates and initializes a faker generator,
        self.faker = Faker()
        self.user()
        self.author()
        self.book()
        self.comment()
        # check a variable that is unlikely been set out of heroku
        # as DYNO to decide which font directory should be used.
        # Be aware that your available fonts may be different
        # from the ones defined here

    '''
        @function: cleanDataBase
        @brief: Function in charge of clearing the database
            and create the superuser.
        @author: Iván Fernández París
    '''
    def cleanDataBase(self):
        User.objects.all().delete()
        Author.objects.all().delete()
        Book.objects.all().delete()
        Comment.objects.all().delete()

        campos = ['alumnodb', 'admin@myproject.com', 'alumnodb']
        User.objects.create_superuser(campos[0], campos[1], campos[2])

    '''
        @function: user
        @brief: Function in charge of generating users.
        @author: Javier Fraile Iglesias
    '''
    def user(self):
        " Insert users"
        self.usersList = []
        self.users = []
        for i in range(0, self.NUMBERUSERS):
            email = self.faker.email()
            user = {"username": email,
                    "password": self.faker.password(),
                    "first_name": self.faker.first_name(),
                    "last_name": self.faker.last_name(),
                    "email": email
                    }
            self.usersList.append(user)
            user = User.objects.create_user(**user)

            # store user id in list
            self.usersList[i]["id"] = user.id
            self.users.append(user)

    '''
        @function: author
        @brief: Function in charge of generating authors.
        @author: Javier Fraile Iglesias
    '''
    def author(self):
        " Insert authors"
        for i in range(0, self.NUMBERAUTHORS):
            author = Author(
                    first_name=self.faker.first_name(),
                    last_name=self.faker.last_name()
            )
            author.save()

    '''
        @function: cover
        @brief: Function in charge of generating book's cover.
        @author: psi teachers
    '''
    def cover(self, book):
        """create fake cover image.
           This function creates a very basic cover
           that show (partially),
           the primary key, title and author name"""

        img = Image.new('RGB', (200, 300), color=(73, 109, 137))
        # your font directory may be different
        fnt = ImageFont.truetype(
            self.font,
            28, encoding="unic")
        d = ImageDraw.Draw(img)
        d.text((10, 100), "PK %05d" % book.id, font=fnt, fill=(255, 255, 0))
        d.text((20, 150), book.title[:15], font=fnt, fill=(255, 255, 0))
        d.text((20, 200), "By %s" % str(
            book.author.all()[0])[:15], font=fnt, fill=(255, 255, 0))
        img.save(os.path.join(STATIC_PATH + "covers/",
                              str(book.path_to_cover_image)))

    '''
        @function: book
        @brief: Function in charge of generating books.
        @author: Javier Fraile Iglesias
    '''
    def book(self):
        " Insert books"
        authors = Author.objects.all()
        for i in range(0, self.NUMBERBOOKS):
            libro = Book(
                    isbn=self.faker.isbn10(),
                    title=self.faker.name(),
                    price=self.faker.random_int(20, 45),
                    number_copies_stock=random.randint(0, self.MAXCOPIESSTOCK),
                    date=self.faker.date(),
                    score=self.faker.random_int(0, 10),
            )
            libro.save()

            # Among all the authors you choose a number
            # of them at random to associate that number
            # of authors to the book
            num_auths = random.randint(1, self.MAXAUTHORSPERBOOK - 1)
            for j in range(0, num_auths):
                auths = random.randint(0, self.NUMBERAUTHORS - 1)
                libro.author.add(authors[auths])
            libro.path_to_cover_image = str(libro.slug) + ".jpg"
            libro.save()

            self.cover(libro)  # We create the book's cover

    '''
        @function: comment
        @brief: Function in charge of generating comments.
        @author: Javier Fraile Iglesias
    '''
    def comment(self):
        " Insert comments"
        users = User.objects.all()
        books = Book.objects.all()
        for i in range(0, self.NUMBERCOMMENTS):
            num_user = random.randint(0, self.NUMBERUSERS - 2)
            num_book = random.randint(0, self.NUMBERBOOKS - 1)
            comment = Comment(
                msg=self.faker.text(),
                user=users[num_user],
                book=books[num_book],
                date=self.faker.date_time(tzinfo=pytz.UTC)
            )
            comment.save()
