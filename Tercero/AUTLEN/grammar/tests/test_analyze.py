import unittest

from grammar.grammar import Grammar, LL1Table, TableCell, ParseTree, SyntaxError
from grammar.utils import GrammarFormat
from typing import Optional, Type

class TestAnalyze(unittest.TestCase):
    def _check_analyze(
            self,
            table: LL1Table,
            input_string: str,
            start: str,
            exception: Optional[Type[Exception]] = None
    ) -> None:
        with self.subTest(string=input_string):
            if exception is None:
                self.assertTrue(table.analyze(input_string, start) is not None)
            else:
                with self.assertRaises(exception):
                    table.analyze(input_string, start)

    def _check_analyze_from_grammar(
            self,
            grammar: Grammar,
            input_string: str,
            start: str,
            exception: Optional[Type[Exception]] = None
    ) -> None:
        with self.subTest(string=input_string):
            table = grammar.get_ll1_table()
            self.assertTrue(table is not None)
            if table is not None:
                if exception is None:
                    self.assertTrue(table.analyze(input_string, start) is not None)
                else:
                    with self.assertRaises(exception):
                        table.analyze(input_string, start)

    def _check_analyze_from_grammar_2(
            self,
            grammar: Grammar,
            input_string: str,
            start: str,
            exception: Optional[Type[Exception]] = None
    ) -> None:
        with self.subTest(string=input_string):
            table = grammar.get_ll1_table()
            self.assertTrue(table is None)

    def _check_parse_tree(
            self,
            table: LL1Table,
            input_string: str,
            start: str,
            tree: ParseTree,
            exception: Optional[Type[Exception]] = None
    ) -> None:
        with self.subTest(string=input_string):
            if exception is None:
                treeTransformed = table.analyze(input_string, start)
                self.assertEqual(treeTransformed, tree)
            else:
                with self.assertRaises(exception):
                    table.analyze(input_string, start)

    def test_case1(self) -> None:
        """Test for syntax analysis from table."""
        terminals = {"(", ")", "i", "+", "*", "$"}
        non_terminals = {"E", "T", "X", "Y"}
        cells = [TableCell('E', '(', 'TX'),
                 TableCell('E', 'i', 'TX'),
                 TableCell('T', '(', '(E)'),
                 TableCell('T', 'i', 'iY'),
                 TableCell('X', '+', '+E'),
                 TableCell('X', ')', ''),
                 TableCell('X', '$', ''),
                 TableCell('Y', '*', '*T'),
                 TableCell('Y', '+', ''),
                 TableCell('Y', ')', ''),
                 TableCell('Y', '$', '')]
        table = LL1Table(non_terminals, terminals, cells)

        self._check_analyze(table, "i*i$", "E")
        self._check_analyze(table, "i*i+i$", "E")
        self._check_analyze(table, "i*i+i+(i*i)$", "E")
        self._check_analyze(table, "a", "E", exception=SyntaxError)
        self._check_analyze(table, "(i$", "E", exception=SyntaxError)
        self._check_analyze(table, "i*i$i", "E", exception=SyntaxError)
        self._check_analyze(table, "i*i", "E", exception=SyntaxError)
        self._check_analyze(table, "+i*i", "E", exception=SyntaxError)
        self._check_analyze(table, "$", "E", exception=SyntaxError)

        # Test adicionales
        self._check_analyze(table, "(i+i*i+i)+i*i$", "E")
        self._check_analyze(table, "(((i+i*i+i+i*i)))$", "E")

    def test_case2(self) -> None:
        """Test for syntax analysis from grammar."""
        grammar_str = """
        E -> TX
        X -> +E
        X ->
        T -> iY
        T -> (E)
        Y -> *T
        Y ->
        """

        grammar = GrammarFormat.read(grammar_str)

        self._check_analyze_from_grammar(grammar, "i*i$", "E")
        self._check_analyze_from_grammar(grammar, "i*i+i$", "E")
        self._check_analyze_from_grammar(grammar, "i*i+i+(i*i)$", "E")
        self._check_analyze_from_grammar(grammar, "a", "E", exception=SyntaxError)
        self._check_analyze_from_grammar(grammar, "(i$", "E", exception=SyntaxError)
        self._check_analyze_from_grammar(grammar, "i*i$i", "E", exception=SyntaxError)
        self._check_analyze_from_grammar(grammar, "i*i", "E", exception=SyntaxError)
        self._check_analyze_from_grammar(grammar, "+i*i", "E", exception=SyntaxError)

    def test_case3(self) -> None:
        """Test for syntax analysis from table."""
        terminals = {"+", "-", "*", "/", "(", ")", "n", "i", "$"}
        non_terminals = {"E", "X", "T", "Q", "F"}
        cells = [TableCell('E', '(', "TX"),
                 TableCell('E', 'n', "TX"),
                 TableCell("X", '+', "+TX"),
                 TableCell("X", '-', "-TX"),
                 TableCell("X", ')', ''),
                 TableCell("X", '$', ''),
                 TableCell('T', '(', "FQ"),
                 TableCell('T', 'n', "FQ"),
                 TableCell('T', 'i', "FQ"),
                 TableCell("Q", '+', ''),
                 TableCell("Q", '-', ''),
                 TableCell("Q", '*', "*FQ"),
                 TableCell("Q", '/', "/FQ"),
                 TableCell("Q", ')', ''),
                 TableCell("Q", '$', ''),
                 TableCell('F', '(', '(E)'),
                 TableCell('F', 'n', 'n'),
                 TableCell('F', 'i', 'i'),
                 ]
        table = LL1Table(non_terminals, terminals, cells)

        self._check_analyze(table, "n+i*n$", "E")
        self._check_analyze(table, "n+i*n+n*i$", "E")
        self._check_analyze(table, "n+i$*n$", "E", exception=SyntaxError)
        self._check_analyze(table, "n+i*n$i", "E", exception=SyntaxError)
        self._check_analyze(table, "n+n+i*i/n*n$", "E")

    def test_case4(self) -> None:
        """Test for parse tree construction."""
        terminals = {"(", ")", "i", "+", "*", "$"}
        non_terminals = {"E", "T", "X", "Y"}
        cells = [TableCell('E', '(', 'TX'),
                 TableCell('E', 'i', 'TX'),
                 TableCell('T', '(', '(E)'),
                 TableCell('T', 'i', 'iY'),
                 TableCell('X', '+', '+E'),
                 TableCell('X', ')', ''),
                 TableCell('X', '$', ''),
                 TableCell('Y', '*', '*T'),
                 TableCell('Y', '+', ''),
                 TableCell('Y', ')', ''),
                 TableCell('Y', '$', '')]
        table = LL1Table(non_terminals, terminals, cells)

        t01 = ParseTree("λ")
        t02 = ParseTree("X", [t01])
        t03 = ParseTree("λ")
        t04 = ParseTree("Y", [t03])
        t05 = ParseTree("i")
        t06 = ParseTree("T", [t05, t04])
        t07 = ParseTree("*")
        t08 = ParseTree("Y", [t07, t06])
        t09 = ParseTree("i")
        t10 = ParseTree("T", [t09, t08])
        tree = ParseTree("E", [t10, t02])

        self._check_parse_tree(table, "i*i$", "E", tree)

    def test_case5(self) -> None:
        """Test for parse tree construction."""
        terminals = {"(", ")", "i", "+", "*", "$"}
        non_terminals = {"E", "T", "X", "Y"}
        cells = [TableCell('E', '(', 'TX'),
                 TableCell('E', 'i', 'TX'),
                 TableCell('T', '(', '(E)'),
                 TableCell('T', 'i', 'iY'),
                 TableCell('X', '+', '+E'),
                 TableCell('X', ')', ''),
                 TableCell('X', '$', ''),
                 TableCell('Y', '*', '*T'),
                 TableCell('Y', '+', ''),
                 TableCell('Y', ')', ''),
                 TableCell('Y', '$', '')]
        table = LL1Table(non_terminals, terminals, cells)

        t17 = ParseTree("λ")
        t16 = ParseTree("X", [t17])
        t15 = ParseTree("λ")
        t14 = ParseTree("Y", [t15])
        t13 = ParseTree("i")
        t12 = ParseTree("T", [t13, t14])
        t11 = ParseTree("E", [t12, t16])
        t01 = ParseTree("+")
        t02 = ParseTree("X", [t01, t11])
        t03 = ParseTree("λ")
        t04 = ParseTree("Y", [t03])
        t05 = ParseTree("i")
        t06 = ParseTree("T", [t05, t04])
        t07 = ParseTree("*")
        t08 = ParseTree("Y", [t07, t06])
        t09 = ParseTree("i")
        t10 = ParseTree("T", [t09, t08])
        tree = ParseTree("E", [t10, t02])

        self._check_parse_tree(table, "i*i+i$", "E", tree)

    def test_case6(self) -> None:
        """Test for parse tree construction."""
        terminals = {"(", ")", "i", "+", "*", "$"}
        non_terminals = {"E", "T", "X", "Y"}
        cells = [TableCell('E', '(', 'TX'),
                 TableCell('E', 'i', 'TX'),
                 TableCell('T', '(', '(E)'),
                 TableCell('T', 'i', 'iY'),
                 TableCell('X', '+', '+E'),
                 TableCell('X', ')', ''),
                 TableCell('X', '$', ''),
                 TableCell('Y', '*', '*T'),
                 TableCell('Y', '+', ''),
                 TableCell('Y', ')', ''),
                 TableCell('Y', '$', '')]
        table = LL1Table(non_terminals, terminals, cells)

        t36 = ParseTree("λ")
        t35 = ParseTree("Y", [t36])
        t34 = ParseTree("i")
        t33 = ParseTree("T", [t34, t35])
        t32 = ParseTree("*")
        t31 = ParseTree("Y", [t32, t33])
        t30 = ParseTree("i")
        t29 = ParseTree("λ")
        t28 = ParseTree("X", [t29])
        t27 = ParseTree("T", [t30, t31])
        t25 = ParseTree("E", [t27, t28])
        t26 = ParseTree(")")
        t24 = ParseTree("(")
        t23 = ParseTree("λ")
        t22 = ParseTree("X", [t23])
        t21 = ParseTree("T", [t24, t25, t26])
        t20 = ParseTree("E", [t21, t22])
        t19 = ParseTree("+")
        t16 = ParseTree("X", [t19, t20])
        t15 = ParseTree("λ")
        t14 = ParseTree("Y", [t15])
        t13 = ParseTree("i")
        t12 = ParseTree("T", [t13, t14])
        t11 = ParseTree("E", [t12, t16])
        t01 = ParseTree("+")
        t02 = ParseTree("X", [t01, t11])
        t03 = ParseTree("λ")
        t04 = ParseTree("Y", [t03])
        t05 = ParseTree("i")
        t06 = ParseTree("T", [t05, t04])
        t07 = ParseTree("*")
        t08 = ParseTree("Y", [t07, t06])
        t09 = ParseTree("i")
        t10 = ParseTree("T", [t09, t08])
        tree = ParseTree("E", [t10, t02])

        self._check_parse_tree(table, "i*i+i+(i*i)$", "E", tree)

    def test_case7(self) -> None:
        """Test for syntax analysis from grammar.
            Falla porque no es LL(1) al haber varios
            reglas en una misma casillas"""

        grammar_str = """
        X -> I*AD
        I -> A*I
        I -> a
        I ->
        A -> aa*A
        A -> a
        A ->
        D -> *
        D ->
        """

        grammar = GrammarFormat.read(grammar_str)

        self._check_analyze_from_grammar_2(grammar, "a$", "X")

    def test_case8(self) -> None:
        """Test for syntax analysis from grammar.
            Falla porque no es LL(1) al haber varios
            reglas en una misma casillas"""

        grammar_str = """
        N -> F
        N -> n
        F -> TR
        T -> t
        T ->
        R -> nA
        R -> A
        A -> aS
        S -> a
        S ->
        """

        grammar = GrammarFormat.read(grammar_str)

        self._check_analyze_from_grammar_2(grammar, "tna$", "N")

    def test_case9(self) -> None:
        """Test for syntax analysis from grammar.
            Falla porque no es LL(1) al haber varios
            reglas en una misma casillas"""

        grammar_str = """
        P -> bZe
        Z -> S;Z
        Z ->
        S -> DV
        D -> i
        D -> f
        V -> dU
        U -> ,V
        U ->
        """

        grammar = GrammarFormat.read(grammar_str)

        self._check_analyze_from_grammar(grammar, "bfd,d;e$", "P")

    def test_case10(self) -> None:
        """Test for syntax analysis from grammar."""
        grammar_str = """
        A -> I=E
        I -> iX
        X ->
        X -> [E]
        E -> k
        E -> i
        E -> f(L)
        L -> ER
        R ->
        R -> ,L
        """

        grammar = GrammarFormat.read(grammar_str)
        table = grammar.get_ll1_table()

        self._check_analyze_from_grammar(grammar, "i=f(k)$", "A")

        t10 = ParseTree(")")
        t14 = ParseTree("λ")
        t13 = ParseTree("R", [t14])
        t12 = ParseTree("k")
        t11 = ParseTree("E", [t12])
        t09 = ParseTree("L", [t11, t13])
        t08 = ParseTree("(")
        t07 = ParseTree("f")
        t06 = ParseTree("E", [t07, t08, t09, t10])
        t05 = ParseTree("=")
        t04 = ParseTree("λ")
        t03 = ParseTree("X", [t04])
        t02 = ParseTree("i")
        t01 = ParseTree("I", [t02, t03])
        tree = ParseTree("A", [t01, t05, t06])

        self._check_parse_tree(table, "i=f(k)$", "A", tree)

if __name__ == '__main__':
    unittest.main()


