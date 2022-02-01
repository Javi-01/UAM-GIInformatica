from django.shortcuts import get_object_or_404, redirect, render
from .cart import Cart
from .forms import CartAddBookForm, OrderCreateForm
from catalog.models import Book
from .models import OrderItem
from django.contrib.auth.decorators import login_required
from django.views.decorators.http import require_POST

'''
    @function: cart_add
    @brief: Function in charge of adding a
        quantity of a book to the cart.
    @author: Iván Fernández París
'''


@login_required
@require_POST
def cart_add(request, slug):
    """ add the book with slug " book_slug " to the
    shopping cart . The number of copies to be bought
    may be obtained from the form CartAddBookForm """
    cart = Cart(request)
    # your code goes here
    form = CartAddBookForm(request.POST)
    if form.is_valid():
        book = get_object_or_404(Book, slug=slug)
        quantity = form.cleaned_data['quantity']

        # Llamamos a add del modulo Cart añadiendo el libro al carrito
        cart.add(book, quantity)
        return redirect('cart_list')


'''
    @function: cart_list
    @brief: Function in charge of loading
        the user's shopping cart.
    @author: Iván Fernández París
'''


@login_required
def cart_list(request):
    cart = Cart(request)

    form = CartAddBookForm()
    return render(request, 'orders/cart_list.html',
                  context={'carrito': cart, 'form': form})


'''
    @function: cart_remove
    @brief: Function in charge of removing
        a book to the cart.
    @author: Iván Fernández París
'''


@login_required
def cart_remove(request, slug):

    cart = Cart(request)

    book = get_object_or_404(Book, slug=slug)
    cart.remove(book)
    return redirect('cart_list')


'''
    @function: order_create
    @brief: Function in charge of confirming
        the purchase of the user's cart.
    @author: Javier Fraile Iglesias
'''


@login_required
def order_create(request):

    if request.method == 'POST':
        form = OrderCreateForm(request.POST)
        if form.is_valid():
            order = form.save()
            cart = Cart(request)
            for item in cart:
                OrderItem.objects.create(
                    order=order,
                    book=get_object_or_404(Book, slug=item['book_slug']),
                    price=item['price'],
                    quantity=item['quantity'],
                    )
            cart.clear()

            return render(request, 'orders/created.html')

    else:
        form = OrderCreateForm()
        return render(request, 'orders/create.html', {'form': form})
