from decimal import Decimal
from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator
from django.template.defaultfilters import slugify
from django.contrib.auth.models import User
import time
from django.urls import reverse

'''
    @function: Author
    @brief: Author Model class
    @author: Javier Fraile Iglesias
'''


class Author(models.Model):
    first_name = models.CharField(max_length=255)
    last_name = models.CharField(max_length=255)

    def __str__(self) -> str:
        return self.first_name + " " + self.last_name


'''
    @function: Book
    @brief: Book Model class
    @author: Javier Fraile Iglesias
'''


class Book(models.Model):
    isbn = models.CharField(max_length=13, unique=True,
                            null=False, blank=False)
    author = models.ManyToManyField(Author, blank=True)
    title = models.CharField(max_length=255, null=False)
    price = models.DecimalField(blank=True, null=True,
                                max_digits=5, decimal_places=2,
                                validators=[MinValueValidator(Decimal('0.0'))])
    path_to_cover_image = models.ImageField(
        default="book.jpg",
        upload_to="cover_img"
    )
    number_copies_stock = models.IntegerField(default=True, null=True)
    date = models.DateField(blank=True, null=True)
    score = models.DecimalField(
        max_digits=3, decimal_places=1, null=True, blank=True)
    number_votes = models.IntegerField(default=0)
    slug = models.SlugField(max_length=255, unique=True)

    '''
        @function: save
        @brief: Function in charge of generating the slug of the book.
        @author: Javier Fraile Iglesias
    '''

    def save(self, *args, **kwargs):
        if not self.slug:
            titulo = self.title.split(' ')
            titulo = '-'.join(titulo)

            date = str(time.time()).split('.')
            date = ''.join(date)

            self.slug = slugify(
                titulo.lower() + '-' + date
            )
        super(Book, self).save(*args, **kwargs)

    class Meta:

        ordering = ["title"]

    def __str__(self) -> str:
        return self.title

    '''
        @function: display_author
        @brief: this function prints the names and surnames
            of the authors of the book.
        @author: Iván Fernández París
    '''

    def display_authors(self):
        return "; ".join(
            [author.first_name + " " + author.last_name
             for author in self.author.all()]
        )

    '''
        @function: get_absolute_url
        @brief: This function tells Django how to
            calculate the URL for a book
        @author: Iván Fernández París
    '''

    def get_absolute_url(self):
        return reverse("detail", kwargs={'slug': str(self.slug)})


'''
    @function: Comment
    @brief: Comment Model class
    @author: Javier Fraile Iglesias
'''


class Comment(models.Model):
    book = models.ForeignKey(Book, on_delete=models.CASCADE)
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    date = models.DateTimeField(null=True, blank=True)
    msg = models.TextField()

    def __str__(self) -> str:
        return self.user.username + str(self.date)


'''
    @function: VoteBook
    @brief: VoteBook Model class
    @author: Javier Fraile Iglesias
'''


class VoteBook(models.Model):

    book = models.ForeignKey(
        Book, on_delete=models.CASCADE, related_name="book_score"
    )
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    user_score = models.IntegerField(validators=[
        MinValueValidator(0), MaxValueValidator(10)
    ])

    def __str__(self) -> str:
        return str(self.id)
