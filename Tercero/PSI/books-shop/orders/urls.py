from django.urls import path
from . import views

urlpatterns = [
    path('cart_add/<slug>', views.cart_add, name='cart_add'),
    path('cart_list', views.cart_list, name='cart_list'),
    path('cart_remove/<slug>', views.cart_remove,
         name='cart_remove'),
    path('order_create', views.order_create, name='order_create'),
]
