import ast
import inspect
from grammar.ast_utils import ASTMagicNumberDetector

def my_fun1(p):
    if p == 1:
        print(p + 1j)
    elif p == 5:
        print(0)
    else:
        print(p - 27.3 * 3j)

source = inspect.getsource(my_fun1)
my_ast = ast.parse(source)
magic_detector = ASTMagicNumberDetector()
magic_detector.visit(my_ast)
print(magic_detector.magic_numbers)
# Debería dar 3

print("-----------------------------------------")

def my_fun2(p):
    if p == 1:
        print(p + 1j)
    elif p == 5:
        print(0)
    else:
        print(p - 27.3 * 3j + 1 - 4 * 89)

source = inspect.getsource(my_fun2)
my_ast = ast.parse(source)
magic_detector = ASTMagicNumberDetector()
magic_detector.visit(my_ast)
print(magic_detector.magic_numbers)
# Debería dar 5 => + previos + 2 por (4 y 89)

print("-----------------------------------------")

def my_fun3(p):
    if p == 1:
        print(p + 1j)
    elif p == 5:
        print(0)
    else:
        print(p - 27.3 * 3j + 1 - 0 * 1j)

source = inspect.getsource(my_fun3)
my_ast = ast.parse(source)
magic_detector = ASTMagicNumberDetector()
magic_detector.visit(my_ast)
print(magic_detector.magic_numbers)
# Debería dar 3