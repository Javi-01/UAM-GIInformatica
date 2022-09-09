from django.test import TestCase
from django.urls import reverse
from .models import Modelo1, Modelo2


class TweetTest(TestCase):

    def setUp(self):

        Modelo1.objects.all().delete()
        Modelo2.objects.all().delete()

    def create_check(self, dictionary, ObjectClass):
        """ create an object of the class 'ObjectClass'
        using the dictionary. Then,
        check that all key-values in the
        dictionary are attributes in the object.
        return created object of class Object
        """
        # check that str function exists
        self.assertTrue(ObjectClass.__str__ is not object.__str__)
        # create object
        item = ObjectClass.objects.create(**dictionary)
        for key, value in dictionary.items():
            self.assertEqual(getattr(item, key), value)
        # execute __str__() so all the code in models.py is checked
        item.__str__()
        return item

    @classmethod
    def decode(cls, txt):
        return txt.decode("utf-8")

    def test_01_caso_error(self):
        modelos1 = [
            {
                "id": 1000,
                "nombre1": "nombre1"
            },
            {
                "id": 1001,
                "nombre1": "nombre2"
            }
        ]
        for modelo1 in modelos1:
            Modelo1(id=modelo1["id"], nombre1=modelo1["nombre1"]).save()

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
            }
        ]

        for modelo2 in modelos2:
            Modelo2(id=modelo2["id"], nombre=modelo2["nombre"],
                    modelo1=modelo2["modelo1"]).save()

        # Se obtiene la url cuya ruta viene dada por el atributo del path "name"
        response = self.client.get(
            reverse('index'), follow=True)
        response_txt = self.decode(response.content)

        #
        self.assertIn(
            "El usuario no tiene ningun tweet asociado", response_txt)

    def test_02_caso_ok(self):

        modelos1 = [
            {
                "id": 1000,
                "nombre1": "nombre1"
            },
            {
                "id": 1001,
                "nombre1": "nombre2"
            }
        ]
        for modelo1 in modelos1:
            Modelo1(id=modelo1["id"], nombre1=modelo1["nombre1"]).save()

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
            }
        ]

        for modelo2 in modelos2:
            Modelo2(id=modelo2["id"], nombre=modelo2["nombre"],
                    modelo1=modelo2["modelo1"]).save()

        # Se obtiene la url cuya ruta viene dada por el atributo del path "name"
        response = self.client.get(
            reverse('index'), follow=True)
        response_txt = self.decode(response.content)

        #
        self.assertIn("Esto", response_txt)
