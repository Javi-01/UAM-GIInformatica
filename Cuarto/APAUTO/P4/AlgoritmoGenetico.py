from Clasificador import Clasificador
import numpy as np
import copy
import random
import statistics
import pandas as pd


class AlgoritmoGenetico(Clasificador):

    def __init__(self, laplace=False, tam_poblacion=10, num_generaciones=10, max_reglas=5, prob_cruce=1, prob_mutacion=0.25, elitismo=10, logs=False) -> None:
        self.tam_poblacion = tam_poblacion
        self.num_generaciones = num_generaciones
        self.max_reglas = max_reglas
        self.prob_cruce = prob_cruce
        self.prob_mutacion = prob_mutacion
        self.elitismo = elitismo
        self.logs = logs

        self.fitness_medio = []
        self.fitness_mejor = []

        self.mejor_individuo = []

        self.poblacion_actual = [[] for _ in range(tam_poblacion)]

        super().__init__(laplace)

    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Crea el modelo a partir de los datos de entrenamiento
    # datosTrain: matriz numpy con los datos de entrenamiento
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def entrenamiento(self, datosTrain, nominalAtributos, diccionario):
        # Funcion que genera la poblacion inicial de manera aleatoria con
        # conjunto de reglas tambien aleatorias
        self.generar_poblacion_inicial()

        # Generamos ya todos los individuos de datos train para que no tenga en
        # cada generacion que generarlos de nuevo
        datos = [self.generar_individuo(fila) for fila in datosTrain]

        # Se itera entre el numero de generaciones que queremos crear
        for _ in range(self.num_generaciones):

            nueva_generacion = []
            # Se itera sobre la poblacion actual
            for index, individuo in enumerate(self.poblacion_actual):
                # Se obtienen las comparaciones de todos los datos de train sobre un
                # individuo
                comparaciones = [self.aciertos_fitness(
                    fila, individuo) for fila in datos]

                # Se obtiene el numero de errores y aciertos
                errores, num_errores = np.unique(
                    comparaciones, return_counts=True)

                # Se llama a la funcion de calcular fitness, y se almacena con el individuo
                nueva_generacion.append((
                    self.poblacion_actual[index],
                    self.fitness(list(errores), list(num_errores))
                ))

            # Se ordenan los puntos
            nueva_generacion = self.seleccion_elite(nueva_generacion)
            n_g = copy.deepcopy(nueva_generacion)
            # Se cruzan y se mutan los puntos
            nueva_generacion = self.cruzar_individuos(nueva_generacion)
            self.poblacion_actual = self.mutar_individuos(nueva_generacion)

            # Se añade fitness medio y mejor
            self.fitness_medio.append(np.mean([val[1] for val in n_g]))
            self.fitness_mejor.append(self.mejor_individuo[1])

        if self.logs:
            print("Mejor individuo: ", self.mejor_individuo[0])
            print("Fitness del mejor individuo: ", self.mejor_individuo[1])


    # TODO: esta funcion debe ser implementada en cada clasificador concreto. Devuelve un numpy array con las predicciones
    # datosTest: matriz numpy con los datos de validaci�n
    # nominalAtributos: array bool con la indicatriz de los atributos nominales
    # diccionario: array de diccionarios de la estructura Datos utilizados para la codificacion de variables discretas

    def clasifica(self, datosTest, nominalAtributos, diccionario):

        # Generamos todos los individuos de test en forma de bits
        datos = [self.generar_individuo(fila) for fila in datosTest]

        # Se realizan las comparaciones sobre la regla del mejor individuo
        predicciones = [
            self.aciertos_fitness(fila, self.mejor_individuo[0], predecir=True) for fila in datos]

        # Se obtiene el numero de errores y aciertos
        return predicciones
    # Funcion que se encarga de generar las reglas de tamaño aleatorio

    def generar_poblacion_inicial(self):

        for individuo in range(self.tam_poblacion):
            self.poblacion_actual[individuo] += self.generar_individuo()

    # Operacion de cruce de tipo intra, la cual intercambia el material genetico parcial de una de las reglas del individuo1,
    # con el material genetico parcial de la regla 2
    def operacion_genetica_cruce(self, individuo1: list, individuo2: list):
        i1 = copy.deepcopy(individuo1)
        i2 = copy.deepcopy(individuo2)
        # Se elige una regla de manera aleatoria que es la que vamos a intercambiar de cada individuo
        # en el caso de que solo tenga una regla, solo podemos coger esa
        regla_cruzar_i1 = i1[random.randint(0, len(i1) - 1)] if len(
            i1) > 1 else i1[0]
        regla_cruzar_i2 = i2[random.randint(0, len(i2) - 1)] if len(
            i2) > 1 else i2[0]

        # Se guarda la posicion de la regla a intercambiar
        indice_regla_i1 = i1.index(regla_cruzar_i1)
        indice_regla_i2 = i2.index(regla_cruzar_i2)

        # Si las longitudes de la regla no son iguales ==> Error
        if len(regla_cruzar_i1) != len(regla_cruzar_i2):
            raise ValueError("Los individuos deben tener la misma longitud")

        idx = random.randint(0, len(regla_cruzar_i1) - 1)

        # Se intercambian las reglas, y estos dos individuos se devuelven como descendientes
        i1[indice_regla_i1][idx:], i2[indice_regla_i2][idx:] = regla_cruzar_i2[idx:], regla_cruzar_i1[idx:]

        return i1, i2

    # Mutcion estandar aplicada a una regla
    def operacion_genetica_mutacion(self, individuo: list, prob_mutacion: float = 0.1):
        i = copy.deepcopy(individuo)

        if len(i) < 1:
            raise ValueError("Los individuos deben tener reglas")

        # Se elige la regla a mutar
        regla_mutar = i[random.randint(0, len(i) - 1)] if len(
            i) > 1 else i[0]

        indice_regla_mutar = i.index(regla_mutar)

        # Por cada bit de la regla, si la porbabilidad de mutacion(pasada por parametros), es mayor, entonces
        # se intercambia el bit, esto se consigue haciendo el valor absoluto de la resta, es lo mismo que
        # cambiar el bit de 0 a 1 o viceversa
        for bit in range(len(regla_mutar)):
            regla_mutar[bit] = regla_mutar[bit] if random.random(
            ) > prob_mutacion else abs(regla_mutar[bit] - 1)

        # Se guarda de nuevo la regla mutada
        i[indice_regla_mutar] = regla_mutar

        return i

    # Funcion que se encarga de generar un individuo, dependiendo de si este recibe
    # la informacion de la fila a tranformar, o no, en cuyo caso directamente se genera
    # de manera aleatoria
    def generar_individuo(self, fila: list = None):
        if fila is None:
            # Si es de la generacion inicial se crea una cadena de tantos bits randoms como longitud de regla,
            # y de tantas reglas aleatorias hasta max_reglas se expecifiquen
            return [random.choices([0, 1], k=self.longitud_regla) for _ in range(random.randint(1, self.max_reglas))]

        # En caso de que si que recibamos la fila, es transformar la fila a cadena de bits
        individuo = []
        for attr_val, attr_long in zip(fila, self.longitudes_attrs):
            individuo += [0 if attr_val !=
                          val else 1 for val in attr_long]

        # Finalmente se añade la clase, que es un bit unico
        individuo += [0 if fila[-1] == 0 else 1]
        return individuo

    # Funcion que se encarga de obtener si nuestro valor de train/test coincide
    # con alguna de las reglas del individuo
    def aciertos_fitness(self, fila: list, individuo: list[list[int]], predecir=False):
        if len(individuo) < 1:
            raise ValueError("Los individuos deben tener reglas")

        aciertos = 0
        errores = 0
        predicciones = []
        # Se iteran por las reglas del individuo
        for regla in individuo:
            if len(regla) != len(fila):
                raise ValueError(
                    "Longitudes distintas entre el individuo y la fila de train")

            len_acumulada = 0
            # Se crea un array de activaciones inicializados a False, de tantas
            # posiciones como atributos, de manera que si se activa, cambiamos
            # a True
            activaciones = [False for _ in range(len(self.longitudes_attrs))]

            # Se recorren los atributos
            for attr_idx, attr in enumerate(self.longitudes_attrs):
                # Se recorren los bits de las reglas, en funcion de la longitud
                # acumulada, y la longitud de ese atributo en cuestion
                for bit_regla, bit_fila in zip(regla[len_acumulada:len(attr) + len_acumulada], fila[len_acumulada:len(attr) + len_acumulada]):
                    # Si ambos la operacion logica AND es 1, significa que ambos
                    # estan activos, con lo cual se activa
                    if bit_fila & bit_regla == 1:
                        activaciones[attr_idx] = True
                        break

                # Se actualiza la longitud acumulada de cada atributo
                len_acumulada += len(attr)

            # Se comprueba ahora las activaciones, si np hay un falso, significa
            # que todos los atributos se han activado, con lo cual, comparamos
            # las clases
            if False not in activaciones:
                # Esto es para train
                if fila[-1] == regla[-1]:
                    aciertos += 1
                else:
                    errores += 1

                # Para test, metemos las
                # clases correspondientes a las reglas que se activan con
                # la fila de train, y esta sera la prediccion que posteriormente
                # se compara con la clase real de la fila de test para sacar
                # la conclusion
                if predecir:
                    predicciones.append(regla[-1])
        # Gracias a la funcion mode, nos devuelve la moda (valor mas frecuente),
        # en nuestro caso, clase mas frecuente. Pero si hay empates, se devuelve
        # la de la primera regla activada
        if predecir:
            try:
                return statistics.mode(predicciones)
            except:
                return -1

        # Si hay un acierto, si que esta bien, porque hay solo una regla que
        # coincide, se devuelve 1, el otro caso que quede es que ninguna regla
        # haya coincidido, en cuyo caso, se devuelve 0
        return 1 if aciertos > errores else 0

    # Lista con los valores 1, 0, y -1 (de las comprobaciones de aciertos)
    # y sus respectivas cuentas, y devuelve el % fitness de acierto
    def fitness(self, errores: list, conts_errores: list):
        # Comprueba que haya al menos 1 error / acierto
        aciertos = conts_errores[errores.index(
            1)] if 1 in errores else 0.0

        fallos = conts_errores[errores.index(
            0)] if 0 in errores else 0.0

        return aciertos / (fallos + aciertos)

    # Se ordenan los individuos por el fitness, y de esa manera nos quedamos a los
    # mejores, que pasan directamente a la siguiente generacion, y de esta manera,
    # nuestro objetivo es mejorarlos
    def seleccion_elite(self, poblacion: list):
        # Se ordenan los individuos por el fitness
        nueva_generacion = sorted(
            poblacion, key=self.get_fitness, reverse=True)
        self.mejor_individuo = nueva_generacion[0]
        return nueva_generacion

    # Funcion para obtener el fitness del individuo, util para cuando queremos
    # ordenar los individuos por esta clave
    def get_fitness(self, individuo):
        return individuo[1]

    # Esta funcion se encarga de seleccionar individuos para cruzar/mutar por el
    # mecanismo de la ruleta. Esto es: Cada individuo con su fitness, tiene una
    # opcion que es proporcional a ese fitness de ser elegido, de manera que los
    # mejores individuos (mayor fitness), tienen mas probabilidad de salir
    def seleccion_individuos_ruleta(self, poblacion: list, cruce=False):

        # Primero se saca la suma de todos los fitness, esto para sacar que
        # porcentaje de este total va a ocupar cada individuo en la ruleta
        fitness_total = sum(individuo[1] for individuo in poblacion)

        if (fitness_total == 0.0):
            # Ahora generamos la ruleta, que consta de todos los fitness/total
            ruleta = [0.0 for _ in poblacion]
            individuos = [individuo[0] for individuo in poblacion]
        else:
            # Ahora generamos la ruleta, que consta de todos los fitness/total
            ruleta = [individuo[1]*100 /
                      fitness_total for individuo in poblacion]
            individuos = [individuo[0] for individuo in poblacion]

        # Si es operacion de cruce
        if cruce:
            # Si no dos individuos (o mas) con fitness > 0 ==> Se eligen dos al azar
            if len(set(ruleta)) < 2:
                return random.choices(individuos, k=2)
            # Se eligen dos valores de individuos, estableciendo los pesos relativos
            # para cada individuo

            return random.choices(individuos, weights=ruleta, k=2)

        # Si es operacion de mutacion, se elige solo el individuo a mutar, que es el
        # caso de que no hay individuos con fitness > 0 ==> Se elige al azar
        if len(set(ruleta)) < 1:
            return random.choices(individuos, k=1)

        return random.choices(individuos, weights=ruleta, k=1)

    # Funcion que se encarga de para una generacion pasada, hacer los correspondientes
    # cruces en funcion de que porcentaje se quiere cruzar de poblacion, y seleccion
    # por ruleta se realice
    def cruzar_individuos(self, poblacion: list):

        nueva_poblacion = [i[0] for i in poblacion]

        # Se itera por la longitud de la poblacion
        for i in range(self.elitismo, len(nueva_poblacion), 2):
            if random.random() <= self.prob_cruce:
                # Se selecciona dos individuos por ruleta de la poblacion
                individuos = self.seleccion_individuos_ruleta(poblacion, True)
                # Se realiza la operacion de cruce entre los individuos por ruleta
                desc_1, desc_2 = self.operacion_genetica_cruce(
                    individuos[0], individuos[1])
                # Se añaden los dos descendientes a la generacion cruzada
                nueva_poblacion[i] = desc_1
                if (i + 1 < len(nueva_poblacion)):
                    nueva_poblacion[i + 1] = desc_2

        return nueva_poblacion

    # Funcion que se encarga de para una poblacion pasada, hacer las correspondientes
    # mutaciones en funcion de que porcentaje (random) se quiere mutar de poblacion
    def mutar_individuos(self, poblacion: list):

        # Se itera por los individuos de la poblacion actual
        for i in range(self.elitismo, len(poblacion)):
            # Si cumplimos la probabilidad de mutar, mutamos
            if random.random() <= self.prob_mutacion:
                # Se genera el nuevo individuo realizando la operacion de mutacion
                descendiente = self.operacion_genetica_mutacion(poblacion[i])
                # Se añaden los dos descendientes a la generacion cruzada
                poblacion[i] = descendiente

        return poblacion

    # Funcion que sirve para definir la longitud total de la regla y las longitudes
    # y la longitud de cada uno de los atributos
    def definir_longitud_regla(self, datos):
        data = pd.DataFrame(datos.datos)
        self.longitudes_attrs = [sorted(np.unique(data[attr]))
                                 for attr in data.columns[:-1]]

        self.longitud_regla = sum(len(val)
                                  for val in self.longitudes_attrs) + 1
