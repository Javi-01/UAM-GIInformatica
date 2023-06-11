from Clasificador import Clasificador
import numpy as np
import math
import random
import pandas as pd


class ClasificadorRegresionLogistica(Clasificador):

    def __init__(self, normalizar=False, laplace=False, learning_rate=1, n_epocs=1000) -> None:
        self.normalizar = normalizar
        self.lr = learning_rate
        self.n_epocs = n_epocs
        self.weights = None
        self.predictions = []
        self.actuals = []
        self.C1 = None
        self.C2 = None

        super().__init__(laplace)

    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Crea el modelo a partir de los datos de entrenamiento
    # datosTrain: matriz numpy con los datos de entrenamiento
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def entrenamiento(self, datosTrain, nominalAtributos, diccionario):

        # Se convierte a un dataframe de pandas
        df_train = pd.DataFrame(datosTrain)

        # Se manda a normalizar
        if self.normalizar:
            self.normalizarDatos(df_train, nominalAtributos)

        # Se guarda el train normalizado
        self.X_train = df_train.iloc[:, :-1].to_numpy()
        self.y_train = df_train.iloc[:, -1].to_numpy()
        self.C1, self.C2 = np.unique(self.y_train)

        # Se inicializa un vector w0 de numeros aleatorios entr 0 y 1
        self.weights = [random.random() for _ in df_train.columns]

        # Para un numero N de epocas
        for _ in range(self.n_epocs):
            # Por cada fila de train
            for x_train, y_train in zip(self.X_train, self.y_train):
                # Añadir el sesgo (x0 = 1)
                x_train = np.concatenate(([1], x_train))
                # calcular la prediccion con la funcion sigmoidal
                y = self.sigmoid_function(
                    self.escalar_product(x_train, self.weights))
                # actualizar el valor de w

                self.weights = np.subtract(
                    self.weights, [self.lr * (y - y_train) * i for i in x_train])


    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Devuelve un numpy array con las predicciones
    # datosTest: matriz numpy con los datos de validaci�n
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def clasifica(self, datosTest, nominalAtributos, diccionario):

        # Se convierte a un dataframe de pandas
        df_test = pd.DataFrame(datosTest)

        # Se manda a normalizar
        if self.normalizar:
            self.normalizarDatos(df_test, nominalAtributos)

        # Se guarda el test normalizado
        X_test = df_test.iloc[:, :-1].to_numpy()
        y_test = df_test.iloc[:, -1].to_numpy()

        predictions = []

        # Se iteran las filas de test
        for x_test, y_t in zip(X_test, y_test):
            # Se añade el sesgo a la columna (x0)
            x_test = np.concatenate(([1], x_test))

            # Se calcula el producto escalar con el vector de pesos entrenado, y  se pasa por la
            # la funcion sigmoide para determinar la clase
            y = self.sigmoid_function(
                self.escalar_product(x_test, self.weights))

            self.predictions.append(y)
            self.actuals.append(y_t)
            predictions.append(self.C2 if y > 0.5 else self.C1)

        return predictions

    def actuales(self):
        return self.actuals, self.C1, self.C2

    def predicciones(self):
        return self.predictions

    def calcularMediasDesv(self, datos, nominalAtributos):
        mean_desv = []
        attr_cont = 0

        # Para cada columna (menos la clase)
        for attr in datos.columns[:-1]:
            # Si es categorico
            if nominalAtributos[attr_cont] == False:
                # Se guarda la media y la desviacion tipica
                mean_desv.append((np.mean(datos[attr]), np.std(datos[attr])))
            else:
                # Si no se guarda un 0
                mean_desv.append((0.0, 0.0))

            attr_cont += 1

        return mean_desv

    def normalizarDatos(self, datos, nominalAtributos):

        # Se llama a calcular las medias
        means_desv = self.calcularMediasDesv(datos, nominalAtributos)

        # Se recorre al mismo tiempo los dos arrays (columnas, y media_desviacion)
        for attr, mean_desv in zip(datos.columns[:-1], means_desv):
            datos[attr] = [(value - mean_desv[0]) / mean_desv[1]
                           for value in datos[attr]]

    # Funcion auxiliar de producto escalar con o sin sesgo
    def escalar_product(self, v1, v2):
        return np.dot(v1, v2)

    # Funcion auxiliar de producto escalar con o sin sesgo

    def sigmoid_function(self, z):
        try:
            z = 1/(1 + math.exp(-z))
        except:
            z = 0
        finally:
            return z
