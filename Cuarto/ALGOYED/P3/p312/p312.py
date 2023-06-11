import numpy as np
import itertools
import sys

from typing import List, Tuple, Dict, Callable, Iterable, Union


def split(t: np.ndarray) -> Tuple[np.ndarray, int, np.ndarray]:
    """Esta función se encarga de repartir los elementos de t en dos arrays con
        los elementos menores y mayores al pivote siendo el pivote el primer
        elemento de t

    Args:
        t (np.ndarray): Lista de numeros.

    Returns:
        Tuple[np.ndarray, int, np.ndarray]: Un array con los elementos menores
        al pivote, el pivote, y otro array con los elementos mayores al pivote.
    """

    if len(t) == 1:
        return ([], t[0], [])

    taux = t[1:]
    menores = [ele for ele in taux if ele <= t[0]]
    mayores = [ele for ele in taux if ele > t[0]]

    return (menores, t[0], mayores)


def qsel(t: np.ndarray, k: int) -> Union[int, None]:
    """Esta función se encarga realizar el quickselect sobre
        una lista a partir de un pivoite de forma recursiva

    Args:
        t (np.ndarray): Lista de numeros.
        k (int): Indice en una ordenacion de t.

    Returns:
        Union[int, None]: El valor del elemento que ocuparia en una ordenacion
        de t si existe o None si no existe.
    """

    if k < 0 or k >= len(t):
        return None

    menores, t0, mayores = split(t)

    if k == len(menores):
        return t0

    elif k <= len(menores):
        return qsel(menores, k)

    else:
        return qsel(mayores, k - (len(menores) + 1))


def qsel_nr(t: np.ndarray, k: int) -> Union[int, None]:
    """Esta función se encarga realizar el quickselect sobre
        una lista a partir de un pivoite eliminando la recursion
        de cola

    Args:
        t (np.ndarray): Lista de numeros.
        k (int): Indice en una ordenacion de t.

    Returns:
        Union[int, None]: El valor del elemento que ocuparia en una ordenacion
        de t si existe o None si no existe.
    """

    if k < 0 or k >= len(t):
        return None

    menores, t0, mayores = split(t)

    while k != len(menores):

        if k <= len(menores):
            t = menores

        else:
            t = mayores
            k -= (len(menores) + 1)

        menores, t0, mayores = split(t)

    return t0


def split_pivot(t: np.ndarray, mid: int) -> Tuple[np.ndarray, int, np.ndarray]:
    """Esta función se encarga de repartir los elementos de t en dos arrays con
        los elementos menores y mayores al pivote siendo el pivote la mediana (mid)
        del array

    Args:
        t (np.ndarray): Lista de numeros.
        mid (int): Elemento mediana del array.

    Returns:
        Tuple[np.ndarray, int, np.ndarray]: Un array con los elementos menores
        al pivote, el pivote, y otro array con los elementos mayores al pivote.
    """

    taux = np.delete(t, list(t).index(mid))
    menores = [ele for ele in taux if ele <= mid]
    mayores = [ele for ele in taux if ele > mid]

    return (menores, mid, mayores)


