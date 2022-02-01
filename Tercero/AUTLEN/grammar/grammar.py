from __future__ import annotations

from collections import deque
from typing import AbstractSet, Collection, MutableSet, Optional


class RepeatedCellError(Exception):
    """Exception for repeated cells in LL(1) tables."""


class SyntaxError(Exception):
    """Exception for parsing errors."""


class Production:
    """
    Class representing a production rule.

    Args:
        left: Left side of the production rule. It must be a character
            corresponding with a non terminal symbol.
        right: Right side of the production rule. It must be a string
            that will result from expanding ``left``.

    """

    def __init__(self, left: str, right: str) -> None:
        self.left = left
        self.right = right

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, type(self)):
            return NotImplemented
        return (
            self.left == other.left
            and self.right == other.right
        )

    def __repr__(self) -> str:
        return (
            f"{type(self).__name__}({self.left!r} -> {self.right!r})"
        )

    def __hash__(self) -> int:
        return hash((self.left, self.right))


class Grammar:
    """
    Class that represent a grammar.

    Args:
        terminals: Terminal symbols of the grammar.
        non_terminals: Non terminal symbols of the grammar.
        productions: Production rules of the grammar.
        axiom: Axiom of the grammar.

    """

    def __init__(
        self,
        terminals: AbstractSet[str],
        non_terminals: AbstractSet[str],
        productions: Collection[Production],
        axiom: str,
    ) -> None:
        if terminals & non_terminals:
            raise ValueError(
                "Intersection between terminals and non terminals "
                "must be empty.",
            )

        if axiom not in non_terminals:
            raise ValueError(
                "Axiom must be included in the set of non terminals.",
            )

        for p in productions:
            if p.left not in non_terminals:
                raise ValueError(
                    f"{p}: "
                    f"Left symbol {p.left} is not included in the set "
                    f"of non terminals.",
                )
            if p.right is not None:
                for s in p.right:
                    if (
                        s not in non_terminals
                        and s not in terminals
                    ):
                        raise ValueError(
                            f"{p}: "
                            f"Invalid symbol {s}.",
                        )

        self.terminals = terminals
        self.non_terminals = non_terminals
        self.productions = productions
        self.axiom = axiom

    def __repr__(self) -> str:
        return (
            f"{type(self).__name__}("
            f"terminals={self.terminals!r}, "
            f"non_terminals={self.non_terminals!r}, "
            f"axiom={self.axiom!r}, "
            f"productions={self.productions!r})"
        )

    def compute_first(self, sentence: str) -> AbstractSet[str]:
        """
        Method to compute the first set of a string.

        Args:
            str: string whose first set is to be computed.

        Returns:
            First set of str.
        """

        # TO-DO: Complete this method for exercise 3...

        set_first = set()
        producciones = set()

        # Si la cadena es vacia...
        if len(sentence) == 0:
            set_first.add(sentence)
            return set_first

        # Comprobamos si los symbolos de la cadena pertenecen a los terminales o no-terminales
        for input in sentence:
            if input not in self.non_terminals and input not in self.terminals:
                raise(ValueError)

        # Obtemos la producciones del primer caracter y las evaluamos
        producciones.update(self.get_producciones_by_symbol(sentence[0]))

        # Completamos los primeros
        self.revisar_producciones(sentence, set_first, producciones)

        return set_first

    def revisar_producciones(self, sentence: str,
                             set_first: set,
                             producciones: set):

        flag_recursivo = False  # Flag para comprobar si hay que analizar el siguiente
                                # simbolo de la cadena de la que se esta calculando
                                # sus primeros

        for p in producciones:
            if len(p) != 0:
                if p[0] in self.terminals:
                    set_first.add(p[0])
                else:

                    rec = True  # Flag para controlar si hay que seguir recorriendo los simbolos de una produccion

                    # numero total de simbolos de la produccion que se pueden inspeccionar
                    # para luego saber si meter lambda o no en el conjunto de primeros
                    cad_len = len(p)
                    count = 0  # contador de los symbolos inspeccionados de una produccion

                    while rec != False and len(p) > 0 and p[0] != sentence[0]:

                        count += 1

                        # Si es un no-terminal, obtengo sus primeros
                        primeros_of_produccion = self.compute_first(p[0])

                        # En caso de que los primeros de un no-terminal tengan lambda,
                        # actualizamos el conjunto de los primeros e inspeccionamos
                        # el siguiente simbolo de la produccion
                        if self.lambda_in_producciones(primeros_of_produccion) == True:
                            rec = True  # Si hay lambda dejamos de analizar la produccion
                            p = p[1:]  # consumimos la cad

                            # Solo se deja lambda si el último simbolo de la produccion la contiene
                            if count != cad_len:
                                primeros_of_produccion.remove('')
                            # Actualizamos el set con primeros
                            set_first.update(primeros_of_produccion)

                        # Si no esta lambda en los primeros de s
                        # dejo de revisar la cadena de la produccion
                        # terminando el bucle y moviendonos en la sentencia
                        else:
                            rec = False  # Si no hay lambda dejamos de analizar la produccion
                            # Actualizamos el set con primeros
                            set_first.update(primeros_of_produccion)

            # Si hay lambda se indica que hay que seguir recorriendo
            # la cadena de la que se quiere saber sus primeros
            else:
                flag_recursivo = True
                set_first.add(p)

        # Una vez obtenidos los primeros del primer simbolo
        # recorremos la cadena
        sentence = sentence[1:]

        # Si no se ha consumido del todo y hay lambda,
        # analizamos el siguiente simbolo de la cadena
        if len(sentence) != 0 and flag_recursivo == True:

            # Actualizamos el set de producciones a analizar
            producciones = set()
            producciones.update(self.get_producciones_by_symbol(sentence[0]))

            if '' in set_first:
                set_first.remove('')

            self.revisar_producciones(sentence, set_first, producciones)

        return

    # Metodo para obtener las producciones donde
    # la izquierda es el simbolo que se pasa por args
    def get_producciones_by_symbol(self, symbol: str):

        producciones = set()

        if symbol in self.terminals:
            producciones.add(symbol)
            return producciones

        for p in self.productions:
            if p.left == symbol:
                producciones.add(p.right)

        return producciones

    # Comprobar si lambda esta en un conjunto de simbolos
    def lambda_in_producciones(self, set: set):

        for element in set:
            if element == '':
                return True

        return False

    def compute_follow(self, symbol: str) -> AbstractSet[str]:
        """
        Method to compute the follow set of a non-terminal symbol.

        Args:
            symbol: non-terminal whose follow set is to be computed.

        Returns:
            Follow set of symbol.
        """

        # TO-DO: Complete this method for exercise 4...
        if symbol not in self.non_terminals or len(symbol) > 1:
            raise(ValueError)

        set_follow = set()
        set_producciones = set()
        dict_symbol_revised = {}
        if symbol == self.axiom:
            # Si el simbolo del que se quieren obtener los siguientes
            # es el simbolo no-terminal inicial, se añade
            # el simbolo de fin de cadena $
            set_follow.add('$')

        dict_symbol_revised[symbol] = set_follow
        set_producciones.update(
            self.get_producciones_where_symbol_is_into(symbol))
        set_follow.update(self.follow_recursive(
            symbol, set_producciones, dict_symbol_revised, set_follow))

        return set_follow

    def follow_recursive(self, symbol: str, set_producciones: set, dict_symbol_revised: dict, set_follow: set):

        # REGLAS:
        # 1) S(A) -> $ si A es el symbolo inicial
        # 2) A -> XB =>
        # 2.1) S(B) = S(A)
        # 3) A -> XBY =>
        # 3.1) S(B) = P(Y) si lambda no esta en P(Y)
        # 3.2) S(B) = P(Y) Union S(A) si lambda esta en P(Y)

        for p in set_producciones:
            splited_right_production = p.right.split(symbol)  # Dividimos la produccion
                                                              # derecha por el symbolo a analizar
            if '' == splited_right_production[1]:
                splited_right_production.remove('')

            # Si la produccion se divide en una sola parte, se aplica la regla 2
            if len(splited_right_production) == 1:
                if p.left in dict_symbol_revised.keys():
                    follows = dict_symbol_revised[p.left]
                    set_follow.update(follows)
                else:
                    # Obtenemos los follows de la izquierda de la produccion
                    follows = self.follows_left_production_symbol(
                        p.left, dict_symbol_revised)

                    # Actualizamos el conjunto de follows del symbol
                    set_follow.update(follows)

            # Si la produccion se divide en dos partes, se comprueba la regla 3
            elif len(splited_right_production) == 2:
                first = self.compute_first(splited_right_production[1])

                if self.lambda_in_producciones(first) == True:  # regla 3.2
                    first.remove('')
                    set_follow.update(first)  # Actualizamos el conjunto follow

                    # Obtenemos los follows de la izquierda de la produccion
                    follows = self.follows_left_production_symbol(
                        p.left, dict_symbol_revised)
                    set_follow.update(follows)

                else:  # regla 3.1
                    set_follow.update(first)

            # Si el symbolo sale al principio y al final en una misma regla
            # Miramos los primeros de la cadena central
            # Y los siguientes de la izquierda
            elif len(splited_right_production) == 3:
                first = self.compute_first(splited_right_production[1])

                if p.left != symbol:
                    set_follow.update(self.compute_follow(p.left))

                if self.lambda_in_producciones(first) == True:  # regla 3.2
                    first.remove('')
                    set_follow.update(first)  # Actualizamos el conjunto follow

                    # Obtenemos los follows de la izquierda de la produccion
                    follows = self.follows_left_production_symbol(
                        p.left, dict_symbol_revised)
                    set_follow.update(follows)
                else:  # regla 3.1
                    set_follow.update(first)

        return set_follow

    def get_producciones_where_symbol_is_into(self, symbol: str):

        producciones = set()

        if symbol in self.terminals:
            return producciones

        for p in self.productions:
            if symbol in p.right:
                producciones.add(p)

        return producciones

    def follows_left_production_symbol(self, left: str, dict_symbol_revised: dict):

        # Preparamos todo para obtener los follows del p.left
        set_first_aux = set()
        producciones = set()

        if left == self.axiom:
            set_first_aux.add('$')

        producciones.update(
            self.get_producciones_where_symbol_is_into(left))
        dict_symbol_revised[left] = set_first_aux

        follows = self.follow_recursive(
            left, producciones, dict_symbol_revised, set_first_aux)

        return follows

    def get_ll1_table(self) -> Optional[LL1Table]:
        """
        Method to compute the LL(1) table.

        Returns:
            LL(1) table for the grammar, or None if the grammar is not LL(1).
        """

        # TO-DO: Complete this method for exercise 5...
        cells = []

        # Recorremos las producciones de la gramatica
        for p in self.productions:
            first_right_production = self.compute_first(p.right)

            # Primero colocamos en las casillas de los simbolos que
            # pertenecen a los primeros pero siendo distinto de lambda
            for t in self.terminals:
                if t in first_right_production:
                    if self.cell_created(cells, p.left, t) == True:
                        return None

                    cell = TableCell(p.left, t, p.right)
                    cells.append(cell)

            # Comprobamos si hay lambda y si es asi miramos los
            # simbolos siguientes
            if '' in first_right_production:
                follows_left_production = self.compute_follow(p.left)
                for t in follows_left_production:
                    # Comprobamos si esa celda ya existia
                    # Si es asi, habra conflicto y por tanto no sera una
                    # gramatica LL(1) y por tanto se lanza excepcion
                    if self.cell_created(cells, p.left, t) == True:
                        return None

                    # Si no esta creada, se crea
                    cell = TableCell(p.left, t, '')
                    cells.append(cell)

        # Añadimos el simbolo $ al conjunto de terminales
        terminals = set()
        for t in self.terminals:
            terminals.add(t)
        terminals.add('$')

        # Creamos la tabla
        table = LL1Table(self.non_terminals, terminals, cells)
        return table

    def is_ll1(self) -> bool:
        return self.get_ll1_table() is not None

    def cell_created(self, list_of_cells: list, non_terminal: str, terminal: str):

        for cell in list_of_cells:
            if cell.non_terminal == non_terminal and cell.terminal == terminal:
                return True  # Celda ya creada

        return False  # Celda no creada


