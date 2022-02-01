"""Test evaluation of ej3."""
import unittest
from abc import ABC

from automata.automaton import FiniteAutomaton
from automata.utils import AutomataFormat, deterministic_automata_isomorphism


class TestTransform_Ej3(ABC, unittest.TestCase):

    def _check_transform(
        self,
        automaton: FiniteAutomaton,
        expected: FiniteAutomaton,
    ) -> None:

        transformed = automaton.to_deterministic()
        equiv_map = deterministic_automata_isomorphism(
            expected,
            transformed,
        )

        self.assertTrue(equiv_map is not None)

    def test_case1(self) -> None:
        """Test Case 1. En este test se comprueba el funcionamiento
            cuando desde el estado q0 no hay ninguna transición lambda. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2 final

            --> q0
            q0 -1-> q1
            q1 -0-> q1
            q1 -1-> q1
            q1 -1-> q2
            q2 -0-> q2
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q1q2 final
            empty

            --> q0
            q0 -0-> empty
            q0 -1-> q1
            q1 -0-> q1
            q1 -1-> q1q2
            q1q2 -0-> q1q2
            q1q2 -1-> q1q2
            empty -0-> empty
            empty -1-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case2(self) -> None:
        """Test Case 2. En este test se comprueba que desde el
            el estado inicial se puede acceder con lambdas a otros
            y por tanto el estado inicial del AFD contendrá a más
            estados que q0. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2
            q3 final

            --> q0
            q0 --> q1
            q1 --> q2
            q1 -1-> q3
            q1 -0-> q2
            q2 --> q3
            q3 -1-> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q1q2q3 final
            q2q3 final
            q1q2q3 final
            empty

            --> q0q1q2q3
            q0q1q2q3 -0-> q2q3
            q0q1q2q3 -1-> q1q2q3
            q2q3 -0-> empty
            q2q3 -1-> q1q2q3
            q1q2q3 -0-> q2q3
            q1q2q3 -1-> q1q2q3
            empty -0-> empty
            empty -1-> empty
        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case3(self) -> None:
        """Test Case 3. En este test se comprueba que no se 
            genera el estado sumidero puesto que todos los 
            estados resultantes tiene todas sus transiciones
            definidas hacia algún estado. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2
            q3 final

            --> q0
            q0 -0-> q1
            q0 -1-> q0
            q1 -0-> q2
            q1 -1-> q3
            q1 -1->q1
            q2 -0-> q2
            q2 -1-> q3
            q2 -0->q1
            q3 -1-> q1
            q3 -0-> q2
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2
            q3 final
            q1q3 final
            q1q2

            --> q0
            q0 -0-> q1
            q0 -1-> q0
            q1 -0-> q2
            q1 -1-> q1q3
            q2 -0-> q1q2
            q2 -1-> q3
            q3 -0-> q2
            q3 -1-> q1
            q1q3 -0-> q2
            q1q3 -1-> q1q3
            q1q2 -0-> q1q2
            q1q2 -1-> q1q3

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case4(self) -> None:
        """Test Case 4. En este test se comprueba que si se 
            genera el estado sumidero cuando alguno de los
            estados del AFD no tiene transición hacia
            ningún otro estado. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2 final

            --> q0
            q0 -0-> q1
            q1 -1-> q2
            q1 -1-> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q1q2 final
            empty

            --> q0
            q0 -0-> q1
            q0 -1-> empty
            q1 -0-> empty
            q1 -1-> q1q2
            q1q2 -0-> empty
            q1q2 -1-> q1q2
            empty -0-> empty
            empty -1-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case5(self) -> None:
        """Test Case 5. En este test se comprueba que pese
            a que desde q0 no hay transiciones lambdas, si 
            otro estado las tiene, despues de consumir un 
            simbolo tambien hara transicion con lambda. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2 final
            q3

            --> q0
            q0 -0-> q1
            q1 -1-> q2
            q1 -1-> q1
            q1 --> q3
            q3 -0-> q2
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q2 final
            q1q3
            q1q2q3 final
            empty

            --> q0
            q0 -0-> q1q3
            q0 -1-> empty
            q2 -0-> empty
            q2 -1-> empty
            q1q3 -0-> q2
            q1q3 -1-> q1q2q3
            q1q2q3 -0-> q2
            q1q2q3 -1-> q1q2q3
            empty -0-> empty
            empty -1-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

if __name__ == '__main__':
    unittest.main()
