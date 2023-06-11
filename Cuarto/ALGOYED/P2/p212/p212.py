from dis import dis
import numpy as np
import itertools
from typing import List, Dict, Callable, Iterable


def init_cd(n: int) -> np.ndarray:
    """Esta función se encarga de inicializar
        un array de n elementos a -1 cada uno de ellos.

    Args:
        n (int): Longitud que se quiere que tenga el array.

    Returns:
        np.ndarray: Lista de n elementos inicializados a -1.
    """

    return np.full_like(np.arange(n, dtype=int), -1)


def union(rep_1: int, rep_2: int, p_cd: np.ndarray) -> int:
    """Esta función se encarga de recibir dos representantes
        x, y, calcula su unión Srep_1 U Srep_2 y devuelve un
        representante del subconjunto Srep_1 U Srep_2.

    Args:
        rep_1 (int): Indice del representante 1.
        rep_2 (int): Indice del representante 2.
        p_cd (np.ndarray): Conjunto disjunto almacenado en un array.

    Returns:
        int: Representante del conjunto obtenido como la union por
            rangos de los representados por los indices
            rep_1, rep_2 en el CD almacenado en el array p_cd.
    """

    # Si la profundidad del T_rep_1 es mayor que la de T_rep_2
    # entonces unimos el T_rep_2 a T_rep_1
    if p_cd[rep_1] < p_cd[rep_2]:
        p_cd[rep_2] = rep_1
        return rep_1

    # Si la profundidad del T_rep_2 es mayor que la de T_rep_1
    # entonces unimos el T_rep_1 a T_rep_2
    elif p_cd[rep_1] > p_cd[rep_2]:
        p_cd[rep_1] = rep_2
        return rep_2

    # En caso de que ambos árboles tengan la misma profundad
    # entonces unimos el T_rep_1 a T_rep_2
    else:
        p_cd[rep_2] = rep_1
        p_cd[rep_1] -= 1
        return rep_1


def find(ind: int, p_cd: np.ndarray) -> int:
    """Esta función se encarga de recibir un elemento
        x ∈ U y devuelve el representante del subconjunto
        p_cd_x de p_cd que contiene a x.

    Args:
        ind (int): Indice del CD.
        p_cd (np.ndarray): Conjunto disjunto almacenado en un array.

    Returns:
        int: Representante del subconjunto p_cd_x.
    """

    root_index = ind

    # Buscamos el representante del nodo que se encuentra
    # en el índice ind
    while p_cd[root_index] >= 0:
        root_index = p_cd[root_index]

    # Realizamos la compresion de caminos desde ind hasta
    # la raiz de su árbol
    while p_cd[ind] >= 0:
        compress_index = p_cd[ind]
        p_cd[ind] = root_index
        ind = compress_index

    return root_index


def cd_2_dict(p_cd: np.ndarray) -> Dict:
    """Esta función se encarga de recorrer los nodos y comprobar,
        si es representante de un subconjunto
        entonces creamos una entrada en el dict y si no, añadimos
        dicho nodo a la lista del subconjunto correspondiente.

    Args:
        p_cd (np.ndarray): Conjunto disjunto almacenado en un array.

    Returns:
        Dict: Diccionario cuyas claves sean los representantes
            de los subconjuntos del CD y donde el valor de la
            clave u del dict sea una lista con los
            miembros del subconjunto representado por u.
    """
    rep_dicts = {}
    cd_length = np.size(p_cd)

    # Recorremos los nodos, si es representante de un subconjunto
    # entonces creamos una entrada en el dict y si no, añadimos
    # dicho nodo a la lista del subconjunto correspondiente
    for i in range(cd_length):
        if p_cd[i] < 0 and str(i) not in rep_dicts.keys():
            rep_dicts[str(i)] = [str(i)]
        else:
            rep = str(find(i, p_cd))
            if rep not in rep_dicts.keys():
                rep_dicts[rep] = [str(i)]
            else:
                rep_dicts[rep].append(str(i))

    return rep_dicts


def ccs(n: int, l: List) -> Dict:
    """Esta función se encarga de devolver las
        componentes conexas de un grafo. Primero
        inicializando un CD vacío, examina las rama
        del grafo y si los dos nodos de la rama pertencen
        a subconjuntos distintos, los unirá. Finalmente
        el CD creado se convertirá en un dict y se devolverá.

    Args:
        n (int): Número de nodos del grafo.
        l (List): Lista que contiene las ramas del grafo.

    Returns:
        Dict: Diccionario con claves los representantes de
                los subconjuntos y como valor una lista
                con los nodos que pertenecen al subconjunto.
    """

    # Creamos un CD vacío
    cd = init_cd(n)

    # Recorremos la lista de aristas
    # y completamos el CD
    for u, v in l:
        rep_u = find(u, cd)
        rep_v = find(v, cd)

        # Si el representante es el mimso, no hacemos
        # nada puesto que ya pertenecen al mismo subconjunto
        # Pero si son diferentes, unimos los subconjuntos
        if rep_u != rep_v:
            union(rep_u, rep_v, cd)

    # Convertimos el cd a un dict y lo devolvemos
    return cd_2_dict(cd)


