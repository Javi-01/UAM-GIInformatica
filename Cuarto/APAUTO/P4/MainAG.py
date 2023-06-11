from Datos import Datos
import EstrategiaParticionado
from AlgoritmoGenetico import AlgoritmoGenetico


dataset = Datos('./titanic.csv')
# dataset = Datos('./xor.csv')
estrategia = EstrategiaParticionado.ValidacionSimple(
    numeroEjec=1, propTest=30)


clasificadorAG = AlgoritmoGenetico(
    tam_poblacion=100, num_generaciones=100, max_reglas=5, prob_mutacion=0.5, elitismo=5, logs=True)
clasificadorAG.definir_longitud_regla(dataset)

errores = clasificadorAG.validacion(estrategia, dataset)
print(errores)
