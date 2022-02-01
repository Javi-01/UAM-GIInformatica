"""Test evaluation of automatas."""
import unittest
from abc import ABC

from automata.automaton import FiniteAutomaton
from automata.utils import AutomataFormat, deterministic_automata_isomorphism


class TestTransform(ABC, unittest.TestCase):
    """Base class for string acceptance tests."""

    def _check_transform(
        self,
        automaton: FiniteAutomaton,
        expected: FiniteAutomaton,
    ) -> None:
        """Test that the transformed automaton is as the expected one."""
        transformed = automaton.to_deterministic()
        equiv_map = deterministic_automata_isomorphism(
            expected,
            transformed,
        )

        self.assertTrue(equiv_map is not None)

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
        """Test Case 1."""
        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            qf final

            --> q0
            q0 -0-> qf
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            qf final
            empty

            --> q0
            q0 -0-> qf
            q0 -1-> empty
            qf -0-> empty
            qf -1-> empty
            empty -0-> empty
            empty -1-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case2(self) -> None:
        """Test Case 2."""
        automaton_str = """
        Automaton:
            Symbols: ab

            q0
            q1
            q2
            q3 final

            --> q0
            q0 -a-> q0
            q0 -b-> q0
            q0 -a-> q1
            q1 -b-> q2
            q2 -a-> q3

        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: ab

            q0
            q0q1
            q0q2
            q0q1q3 final

            --> q0
            q0 -b-> q0
            q0 -a-> q0q1
            q0q1 -a-> q0q1
            q0q1 -b-> q0q2
            q0q2 -b-> q0
            q0q2 -a-> q0q1q3
            q0q1q3 -b-> q0q2
            q0q1q3 -a-> q0q1

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case3(self) -> None:
        """Test Case 3."""
        automaton_str = """
        Automaton:
            Symbols: abc

            q0
            q1
            q2
            q3
            q4
            q5
            q6
            q7
            qf final

            --> q0
            q0 --> qf
            q0 --> q1
            q1 --> q2
            q1 --> q5
            q2 -a-> q3
            q3 -b-> q4
            q5 -c-> q6
            q4 --> q7
            q6 --> q7
            q7 --> qf
            q7 --> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: abc

            q3
            q0q1q2q5qf final
            q1q2q5q6q7qf final
            q1q2q4q5q7qf final
            empty

            --> q0q1q2q5qf
            q0q1q2q5qf -a-> q3
            q0q1q2q5qf -b-> empty
            q0q1q2q5qf -c-> q1q2q5q6q7qf
            q3 -a-> empty
            q3 -b-> q1q2q4q5q7qf
            q3 -c-> empty
            q1q2q5q6q7qf -a-> q3
            q1q2q5q6q7qf -b-> empty
            q1q2q5q6q7qf -c-> q1q2q5q6q7qf
            q1q2q4q5q7qf -a-> q3
            q1q2q4q5q7qf -b-> empty
            q1q2q4q5q7qf -c-> q1q2q5q6q7qf
            empty -a-> empty
            empty -b-> empty
            empty -c-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case4(self) -> None:
        """Test Case 4."""
        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            qf final

            --> q0
            q0 -1-> q0
            q0 -0-> q0
            q0 -1-> q1
            q1 -1-> qf
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q0q1
            q0q1qf final

            --> q0
            q0 -0-> q0
            q0 -1-> q0q1
            q0q1 -0-> q0
            q0q1 -1-> q0q1qf
            q0q1qf -1-> q0q1qf
            q0q1qf -0-> q0

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case5(self) -> None:
        """Test Case 5."""
        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2
            q3
            q4
            q5
            q7
            q6 final

            --> q0
            q0 --> q1
            q0 --> q2
            q1 -1-> q5
            q1 -1-> q3
            q2 -0-> q4
            q5 --> q7
            q5 -0-> q4
            q5 -0-> q3
            q7 --> q6
            q4 -1-> q6
            
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q1q2
            q4
            q6 final
            q3q4
            q3q5q6q7 final
            empty

            --> q0q1q2
            q0q1q2 -0-> q4
            q0q1q2 -1-> q3q5q6q7
            q4 -1-> q6
            q4 -0-> empty
            q3q5q6q7 -0-> q3q4
            q3q5q6q7 -1-> empty
            q3q4 -1-> q6
            q3q4 -0-> empty
            q6 -1-> empty
            q6 -0-> empty
            empty -1-> empty
            empty -0-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case6(self) -> None:
        """Test Case 6."""
        automaton_str = """
        Automaton:
            Symbols: .+-0123456789

            q0
            q1
            q2
            q3
            q4
            q5 final

            --> q0
            q0 -+-> q1
            q0 ---> q1
            q0 --> q1
            q1 -0-> q1
            q1 -1-> q1
            q1 -2-> q1
            q1 -3-> q1
            q1 -4-> q1
            q1 -5-> q1
            q1 -6-> q1
            q1 -7-> q1
            q1 -8-> q1
            q1 -9-> q1
            q1 -0-> q4
            q1 -1-> q4
            q1 -2-> q4
            q1 -3-> q4
            q1 -4-> q4
            q1 -5-> q4
            q1 -6-> q4
            q1 -7-> q4
            q1 -8-> q4
            q1 -9-> q4
            q1 -.-> q2
            q2 -0-> q3
            q2 -1-> q3
            q2 -2-> q3
            q2 -3-> q3
            q2 -4-> q3
            q2 -5-> q3
            q2 -6-> q3
            q2 -7-> q3
            q2 -8-> q3
            q2 -9-> q3
            q4 -.-> q3
            q3 --> q5
            q3 -0-> q3
            q3 -1-> q3
            q3 -2-> q3
            q3 -3-> q3
            q3 -4-> q3
            q3 -5-> q3
            q3 -6-> q3
            q3 -7-> q3
            q3 -8-> q3
            q3 -9-> q3

        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: .+-0123456789

            q0q1
            q1
            q2
            q1q4
            q2q3q5 final
            q3q5 final
            empty

            --> q0q1
            q0q1 -+-> q1
            q0q1 ---> q1
            q0q1 -.-> q2
            q0q1 -0-> q1q4
            q0q1 -1-> q1q4
            q0q1 -2-> q1q4
            q0q1 -3-> q1q4
            q0q1 -4-> q1q4
            q0q1 -5-> q1q4
            q0q1 -6-> q1q4
            q0q1 -7-> q1q4
            q0q1 -8-> q1q4
            q0q1 -9-> q1q4
            q1 -+-> empty
            q1 ---> empty
            q1 -.-> q2
            q1 -0-> q1q4
            q1 -1-> q1q4
            q1 -2-> q1q4
            q1 -3-> q1q4
            q1 -4-> q1q4
            q1 -5-> q1q4
            q1 -6-> q1q4
            q1 -7-> q1q4
            q1 -8-> q1q4
            q1 -9-> q1q4
            q2 -+-> empty
            q2 ---> empty
            q2 -.-> empty
            q2 -0-> q3q5
            q2 -1-> q3q5
            q2 -2-> q3q5
            q2 -3-> q3q5
            q2 -4-> q3q5
            q2 -5-> q3q5
            q2 -6-> q3q5
            q2 -7-> q3q5
            q2 -8-> q3q5
            q2 -9-> q3q5
            q1q4 -+-> empty
            q1q4 ---> empty
            q1q4 -.-> q2q3q5
            q1q4 -0-> q1q4
            q1q4 -1-> q1q4
            q1q4 -2-> q1q4
            q1q4 -3-> q1q4
            q1q4 -4-> q1q4
            q1q4 -5-> q1q4
            q1q4 -6-> q1q4
            q1q4 -7-> q1q4
            q1q4 -8-> q1q4
            q1q4 -9-> q1q4
            q2q3q5 -+-> empty
            q2q3q5 ---> empty
            q2q3q5 -.-> empty
            q2q3q5 -0-> q3q5
            q2q3q5 -1-> q3q5
            q2q3q5 -2-> q3q5
            q2q3q5 -3-> q3q5
            q2q3q5 -4-> q3q5
            q2q3q5 -5-> q3q5
            q2q3q5 -6-> q3q5
            q2q3q5 -7-> q3q5
            q2q3q5 -8-> q3q5
            q2q3q5 -9-> q3q5
            q3q5 -+-> empty
            q3q5 ---> empty
            q3q5 -.-> empty
            q3q5 -0-> q3q5
            q3q5 -1-> q3q5
            q3q5 -2-> q3q5
            q3q5 -3-> q3q5
            q3q5 -4-> q3q5
            q3q5 -5-> q3q5
            q3q5 -6-> q3q5
            q3q5 -7-> q3q5
            q3q5 -8-> q3q5
            q3q5 -9-> q3q5
            empty -+-> empty
            empty ---> empty
            empty -.-> empty
            empty -0-> empty
            empty -1-> empty
            empty -2-> empty
            empty -3-> empty
            empty -4-> empty
            empty -5-> empty
            empty -6-> empty
            empty -7-> empty
            empty -8-> empty
            empty -9-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case7_minimize(self) -> None:
        """Test Case 7."""
        automaton_str = """
        Automaton:
            Symbols: 01

            A
            B
            C final
            D
            E
            F
            G
            H

            --> A
            A -0-> B
            A -1-> F
            B -0-> G
            B -1-> C
            F -0-> C
            F -1-> G
            C -0-> A
            C -1-> C
            G -1-> E
            G -0-> G
            E -1-> F
            E -0-> H
            H -1-> C
            H -0-> G
            D -1-> G
            D -0-> C
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            AE
            BH 
            F
            C final
            G

            --> AE
            AE -0-> BH
            AE -1-> F
            F -0-> C
            F -1-> G
            BH -0-> G
            BH -1-> C
            G -0-> G
            G -1-> AE
            C -1-> C
            C -0-> AE

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case8_minimize(self) -> None:
        """Test Case 8."""
        automaton_str = """
        Automaton:
            Symbols: abc

            A final
            B final
            C final
            D final
            E

            --> A
            A -a-> B
            A -c-> B
            A -b-> C
            B -a-> B
            B -c-> B
            B -b-> C
            C -a-> B
            C -b-> D
            C -c-> B
            D -a-> E
            D -b-> E
            D -c-> E
            E -a-> E
            E -b-> E
            E -c-> E
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: abc

            AB final
            D final
            E
            C final

            --> AB
            AB -a-> AB
            AB -c-> AB
            AB -b-> C
            C -a-> AB
            C -c-> AB
            C -b-> D
            D -a-> E
            D -c-> E
            D -b-> E
            E -a-> E
            E -c-> E
            E -b-> E

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case9_minimize(self) -> None:
        """Test Case 9."""
        automaton_str = """
        Automaton:
            Symbols: ab

            q0
            q1 final
            q2
            q3 final
            q4

            --> q0
            q0 -a-> q1
            q0 -b-> q3
            q1 -a-> q2
            q1 -b-> q1
            q3 -a-> q4
            q3 -b-> q3
            q2 -a-> q1
            q2 -b-> q2
            q4 -a-> q3
            q4 -b-> q4
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: ab

            q0
            q1q3 final
            q2q4

            --> q0
            q0 -a-> q1q3
            q0 -b-> q1q3
            q1q3 -a-> q2q4
            q1q3 -b-> q1q3
            q2q4 -a-> q1q3
            q2q4 -b-> q2q4

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case10_minimize(self) -> None:
        """Test Case 10."""
        automaton_str = """
        Automaton:
            Symbols: 01

            q0 final
            q1
            q2 final
            q3
            q4 final
            q5

            --> q0
            q0 -0-> q1
            q0 -1-> q1
            q1 -0-> q2
            q1 -1-> q2
            q2 -0-> q3
            q2 -1-> q3
            q3 -0-> q4
            q3 -1-> q4
            q4 -0-> q5
            q4 -1-> q5
            q5 -0-> q0
            q5 -1-> q0
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0q2q4 final
            q1q3q5

            --> q0q2q4
            q0q2q4 -0-> q1q3q5
            q0q2q4 -1-> q1q3q5
            q1q3q5 -0-> q0q2q4
            q1q3q5 -1-> q0q2q4

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case11_minimize(self) -> None:
        """Test Case 11."""
        automaton_str = """
        Automaton:
            Symbols: ab

            q0
            q1 final
            q2
            q3
            q4
            q5
            q6
            q7 final
            q8
            q9 final

            --> q0
            q0 -a-> q1
            q0 -b-> q6
            q1 -a-> q2
            q1 -b-> q3
            q2 -a-> q4
            q2 -b-> q2
            q3 -a-> q5
            q3 -b-> q3
            q4 -a-> q4
            q4 -b-> q8
            q5 -a-> q5
            q5 -b-> q4
            q6 -a-> q6
            q6 -b-> q7
            q7 -a-> q0
            q7 -b-> q7
            q8 -a-> q8
            q8 -b-> q9
            q9 -a-> q0
            q9 -b-> q9
            
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: ab

            q0
            q1 final
            q2
            q3
            q4
            q5
            q6q8
            q7q9 final
            

            --> q0
            q0 -a-> q1
            q0 -b-> q6q8
            q1 -a-> q2
            q1 -b-> q3
            q2 -a-> q4
            q2 -b-> q2
            q3 -a-> q5
            q3 -b-> q3
            q4 -a-> q4
            q4 -b-> q6q8
            q5 -a-> q5
            q5 -b-> q4
            q6q8 -a-> q6q8
            q6q8 -b-> q7q9
            q7q9 -a-> q0
            q7q9 -b-> q7q9
            

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform_minimize(automaton, expected)

    def test_case12(self) -> None:
        """Test Case 12."""
        automaton_str = """
        Automaton:
            Symbols: ab

            q1 final
            q2
            q3

            --> q1
            q1 --> q3
            q1 -b-> q2
            q2 -a-> q2
            q2 -a-> q3
            q2 -b-> q3
            q3 -a-> q1

        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: ab

            q1q3 final
            q2
            q2q3
            q3
            q1q2q3 final
            empty

            --> q1q3
            q1q3 -a-> q1q3
            q1q3 -b-> q2
            q2 -a-> q2q3
            q2 -b-> q3
            q2q3 -a-> q1q2q3
            q2q3 -b-> q3
            q3 -a-> q1q3
            q3 -b-> empty
            q1q2q3 -a-> q1q2q3
            q1q2q3 -b-> q2q3
            empty -a-> empty
            empty -b-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case3(self) -> None:
        """Test Case 3."""
        automaton_str = """
        Automaton:
            Symbols: abc

            q0
            q1
            q2
            q3
            q4
            q5
            q6
            q7
            qf final

            --> q0
            q0 --> qf
            q0 --> q1
            q1 --> q2
            q1 --> q5
            q2 -a-> q3
            q3 -b-> q4
            q5 -c-> q6
            q4 --> q7
            q6 --> q7
            q7 --> qf
            q7 --> q1
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: abc

            q3
            q0q1q2q5qf final
            q1q2q5q6q7qf final
            q1q2q4q5q7qf final
            empty

            --> q0q1q2q5qf
            q0q1q2q5qf -a-> q3
            q0q1q2q5qf -b-> empty
            q0q1q2q5qf -c-> q1q2q5q6q7qf
            q3 -a-> empty
            q3 -b-> q1q2q4q5q7qf
            q3 -c-> empty
            q1q2q5q6q7qf -a-> q3
            q1q2q5q6q7qf -b-> empty
            q1q2q5q6q7qf -c-> q1q2q5q6q7qf
            q1q2q4q5q7qf -a-> q3
            q1q2q4q5q7qf -b-> empty
            q1q2q4q5q7qf -c-> q1q2q5q6q7qf
            empty -a-> empty
            empty -b-> empty
            empty -c-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case13(self) -> None:
        """Test Case 13."""
        automaton_str = """
        Automaton:
            Symbols: 01

            q0
            q1
            q2 final

            --> q0
            q0 -1-> q0
            q0 -0-> q0
            q0 -0-> q1
            q1 -1-> q2
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: 01

            q0
            q0q1
            q0q2 final

            --> q0
            q0 -0-> q0q1
            q0 -1-> q0
            q0q1 -0-> q0q1
            q0q1 -1-> q0q2
            q0q2 -1-> q0
            q0q2 -0-> q0q1

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

    def test_case14(self) -> None:
        """Test Case 14."""
        automaton_str = """
        Automaton:
            Symbols: ab

            q0
            q1
            q2
            q3
            q4
            q5 final
            q6 final

            --> q0
            q0 --> q1
            q0 --> q3
            q1 --> q2
            q3 -a-> q4
            q2 --> q5
            q4 -b-> q5
            q4 --> q6
        """

        automaton = AutomataFormat.read(automaton_str)

        expected_str = """
        Automaton:
            Symbols: ab

            q0q1q2q3q5 final
            q4q6 final
            q5 final
            empty

            --> q0q1q2q3q5
            q0q1q2q3q5 -a-> q4q6
            q0q1q2q3q5 -b-> empty
            q4q6 -a-> empty
            q4q6 -b-> q5
            q5 -a-> empty
            q5 -b-> empty
            empty -a-> empty
            empty -b-> empty

        """

        expected = AutomataFormat.read(expected_str)

        self._check_transform(automaton, expected)

if __name__ == '__main__':
    unittest.main()
