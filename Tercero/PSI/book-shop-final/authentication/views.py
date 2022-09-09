from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.contrib.auth import logout
from .forms import UserRegistrerForm
from bookshop import settings

'''
    @function: register
    @brief: Function in charge of user registration
    @author: Javier Fraile Iglesias
'''


def register(request):
    if request.method == "POST":
        form = UserRegistrerForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('home')
    else:
        form = UserRegistrerForm()
    return render(request, 'authentication/register.html', {'form': form})


'''
        @function: logout_user
        @brief: Function in charge of log out for
            be able to maintain the user's session.
        @author: Javier Fraile Iglesias
'''


@login_required
def logout_user(request):
    data = request.session.get('cart', None)
    logout(request)
    session = request.session
    if data:
        session[settings.CART_SESSION_ID] = data
        session.modified = True
    return redirect('home')
