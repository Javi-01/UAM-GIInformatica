from abc import ABCMeta, abstractmethod
import random
import math


class Particion():

    # Esta clase mantiene la lista de �ndices de Train y Test para cada partici�n del conjunto de particiones
    def __init__(self):
        self.indicesTrain = []
        self.indicesTest = []

#####################################################################################################


class EstrategiaParticionado:

    # Clase abstracta
    __metaclass__ = ABCMeta

    # Atributos: deben rellenarse adecuadamente para cada estrategia concreta. Se pasan en el constructor
    def __init__(self):
        self.particiones = []

    @abstractmethod
    # TODO: esta funcion deben ser implementadas en cada estrategia concreta
    def creaParticiones(self, datos, seed=None):
        pass


#####################################################################################################

class ValidacionSimple(EstrategiaParticionado):

    def __init__(self, numeroEjec, propTest):
        self.numeroEjecuciones = numeroEjec
        self.proporcionTest = propTest
        super().__init__()

    # Crea particiones segun el metodo tradicional de division de los datos segun el porcentaje deseado y el n�mero de ejecuciones deseado
    # Devuelve una lista de particiones (clase Particion)
    # TODO: implementar
    def creaParticiones(self, datos, seed=None):
        # Se obtiene el numero de filas y se crea una lista con tantos indices como elementos haya en la lista
        filas = len(datos.datos)
        indices = [i for i in range(0, filas)]

        # Se iteran entre el numero de ejecuciones que se quieren realizar
        for _ in range(0, self.numeroEjecuciones):
            # Se hace un mezclado de las filas (indices) del dataset
            random.shuffle(indices)
            # Se crea un nuevo objeto de particionado
            part = Particion()
            # Se añaden los indices de test e indices de entrenamiento en funcion de la proprcion de Test
            part.indicesTrain.extend(indices[0: self.proporcionTest - 1])
            part.indicesTest.extend(indices[self.proporcionTest: filas - 1])
            # Se añade a la lista de particiones, la nueva particion creada
            self.particiones.append(part)


#####################################################################################################
class ValidacionCruzada(EstrategiaParticionado):

    def __init__(self, numParticiones):
        self.numeroParticiones = numParticiones
        super().__init__()

    # Crea particiones segun el metodo de validacion cruzada.
    # El conjunto de entrenamiento se crea con las nfolds-1 particiones y el de test con la particion restante
    # Esta funcion devuelve una lista de particiones (clase Particion)
    # TODO: implementar

    def creaParticiones(self, datos, seed=None):
        # Se obtiene el numero de filas y se calcula cuanto es el tamaño de particion, dependiendo de cuantas queremos
        filas = len(datos.datos)
        indices = [i for i in range(0, filas)]
        tamParticion = math.ceil(filas / self.numeroParticiones)

        # Se itera entre el numero de particiones
        for i in range(0, self.numeroParticiones):

            # Se crea una nueva particione
            part = Particion()
            # La parte de test la sacamos desde el indice que estamos hasta el indice + 1
            # por el tamaño de la particion
            part.indicesTest.extend(indices[tamParticion *
                                            i: tamParticion * (i + 1)])
            # La parte de train,se divide en la parte anterior y posterior del train
            part.indicesTrain.extend(indices[0: tamParticion * i])
            part.indicesTrain.extend(indices[tamParticion * (i + 1):])

            # Se añade a la lista de particiones
            self.particiones.append(part)
