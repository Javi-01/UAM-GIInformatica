"""Test evaluation of ej4."""
import unittest
from abc import ABC

from automata.automaton import FiniteAutomaton
from automata.utils import AutomataFormat, deterministic_automata_isomorphism


class TestTransform_Ej4(ABC, unittest.TestCase):

    def _check_transform_minimize(
        self,
        automaton: FiniteAutomaton,
        expected: FiniteAutomaton,
    ) -> None:
        """Test that the transformed automaton is as the expected one."""
        transformed = automaton.to_minimized()
        equiv_map = deterministic_automata_isomorphism(
            expected,
            transformed,
        )

        self.assertTrue(equiv_map is not None)

    def test_case1(self) -> None:
        """Test Case 1. En este test se comprueba que la 
            minimizacion la hace correctamente cuando
            el AFD solo tiene un estado final y por tanto
            desde el principio, este es distinguible
            del resto y además entre los estados indistinguibles
            hay estados distinguibles entre ellos. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1 final
            q2
            q3

            --> q0
            q0 -0-> q2
            q0 -1-> q1
            q1 -0-> q2
            q1 -1-> q1
            q2 -1-> q2
            q2 -0-> q3
            q3 -0-> q2
            q3 -1-> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q3
            q1 final
            q2

            --> q0q3
            q0q3 -0-> q2
            q0q3 -1-> q1
            q1 -0-> q2
            q1 -1-> q1
            q2 -0-> q0q3
            q2 -1-> q2
        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case2(self) -> None:
        """Test Case 2. En este test se comprueba que la
            minimizacion la hace correctamente cuando
            el AFD tiene varios estados finales
            y además entre esos estados finales, ambos
            son distinguibles uno del otro. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1 final
            q2 final
            q3

            --> q0
            q0 -0-> q2
            q0 -1-> q1
            q1 -0-> q2
            q1 -1-> q1
            q2 -1-> q2
            q2 -0-> q3
            q3 -0-> q2
            q3 -1-> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q3
            q1 final
            q2 final

            --> q0q3
            q0q3 -0-> q2
            q0q3 -1-> q1
            q1 -0-> q2
            q1 -1-> q1
            q2 -0-> q0q3
            q2 -1-> q2
        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case3(self) -> None:
        """Test Case 3. En este test se comprueba que la 
            minimizacion la hace correctamente cuando
            todos los estados entre estadis distinguibles 
	    se forman clases 2 a 2 entre los estados finales. """

        automaton_str = """
        Automaton:
            Symbols: 01

            q0 final
            q1 final
            q2 final
            q3 final
            q4

            --> q0
            q0 -0-> q1
            q0 -1-> q2
            q1 -0-> q1
            q1 -1-> q3
            q2 -0-> q1
            q2 -1-> q4
            q3 -0-> q1
            q3 -1-> q4
            q4 -0-> q4
            q4 -1-> q4
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q1 final
            q2q3 final
            q4

            --> q0q1
            q0q1 -0-> q0q1
            q0q1 -1-> q2q3
            q2q3 -0-> q0q1
            q2q3 -1-> q4
            q4 -0-> q4
            q4 -1-> q4
        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case4(self) -> None:
        """Test Case 4. En este test se comprueba que la
            minimizacion la hace correctamente cuando
            el AFD tiene estados inaccesibles, 
            en este caso el estado q4 es innacesible"""

        automaton_str = """
        Automaton:
            Symbols: 01

            q0 final
            q1
            q2
            q3 final
            q4

            --> q0
            q0 -0-> q3
            q0 -1-> q2
            q1 -0-> q1
            q1 -1-> q2
            q2 -1-> q1
            q2 -0-> q2
            q3 -0-> q2
            q3 -1-> q0
            q4 -0-> q3
            q4 -1-> q4
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0 final
            q1q2
            q3 final

            --> q0
            q0 -0-> q3
            q0 -1-> q1q2
            q1q2 -0-> q1q2
            q1q2 -1-> q1q2
            q3 -0-> q1q2
            q3 -1-> q0
        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)
        
    def test_case5(self) -> None:
        """Test Case 5. En este test se comprueba que la
            minimizacion la hace correctamente cuando
            el algoritmo llega hasta un Q/Ex 
	    donde las cadenas que se comparan para cada
	    cada estado de una clase son de al menos longitud
	    3, en este caso se comprueba que el algoritmo llega
	    hasta Q/E3"""

        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2
            q3
            q4
            q5
            q6
            q7 final

            --> q0
            q0 -0-> q1
            q0 -1-> q6
            q1 -0-> q2
            q1 -1-> q3
            q2 -0-> q4
            q2 -1-> q2
            q3 -0-> q5
            q3 -1-> q3
            q4 -0-> q4
            q4 -1-> q5
            q5 -0-> q5
            q5 -1-> q4
            q6 -0-> q6
            q6 -1-> q7
            q7 -0-> q0
            q7 -1-> q7
            
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q1q2q3q4q5
            q6
            q7 final

            --> q0
            q0 -0-> q1q2q3q4q5
            q0 -1-> q6
            q1q2q3q4q5 -0-> q1q2q3q4q5
            q1q2q3q4q5 -1-> q1q2q3q4q5
            q6 -0-> q6
            q6 -1-> q7
            q7 -0-> q0
            q7 -1-> q7

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

if __name__ == '__main__':
    unittest.main()
