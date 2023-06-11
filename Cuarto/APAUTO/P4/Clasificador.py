from abc import ABCMeta, abstractmethod
from EstrategiaParticionado import ValidacionCruzada, ValidacionSimple
import pandas as pd
import numpy as np


class Clasificador:
    # Clase abstracta
    __metaclass__ = ABCMeta

    def __init__(self, laplace) -> None:
        self.laplace = laplace
    # Metodos abstractos que se implementan en casa clasificador concreto

    @abstractmethod
    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Crea el modelo a partir de los datos de entrenamiento
    # datosTrain: matriz numpy con los datos de entrenamiento
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas
    def entrenamiento(self, datosTrain, nominalAtributos, diccionario):
        pass

    @abstractmethod
    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Devuelve un numpy array con las predicciones
    # datosTest: matriz numpy con los datos de validaci�n
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas
    def clasifica(self, datosTest, nominalAtributos, diccionario):
        pass

    @abstractmethod
    def puntuaciones(self):
        pass

    # Obtiene el numero de aciertos y errores para calcular la tasa de fallo
    # TODO: implementar
    def error(self, datos, pred):
        # Aqui se compara la prediccion (pred) con las clases reales y se calcula el error

        numAciertos = 0
        numFallos = 0

        for actual, predicted in zip(datos, pred):
            if actual[-1] == predicted:
                numAciertos += 1
            else:
                numFallos += 1

        return numFallos / (numFallos + numAciertos)

    def clasificaciones(self, datos, pred):
        # Aqui se compara la prediccion (pred) con las clases reales y se calcula el error
        TP, FN, FP, TN = 0, 0, 0, 0
        C1, C2 = np.unique([dato[-1] for dato in datos])

        for actual, predicted in zip(datos, pred):
            if actual[-1] == predicted:
                if (actual[-1] == C2):
                    TP += 1
                else:
                    TN += 1
            else:
                if (actual[-1] == C2 and predicted == C1):
                    FN += 1
                else:
                    FP += 1

        return {"TPR": TP/(TP+FN),
                "FNR": FN/(TP+FN),
                "FPR": FP/(FP+TN),
                "TNR": TN/(FP+TN)
                }
    # Realiza una clasificacion utilizando una estrategia de particionado determinada
    # TODO: implementar esta funcion

    def validacion(self, particionado, dataset, seed=None):

        listaErrores = []

        # Creamos las particiones siguiendo la estrategia llamando a particionado.creaParticiones
        particionado.creaParticiones(dataset, seed)

        # - Para validacion cruzada: en el bucle hasta nv entrenamos el clasificador con la particion de train i
        # y obtenemos el error en la particion de test i
        numInteraciones = 0
        if isinstance(particionado, ValidacionCruzada):
            numInteraciones += particionado.numeroParticiones

        # - Para validacion simple (hold-out): entrenamos el clasificador con la particion de train
        # y obtenemos el error en la particion test. Otra opci�n es repetir la validaci�n simple un n�mero
        # especificado de veces, obteniendo en cada una un error. Finalmente se calcular�a la media.
        if isinstance(particionado, ValidacionSimple):
            numInteraciones += particionado.numeroEjecuciones

        # Correccion de lapace sobre el dataset
        if self.laplace:
            attr_count = 0
            df = pd.DataFrame(dataset.datos)
            for attr in df.columns[0: len(df.columns)-1]:
                if dataset.nominalAtributos[attr_count] == False:
                    for i in range(0, len(df[attr])):
                        df[attr][i] += 1
                attr_count += 1

            dataset.datos = df.to_numpy()

        for i in range(0, numInteraciones):
            self.entrenamiento(
                dataset.extraeDatos(particionado.particiones[i].indicesTrain), dataset.nominalAtributos, dataset.diccionarios)
            prediciones = self.clasifica(
                dataset.extraeDatos(particionado.particiones[i].indicesTest), dataset.nominalAtributos, dataset.diccionarios)

            error = self.error(
                dataset.extraeDatos(particionado.particiones[i].indicesTest), prediciones)

            listaErrores.append(error)

        return np.mean(listaErrores)
