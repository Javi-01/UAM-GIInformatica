"""Evaluation of automata."""
from typing import Set

from automata.automaton import FiniteAutomaton, State
from automata.interfaces import AbstractFiniteAutomatonEvaluator


class FiniteAutomatonEvaluator(
    AbstractFiniteAutomatonEvaluator[FiniteAutomaton, State],
):
    """Evaluator of an automaton."""

    def process_symbol(self, symbol: str) -> None:

        set_symbol = set()

        if symbol not in self.automaton.symbols:
            raise ValueError("Simbolo no aceptado por el lenguaje del automata")

        for state in self.current_states:
            for transitions in self.automaton.transitions:
                if transitions.initial_state == state and transitions.symbol == symbol:
                    set_symbol.add(transitions.final_state)

        self._complete_lambdas(set_symbol)
        self.current_states = set_symbol

        return

    def _complete_lambdas(self, set_to_complete: Set[State]) -> None:
        set_lamda = set()

        for state in set_to_complete:
            for transitions in self.automaton.transitions:
                if transitions.initial_state == state and transitions.symbol == None:
                    set_lamda.add(transitions.final_state)

        if len(set_lamda) != 0:
            self._complete_lambdas(set_lamda)

        set_to_complete.update(set_lamda)
        return
    
    def is_accepting(self) -> bool:

        for state in self.current_states:
            if state.is_final:
                return True

        return False
