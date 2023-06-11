from Datos import Datos
import EstrategiaParticionado
from ClasificadorRegresionLogistica import ClasificadorRegresionLogistica
from ClasificadorNB import ClasificadorNaiveBayes


# dataset = Datos('./pima-indians-diabetes.csv')
dataset = Datos('./wdbc.csv')
estrategia = EstrategiaParticionado.ValidacionSimple(
    numeroEjec=100, propTest=30)
clasificadorRL = ClasificadorRegresionLogistica(
    normalizar=True, learning_const=1, n_epocs=100)
errores, errors_matrix = clasificadorRL.validacion(estrategia, dataset)
print(errores, errors_matrix)

clasificadorNB = ClasificadorNaiveBayes(laplace=True)

errores, errors_matrix =clasificadorNB.validacion(estrategia, dataset)
print(errores, errors_matrix)