def pivot5(t: np.ndarray) -> int:
    """Esta función se encarga de aplicar el procedimiento
        “mediana de medianas de 5 elementos”

    Args:
        t (np.ndarray): Lista de numeros.

    Returns:
        int: El “pivote 5” del array t
    """

    if len(t) <= 5:
        return sorted(t)[len(t) // 2]

    m = []
    u = 0

    for i in range(5, len(t), 5):
        m.append(sorted(t[u:i])[2])
        u = i

    if u != len(t):
        m.append(sorted(t[u:])[len(t[u:]) // 2])

    return qsel_nr(m, len(m) // 2)


def qsel5_nr(t: np.ndarray, k: int) -> Union[int, None]:
    """Esta función se encarga realizar el quickselect5 sobre
        una lista a partir de un pivoite obtenido mediante
        pivot5 y eliminando la recursion de cola

    Args:
        t (np.ndarray): Lista de numeros.
        k (int): Indice en una ordenacion de t.

    Returns:
        Union[int, None]: El valor del elemento que ocuparia en una ordenacion
        de t si existe o None si no existe.
    """

    if k < 0 or k >= len(t):
        return None

    m = pivot5(t)
    menores, t0, mayores = split_pivot(t, m)

    while k != len(menores):

        if k <= len(menores):
            t = menores

        else:
            t = mayores
            k -= (len(menores) + 1)

        m = pivot5(t)
        menores, t0, mayores = split_pivot(t, m)

    return t0


def qsort_5(t: np.ndarray) -> np.ndarray:
    """Esta función se encarga realizar la ordenacion de una tabla haciendo
        uso de pivot5

    Args:
        t (np.ndarray): Lista de numeros.

    Returns:
        np.ndarray: Ordenacion de la lista t.
    """

    if len(t) == 0:
        return []
    if len(t) == 1:
        return t

    m = pivot5(t)
    menores, t0, mayores = split_pivot(t, m)

    return np.concatenate((qsort_5(menores), np.concatenate(
        ([t0], qsort_5(mayores)), axis=None)), axis=None)


def edit_distance(str_1: str, str_2: str) -> int:
    """Esta función se encarga de calcular la distancia
        de edición entre dos cadenas.

    Args:
        str_1 (str): Cadena de caracteres.
        str_2 (str): Cadena de caracteres.

    Returns:
        int: Distancia de edicion entre la cadena str_1 y str_2.
    """

    # Creamos la matriz de numero de operaciones necesarias
    # para cambiar una cadena a otra
    mtx = np.zeros([len(str_2) + 1, len(str_1) + 1], dtype=np.int32)

    # Completamos la fila 0
    for i in range(0, len(str_1)):
        mtx[0][i + 1] += (i + 1)

    # Completamos la columna 0
    for i in range(0, len(str_2)):
        mtx[i + 1][0] += (i + 1)

    # Completamos el resto de la matriz de distancias
    for fil in range(1, len(str_2) + 1):
        for col in range(1, len(str_1) + 1):
            if str_2[fil - 1] == str_1[col - 1]:  # Si = Tj
                mtx[fil][col] = mtx[fil - 1][col - 1]
            else:  # Si != Tj
                mtx[fil][col] = 1 + min(mtx[fil - 1][col - 1],
                                        mtx[fil - 1][col], mtx[fil][col - 1])

    return mtx[mtx.shape[0] - 1][mtx.shape[1] - 1]


def max_subsequence_length(str_1: str, str_2: str) -> int:
    """Esta función se encarga de calcular la longitud de una subcadena
        común a las cadenas str_1 y str_2 aunque no necesariamente consecutivas.

    Args:
        str_1 (str): Cadena de caracteres.
        str_2 (str): Cadena de caracteres.

    Returns:
        int: Longitud de una subcadena común.
    """

    # Creamos la matriz de numero de operaciones necesarias
    # para cambiar una cadena a otra
    mtx = np.zeros([len(str_2) + 1, len(str_1) + 1], dtype=np.int32)

    # Completamos el resto de la matriz de distancias
    for fil in range(1, len(str_2) + 1):
        for col in range(1, len(str_1) + 1):
            if str_2[fil - 1] == str_1[col - 1]:  # Si = Tj
                mtx[fil][col] = 1 + mtx[fil - 1][col - 1]
            else:  # Si != Tj
                mtx[fil][col] = max(mtx[fil - 1][col], mtx[fil][col - 1])

    return mtx[mtx.shape[0] - 1][mtx.shape[1] - 1]


def max_common_subsequence(str_1: str, str_2: str) -> str:
    """Esta función se encarga de encontrar la subcadena común
        a las cadenas str_1 y str_2 aunque no necesariamente consecutiva.

    Args:
        str_1 (str): Cadena de caracteres.
        str_2 (str): Cadena de caracteres.

    Returns:
        str: Subcadena común.
    """

    # Creamos una matriz de cadenas vacías
    mtx = [["" for _ in range(len(str_2))] for _ in range(len(str_1))]

    # Recorremos la matriz y generamos subcadenas
    for i in range(0, len(str_1)):
        for j in range(0, len(str_2)):
            if str_1[i] == str_2[j]:
                if i == 0 or j == 0:
                    mtx[i][j] = str_1[i]
                else:
                    mtx[i][j] = mtx[i - 1][j - 1] + str_1[i]
            else:
                mtx[i][j] = max(mtx[i - 1][j], mtx[i][j - 1], key=len)

    subsequence = mtx[len(str_1) - 1][len(str_2) - 1]

    return subsequence


def min_mult_matrix(l_dims: List[int]) -> int:
    """Esta función se encarga de calcular el número mínimo de productos
        para multiplicar n matrices cuyas dimensiones vienen indicadas en
        la lista l_dims.

    Args:
        l_dims (List[int]): Lista de números con las dimensiones
                            de las matrices.

    Returns:
        int: Mínimo numero de productos para multiplicar n matrices.
    """

    # Creamos una matriz triangular donde se iran almacenando las soluciones
    mtx = [[0 for _ in range(len(l_dims))] for _ in range(len(l_dims))]

    # Calculamos los valores mi,i+2 para 1 ≤ i ≤ N − 2
    for l in range(2, len(l_dims)):

        # Calculamos los valores mi,i+1 para 1 ≤ i ≤ N − 1
        for i in range(1, len(l_dims) - l + 1):
            j = i + l - 1

            # Almacenamos un valor que será máximo para posteriores
            # comparaciones
            mtx[i][j] = sys.maxsize

            for k in range(i, j):
                # Aplicamos la siguiente fórmula:
                # C[i,j] = min { C[i,k] + C[k+1,j] + d[i-1]*d[k]*d[j] } para
                # i<=k<j
                mtx[i][j] = min(mtx[i][k] + mtx[k + 1][j] + l_dims[i - 1]
                                * l_dims[k] * l_dims[j], mtx[i][j])

    # last element of first row contains the result to the entire problem
    return mtx[1][len(l_dims) - 1]
