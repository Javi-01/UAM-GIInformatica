from Clasificador import Clasificador
import numpy as np
import pandas as pd
from collections import Counter


class ClasificadorKNN(Clasificador):

    def __init__(self, normalizar=False, K=1, laplace=False) -> None:
        self.K = K
        self.normalizar = normalizar
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
        self.X_train = df_train.to_numpy()
        self.y_train = df_train.iloc[:, -1].to_numpy()

    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Devuelve un numpy array con las predicciones
    # datosTest: matriz numpy con los datos de validaci�n
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def clasifica(self, datosTest, nominalAtributos, diccionario):

        # Dataframe de pandas para la seccion de test
        df_test = pd.DataFrame(datosTest)

        # Se normalizan los datos y se guarda en X_test
        if self.normalizar:
            self.normalizarDatos(df_test, nominalAtributos)

        X_test = df_test.to_numpy()

        predictions = []

        # Por cada fila del test
        for val_test in X_test:
            # Se calcula la distancia euclidea de cada fila de test sobre todas los de train
            # y se guarda en un array de tantas filas como el train tenga
            distances = [self.calculateEuclideanDistance(
                val_test, x_train) for x_train in self.X_train]

            # Se obtienen los K mas cercanos (primero obteniendo los K menores indices)
            # y luego el valor de la clase para ese indice (en la lista de y_train)
            # ya que estos deben coincidir en posicion
            k_indexes = np.argsort(distances)[:self.K]

            k_nearest_classes = self.y_train[k_indexes]

            # Finalmente, se obtiene el valor de la clase mas comun para esta fila de test
            # Para ello Counter nos devuelve el mas comun (1) especificando solo 1 (el mas comun)
            predictions.append(Counter(k_nearest_classes).most_common(1)[0][0])

        print(predictions)
        return predictions

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

    def calculateEuclideanDistance(self, x1, x2):
        # Calcula la distancia euclidea de dos valores
        return np.sqrt(np.sum((x1 - x2) ** 2))
