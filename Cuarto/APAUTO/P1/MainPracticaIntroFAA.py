from Datos import Datos
import EstrategiaParticionado
import ClasificadorNB

dataset = Datos('./german.csv')
estrategia = EstrategiaParticionado.ValidacionSimple(numeroEjec=5, propTest=30)
clasificador = ClasificadorNB.ClasificadorNaiveBayes(laplace=False)
clasificador_lapce = ClasificadorNB.ClasificadorNaiveBayes(laplace=True)
errores = clasificador.validacion(estrategia, dataset)
errores_lp = clasificador_lapce.validacion(estrategia, dataset)
print(errores, errores_lp)
