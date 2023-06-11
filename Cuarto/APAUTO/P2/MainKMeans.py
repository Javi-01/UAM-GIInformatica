from Datos import Datos
from ClusteringKMeans import ClusteringKMeans

K = 5
dataset = Datos('./iris.csv')
kmeans = ClusteringKMeans(K=K)

rets = kmeans.predict(dataset.datos)
print(rets)
kmeans.get_confusion_matrix()
