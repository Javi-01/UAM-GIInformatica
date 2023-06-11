from Datos import Datos
import EstrategiaParticionado
from ClasificadorKNN import ClasificadorKNN


dataset = Datos('./wdbc.csv')
estrategia = EstrategiaParticionado.ValidacionSimple(numeroEjec=1, propTest=30)
clasificador = ClasificadorKNN(normalizar=True, K=3)
errores = clasificador.validacion(estrategia, dataset)
print(errores)
