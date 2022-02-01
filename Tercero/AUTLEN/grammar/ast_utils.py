import ast
import inspect
import numbers
import types
from typing import List, Optional, Union


class ASTMagicNumberDetector (ast.NodeVisitor):

    def __init__(self):
        self.magic_numbers = 0

    def _inspect(self, value):
        if value != 0 and value != 1 and value != 1j:
            self.magic_numbers += 1

    def visit_Num(self, node):
        node_value = node.n
        self._inspect(node_value)

    def visit_Constant(self, node):
        node_value = node.value
        if isinstance(node_value, numbers.Number):
            self.visit_Num(node)
        else:
            self._inspect(node_value)


class ASTDotVisitor(ast.NodeVisitor):

    def __init__(self) -> None:
        self.level = 0
        self.n_node = 0
        self.last_parent: Optional[int] = None  # Id del padre de la transicion
        self.last_field_name = ""  # Nombre de la etiqueta de la transicion
        self.dot_cad = ""

    def generic_visit(self, node: ast.AST):

        # Iniciamos la cadena de formato dot
        if self.level == 0:
            self.dot_cad = "digraph {\n"

        self.level += 1
        node_childs = {}  # Lista de hijos del nodo
        node_info = ""
        actual_node = self.n_node
        for field, value in ast.iter_fields(node):

            # En caso de que el nodo solo tenga varios hijos
            if isinstance(value, list):
                for item in value:
                    if isinstance(item, ast.AST):
                        # Guardamos al hijo y la transicion al mismo
                        node_childs[field] = item

            # En caso de que el nodo solo tenga un único hijo
            elif isinstance(value, ast.AST):
                node_childs[field] = value  # Se añade el hijo y la transicion

            # Lo que se encuentra entre parentesis en el dot que serian los argumentos
            else:
                node_info += str(field) + "="
                if not isinstance(value, str):
                    node_info += str(value) + ", "
                else:
                    node_info += "\'" + str(value) + "\', "

        # Cuando terminamos de recorrer el nodo imprimos su info
        # Quitamos el espacio y la coma que se le añade al último arg
        node_info = node_info[0:(len(node_info)-2)]
        # Imprimimos el nivel correspondiente del dot
        dot_line = "s" + \
            str(self.n_node) + \
            '[label=\"' + node.__class__.__name__ + "(" + node_info + ')\"]'
        self.dot_cad += dot_line + "\n"

        # Creamos la transicion del nodo con su nodo padre
        # Que no sea el nodo inicial (e.  Module())
        if self.last_parent is not None:
            transicion = "s" + str(self.last_parent) + " -> s" + \
                str(self.n_node) + '[label=\"' + self.last_field_name + '\"]'
            self.dot_cad += transicion + "\n"

        # Visitamos los nodos hijos recorriendo el dict de transicion:nodo
        # actualizando los parametros que este usará
        for transicion, node_hijo in node_childs.items():
            self.last_field_name = transicion
            self.last_parent = actual_node

            # Actualizamos el id que usará el hijo
            self.n_node += 1
            self.visit(node_hijo)

        # Tras recorrer una entera, subimos volvemos a los niveles previos de la altura del arbol dot
        self.level -= 1

        # Si volvemos al primer nodo cerramos la cadena de formato dot
        if self.level == 0:
            self.dot_cad += "}"
            print(self.dot_cad)


class ASTReplaceNum(ast.NodeTransformer):

    def __init__(self, number: complex):
        self.number = number

    # devolver un nuevo nodo AST con self.number
    def visit_Num(self, node: ast.Num) -> ast.AST:
        return ast.Num(self.number, lineno=0, col_offset=0)

    # Para Python >= 3.8
    # devolver un nuevo nodo AST con self.number si la constante es un número
    def visit_Constant(self, node: ast.Constant) -> ast.AST:
        node_value = node.value
        if isinstance(node_value, numbers.Number):
            return self.visit_Num(node)
        else:
            return node


class ASTRemoveConstantIf(ast.NodeTransformer):

    # usar node.test, node.test.value, node.body y node.orelse
    def visit_If(self, node: ast.If) -> Union[ast.AST, List[ast.stmt]]:

        # Comprobar si el If tiene como test un objeto ast de tipo Constante
        # y que su valor sea True o False, ya que esta sería la condicion impuesta
        lista = []

        if isinstance(node.test, ast.NameConstant):
            if node.test.value is True:
                for body in node.body:
                    ret_body = self.visit(body)
                    if isinstance(ret_body, ast.AST):
                        lista.append(ret_body)
                    elif isinstance(ret_body, list):
                        lista.extend(ret_body)
            elif node.test.value is False:
                for orelse in node.orelse:
                    ret_orelse = self.visit(orelse)
                    if isinstance(ret_orelse, ast.AST):
                        lista.append(ret_orelse)
                    elif isinstance(ret_orelse, list):
                        lista.extend(ret_orelse)
        else: # En caso de ser un If que no tenga constante en su test

            for body in node.body:
                if isinstance(body.test, ast.NameConstant):

                    # Si el If del body es una constante, vemos
                    # si obtener su body o su orlese dependiendo
                    # del valor de la constante
                    if body.test.value is True:
                        ret = self.visit(body)
                    elif body.test.value is False:
                        ret = self.visit(body)

                    # Creamos el nuevo ast.IF con la simplificacion de
                    # un IF con constante en su test
                    lista.append(ast.If(node.test, ret, node.orelse))

        return lista


def transform_code(f, transformer):
    f_ast = ast.parse(inspect.getsource(f))

    new_tree = ast.fix_missing_locations(transformer.visit(f_ast))

    old_code = f.__code__
    code = compile(new_tree, old_code.co_filename, 'exec')
    new_f = types.FunctionType(code.co_consts[0], f.__globals__)

    return new_f
