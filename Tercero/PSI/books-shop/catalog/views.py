from django.shortcuts import render
from .models import Book, Comment, Author
from django.views import generic
from django.db.models import Q
from orders.forms import CartAddBookForm

'''
        @function: home
        @brief: Function that takes care of getting the best rated
            and the most recent books and rendering in the template.
        @author: Iván Fernández París
'''


def home(request):

    mayor = Book.objects.all().order_by('-score')[:5]
    recent = Book.objects.all().order_by('-date')[:5]

    context = {"mayor": mayor, "recents": recent}

    return render(request, 'home.html', context=context)


'''
    @function: DetailView
    @brief: Function in charge of the view of
        a book from the slug of the book.
    @author: Iván Fernández París
'''


class DetailView(generic.DetailView):

    model = Book
    template_name = 'catalog/book_detail.html'

    '''
        @function: get_context_data
        @brief: This function is in charge of obtaining
            the context of a query by a book slug.
        @author: Iván Fernández París
    '''
    def get_context_data(self, *args, **kwargs):
        context = super(DetailView, self).get_context_data(**kwargs)

        context['pk'] = self.kwargs.get('slug')
        book = Book.objects.get(slug=context['pk'])
        comments = Comment.objects.all().filter(book=book)
        context['comments'] = comments

        # Creamos el formulario para añadir libros al carrito
        context['form'] = CartAddBookForm()

        return context


'''
    @function: SearchListView
    @brief: Function in charge of the book
        search view from a filtered text.
    @author: Iván Fernández París
'''


class SearchListView (generic.ListView):

    model = Book
    template = 'catalog/book_list.html'
    paginate_by = 5
    context = {}

    '''
        @function: get_context_data
        @brief: This function is in charge of obtaining
            the context of a query allowing to
            better manage the pagination.
        @author: Javier Fraile Iglesias
    '''
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['query'] = self.get_queryset()
        context['search'] = self.request.GET.get('q', '')
        return context

    '''
        @function: get_queryset
        @brief: This function modifies the initial query
            of the view from the string entered by the user.
        @author: Iván Fernández París
    '''
    def get_queryset(self):

        q = self.request.GET.get('q', '')
        if q:
            title_books_by_authors = []

            filter = Q(first_name__icontains=q) | Q(last_name__icontains=q)
            authors = Author.objects.filter(filter)

            '''We check for each author that contains in his
                or her first name or in his or her surname
                the surname in the string entered,
                those books written by them by them'''
            for author in authors:
                for book in author.book_set.all():
                    if book.title not in title_books_by_authors:
                        title_books_by_authors.append(book.title)

            f = Q(title__icontains=q) | Q(title__in=title_books_by_authors)
            query = self.model.objects.filter(f)
        else:
            query = self.model.objects.all()
        return query
