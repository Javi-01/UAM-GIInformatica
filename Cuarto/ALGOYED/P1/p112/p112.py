import numpy as np
from typing import List


def matrix_multiplication(m_1: np.ndarray, m_2: np.ndarray) -> np.ndarray:
    """Realiza la multiplicación de dos matrices devolviendo como
    resultado la matriz resultante de la multiplicacion.

    Args:
        m_1 (np.ndarray): matriz 1,
        m_2 (np.ndarray): matriz 2

    Returns:
        la matriz resultante de la multiplicación de las matrices m1 y m2
            o None en caso de que la longitud de las matrices no sea la misma
    """

    if m_1.shape[1] == m_2.shape[0]:
        m_3 = np.zeros((m_1.shape[0], m_2.shape[1]), np.int32)
        for fila in range(m_1.shape[0]):
            for col in range(m_2.shape[1]):
                for k in range(m_1.shape[1]):
                    m_3[fila][col] += m_1[fila][k] * m_2[k][col]
        return m_3
    else:
        return None


def rec_bb(t: List, f: int, l: int, key: int) -> int:
    """Realizar la busqueda de un elemento key en una
    lista t de manera recursiva.

    Esto es, comprobamos que no hayamos recorrido ya
    la lista t, en caso de no haberla terminado de
    recorrerla, calculamos la mitad de la lista y
    vemos si el elemento en dicha posición es mayor
    o menor que la key y dependiendo de si es mayor
    o menor, recorremos la lista de mitad para alante
    o para atras.

    Args:
        t (List): lista de elementos,
        f (int): indice de la posicion inicial,
        l (int): indice de la posicion final,
        key (int): elemento a buscar en la lista

    Returns:
        int: indice de la posicion del elemento k en la lista l
    """

    if len(t) < 1:
        return None
    if f > l:
        return None
    if f == l:
        if t[f] == key:
            return f
        else:
            return None
    m = (f + l)//2

    if t[m] == key:
        return m
    elif t[m] > key:
        return rec_bb(t, f, m-1, key)
    else:
        return rec_bb(t, m+1, l, key)


def bb(t: List, f: int, l: int, key: int) -> int:
    """Realizar la busqueda de un elemento key en una
    lista t.

    Esto es, mientras no hayamos recorrido la lista t
    ca, calculamos la mitad de la lista y
    vemos si el elemento en dicha posición es mayor
    o menor que la key y dependiendo de si es mayor
    o menor, modificamos el valor inicial o final de
    la lista

    Args:
        t (List): lista de elementos,
        f (int): indice de la posicion inicial,
        l (int): indice de la posicion final,
        key (int): elemento a buscar en la lista

    Returns:
        int: indice de la posicion del elemento k en la lista l
    """

    if (len(t) == 0):
        return None
    while f <= l:
        m = (f + l)//2
        if key == t[m]:
            return m
        elif key < t[m]:
            l = m-1
        else:
            f = m+1
    return None


def min_heapify(h: np.ndarray, i: int):
    """Aplicar la operación de heapify al elemento de la posición i
    del array h recibido por parámetros

    Args:
        h (np.ndarray): heap sobre la que se aplicará la operacion de heapify,
        i (int): posicion en la que se realiara la operacion de heapify
    """
    while 2*i+1 < len(h):
        n_i = i
        if h[i] > h[2*i+1]:
            n_i = 2*i+1
        if 2*i+2 < len(h) and h[i] > h[2*i+2] and h[2*i+2] < h[n_i]:
            n_i = 2*i+2
        if n_i > i:
            h[i], h[n_i] = h[n_i], h[i]
            i = n_i
        else:
            return
    return 0


def insert_min_heap(h: np.ndarray, k: int) -> np.ndarray:
    """Insertar el entero k en el min heap contenido en
    el argumento h y que devuelva el nuevo min heap tras
    insertar el nuevo elemento

    Args:
        h (np.ndarray): heap al que se le insertara el elemento,
        k (int): elemento a insertar

    Returns:
        np.ndarray: nuevo min heap con el elemento k insertado
    """
    if h is None or len(h) == 0:
        return np.array([k])
    else:
        heapify = np.concatenate((h, [k]), axis=None)
        j = len(heapify) - 1

        while j >= 1 and heapify[(j-1) // 2] > heapify[j]:
            heapify[(j-1) // 2], heapify[j] = heapify[j], heapify[(j-1) // 2]
            j = (j-1) // 2

        return heapify


def create_min_heap(h: np.ndarray):
    """Crear un min heap sobre el array h que se pasa
    por argumentos

    Args:
        h (np.ndarray): heap a crear
    """

    # Calculamos la mitad del árbol para no contar las hojas
    i = (len(h) - 1) // 2

    # Desde la mitad a la raiz comprobamos que se cumpla la
    # condicion de min heap
    while i >= 0:
        min_heapify(h, i)
        i -= 1

    return h


def pq_ini():
    """Inicializar un array vacio

    Returns:
        ndarray: lista vacia
    """
    return np.array([])


def pq_insert(h: np.ndarray, k: int) -> np.ndarray:
    """Insertar el elemento k en la cola de prioridad
    h y devolver la nueva cola de prioridad

    Args:
        h (np.ndarray): cola de prioridad,
        k (int): elemento a insertar en la cola de prioridad

    Returns:
        np.ndarray: _description_
    """
    return insert_min_heap(h, k)


def pq_remove(h: np.ndarray) -> np.ndarray:
    """Eliminar el elemento con el menor valor de prioridad
    de h y devolver el elemento eliminado y la nueva cola
    de prioridad

    Args:
        h (np.ndarray): cola de prioridad

    Returns:
        np.ndarray: lista con el elemento eliminado
        y la cola de prioridad resultante de eliminar el elemento k
    """

    # Intercambiamos el ultimo a la raiz
    h[len(h) - 1], h[0] = h[0], h[len(h) - 1]

    # Eliminamos el elemento de menor prioridad (raiz)
    ele = h[len(h) - 1]
    h = np.delete(h, len(h) - 1, axis=None)

    # Aplicamos heapify desde la raiz
    min_heapify(h, 0)

    return [ele, h]


def select_min_heap(h: np.ndarray, k: int) -> int:
    """Encontrar el elemento de un heap desordenado h
    con n elementos que ocuparía el elemento k-ésimo
    en una ordenación de dicha tabla

    Args:
        h (np.ndarray): heap
        k (int): inidice en el heap

    Returns:
        int: elemento que deberia de estar situado en la posicion k
    """
    if k > len(h):
        return None
    else:
        h = np.negative(h)
        h_aux = pq_ini()

        i = 0
        while i <= k - 1:  # Añadimos los K elementos
            h_aux = pq_insert(h_aux, h[i])
            i += 1

        create_min_heap(h_aux)  # Creamos un min heap de k elementos

        # Comprobamos si hay algun elemento en h desde k hacia el
        # final que sea menor que el mayor elemento de h_aux
        while i <= (len(h) - 1):
            if h[i] > h_aux[0]:
                h_aux[0] = h[i]
                create_min_heap(h_aux)
            i += 1

        return h_aux[0] * (-1)


def is_heap(a):
    i = 0
    while 2*i+1 < len(a):
        if a[i] > a[2*i+1]:
            return False
        if 2*i+2 < len(a) and a[i] > a[2*i+2]:
            return False
        i += 1
    return True


print(select_min_heap(np.array([0, 1, 2, 3, 4, 5, 6, 7, 8]), 1))
