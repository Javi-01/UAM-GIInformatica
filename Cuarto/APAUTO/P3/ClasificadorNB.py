from Clasificador import Clasificador

# from scipy.stats import norm
import numpy as np


class ClasificadorNaiveBayes(Clasificador):

    def __init__(self, laplace=False) -> None:
        super().__init__(laplace)
    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Crea el modelo a partir de los datos de entrenamiento
    # datosTrain: matriz numpy con los datos de entrenamiento
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def entrenamiento(self, datosTrain, nominalAtributos, diccionario):
        self.prioris = {}  # P(H)
        self.verosimilitudes = {}  # P(Ai|H)
        attr_cont = 0

        # Se calculan las prioris, obteniedo los valores unicos de clase, y las ocurrencias de cada uno
        values_class, counts_class = np.unique(
            [fila[-1] for fila in datosTrain], return_counts=True)

        # Se guarda en el diccionario de prioris => por cada clase el numero de ocurrencias entre el total
        for class_, counts in zip(values_class, counts_class):
            self.prioris[class_] = counts/len(datosTrain)


        attr_cont = 0
        # Se calculan las verosimilitudes (Probabilidades condicionadas sobre la clase)
        for attr in diccionario.keys():
            # Si es la clase, se termina de iterar
            if (attr_cont == len(diccionario.keys()) - 1):
                break
            # Se crea una entrada para cada atributo
            self.verosimilitudes[attr] = {}
            # Crear una entrada de diccionario por cada valor de clase
            for class_key in self.prioris.keys():
                self.verosimilitudes[attr][class_key] = {}
                # Si la columna es de tipo nominal
            if nominalAtributos[attr_cont] == True:
                # Se itera por filas del dataset
                for fila in datosTrain:
                    # Se busca si existe ese valor nominal para el valor de clase de dicha fila
                    if fila[attr_cont] in self.verosimilitudes[attr][fila[len(fila) - 1]]:
                        self.verosimilitudes[attr][fila[len(
                            fila) - 1]][fila[attr_cont]] += 1
                    else:
                        self.verosimilitudes[attr][fila[len(
                            fila) - 1]][fila[attr_cont]] = 1

                # Se calcula la probabilidad de las ocurrencias sobre la clase
                for counts, class_key in enumerate(self.verosimilitudes[attr].keys()):
                    for value in self.verosimilitudes[attr][class_key].keys():
                        self.verosimilitudes[attr][class_key][value] /= counts

            # Columna de tipo categorico
            else:
                # Se iteran por las claves
                for class_key in self.verosimilitudes[attr].keys():
                    # Se sacan guardan los valores continuos que tienen la clase "class_key"
                    attr_values = [fila[attr_cont]
                                   for fila in datosTrain if fila[-1] == class_key]
                    # Se calcula la media y la desviacion tipica de los valores sobre el valor de clase
                    self.verosimilitudes[attr][class_key] = {
                        "desv": np.std(attr_values),
                        "mean": np.mean(attr_values)
                    }
            attr_cont += 1

    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Devuelve un numpy array con las predicciones
    # datosTest: matriz numpy con los datos de validaci�n
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def clasifica(self, datosTest, nominalAtributos, diccionario):

        self.posterioris = []  # P(H|Ai)
        cont_fila = 0

        # Se iteran por filas del dataset
        for fila in datosTest:
            # Tabla temporal que va a almacenar los valores posterioris para cada clase
            temp_posterioris = {}
            # Se iteran por atributos de clase
            for class_key in self.prioris.keys():

                cont_attr = 0
                posteriori_val = self.prioris[class_key]  # P(Hi)

                # Se itera ahora por todos los atributos
                for attr in diccionario.keys():
                    # Si el atributo es el de clase, finalizamos
                    if cont_attr == len(diccionario.keys()) - 1:
                        break

                    # P(H) = P(Hi) * P(A1|Hi) * P(A2|Hi) * .... * P(An|Hi)
                    # Para atributos nominales
                    if nominalAtributos[cont_attr] == True:
                        # Si es nominal, teniamos ya almacenada la verosimilitud
                        if fila[cont_attr] not in self.verosimilitudes[attr][class_key].keys():
                            posteriori_val *= 0.0
                        else:
                            posteriori_val *= self.verosimilitudes[attr][class_key][fila[cont_attr]]

                    # Para atributos continuos
                    else:
                        # Obtenemos el valor de media y desviacion tipica de el atributo
                        desv = self.verosimilitudes[attr][class_key]['desv']
                        mean = self.verosimilitudes[attr][class_key]['mean']


                        # Usamos la formula de los atributos continuos para obtener la posteriori
                        if desv == 0.0:
                            desv += 0.00000000001

                        base = 1 / (desv * np.sqrt(2 * np.pi))

                        exponent = np.e * -np.exp(
                            ((fila[cont_attr] - mean)* np.exp(2))
                            / (2 * (desv * np.exp(2))))
                        posteriori_val *= (base * exponent)

                    cont_attr += 1

                # Se guarda para cada clase, la probabilidad a posteriori
                temp_posterioris[class_key] = posteriori_val

            # Se guarda en el diccionario de posterioris, el numero de la fila, y la clase la cual tiene posteriori mas alta
            # print(temp_posterioris)
            self.posterioris.append(max(
                temp_posterioris, key=temp_posterioris.get))

            cont_fila += 1

        return self.posterioris
