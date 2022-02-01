import unittest
from typing import AbstractSet

from grammar.grammar import Grammar
from grammar.utils import GrammarFormat


class TestFollow(unittest.TestCase):
    def _check_follow(
        self,
        grammar: Grammar,
        symbol: str,
        follow_set: AbstractSet[str],
    ) -> None:
        with self.subTest(string=f"Follow({symbol}), expected {follow_set}"):
            computed_follow = grammar.compute_follow(symbol)
            self.assertEqual(computed_follow, follow_set)

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
        self._check_follow(grammar, "E", {'$', ')'})
        self._check_follow(grammar, "T", {'$', ')', '+'})
        self._check_follow(grammar, "X", {'$', ')'})
        self._check_follow(grammar, "Y", {'$', ')', '+'})

    def test_case2(self) -> None:
        """Test Case 2."""
        grammar_str = """
        E -> TX
        X -> +TX
        X -> -TX
        X ->
        T -> FY
        Y -> *FY
        Y -> /FY
        Y ->
        F -> (E)
        F -> n
        F -> i
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "E", {'(', 'i', 'n'})
        self._check_first(grammar, "X", {'+', '-', ''})
        self._check_first(grammar, "T", {'(', 'n', 'i'})
        self._check_first(grammar, "Y", {'', '*', '/'})
        self._check_first(grammar, "F", {'(', 'n', 'i'})
        # -------------------------------------------------------------
        self._check_follow(grammar, "E", {'$', ')'})
        self._check_follow(grammar, "X", {'$', ')'})
        self._check_follow(grammar, "T", {'$', ')', '+', '-'})
        self._check_follow(grammar, "Y", {'$', ')', '+', '-'})
        self._check_follow(grammar, "F", {'$', ')', '+', '-', '*', '/'})

    def test_case3(self) -> None:
        """Test Case 3."""
        grammar_str = """
        S -> fAB
        A ->
        A -> cS
        B ->
        B -> dS
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "S", {'f'})
        self._check_first(grammar, "A", {'c', ''})
        self._check_first(grammar, "B", {'d', ''})
        # -------------------------------------------------------------
        self._check_follow(grammar, "S", {'$', 'd'})
        self._check_follow(grammar, "A", {'$', 'd'})
        self._check_follow(grammar, "B", {'$', 'd'})

    def test_case4(self) -> None:
        """Test Case 4."""
        grammar_str = """
        E -> T+E
        E -> T
        T -> i*T
        T -> I
        T -> (E)
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "E", {'$', ')'})
        self._check_follow(grammar, "T", {'$', ')', '+'})

    def test_case5(self) -> None:
        """Test Case 5."""
        grammar_str = """
        S -> A
        S -> B
        A -> cA+b
        A -> a
        B -> cB+a
        B -> b
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_first(grammar, "S", {'a', 'b', 'c'})
        self._check_first(grammar, "A", {'c', 'a'})
        self._check_first(grammar, "B", {'c', 'b'})
        # -------------------------------------------------------------
        self._check_follow(grammar, "S", {'$'})
        self._check_follow(grammar, "A", {'$', '+'})
        self._check_follow(grammar, "B", {'$', '+'})

    def test_case6(self) -> None:
        """Test Case 6."""
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
        self._check_follow(grammar, "A", {'$'})
        self._check_follow(grammar, "B", {'$', 'e', '.', ','})
        self._check_follow(grammar, "X", {'$', '1', '0'})

    def test_case7(self) -> None:
        """Test Case 7."""
        grammar_str = """
        S -> aBCd
        S -> aS
        B -> Bb
        B -> b
        C -> cBd
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "S", {'$'})
        self._check_follow(grammar, "B", {'d', 'b', 'c'})
        self._check_follow(grammar, "C", {'d'})

    def test_case8(self) -> None:
        """Test Case 8."""
        grammar_str = """
        X -> Ye
        X -> eYZf
        Y -> g
        Y -> Yg
        Z -> h
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "X", {'$'})
        self._check_follow(grammar, "Y", {'g', 'e', 'h'})
        self._check_follow(grammar, "Z", {'f'})

    def test_case9(self) -> None:
        """Test Case 9."""
        grammar_str = """
        E -> (L)
        E -> a
        L -> L,E
        L -> E
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "E", {'$', ')', ','})
        self._check_follow(grammar, "L", {')', ','})

    def test_case10(self) -> None:
        """Test Case 10."""
        grammar_str = """
        Q -> fXY
        X -> cQ
        X ->
        Y -> iQ
        Y ->
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "Q", {'$', 'i'})
        self._check_follow(grammar, "X", {'$', 'i'})
        self._check_follow(grammar, "Y", {'$', 'i'})

    def test_case11(self) -> None:
        """Test Case 11."""
        grammar_str = """
        S -> L=R
        S -> R
        L -> *R
        L -> i
        R -> L
        """

        grammar = GrammarFormat.read(grammar_str)
        self._check_follow(grammar, "L", {'$', '='})
        self._check_follow(grammar, "R", {'$', '='})
        self._check_follow(grammar, "S", {'$'})

if __name__ == '__main__':
    unittest.main()
