"""Conversion from regex to automata."""
from typing import Collection
from automata.automaton import FiniteAutomaton, State, Transition
from automata.re_parser_interfaces import AbstractREParser


class REParser(AbstractREParser):
    """Class for processing regular expressions in Kleene's syntax."""

    def _create_automaton_empty(
        self,
    ) -> FiniteAutomaton:

        stateIni = State(name=str(self.state_counter), is_final=False)
        self.state_counter += 1

        stateFin = State(name=str(self.state_counter), is_final=True)
        self.state_counter += 1

        states = [stateIni]
        states.append(stateFin)

        automata = FiniteAutomaton(
            initial_state=stateIni,
            states=states,
            transitions=[],
            symbols=set()
        )

        return automata

    def _create_automaton_lambda(
        self,
    ) -> FiniteAutomaton:
        
        stateIni = State(name=str(self.state_counter), is_final=False)
        self.state_counter += 1

        stateFin = State(name=str(self.state_counter), is_final=True)
        self.state_counter += 1

        states = [stateIni]
        states.append(stateFin)

        automata = FiniteAutomaton(
            initial_state=stateIni,
            states=states,
            transitions=[Transition(initial_state=stateIni, symbol=None, final_state=stateFin)],
            symbols=set(None),
        )

        return automata

    def _create_automaton_symbol(
        self,
        symbol: str,
    ) -> FiniteAutomaton:
        
        stateIni = State(name=str(self.state_counter), is_final=False)
        self.state_counter += 1

        stateFin = State(name=str(self.state_counter), is_final=True)
        self.state_counter += 1

        states = [stateIni]
        states.append(stateFin)

        automata = FiniteAutomaton(
            initial_state=stateIni,
            states=states,
            transitions=[Transition(initial_state=stateIni, symbol=symbol, final_state=stateFin)],
            symbols=set(symbol)
        )

        return automata

    def _create_automaton_star(
        self,
        automaton: FiniteAutomaton,
    ) -> FiniteAutomaton:

        for i in range(0, len(automaton.states)):
            if automaton.states[i].is_final is True:
                state_final_aut = automaton.states[i]
            automaton.states[i].is_final = False

        stateIni = State(name=str(self.state_counter), is_final=False)
        self.state_counter += 1

        stateFin = State(name=str(self.state_counter), is_final=True)
        self.state_counter += 1

        trans_ini_to_fin = Transition(
                        initial_state=stateIni,
                        symbol=None,
                        final_state=stateFin
                        )

        trans_ini_to_aut = Transition(
                        initial_state=stateIni,
                        symbol=None,
                        final_state=automaton.initial_state
                        )

        trans_aut_to_fin = Transition(
                        initial_state=state_final_aut,
                        symbol=None,
                        final_state=stateFin
                        )

        trans_aut_fin_to_aut_ini = Transition(
                        initial_state=state_final_aut,
                        symbol=None,
                        final_state=automaton.initial_state
                        )

        transitions = [transition for transition in automaton.transitions]
        transitions.extend([trans_ini_to_fin, trans_ini_to_aut, trans_aut_fin_to_aut_ini, trans_aut_to_fin])       
        
        states = [states for states in automaton.states]
        states.extend([stateIni, stateFin])

        automata = FiniteAutomaton(
            initial_state=stateIni,
            states=states,
            symbols=set([symbol for symbol in automaton.symbols]),
            transitions=transitions
        )
        return automata


    def _create_automaton_union(
        self,
        automaton1: FiniteAutomaton,
        automaton2: FiniteAutomaton,
    ) -> FiniteAutomaton:
        
        for i in range(0, len(automaton1.states)):
            if automaton1.states[i].is_final:
                state_final_aut1 = automaton1.states[i]
            
            automaton1.states[i].is_final = True

        for i in range(0, len(automaton2.states)):
            if automaton2.states[i].is_final:
                state_final_aut2 = automaton2.states[i]

            automaton2.states[i].is_final = False
        
        stateIni = State(name=str(self.state_counter), is_final=False)
        self.state_counter += 1

        stateFin = State(name=str(self.state_counter), is_final=True)
        self.state_counter += 1

        trans_ini_to_aut1 = Transition(
                        initial_state=stateIni,
                        symbol=None,
                        final_state=automaton1.initial_state
                        )
        
        trans_ini_to_aut2 = Transition(
                        initial_state=stateIni,
                        symbol=None,
                        final_state=automaton2.initial_state
                        )

        trans_aut1_to_fin = Transition(
                        initial_state=state_final_aut1,
                        symbol=None,
                        final_state=stateFin
                        )
        
        trans_aut2_to_fin = Transition(
                        initial_state=state_final_aut2,
                        symbol=None,
                        final_state=stateFin
                        )

        symbols = set([symbol for symbol in automaton1.symbols])
        symbols.update([symbol for symbol in automaton2.symbols])

        transitions = [transition for transition in automaton1.transitions]
        transitions.extend([trans_ini_to_aut1, trans_ini_to_aut2, trans_aut1_to_fin, trans_aut2_to_fin])
        transitions.extend([transition for transition in automaton2.transitions])

        states = [state for state in automaton1.states]
        states.extend([stateIni, stateFin])
        states.extend([states for states in automaton2.states])

        automata = FiniteAutomaton(
            initial_state=stateIni,
            states=states,
            transitions=transitions,
            symbols=symbols
        )
        return automata


    def _create_automaton_concat(
        self,
        automaton1: FiniteAutomaton,
        automaton2: FiniteAutomaton,
    ) -> FiniteAutomaton:

        for i in range(0, len(automaton1.states)):
            if automaton1.states[i].is_final is True:
                state_final_aut = automaton1.states[i]
            automaton1.states[i].is_final = False

        for i in range(0, len(automaton2.states) - 1):
            automaton2.states[i].is_final = False
        
        trans_lambda = Transition(
                        initial_state=state_final_aut,
                        symbol=None,
                        final_state= automaton2.initial_state
                        )

        symbols = set([symbol for symbol in automaton1.symbols])
        symbols.update([symbol for symbol in automaton2.symbols])


        transitions = [transition for transition in automaton1.transitions]
        transitions.append(trans_lambda)
        transitions.extend([transition for transition in automaton2.transitions])
        

        states = [state for state in automaton1.states]
        states.extend([state for state in automaton2.states])
        
        automata = FiniteAutomaton(
            initial_state=automaton1.initial_state,
            states=states,        
            transitions=transitions,
            symbols=symbols
        )
        return automata

    