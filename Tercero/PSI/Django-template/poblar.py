import os
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'proyecto.settings')

import django
django.setup()

from aplicacion.models import Modelo1, Modelo2

def limpiar_modelos():
    Modelo1.objects.all().delete()
    Modelo2.objects.all().delete()


def poblar_modelo1():
    modelos1 = [
        {
            "id": 1000,
            "nombre1": "nombre1"
        },
        {
            "id": 1001,
            "nombre1": "nombre2"
        },
        {
            "id": 1002,
            "nombre1": "nombre3"
        }
    ]

    for modelo1 in modelos1:
        Modelo1(id=modelo1["id"], nombre1=modelo1["nombre1"]).save()


def poblar_modelo2():

    modelos2 = [
        {
            "id": 1001,
            "nombre": "nombre1",
            "modelo1": Modelo1.objects.get(id=1000)
        },
        {
            "id": 1002,
            "nombre": "nombre2",
            "modelo1": Modelo1.objects.get(id=1001)
        },
        {
            "id": 1003,
            "nombre": "nombre1",
            "modelo1": None
        }
    ]

    for modelo2 in modelos2:
        Modelo2(id=modelo2["id"], nombre=modelo2["nombre"], modelo1=modelo2["modelo1"]).save()


if __name__ == "__main__":
    limpiar_modelos()
    poblar_modelo1()
    poblar_modelo2()
