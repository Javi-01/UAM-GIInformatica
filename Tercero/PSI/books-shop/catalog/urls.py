from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('detail/<slug>', views.DetailView.as_view(), name='detail'),
    path('search/', views.SearchListView.as_view(), name='search'),
]
