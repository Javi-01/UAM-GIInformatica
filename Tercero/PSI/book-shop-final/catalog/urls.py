from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('vote_book/<slug>', views.add_vote, name='vote_book'),
    path('detail/<slug>', views.DetailView.as_view(), name='detail'),
    path('search/', views.SearchListView.as_view(), name='search'),
]
