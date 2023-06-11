import pandas as pd
import numpy as np


class Datos:

    # Constructor: procesar el fichero para asignar correctamente las
    # variables nominalAtributos, datos y diccionario
    def __init__(self, nombreFichero):

        # Leemos el dataset pasado por argumentos, y lo canvertimos en un DataFrame
        # de pandas
        df = pd.read_csv(nombreFichero)
        # Listas de valores nominales (True), o numericos (False)
        self.nominalAtributos = []
        # conversor de datos nominales a datos numéricos
        self.diccionarios = {}
        # Array numpy de datos convertidos a forma numerica
        self.datos = np.array([])

        # Iterar por los atributos (incluye clase)
        for attr in df.columns:
            # Crear una entrada al diccionario para cada atributo
            self.diccionarios[attr] = {}
            # Se guarda si es atributo nominal o no, en el array nominalAtributos
            if df[attr].dtypes in [np.int64, np.float64]:
                self.nominalAtributos.append(False)
            elif isinstance(df[attr].dtypes, object):
                self.nominalAtributos.append(True)
                value = 1
                # Se iteran los datos únicos obtenidos para cada atributo nominal, de manera ordenanda
                for key in sorted(df[attr].unique()):

                    # Se crea una nueva entrada para cada dato nominal con el valor al cual se va a tranformar
                    self.diccionarios[attr][key] = value

                    value += 1
            else:
                raise ValueError(
                    "El valor del atributo debe ser nominal o continuo")

        # Con el metodo replace, reemplazara los datos de nuestro DataFrame con el mapeo de claves-valor realizadas en diccionarios
        df = df.replace(self.diccionarios)

        # Finalmente se pasa a array de numpy
        self.datos = df.to_numpy()

    def extraeDatos(self, idx):

        # Lista que almacenará los datos correspondientes a los indices pedidos
        listaDatos = []

        for i in idx:
            listaDatos.append(self.datos[i])

        return listaDatos
