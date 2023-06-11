from abc import ABCMeta, abstractmethod
from EstrategiaParticionado import ValidacionCruzada, ValidacionSimple


class Clasificador:

    # Clase abstracta
    __metaclass__ = ABCMeta

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

    # Obtiene el numero de aciertos y errores para calcular la tasa de fallo
    # TODO: implementar
    def error(self, datos, pred):
        # Aqui se compara la prediccion (pred) con las clases reales y se calcula el error

        numAciertos = 0
        numFallos = 0

        for i in range(0, len(pred)):
            if pred[i] == datos[i][len(datos[i]) - 1]:
                numAciertos += 1
            else:
                numFallos += 1

        return numFallos / (numFallos + numAciertos)

    # Realiza una clasificacion utilizando una estrategia de particionado determinada
    # TODO: implementar esta funcion
    def validacion(self, particionado, dataset, clasificador, seed=None):

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

        for i in range(0, numInteraciones):
            clasificador.entrenamiento(
                particionado.particiones[i].indicesTrain, dataset.nominalAtributos, dataset.diccionarios)
            clasificador.clasifica(
                particionado.particiones[i].indicesTest, dataset.nominalAtributos, dataset.diccionarios)
            error = clasificador.error(
                dataset.datos, particionado.particiones[i].indicesTest)

            listaErrores.append(error)

#####################################################################################################


class ClasificadorKNN(Clasificador):

    def __init__(self):
        super().__init__()

#####################################################################################################


class ClasificadorNaiveBayes(Clasificador):

    def __init__(self):
        super().__init__()