class TableCell:
    """
    Cell of a LL1 table.

    Args:
        non_terminal: Non terminal symbol.
        terminal: Terminal symbol.
        right: Right part of the production rule.

    """

    def __init__(self, non_terminal: str, terminal: str, right: str) -> None:
        self.non_terminal = non_terminal
        self.terminal = terminal
        self.right = right

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, type(self)):
            return NotImplemented
        return (
            self.non_terminal == other.non_terminal
            and self.terminal == other.terminal
            and self.right == other.right
        )

    def __repr__(self) -> str:
        return (
            f"{type(self).__name__}({self.non_terminal!r}, {self.terminal!r}, "
            f"{self.right!r})"
        )

    def __hash__(self) -> int:
        return hash((self.non_terminal, self.terminal))


class LL1Table:
    """
    LL1 table.

    Args:
        non_terminals: Set of non terminal symbols.
        terminals: Set of terminal symbols.
        cells: Cells of the table.

    """

    def __init__(
        self,
        non_terminals: AbstractSet[str],
        terminals: AbstractSet[str],
        cells: Collection[TableCell],
    ) -> None:

        if terminals & non_terminals:
            raise ValueError(
                "Intersection between terminals and non terminals "
                "must be empty.",
            )

        for c in cells:
            if c.non_terminal not in non_terminals:
                raise ValueError(
                    f"{c}: "
                    f"{c.non_terminal} is not included in the set "
                    f"of non terminals.",
                )
            if c.terminal not in terminals:
                raise ValueError(
                    f"{c}: "
                    f"{c.terminal} is not included in the set "
                    f"of terminals.",
                )
            for s in c.right:
                if (
                    s not in non_terminals
                    and s not in terminals
                ):
                    raise ValueError(
                        f"{c}: "
                        f"Invalid symbol {s}.",
                    )

        self.terminals = terminals
        self.non_terminals = non_terminals
        self.cells = {(c.non_terminal, c.terminal): c.right for c in cells}

    def __repr__(self) -> str:
        return (
            f"{type(self).__name__}("
            f"terminals={self.terminals!r}, "
            f"non_terminals={self.non_terminals!r}, "
            f"cells={self.cells!r})"
        )

    def add_cell(self, cell: TableCell) -> None:
        """
        Adds a cell to an LL(1) table.

        Args:
            cell: table cell to be added.

        Raises:
            RepeatedCellError: if trying to add a cell already filled.
        """
        if (cell.non_terminal, cell.terminal) in self.cells:
            raise RepeatedCellError(
                f"Repeated cell ({cell.non_terminal}, {cell.terminal}).")
        else:
            self.cells[(cell.non_terminal, cell.terminal)] = cell.right

    def analyze(self, input_string: str, start: str) -> ParseTree:
        """
        Method to analyze a string using the LL(1) table.

        Args:
            input_string: string to analyze.
            start: initial symbol.

        Returns:
            ParseTree object with either the parse tree (if the elective exercise is solved)
            or an empty tree (if the elective exercise is not considered).

        Raises:
            SyntaxError: if the input string is not syntactically correct.
        """

        # TO-DO: Complete this method for exercise 2...

        # Inicializamos la pila de simbolos
        stack = ['$', start]
        tree = ParseTree(start)

        # Mientras que la pila no este vacia aplicamos el algoritmo LL(1)
        while len(stack) != 0:

            # Comprobamos si no hemos consumido la cadena de entrada por completo
            # estando la pila no vacia
            if len(input_string) != 0:
                input_symbol = input_string[0]
            else:  # En caso de que la cadena de entrada este vacía pero la pila no...
                raise SyntaxError()

            # Sacamos el simbolo de la pila y comprobamos...
            top_symbol = stack.pop()

            if top_symbol in self.non_terminals and input_symbol in self.terminals:
                self.update_stack(top_symbol, input_symbol, stack, tree)
            elif input_symbol == top_symbol and top_symbol in self.terminals:
                # Consumimos el simbolo de la cadena
                input_string = input_string[1:]
            else:
                raise SyntaxError()

        # Si la cadena es aceptada, creamos su arbol de derivacion
        if len(input_string) == 0:
            return tree
        else:
            raise SyntaxError()

    def update_stack(self, top_symbol: str, input_symbol: str, stack: list, tree: ParseTree):

        for no_terminal_token, produccion in self.cells.items():
            if no_terminal_token[0] == top_symbol and no_terminal_token[1] == input_symbol:

                # Vamos completando el arbol
                child_list = []
                for p in produccion:
                    child = ParseTree(p)
                    child_list.append(child)
                if len(child_list) == 0:
                    child = ParseTree("λ")
                    child_list.append(child)
                self.complete_tree(tree, child_list, top_symbol)

                # Añadimos la regla de produccion a la stack
                produccionInvertida = produccion[::-1]
                for symbol in produccionInvertida:
                    stack.append(symbol)
                return

        raise SyntaxError()

    def complete_tree(self, tree: ParseTree, child_list: list, top_symbol: str):
        # La idea es recorrer el arbol de arriba abajo y de izquierda a derecha

        ret = False

        # Si se llega al nodo raiz que coincide con el padre de la lista de hijos
        # y el arbol asociado al nodo raiz esta vacio, entonces estamos en el árbol
        # correcto y por tanto actualizamos la lista de arboles hijos
        if tree.root == top_symbol and len(tree.children) == 0:
            tree.children = child_list
            return True

        # En cualquier otro caso, recorremos el arbol mirando
        # para cada hijo del tree que se esta revisando si en su arbol
        # se encuentra el top_symbol correspondiente
        for child in tree.children:
            ret = self.complete_tree(child, child_list, top_symbol)
            if ret == True:  # Se ha actualizado un subarbol y por tanto
                # no se debe revisar mas
                break

        return ret


class ParseTree():
    """
    Parse Tree.

    Args:
        root: root node of the tree.
        children: list of children, which are also ParseTree objects.
    """

    def __init__(self, root: str, children: Collection[ParseTree] = []) -> None:
        self.root = root
        self.children = children

    def __repr__(self) -> str:
        return (
            f"{type(self).__name__}({self.root!r}: {self.children})"
        )

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, type(self)):
            return NotImplemented
        return (
            self.root == other.root
            and len(self.children) == len(other.children)
            and all([x.__eq__(y) for x, y in zip(self.children, other.children)])
        )

    def add_children(self, children: Collection[ParseTree]) -> None:
        self.children = children