def dist_matrix(n_nodes: int, w_max=10) -> np.ndarray:
    """Esta función se encarga de generar una matriz
        de distancias de un grafo con n nodos donde las
        distancias tendrán como máximo valor v_max y que
        será simétrica y con diagonal 0.

    Args:
        n_nodes (int): Numero de nodos que tendra la matriz.
        w_max (int, optional): Cota superior para el valor
                                de los nodos que tendra la
                                matriz de distancias.
                                Por defecto 10.

    Returns:
        np.ndarray: Matriz de distancias simetrica con diagonal 0.
    """

    matriz = np.random.randint(0, w_max, (n_nodes, n_nodes))
    matriz = (matriz + matriz.T) // 2
    np.fill_diagonal(matriz, 0)

    return matriz


def greedy_tsp(dist_m: np.ndarray, node_ini=0) -> List:
    """Esta función se encarga de devolver un circuito codicioso
        en forma de lista con valores entre 0 y el numero de
        nodos menos 1 a partir de una matriz de distancias
        y un cierto nodo inicial.

    Args:
        dist_m (np.ndarray): Matriz de distancias.
        node_ini (int, optional): Nodo inicial. Por defecto 0.

    Returns:
        List: Lista de valores entre 0 y el numero de nodos
                menos 1 que representan el circuito codicioso.
                Vendría siendo el circuito.
    """

    num_nodes = dist_m.shape[0]
    circuit = [node_ini]

    # La longitud del camino tiene que ser menor que el numero
    # total de nodos que hay
    while len(circuit) < num_nodes:
        actual_node = circuit[-1]

        # Bbtenemos las distancias del node actual respecto
        # del resto, dicha lista la convertimos a una lista
        # de indices y se orden las indices segun la distancia
        options = list(np.argsort(dist_m[actual_node]))

        # De entre todas las opciones a las que el nodo actual
        # podria moverse se selecciona aquella mas cercana pero
        # que no se encuentre ya en el circuito
        for node in options:
            if node not in circuit:
                circuit.append(node)
                break

    # el circuito empieza y acaba en el mismo punto
    return circuit + [node_ini]


def len_circuit(circuit: List, dist_m: np.ndarray) -> int:
    """Esta función se encarga de calcular la longitud del
        circuito.

    Args:
        circuit (List): Circuito de nodos.
        dist_m (np.ndarray): Matriz de distancias.

    Returns:
        int: Longitud del circuito.
    """

    # Contador de distancias
    dist = 0

    # Nodo inicial de un camino entre dos nodos
    node_ini = circuit[0]

    # Recorremos el camino desde el segundo hasta el final
    # y calculamos la distancia del camino entre los nodos
    # node_ini y node_last ( node_ini -> node_last )
    # y sumamos al contador la distancias entre los dos nodos
    for node_last in circuit[1:]:
        dist += dist_m[node_ini][node_last]
        node_ini = node_last

    return dist


def repeated_greedy_tsp(dist_m: np.ndarray) -> List:
    """Esta función se encarga de devolver el circuito
        con la menor longitud. En este caso los circutos
        son generados por greedy_tsp a partir de un cierto nodo

    Args:
        dist_m (np.ndarray): Matriz de distancias.

    Returns:
        List: Circuito con la menor longitud.
    """

    num_nodes = dist_m.shape[0]

    # Usamos el circuito del nodo 0 como referencia
    shortest_circuit = greedy_tsp(dist_m, 0)
    shortest_circuit_len = len_circuit(shortest_circuit, dist_m)

    # Para el resto de curcuitos vemos cual es su longitud
    # y en caso de ser menor a la del circuito que se usa como
    # referencia pues actualizamos tanto el circuito de referencia
    # como la longitud mínima
    for i in range(1, num_nodes):
        actual_circuit = greedy_tsp(dist_m, i)
        actual_circuit_len = len_circuit(actual_circuit, dist_m)

        if actual_circuit_len < shortest_circuit_len:
            shortest_circuit = actual_circuit
            shortest_circuit_len = actual_circuit_len

    return shortest_circuit


def exhaustive_tsp(dist_m: np.ndarray) -> List:
    """Esta función se encarga de devolver el circuito
        con la menor longitud. En este caso los circuitos
        son las diferentes permutaciones entre los nodos
        de la matriz.

    Args:
        dist_m (np.ndarray): Matriz de distancias.

    Returns:
        List: Circuito con la menor longitud.
    """

    num_nodes = dist_m.shape[0]
    list_nodes = np.arange(num_nodes, dtype=int)
    permutation_list = list(itertools.permutations(list_nodes))

    # Completamos las permutaciones
    for i in range(0, len(permutation_list)):
        circuite = list(permutation_list[i])
        circuite.append(permutation_list[i][0])
        permutation_list[i] = circuite

    # Usamos el circuito del nodo 0 como referencia
    shortest_circuit = permutation_list[0]
    shortest_circuit_len = len_circuit(shortest_circuit, dist_m)

    # Para el resto de curcuitos vemos cual es su longitud
    # y en caso de ser menor a la del circuito que se usa como
    # referencia pues actualizamos tanto el circuito de referencia
    # como la longitud mínima
    for circuite in permutation_list[1::]:
        actual_circuit_len = len_circuit(circuite, dist_m)

        if actual_circuit_len < shortest_circuit_len:
            shortest_circuit = circuite
            shortest_circuit_len = actual_circuit_len

    return shortest_circuit
