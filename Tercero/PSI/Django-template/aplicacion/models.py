from django.db import models
from django.urls import reverse


class Modelo1(models.Model):
    nombre1 = models.CharField(max_length=255)

    def __str__(self) -> str:
        return str(self.id) + " " + self.nombre1

    def get_abosolute_url(self):
        return reverse("get_id", args=[str(self.id)])


class Modelo2(models.Model):
    modelo1 = models.ForeignKey(
        Modelo1, on_delete=models.SET_NULL, null=True)
    nombre = models.CharField(max_length=255)

    def __str__(self) -> str:
        return self.nombre
