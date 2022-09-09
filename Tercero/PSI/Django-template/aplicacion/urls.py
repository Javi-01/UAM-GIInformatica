from django.urls import path
from . import views

urlpatterns = [
    path('index/', views.index, name="index"),
    path('get_id/<id>/', views.get_id, name="get_id")
]
