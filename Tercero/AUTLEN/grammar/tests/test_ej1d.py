import ast
import inspect
from grammar.ast_utils import ASTRemoveConstantIf, ASTDotVisitor

def my_fun1(p):
    if True:
        return 1
    else:
        return 0

source = inspect.getsource(my_fun1)
my_ast = ast.parse(source)

if_remover = ASTRemoveConstantIf()
new_ast = if_remover.visit(my_ast)

dot_visitor = ASTDotVisitor()
dot_visitor.visit(new_ast)


print("\n----------------------------------------------------\n")

# FUNCION ALTERNATIVA DE PRUEBA
def my_fun2(p):
    i = 1
    if i == 1:
            if True:
                if False:
                    return 1
                else:
                    return 4
            else:
                return 3
    else:
        return 0

source = inspect.getsource(my_fun2)
my_ast = ast.parse(source)

if_remover = ASTRemoveConstantIf()
new_ast = if_remover.visit(my_ast)

dot_visitor = ASTDotVisitor()
dot_visitor.visit(new_ast)

print("\n----------------------------------------------------\n")

# FUNCION ALTERNATIVA DE PRUEBA
def my_fun3(p):
    if True:
            if True:
                if False:
                    return 1
                else:
                    return 4
            else:
                return 3
    else:
        return 0

source = inspect.getsource(my_fun3)
my_ast = ast.parse(source)

if_remover = ASTRemoveConstantIf()
new_ast = if_remover.visit(my_ast)

dot_visitor = ASTDotVisitor()
dot_visitor.visit(new_ast)

print("\n----------------------------------------------------\n")

# FUNCION ALTERNATIVA DE PRUEBA
def my_fun4(p):
    i = 1
    if i == 1:
            if True:
                if False:
                    return 1
                else:
                    if i == 1:
                        if False:
                            return 4
                        else:
                            return 5
            else:
                return 3
    else:
        return 0

source = inspect.getsource(my_fun4)
my_ast = ast.parse(source)

if_remover = ASTRemoveConstantIf()
new_ast = if_remover.visit(my_ast)

dot_visitor = ASTDotVisitor()
dot_visitor.visit(new_ast)

print("\n----------------------------------------------------\n")

# FUNCION ALTERNATIVA DE PRUEBA
def my_fun5(p):
    i = 1
    if i == 1:
            if True:
                if False:
                    return 1
                else:
                    if i == 1:
                        if True:
                            return 4
                        else:
                            return 5
                    else:
                        return 9
            else:
                return 3
    else:
        return 0

source = inspect.getsource(my_fun5)
my_ast = ast.parse(source)

if_remover = ASTRemoveConstantIf()
new_ast = if_remover.visit(my_ast)

dot_visitor = ASTDotVisitor()
dot_visitor.visit(new_ast)
