from multiprocessing import context
from django.shortcuts import render
from .models import Modelo2


def index(request):
    modelos2 = Modelo2.objects.filter(modelo1=None)

    if modelos2:
        context = {"nombres": modelos2}
    else:
        context = {"error": "No se ha..."}

    return render(request, "aplicacion/index.html", context=context)


def get_id(request, id):
    modelo2 = Modelo2.objects.get(id=id)
    return render(request, "aplicacion/get_id.html", context={"modelo": modelo2})
