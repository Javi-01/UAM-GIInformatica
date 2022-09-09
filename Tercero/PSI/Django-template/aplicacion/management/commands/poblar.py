import os
from aplicacion.models import Modelo1, Modelo2
from django.core.management.base import BaseCommand


FONTDIR = "/usr/share/fonts/truetype/freefont/FreeMono.ttf"


class Command(BaseCommand):
    help = """populate database
           """

    def handle(self, *args, **kwargs):

        if 'DYNO' in os.environ:
            self.font = \
                "/usr/share/fonts/truetype/dejavu/DejaVuSansMono-Bold.ttf"
        else:
            self.font = \
                "/usr/share/fonts/truetype/freefont/FreeMono.ttf"

        self.limpiar_modelos()
        self.poblar_modelo1()
        self.poblar_modelo2()

    def limpiar_modelos(self):
        Modelo1.objects.all().delete()
        Modelo2.objects.all().delete()

    def poblar_modelo1(self):
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

    def poblar_modelo2(self):
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
            Modelo2(id=modelo2["id"], nombre=modelo2["nombre"],
                    modelo1=modelo2["modelo1"]).save()
