from Datos import Datos
import EstrategiaParticionado
import Clasificador

dataset = Datos('./tic-tac-toe.csv')
estrategia = EstrategiaParticionado.ValidacionCruzada(100)
clasificador = Clasificador.ClasificadorNaiveBayes()
# errores = clasificador.validacion(estrategia, dataset, clasificador)
