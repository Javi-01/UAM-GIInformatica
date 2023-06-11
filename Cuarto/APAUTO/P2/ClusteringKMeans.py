import numpy as np
import matplotlib.pyplot as plt
from sklearn import metrics
from collections import Counter


class ClusteringKMeans():

    def __init__(self, K=1):
        self.K = K
        # Crear K listas para cada cluster
        self.clusters = [[] for _ in range(self.K)]
        # Guardar los k centroides
        self.centroids = []

    # Funcion para prededir el resultado, para ello, se le pasa el dataset

    def predict(self, dataset):
        # Se guardan los datos  sin la clase y la clase
        self.X = dataset[:, :-1]
        self.y = dataset[:, -1]

        # Se cuarda el numero de filas y columnas
        self.n_rows, self.n_cols = self.X.shape

        # Inicializar los centorides
        # Se obtienen k indices no repetidos (replace=False), para el numero de filas del dataset
        centroid_idxs = np.random.choice(self.n_rows, self.K, replace=False)
        self.centroids = [self.X[idx] for idx in centroid_idxs]

        # Iterar
        for _ in range(10000):
            # Actualizar los clusters (listas)
            self.clusters = self.clusters_assigment(self.centroids)
            # Recalcular los centroides
            old_centroids = self.centroids
            self.centroids = self.recalculate_centroids(self.clusters)
            # Si el criterio se cumple, se termina
            if self.converge_criterion(old_centroids, self.centroids):
                break

        # Devuelve para cada cluster la clase de los centroides
        predictions = []
        for cluster in self.clusters:
            predictions += [dict(Counter(self.y[cluster]))]

        return predictions

    # Obtener la matriz de confusion
    def get_confusion_matrix(self):
        classes = []

        # Se obtienen los clusters con pares de clave-valor, con la clase actual y la predicha
        classes_and_redictions = self.get_clusters_classes(self.clusters)
        for i in range(len(classes_and_redictions)):
            classes += classes_and_redictions[i]

        # Se itera entre las predicciones, y se obtiene las listas de predicciones y actuales
        predicts = [ele[0] for ele in classes]
        actuals = [ele[1] for ele in classes]

        confusion_matrix = metrics.confusion_matrix(actuals, predicts)

        cm_display = metrics.ConfusionMatrixDisplay(
            confusion_matrix=confusion_matrix, display_labels=[1, 2, 3])

        cm_display.plot()
        plt.show()
        return

  # Funcion que se encarga de iterar entre los puntos, y añadirlos al cluster correspondiente
  # en funcion de su distancia euclidea frente a los centroides

    def clusters_assigment(self, centroids):
        # Creamos k clusters auxiliares
        clusters = [[] for _ in range(self.K)]
        row_idx = 0

        # Iteramos entre el dataset
        for row in self.X:
            # Se calculan las distancias euclideas de nuestra fila, al centroide
            distances = [self.calculate_euclidean_dist(
                row, centroid) for centroid in centroids]
            # Se obtiene el indice del centroide con menor distancia
            closest_idx_centroid = np.argmin(distances)
            # Se guarda en la lista de ese centroide mas cercano, el indice de la fila
            clusters[closest_idx_centroid].append(row_idx)
            row_idx += 1

        return clusters

    # Funcion que se encarga de recalcular los centroides, para ello, se obtiene de cada cluster,
    # (el cual contiente k listas con los indices calculados anteriormente para cada uno) el centro
    # de masas, que no deja de ser la media ponderada de todas las columnas de cada cluster
    def recalculate_centroids(self, clusters):
        # Creamos K arrays auxiliares del numero de columnas del dataset cada uno
        centroids = np.zeros((self.K, self.n_cols))

        idx_cluster = 0
        # Se iteran por los cluster (se obtiene una lista de indices por iteracion)
        for cluster in clusters:

            # se guarda en el centroide, la media de las filas (axis=0) indica la fila
            # obteniendo primero los elementos en el dataset sabiendo los indices(cluster)
            centroid = np.mean(self.X[cluster], axis=0)
            # Se guarda el nuevo centroide en la posicion correspondiente
            centroids[idx_cluster] = centroid
            idx_cluster += 1

        return centroids

    # Funcion que comprueba si se ha llegado al criterio de convergencia, esto es que
    # la distancia minima de la suma de los centroides anteriores con los nuevos sea
    # muy pequeña o nula
    def converge_criterion(self, old_centroid, centroids):
        # Se calculan las disntacias por centroide
        distances = [self.calculate_euclidean_dist(
            old_centroid[i], centroids[i]) for i in range(self.K)]

        # Si la suma de las distancias es 0, se devuelve true, en otro caso false
        return True if np.sum(distances) == 0 else False

    # Calcula la distancia euclidea de dos arrays
    def calculate_euclidean_dist(self, x1, x2):
        # Calcula la distancia euclidea de dos valores
        return np.sqrt(np.sum((x1 - x2) ** 2))

    # Calcula la clase mas comun para cada cluster (la que mas se repite en la lista del cluster)
    def get_clusters_classes(self, clusters):
        classes = [[] for _ in range(self.K)]
        cluster_idx = 0
        # Se iteran los clusters
        for cluster in clusters:
            # Se obtiene la label de la clase mas predicha
            pred_c = self._predicted_class(self.y[cluster])
            # Se recorren las labels para añadir la clase predicha a las clases actuales de dichos labels
            for actual_c in self.y[cluster]:
                classes[cluster_idx].append((pred_c, actual_c))

            cluster_idx += 1

        return classes

    # Devuelve la clase predicha en ese cluster
    def _predicted_class(self, _class):
        # Se obtiene con counter para los K clusters, la clase mas repetida
        return Counter(_class).most_common(self.K)[0][0]
