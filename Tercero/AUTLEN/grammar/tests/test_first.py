import unittest
from typing import AbstractSet

from grammar.grammar import Grammar
from grammar.utils import GrammarFormat

class TestFirst(unittest.TestCase):
    def _check_first(
        self,
        grammar: Grammar,
        input_string: str,
        first_set: AbstractSet[str],
    ) -> None:
        with self.subTest(
            string=f"First({input_string}), expected {first_set}",
        ):
            computed_first = grammar.compute_first(input_string)
            self.assertEqual(computed_first, first_set)

    def test_case1(self) -> None:
        """Test Case 1."""
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
        self._check_first(grammar, "E", {'(', 'i'})
        self._check_first(grammar, "T", {'(', 'i'})
        self._check_first(grammar, "X", {'', '+'})
        self._check_first(grammar, "Y", {'', '*'})
        self._check_first(grammar, "", {''})
        self._check_first(grammar, "Y+i", {'+', '*'})
        self._check_first(grammar, "YX", {'+', '*', ''})
        self._check_first(grammar, "YXT", {'+', '*', 'i', '('})

    def test_case2(self) -> None:
        """Test Case 2."""
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
        self._check_first(grammar, "X", {'a', '*'})
        self._check_first(grammar, "I", {'a', '*', ''})
        self._check_first(grammar, "A", {'', 'a'})
        self._check_first(grammar, "D", {'', '*'})

    def test_case3(self) -> None:
        """Test Case 3."""
        grammar_str = """
        A -> BCD
        B -> <
        B ->
        C -> 0C;
        C -> 1C;
        D -> 0>
        D -> 1>
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "A", {'<', '0', '1'})
        self._check_first(grammar, "B", {'<', ''})
        self._check_first(grammar, "C", {'0', '1'})
        self._check_first(grammar, "D", {'0', '1'})

    def test_case4(self) -> None:
        """Test Case 4."""
        grammar_str = """
        T -> FGH
        F -> Gb
        F ->
        G -> Nd
        G ->
        H -> aA
        H ->
        N -> 0N
        N -> 1N
        N ->
        A -> a
        A ->
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "T", {'0', '1', 'd', 'b', 'a', ''})
        self._check_first(grammar, "F", {'0', '1', 'd', 'b', ''})
        self._check_first(grammar, "G", {'0', '1', 'd', ''})
        self._check_first(grammar, "H", {'a', ''})
        self._check_first(grammar, "N", {'0', '1', ''})
        self._check_first(grammar, "A", {'a', ''})

    def test_case5(self) -> None:
        """Test Case 5."""
        grammar_str = """
        P -> bZe
        Z -> S;Z
        Z ->
        S -> DV
        D -> i
        D -> f
        V -> yU
        U -> ,V
        U ->
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "P", {'b'})
        self._check_first(grammar, "Z", {'i', 'f', ''})
        self._check_first(grammar, "S", {'i', 'f'})
        self._check_first(grammar, "D", {'i', 'f'})
        self._check_first(grammar, "V", {'y'})
        self._check_first(grammar, "U", {',', ''})

    def test_case6(self) -> None:
        """Test Case 6."""
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
        self._check_first(grammar, "A", {'i'})
        self._check_first(grammar, "I", {'i'})
        self._check_first(grammar, "X", {'', '['})
        self._check_first(grammar, "E", {'k', 'i', 'f'})
        self._check_first(grammar, "L", {'k', 'i', 'f'})
        self._check_first(grammar, "R", {',', ''})

    def test_case7(self) -> None:
        """Test Case 7."""
        grammar_str = """
        A -> BXB
        X -> e
        X -> ,
        X -> .
        B -> 0B
        B -> 1B
        B ->
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "A", {'1', '0', 'e', '.', ','})
        self._check_first(grammar, "B", {'', '1', '0'})
        self._check_first(grammar, "X", {'e', '.', ','})

    def test_case8(self) -> None:
        """Test Case 8."""
        grammar_str = """
        S -> aBCd
        S -> aS
        B -> Bb
        B -> b
        C -> cBd
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "S", {'a'})
        self._check_first(grammar, "B", {'b'})
        self._check_first(grammar, "C", {'c'})

    def test_case9(self) -> None:
        """Test Case 9."""
        grammar_str = """
        X -> Ye
        X -> eYZf
        Y -> g
        Y -> Yg
        Z -> h
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "X", {'g', 'e'})
        self._check_first(grammar, "Y", {'g'})
        self._check_first(grammar, "Z", {'h'})

    def test_case10(self) -> None:
        """Test Case 10."""
        grammar_str = """
        E -> (L)
        E -> a
        L -> L,E
        L -> E
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "E", {'a', '('})
        self._check_first(grammar, "L", {'a', '('})

    def test_case11(self) -> None:
        """Test Case 11."""
        grammar_str = """
        Q -> fXY
        X -> cQ
        X ->
        Y -> iQ
        Y ->
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "Q", {'f'})
        self._check_first(grammar, "X", {'c', ''})
        self._check_first(grammar, "Y", {'i', ''})

    def test_case12(self) -> None:
        """Test Case 12."""
        grammar_str = """
        S -> L=R
        S -> R
        L -> *R
        L -> i
        R -> L
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "L", {'*', 'i'})
        self._check_first(grammar, "R", {'*', 'i'})
        self._check_first(grammar, "S", {'*', 'i'})

if __name__ == '__main__':
    unittest.main()