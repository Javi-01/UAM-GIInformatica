"""Automaton implementation."""
from re import T
from typing import Collection, List, Dict

from automata.interfaces import (
    AbstractFiniteAutomaton,
    AbstractState,
    AbstractTransition,
)


class State(AbstractState):
    """State of an automaton."""

    # You can add new attributes and methods that you think that make your
    # task easier, but you cannot change the constructor interface.


class Transition(AbstractTransition[State]):
    """Transition of an automaton."""

    # You can add new attributes and methods that you think that make your
    # task easier, but you cannot change the constructor interface.


class FiniteAutomaton(
    AbstractFiniteAutomaton[State, Transition],
):
    """Automaton."""

    def __init__(
        self,
        *,
        initial_state: State,
        states: Collection[State],
        symbols: Collection[str],
        transitions: Collection[Transition],
    ) -> None:
        super().__init__(
            initial_state=initial_state,
            states=states,
            symbols=symbols,
            transitions=transitions,
        )

        # Add here additional initialization code.
        # Do not change the constructor interface.

    def to_deterministic(
        self,
    ) -> "FiniteAutomaton":

        transitions = []
        states = []
        processed_states = []
        not_processed_states = []
        state_transitions_dict = {}
        initial_state_list = [self.initial_state]
        self._complete_lambdas(initial_state_list)

        # Creamos el dicc con todos los nuevos estados y a donde se puede mover con cada símbolo
        not_processed_states.append(initial_state_list)
        state_transitions_dict = self._process_symbols(not_processed_states, processed_states, None)

        # Añadimos los estados que han sido procesados
        self._add_states(processed_states, states)

        # Creamos las transiciones de la tabla de trasiciones por cada simbolo y estado al que transita
        self._complete_transitions(state_transitions_dict, states, transitions) 

        automaton = FiniteAutomaton(
            initial_state=states[0],
            symbols=set(symbol for symbol in self.symbols if symbol != None),
            states=states,
            transitions=transitions
        )

        return automaton

    def _process_symbols(self, 
        list_of_not_process_states: List[State], 
        list_of_processed_states: List[State], 
        dict:Dict
    ) -> Dict:
        
        tabla_transiciones = {}

        for set_not_procesesed_states in list_of_not_process_states:
            if set_not_procesesed_states not in list_of_processed_states:
                list_of_processed_states.append(set_not_procesesed_states)
                list_of_not_process_states.remove(set_not_procesesed_states)
                new_state = self._create_new_state(set_not_procesesed_states)
                tabla_transiciones[new_state.name] = {}
                for symbol in self.symbols:
                    dic_symbol_estados = {}
                    dic_symbol_estados[symbol] = []
                    transitions_with_symbol = []
                    for state in set_not_procesesed_states:
                        for transition in self.transitions:
                            if transition.initial_state == state and transition.symbol == symbol:
                                if self._state_in_automaton(transition.final_state, transitions_with_symbol) == False:
                                    transitions_with_symbol.append(transition.final_state)
                                    self._complete_lambdas(transitions_with_symbol)
                                self._complete_dict(dic_symbol_estados, transitions_with_symbol, symbol)
                    
                    #Una vez que tenemos el set completo con los estados a los que se puede mover desde new_state con un cierto symbol y lambda 
                    if len(dic_symbol_estados[symbol]) != 0:
                        if self._is_list_in_another(list_of_not_process_states, list_of_processed_states, dic_symbol_estados[symbol]) is True: 
                            list_of_not_process_states.append(dic_symbol_estados[symbol])
                    
                    if len(tabla_transiciones[new_state.name].values()) == 0:
                        tabla_transiciones[new_state.name] = dic_symbol_estados
                    else: 
                        tabla_transiciones[new_state.name][symbol] = dic_symbol_estados[symbol]       

        if len(list_of_not_process_states) > 0: 
            # Combinamos los diccionarios
            tabla_transiciones = {**tabla_transiciones, **self._process_symbols(list_of_not_process_states, list_of_processed_states, tabla_transiciones)}
        
        return tabla_transiciones

    def _complete_lambdas(self, 
        list_to_complete: List[State]) -> None:

            list_lamda = []

            for state in list_to_complete:
                for transitions in self.transitions:
                    if transitions.initial_state == state and transitions.symbol == None:
                        if self._state_in_automaton(transitions.final_state, list_lamda) == False:
                            list_lamda.append(transitions.final_state)

            if len(list_lamda) != 0:
                self._complete_lambdas(list_lamda)

            list_to_complete.extend(list_lamda)

            return

    def _create_new_state(self, 
        state_list:List[State]):
        
        state = State(
                name=self._create_name_new_state(state_list),
                is_final=self._final_state_in_list(state_list)
            )

        return state

    def _create_name_new_state(self, 
        state_list:List[State]):
        
        if len(state_list) == 0:
            return "empty"

        state_list.sort(key=lambda state: state.name)
        return "".join(state.name for state in state_list)

    def _final_state_in_list(self, 
        state_list:List[State]):
        
        for state in state_list:
            if state.is_final: 
                return True

        return False

    def _state_in_automaton(self, 
        state:State, 
        state_list:List[State]):

        if state in state_list:
            return True

        return False
    
    def _state_by_a_name(self, 
        name, 
        list_of_states:List[State]):
        
        for state in list_of_states:
            if state.name == name: 
                return state

        return None

    def _complete_dict(self, 
        dict:Dict, 
        states:List[State], 
        key):

        values = dict[key]

        for state in states: 
            if state not in values: 
                values.append(state)

        dict[key] = values  

    def _is_list_in_another(
        self, list_of_not_process_states:List[State], 
        list_of_processed_states:List[State], 
        states:List[State]):

        states.sort(key=lambda state: state.name)
        if states not in list_of_not_process_states and states not in list_of_processed_states:
            return True

        return False

    def _add_states(self, 
        list_of_processed_states:List[State], 
        states:List[State]):

        for state in list_of_processed_states:
            new_state = self._create_new_state(state)
            states.append(new_state)

        return

    def _complete_transitions(self, 
        state_transitions_dict : Dict, 
        states : List[State], 
        transitions : List[Transition]): 

        for key_state, column_set in state_transitions_dict.items():
            for key_symbol, set_states in column_set.items():
                state = self._create_new_state(set_states)
                if state not in states:
                    states.append(state)
                    
                # Creamos la nueva transition
                state_ini = self._state_by_a_name(key_state, states)
                if state_ini is not None:
                    transition = Transition(
                            initial_state=state_ini,
                            symbol=key_symbol, 
                            final_state=self._state_by_a_name(state.name, states)
                        )

                transitions.append(transition)

        # Creamos la transicion para los estados vacios
        empty_state = self._state_by_a_name("empty", states)
        if empty_state is not None:
            for symbol in self.symbols: 
                if symbol != None: 
                    transition = Transition(
                                initial_state=empty_state,
                                symbol=symbol, 
                                final_state=empty_state
                            )
                    transitions.append(transition)

        return

    def to_minimized(
        self,
    ) -> "FiniteAutomaton":

        list_of_not_proccessed = [self.initial_state]
        list_of_accessible = []
        clases_equivalencia = []
        index = 1

        # Buscamos los estados que son accesibles
        self._eliminar_estados_inaccesibles(list_of_not_proccessed, list_of_accessible)
        list_of_accessible.sort(key=lambda state: state.name)
        
        # Eliminamos del conjunto de estados y transiciones lo que 
        # esté relacionado con los estados no accesibles
        self._update_automata(list_of_accessible)

        # Crear la matriz revisando la diagonal inferior de la misma
        matriz = self._create_matriz()

        # Completamos la matriz de estados
        clases_equivalencia.append([state for state in self.states if state.is_final != True])
        clases_equivalencia.append([state for state in self.states if state.is_final == True])
        clases_equivalencia = self._completar_matriz_estados(matriz, clases_equivalencia, index)
        
        # Crear el nuevo automata
        states = []
        for clase in clases_equivalencia: 
            state = self._create_new_state(clase)
            if self.initial_state in clase:
                initial_state = state
            states.append(state)

        transitions = self._create_transitions(clases_equivalencia, states)

        automaton = FiniteAutomaton(
            initial_state=initial_state,
            symbols=set(symbol for symbol in self.symbols if symbol != None),
            states=states,
            transitions=transitions
        )

        return automaton 

    def _eliminar_estados_inaccesibles(self, list_of_not_proccessed : List[State], list_of_accessible : List[State]):

        for state in list_of_not_proccessed:
            list_of_accessible.append(state)
            for transition in self.transitions:
                if transition.initial_state == state:
                    if transition.final_state not in list_of_not_proccessed and transition.final_state not in list_of_accessible:
                        list_of_not_proccessed.append(transition.final_state)
            
            list_of_not_proccessed.remove(state)

        if len(list_of_not_proccessed) != 0:
            self._eliminar_estados_inaccesibles(list_of_not_proccessed, list_of_accessible)

        return

    def _update_automata(self, list_of_accessible : List[State]):

        set_states = [state for state in self.states if state in list_of_accessible]
        set_transitions = [transition for transition in self.transitions if transition.initial_state in list_of_accessible]

        self.states = set_states
        self.transitions = set_transitions

        return

    def _create_matriz(self): 

        dict_general = {}

        # Creamos la mitad inferiror de la matriz de estados 
        for state1 in self.states[1:]:
            dict_columnas = {}
            for state2 in self.states[:self.states.index(state1)]:
                dict_columnas[state2] = " "

            dict_general[state1] = dict_columnas

        # Si solo hay un estado final, este ya sera indistinguible
        final_states = [state for state in self.states if state.is_final == True]
        if len(final_states) == 1:
            for k1, v1 in dict_general.items(): 
                
                # Indicamos la fila de los estados distinguible con X
                if k1 in final_states:
                    for k2, v2 in v1.items(): 
                        if k2 not in final_states:
                            v2 = "X"
                            dict_general[k1][k2] = v2

                # Indicamos para el resto de estados no finales la columna que corresponde
                # a los estados finales como distinguible
                else: 
                    for k2, v2 in v1.items(): 
                        if k2 in final_states:
                            v2 = "X"
                            dict_general[k1][k2] = v2

        return dict_general 

    def _completar_matriz_estados(self, matriz_estados : Dict, clases_equivalencia : List[State], indice_cociente : int):

        is_clases_equivalencia_modified = False

        for state in self.states[:(len(self.states) - 1)]:
            for k1, v1 in matriz_estados.items():
                for k2, v2 in v1.items():
                    if k2 == state:
                        if k1 in self._clase_of_a_state(clases_equivalencia, state):
                            transitions_A = []
                            transitions_B = []
                            for symbol in self.symbols:
                                aux_A = [state]
                                list_A = self._process_symbol(aux_A, symbol, indice_cociente, 1)
                                if len(list_A) != 0:
                                    for s1 in list_A:
                                        transitions_A.append(s1)
                                aux_B = [k1]
                                list_B =  self._process_symbol(aux_B, symbol, indice_cociente, 1)
                                if len(list_B) != 0:
                                    for s2 in list_B:
                                        transitions_B.append(s2)
                            
                            if self._indistinguibles_states(transitions_A, transitions_B, clases_equivalencia) is False:
                                is_clases_equivalencia_modified = True
                                v2 = "X"
                                matriz_estados[k1][k2] = v2
                        else:
                            if v2 != "X":
                                v2 = "X"
                                matriz_estados[k1][k2] = v2

        # Vemos que casillas relacionadas no tienen X para formar las nuevas clases
        new_clases = []
        for state in self.states[:(len(self.states) - 1)]:                     
            for k1, v1 in matriz_estados.items():
                for k2, v2 in v1.items():
                    if v2 != "X" and state == k2:
                        clase = self._clase_of_a_state(new_clases, state)
                        if clase is not None:
                            if k1 not in clase: 
                                index = new_clases.index(clase)
                                clase.append(k1)
                                clase.sort(key=lambda state: state.name)
                                new_clases[index] = clase
                        else: 
                            clase = [k1, state]
                            clase.sort(key=lambda state: state.name)
                            new_clases.append(clase)

        clases_equivalencia = new_clases

        # Actualizamos el conjunto 
        for state in self.states: 
                if self._clase_of_a_state(clases_equivalencia, state) is None:
                    clases_equivalencia.append([state])

        # Comprobamos la condiciones de recursion
        if is_clases_equivalencia_modified is True:
            indice_cociente += 1
            clases_equivalencia = self._completar_matriz_estados(matriz_estados, clases_equivalencia, indice_cociente)

        return clases_equivalencia

    def _process_symbol(self, list_of_states : List[State], symbol: str, indice_cociente : int, num_recursividad : int) -> None:

        states = []

        if num_recursividad == 1:
            for state in list_of_states:
                for transitions in self.transitions:
                    if transitions.initial_state == state and transitions.symbol == symbol:
                        states.append(transitions.final_state)
        else: 
            for state in list_of_states:
                for s in self.symbols:
                    for transitions in self.transitions:
                        if transitions.initial_state == state and transitions.symbol == s:
                            states.append(transitions.final_state)

        indice_cociente -= 1
        if len(list_of_states) != 0 and indice_cociente != 0:
            list_of_states = states
            num_recursividad += 1
            states = self._process_symbol(list_of_states, symbol, indice_cociente, num_recursividad)

        return states

    def _clase_of_a_state (self, clases:List[State], state : State) : 

        for clase in clases:
            if state in clase: 
                    return clase
         
        return None

    def _indistinguibles_states(self, 
        transition_A : List[State], 
        transition_B : List[State], 
        clases:List[State]):

        # Primero comprobar si son las mismas transiciones
        # y en caso contrario ver si pertenecen a la misma clase
        for i in range(len(transition_A)):
            if transition_A[i] != transition_B[i]:
                # Obtenemos la clase de cada estados que se comparar y comprobamos que sea sea la misma
                # en caso de ser la misma se siguen comparando los símbolos y en caso contrario 
                # se devuelve False indicando que son distinguibles
                clase_A = self._clase_of_a_state(clases, transition_A[i])
                clase_B = self._clase_of_a_state(clases, transition_B[i])

                if clase_A != clase_B: 
                    return False

        return True

    def _create_transitions(self,
        clases:List[State],
        states: List[State]) : 

        transitions = []
        tabla_transiciones = {}

        # Creamos la nueva tabla de transiciones
        for clase in clases:
            tabla_transiciones[self._create_name_new_state(clase)] = {}
            for symbol in self.symbols:
                moves = []
                for s in clase:
                    for transition in self.transitions:
                        if transition.initial_state == s and transition.symbol == symbol:
                            if transition.final_state not in moves:
                                moves.append(transition.final_state)
                        
                if len(moves) != 0:
                        st1 = self._create_new_state(self._clase_of_a_state(clases, moves[0]))
                        st2 = self._state_by_a_name(st1.name, states)
                        if st2 is not None:
                            tabla_transiciones[self._create_name_new_state(clase)][symbol] = st2.name

        for key_state, column_set in tabla_transiciones.items():
            state_ini = self._state_by_a_name(key_state, states)
            for key_symbol, transition in column_set.items():
                    
                # Creamos la nueva transition
                if state_ini is not None:
                    t = Transition(
                            initial_state=state_ini,
                            symbol=key_symbol, 
                            final_state=self._state_by_a_name(transition, states)
                        )

                transitions.append(t)

        return transitions
