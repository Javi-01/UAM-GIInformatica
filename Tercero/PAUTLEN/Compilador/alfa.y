/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

%{
	#include <stdio.h>
	#include <stdlib.h>
	#include <string.h>

	#include "alfa.h"
	#include "generacion.h"
	#include "tablaSimbolos.h"
	#include "tablaHash.h"

	void yyerror(char *s);
	extern int yylex();
	extern FILE *yyout;

	TABLA_SIMBOLO *tabla_simbolos = NULL;

	extern int count_lines, count_columns, flag_error, yyleng;

	/* Variables auxiliares para declarar un nodo */
	int clase_actual, tipo_actual, tipo_funcion_actual, categoria_actual, tamanio_vector_actual = 0;
	int num_variables_locales_actual = 0, pos_variable_local_actual = 0, num_parametros_actual = 0, num_parametros_llamada_actual = 0, pos_parametro_actual = 0;

	/* Variables aux para funciones */
	int etiqueta = 0, en_funcion = 0, en_explist = 0, ret = 0;

%}

%code requires {
	/* Atributos necesarios en los nodos */
    struct tipo_atributos{
        char lexema[101];
    	int tipo;
    	int valor_entero;
        int es_direccion; /* si es direccion de memoria o no */
		int etiqueta; /* para gestion de etiquetas iterativas */
    };
}

%union
{
   struct tipo_atributos atributos;
}

%token TOK_MAIN
%token TOK_INT
%token TOK_BOOLEAN
%token TOK_ARRAY
%token TOK_FUNCTION
%token TOK_IF
%token TOK_ELSE
%token TOK_WHILE
%token TOK_SCANF
%token TOK_PRINTF
%token TOK_RETURN

%token TOK_PUNTOYCOMA
%token TOK_COMA
%token TOK_PARENTESISIZQUIERDO
%token TOK_PARENTESISDERECHO
%token TOK_CORCHETEIZQUIERDO
%token TOK_CORCHETEDERECHO
%token TOK_LLAVEIZQUIERDA
%token TOK_LLAVEDERECHA
%token TOK_ASIGNACION
%token TOK_MAS
%token TOK_MENOS
%token TOK_DIVISION
%token TOK_ASTERISCO
%token TOK_AND
%token TOK_OR
%token TOK_NOT
%token TOK_IGUAL
%token TOK_DISTINTO
%token TOK_MENORIGUAL
%token TOK_MAYORIGUAL
%token TOK_MENOR
%token TOK_MAYOR

%token <atributos> TOK_IDENTIFICADOR

%token <atributos> TOK_CONSTANTE_ENTERA
%token <atributos> TOK_TRUE
%token <atributos> TOK_FALSE

%token TOK_ERROR

%type <atributos> exp
%type <atributos> comparacion
%type <atributos> condicional
%type <atributos> if_exp
%type <atributos> if_exp_sentencias
%type <atributos> if_else_exp_sentencias
%type <atributos> bucle
%type <atributos> while
%type <atributos> while_exp
%type <atributos> elemento_vector
%type <atributos> constante
%type <atributos> constante_entera
%type <atributos> constante_logica
%type <atributos> identificadores
%type <atributos> identificador
%type <atributos> fn_name
%type <atributos> fn_declaration
%type <atributos> clase
%type <atributos> clase_escalar
%type <atributos> clase_vector
%type <atributos> idpf
%type <atributos> idf_llamada_funcion
%type <atributos> asignacion

// preferencias de los operadores
%left TOK_MENOS TOK_MAS TOK_OR
%left TOK_DIVISION TOK_ASTERISCO TOK_AND
%left TOK_NOT

%start programa

%%
programa: iniciar_ambito_global escribir_cabecera_bss escribir_subseccion_data TOK_MAIN TOK_LLAVEIZQUIERDA declaraciones escribir_segmento_bss funciones escribir_inicio_main sentencias TOK_LLAVEDERECHA {
	fprintf(yyout, ";R1:\t<programa> ::= main { <declaraciones> <funciones> <sentencias> }\n");
	escribir_fin(yyout);
	free_tabla_simbolos(tabla_simbolos);}
;

iniciar_ambito_global: {
	tabla_simbolos = init_tabla_simbolos();
	if (tabla_simbolos == NULL){
		yyerror("Error al inicialiar el ambito global");
		flag_error =- 1;
		return -1;
	}}
;

escribir_cabecera_bss: {escribir_subseccion_data(yyout);}
;

escribir_subseccion_data:{escribir_cabecera_bss(yyout);}
;

escribir_segmento_bss: {escribir_segmento_codigo(yyout);}
;

escribir_inicio_main: {escribir_inicio_main(yyout);}
;

declaraciones: declaracion {fprintf(yyout, ";R2:\t<declaraciones> ::= <declaracion>\n");} |
			   declaracion declaraciones {fprintf(yyout, ";R3:\t<declaraciones> ::= <declaracion> <declaraciones>\n");}
;

declaracion: clase identificadores TOK_PUNTOYCOMA {$2.tipo = $1.tipo; fprintf(yyout, ";R4:\t<declaracion> ::= <clase> <identificadores> ;\n");}
;

clase: clase_escalar {clase_actual = ESCALAR; fprintf(yyout, ";R5:\t<clase> ::= <clase_escalar>\n");} |
	   clase_vector {clase_actual = VECTOR; fprintf(yyout, ";R7:\t<clase> ::= <clase_vector>\n");}
;

clase_escalar: tipo {fprintf(yyout, ";R9:\t<clase_escalar> ::= <tipo>\n");}
;

tipo: TOK_INT {clase_actual = ESCALAR; tipo_actual = INT; fprintf(yyout, ";R10:\t<tipo> ::= int\n");} |
	  TOK_BOOLEAN {clase_actual = ESCALAR; tipo_actual = BOOLEAN; fprintf(yyout, ";R11:\t<tipo> ::= boolean\n");}
;

clase_vector: TOK_ARRAY tipo TOK_CORCHETEIZQUIERDO TOK_CONSTANTE_ENTERA TOK_CORCHETEDERECHO {
	tamanio_vector_actual = $4.valor_entero;

	/* Comprobamos si el vector tiene un tamaño valido */
	if (tamanio_vector_actual < 1 || tamanio_vector_actual > MAX_TAMANIO_VECTOR) {
		fprintf(stderr, "****Error semantico en lin %d: El tamanyo del vector %s excede los limites permitidos (1,64).\n", count_lines, $$.lexema);
		return TOK_ERROR;
	}

	/* Propagamos el valor */
	$$.valor_entero = tamanio_vector_actual;

	fprintf(yyout, ";R15:\t<clase_vector> ::= array <tipo> [ <constante_entera> ]\n");}
;

identificadores: identificador {fprintf(yyout, ";R18:\t<identificadores> ::= <identificador>\n");} |
	  			 identificador TOK_COMA identificadores {fprintf(yyout, ";R19:\t<identificadores> ::= <identificador> , <identificadores>\n");}
;

funciones: funcion funciones {fprintf(yyout, ";R20:\t<funciones> ::= <funcion> <funciones>\n");} |
		   /* lambda */ {fprintf(yyout, ";R21:\t<funciones> ::=\n");} // En el word donde sale la gramática de ALFA hay alguna expresion y entendemos que es que lea lambda o cadena vacia
;

funcion: fn_declaration sentencias TOK_LLAVEDERECHA{
	INFO_SIMBOLO *info_nodo = NULL;

	/* Si ya hay una funcion abierta... */
	if (!ret){
		fprintf(stderr, "****Error semantico en lin %d: Funcion %s sin sentencia de retorno.\n", count_lines, $1.lexema);
		return TOK_ERROR;
	}

	/* Buscamos si ya existe el lexema en el ambito global */
	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
		return TOK_ERROR;
	}

	/* Cerramos el ambito global que pudiera estar  abierto */
	cerrarAmbitoLocal();
	ret = 0;
	en_funcion = 0;

	info_nodo->num_parametros = num_parametros_actual; /* Actualizamos su info */

	fprintf(yyout, ";R22:\t<funcion> ::= function <tipo> <identificador> ( <parametros_funcion> ) { <declaraciones_funcion> <sentencias> }\n");}
;

fn_name: TOK_FUNCTION tipo TOK_IDENTIFICADOR {

	INFO_SIMBOLO *info_nodo = NULL;
	STATUS ret = ERR;

	info_nodo = buscarNodo(tabla_simbolos, $3.lexema);
	if (info_nodo != NULL){
		fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
		return TOK_ERROR;
	}

	/* Si no se encuentra la función, abrimos su ambito */
	info_nodo = init_info_symbol($3.lexema, FUNCION, clase_actual, tipo_actual, tamanio_vector_actual, num_variables_locales_actual, pos_variable_local_actual, num_parametros_actual, pos_parametro_actual);
	ret = abrirAmbitoLocal(tabla_simbolos, info_nodo);
	if(ret == ERR){ /* Funcion ya declarada */
        fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
    }

	/* Inicializar las siguientes variables globales */
	num_variables_locales_actual = 0;
    pos_variable_local_actual = 1;
    num_parametros_actual = 0;
    pos_parametro_actual = 0;
    en_funcion = 1;

	/* Propagar a la parte izquierda de la regla el lexema del identificador */
	$$.tipo = tipo_actual;
    tipo_funcion_actual = tipo_actual;
    strcpy($$.lexema, $3.lexema);}
;

fn_declaration: fn_name TOK_PARENTESISIZQUIERDO parametros_funcion TOK_PARENTESISDERECHO TOK_LLAVEIZQUIERDA declaraciones_funcion {

	INFO_SIMBOLO *info_nodo = NULL;

	/* Buscamos la funcion */
	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
		return TOK_ERROR;
	}

	/* Actualizamos el num de parametros y propagamos la regla */
	info_nodo->num_parametros = num_parametros_actual;
    strcpy($$.lexema, $1.lexema);
	$$.tipo = $1.tipo;

	declararFuncion(yyout, $1.lexema, num_variables_locales_actual);}
;

parametros_funcion: parametro_funcion resto_parametros_funcion {fprintf(yyout, ";R23:\t<parametros_funcion> ::=  <parametro_funcion> <resto_parametros_funcion>\n");} |
					/* lambda */ {fprintf(yyout, ";R24:\t<parametros_funcion> ::=\n");}
;

resto_parametros_funcion: TOK_PUNTOYCOMA parametro_funcion resto_parametros_funcion {fprintf(yyout, ";R25:\t<resto_parametros_funcion> ::= ; <parametro_funcion> <resto_parametros_funcion>\n");} |
							/* lambda */ {fprintf(yyout, ";R26:\t<resto_parametros_funcion> ::=\n");}
;

parametro_funcion: tipo idpf {fprintf(yyout, ";R27:\t<parametro_funcion> ::= <tipo> <identificador>\n");}
;

idpf: TOK_IDENTIFICADOR {

	INFO_SIMBOLO *info_nodo = NULL;
	STATUS ret = ERR;

	/* Buscamos el parametros en el ambito local */
	info_nodo = buscarNodoEnLocal(tabla_simbolos, $1.lexema);
	if (info_nodo != NULL){
		fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
		return TOK_ERROR;
	}

	/* Si no se encuentra la función, abrimos su ambito */
	info_nodo = init_info_symbol($1.lexema, PARAMETRO, clase_actual, tipo_actual, tamanio_vector_actual, num_variables_locales_actual, pos_variable_local_actual, num_parametros_actual, pos_parametro_actual);
	ret = insertarLocal(tabla_simbolos, info_nodo);
	if(ret == ERR){
        fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).", count_lines, $1.lexema);
    }

	/* Si se detecta un parametro, se actualiza el numero total de ellos y la posicion del mismo */
	num_parametros_actual++;
    pos_parametro_actual++;}
;

declaraciones_funcion: declaraciones {fprintf(yyout, ";R28:\t<declaraciones_funcion> ::= <declaraciones>\n");} |
					   /* lambda */ {fprintf(yyout, ";R29:\t<declaraciones_funcion> ::=\n");}
;

sentencias: sentencia {fprintf(yyout, ";R30:\t<sentencias> ::= <sentencia>\n");} |
			sentencia sentencias {fprintf(yyout, ";R31:\t<sentencias> ::= <sentencia> <sentencias>\n");}
;

sentencia: sentencia_simple TOK_PUNTOYCOMA {fprintf(yyout, ";R32:\t<sentencia> ::= <sentencia_simple> ;\n");} |
		   bloque {fprintf(yyout, ";R33:\t<sentencia> ::= <bloque>\n");}
;

sentencia_simple: asignacion {fprintf(yyout, ";R34:\t<sentencia_simple> ::= <asignacion>\n");} |
				  lectura {fprintf(yyout, ";R35:\t<sentencia_simple> ::= <lectura>\n");} |
				  escritura {fprintf(yyout, ";R36:\t<sentencia_simple> ::= <escritura>\n");} |
				  retorno_funcion {fprintf(yyout, ";R38:\t<sentencia_simple> ::= <retorno_funcion>\n");}
;

bloque: condicional {fprintf(yyout, ";R40:\t<bloque> ::= <condicional>\n");} |
		bucle {fprintf(yyout, ";R41:\t<bloque> ::= <bucle>\n");}
;

asignacion: TOK_IDENTIFICADOR TOK_ASIGNACION exp {

	INFO_SIMBOLO *info_nodo = NULL;

	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
		return TOK_ERROR;
	}

	/* En caso de existir, realizamos una serie de comprobacones */
	if (info_nodo->categoria == FUNCION){
		fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
		return TOK_ERROR;
	}

	if (info_nodo->clase == VECTOR){
		fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
		return TOK_ERROR;
	}

	if (info_nodo->tipo != $3.tipo){
		fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
		return TOK_ERROR;
	}

	/* Si esta abierto el ambito local... */
	if (en_funcion == 1){
		if(info_nodo->categoria == PARAMETRO) { /* Y si es un parametro lo escribimos como tal */
            escribirParametro(yyout, info_nodo->posicion_parametro, num_parametros_actual);
        }
        else { /* Si no escribimos que es una variable local */
            escribirVariableLocal(yyout, info_nodo->num_variables_locales + 1);
        }
        asignarDestinoEnPila(yyout, $3.es_direccion); /* Y asignamos su destino */
    }
	else {
		/* En caso de no estar dentro de un ambito local, lo asignamos en la direccion correspondiente */
	    asignar(yyout, $1.lexema, $3.es_direccion);
	}
	fprintf(yyout, ";R43:\t<asignacion> ::= <identificador> = <exp>\n");
	}
	|
	elemento_vector TOK_ASIGNACION exp {

		INFO_SIMBOLO *info_nodo = NULL;

		info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
		if (info_nodo == NULL){
			fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
			return TOK_ERROR;
		}

		if ($1.tipo != $3.tipo){
			fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
			return TOK_ERROR;
		}

		/* En caso de captar un vector, lo asignamos como tal */
		char value[20];
		sprintf(value, "%d", $1.valor_entero);
		escribir_operando(yyout, value, 0);

		escribir_elemento_vector(yyout, $1.lexema, info_nodo->tamano, $3.es_direccion);
		asignarDestinoEnPila(yyout, $3.es_direccion);

		fprintf(yyout, ";R44:\t<asignacion> ::= <elemento_vector> = <exp>\n");}
;

elemento_vector: TOK_IDENTIFICADOR TOK_CORCHETEIZQUIERDO exp TOK_CORCHETEDERECHO{

	INFO_SIMBOLO *info_nodo = NULL;

	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
		return TOK_ERROR;
	}

	/* Comprobamos que la indexacion sea de un vector y que en su interior haya un integer  */
	if (info_nodo->categoria == FUNCION){
		fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
		return TOK_ERROR;
	}

	if (info_nodo->clase != VECTOR){
		fprintf(stderr, "****Error semantico en lin %d: Intento de indexacion de una variable que no es de tipo vector.\n", count_lines);
		return TOK_ERROR;
	}

	if ($3.tipo != INT){
		fprintf(stderr, "****Error semantico en lin %d: El indice en una operacion de indexacion tiene que ser de tipo entero.\n", count_lines);
		return TOK_ERROR;
	}

	/* Propagamos atributos */
	$$.tipo = info_nodo->tipo;
	$$.es_direccion = 1;
	$$.valor_entero = $3.valor_entero;
	escribir_elemento_vector(yyout, $1.lexema, info_nodo->tamano, $3.es_direccion);

	fprintf(yyout, ";R48:\t<elemento_vector> ::= <identificador> [ <exp> ]\n");}
;

condicional: if_exp_sentencias TOK_LLAVEDERECHA {
	ifthenelse_fin(yyout, $1.etiqueta);
	fprintf(yyout, ";R50:\t<condicional> ::= if ( <exp> ) { <sentencias> }\n");}
	|
	if_else_exp_sentencias sentencias TOK_LLAVEDERECHA {
		ifthenelse_fin(yyout, $1.etiqueta);
		fprintf(yyout, ";R51:\t<condicional> ::= if ( <exp> ) { <sentencias> } else { <sentencias> }\n");}
;

/* CRITICO */
if_else_exp_sentencias: if_exp_sentencias TOK_LLAVEDERECHA TOK_ELSE TOK_LLAVEIZQUIERDA {$$.etiqueta = $1.etiqueta;}
;

if_exp_sentencias: if_exp sentencias {$$.etiqueta = $1.etiqueta; ifthenelse_fin_then(yyout, $1.etiqueta);}
;

if_exp: TOK_IF TOK_PARENTESISIZQUIERDO exp TOK_PARENTESISDERECHO TOK_LLAVEIZQUIERDA {

	/* Comprobamos que la expresion es de tipo Boolean */
	if ($3.tipo != BOOLEAN){
		fprintf(stderr, "****Error semantico en lin %d: Condicional con condicion de tipo int.\n", count_lines);
		return TOK_ERROR;
	}

	etiqueta++;
	$$.etiqueta=etiqueta;
	ifthen_inicio(yyout, $3.es_direccion, $$.etiqueta);}
;

bucle: while_exp sentencias TOK_LLAVEDERECHA {while_fin(yyout, $1.etiqueta); fprintf(yyout, ";R52:\t<bucle> ::= while ( <exp> ) { <sentencias> }\n");}
;

while_exp: while exp TOK_PARENTESISDERECHO TOK_LLAVEIZQUIERDA {

	/* Comprobamos que la expresion es de tipo Boolean */
	if ($2.tipo != BOOLEAN){
		fprintf(stderr, "****Error semantico en lin %d: Bucle con condicion de tipo int.\n", count_lines);
		return TOK_ERROR;
	}

	$$.etiqueta = $1.etiqueta;
	while_exp_pila(yyout, $2.es_direccion, $1.etiqueta);}
;

while: TOK_WHILE TOK_PARENTESISIZQUIERDO {$$.etiqueta=etiqueta++; while_inicio(yyout, $$.etiqueta);}
;

lectura: TOK_SCANF TOK_IDENTIFICADOR {
	INFO_SIMBOLO *info_nodo = NULL;

	info_nodo = buscarNodo(tabla_simbolos, $2.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $2.lexema);
		return TOK_ERROR;
	}

	/* Realizamos comprobaciones correspondientes */
	if (info_nodo->categoria == FUNCION){
		fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
		return TOK_ERROR;
	}

	if (info_nodo->clase == VECTOR){
		fprintf(stderr, "****Error semantico en lin %d: Intento de indexacion de una variable que no es de tipo vector.\n", count_lines);
		return TOK_ERROR;
	}

	/* Indicamos que es una lectura llamando a la funcion
	correspondiente en el modulo generaciones */
	leer(yyout, $2.lexema, info_nodo->tipo);
	fprintf(yyout, ";R54:\t<lectura> ::= scanf <identificador>\n");}
;

/* No se realizan comprobaciones, solo se llama a la funcion de
escribir del modulo generaciones */
escritura: TOK_PRINTF exp {escribir(yyout, $2.es_direccion, $2.tipo); fprintf(yyout, ";R56:\t<escritura> ::= printf <exp>\n");}
;

retorno_funcion: TOK_RETURN exp {

	/* Reliazamos comprobaciones de ver que el retorno este dentro de una funcion,
	y mismo tipo de expresion que la funcion */
	if(en_funcion == 0){
        fprintf(stderr, "****Error semantico en lin %d: Sentencia de retorno fuera del cuerpo de una función.\n", count_lines);
        return TOK_ERROR;
    }

    if($2.tipo != tipo_funcion_actual) {
        fprintf(stderr, "****Error semantico en lin %d: Retorno con distinto tipo que la función.\n", count_lines);
        return TOK_ERROR;
    }

	/* Gestionamos el retorno e indicamos que retornamos la funcion */
	ret = 1;
	retornarFuncion(yyout, $2.es_direccion);
	fprintf(yyout, ";R61:\t<retorno_funcion> ::= return <exp>\n");}
;


exp: exp TOK_MAS exp {
		if ($1.tipo == INT && $3.tipo == INT) {
			$$.tipo = INT;
			$$.es_direccion = 0;
			sumar(yyout, $1.es_direccion, $3.es_direccion);
		}
		else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R72:\t<exp> ::= <exp> + <exp>\n");}
		|
	 exp TOK_MENOS exp {
		if ($1.tipo == INT && $3.tipo == INT){
			$$.tipo = INT;
			$$.es_direccion = 0;
			restar(yyout, $1.es_direccion, $3.es_direccion);
		}
		else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R73:\t<exp> ::= <exp> - <exp>\n");}
		|
	 exp TOK_DIVISION exp {
		if ($1.tipo == INT && $3.tipo == INT){
			$$.tipo = INT;
			$$.es_direccion = 0;
			dividir(yyout, $1.es_direccion, $3.es_direccion);
		}
		else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R74:\t<exp> ::= <exp> / <exp>\n");}
		|
	 exp TOK_ASTERISCO exp {
		if ($1.tipo == INT && $3.tipo == INT) {
			$$.tipo = INT;
			$$.es_direccion = 0;
			multiplicar(yyout, $1.es_direccion, $3.es_direccion);
		}
		else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R75:\t<exp> ::= <exp> * <exp>\n");}
		|
	 TOK_MENOS exp {
		if ($2.tipo == INT){
			$$.tipo = INT;
			$$.es_direccion = 0;
			cambiar_signo(yyout, $2.es_direccion);
		}else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R76:\t<exp> ::= - <exp>\n");}
		|
	 exp TOK_AND exp {
		if ($1.tipo == BOOLEAN && $3.tipo == BOOLEAN) {
			$$.tipo = BOOLEAN;
			$$.es_direccion = 0;
			y(yyout, $1.es_direccion, $3.es_direccion);
		}else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion logica con operandos int.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R77:\t<exp> ::= <exp> && <exp>\n");}
	 	|
	 exp TOK_OR exp {
		if ($1.tipo == BOOLEAN && $3.tipo == BOOLEAN){
			$$.tipo = BOOLEAN;
			$$.es_direccion = 0;
			o(yyout, $1.es_direccion, $3.es_direccion);
		}else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion logica con operandos int.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R78:\t<exp> ::= <exp> || <exp>\n");}
		|
	 TOK_NOT exp {
		if ($2.tipo == BOOLEAN){
			$$.tipo = BOOLEAN;
			$$.es_direccion = 0;
			$$.etiqueta = etiqueta;
			no(yyout, $2.es_direccion, $$.etiqueta);
			etiqueta++;
		}else{
        	fprintf(stderr, "****Error semantico en lin %d: Operacion logica con operandos int.\n", count_lines);
        	return TOK_ERROR;
		}
		fprintf(yyout, ";R79:\t<exp> ::= ! <exp>\n");}
		|
	 TOK_IDENTIFICADOR {
		INFO_SIMBOLO *info_nodo = NULL;
		/* Buscamos el identificador */
		info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
		if (info_nodo == NULL) {
			fprintf(yyout, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
		}
		/* En caso de existir, realizamos una serie de comprobacones */
		if (info_nodo->categoria == FUNCION){
			fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
			return TOK_ERROR;
		}
		if (info_nodo->clase == VECTOR){
			fprintf(stderr, "****Error semantico en lin %d: Asignacion incompatible.\n", count_lines);
			return TOK_ERROR;
		}

		strcpy($$.lexema, $1.lexema);
		$$.tipo = info_nodo->tipo;
		$$.es_direccion = 1;

		/* Si esta abierto el ambito local... */
		if (en_funcion == 1){
			if(info_nodo->categoria == PARAMETRO) { /* Y si es un parametro lo escribimos como tal */
				escribirParametro(yyout, info_nodo->posicion_parametro, num_parametros_actual);
			}
			else { /* Si no escribimos que es una variable local */
				escribirVariableLocal(yyout, info_nodo->num_variables_locales + 1);
			}
		/* Si es global... */
    	}else {
			escribir_operando(yyout, $1.lexema, 1);
			if (en_explist == 1){
				operandoEnPilaAArgumento(yyout, 1);
			}
		}

		fprintf(yyout, ";R80:\t<exp> ::= <identificador>\n");}
	 	|
	 constante {
		$$.tipo = $1.tipo;
		$$.es_direccion = $1.es_direccion;
		fprintf(yyout, ";R81:\t<exp> ::= <constante>\n");}
		|
	 TOK_PARENTESISIZQUIERDO exp TOK_PARENTESISDERECHO {
		$$.tipo = $2.tipo;
		$$.es_direccion = $2.es_direccion;
		fprintf(yyout, ";R82:\t<exp> ::= ( <exp> )\n");}
		|
	 TOK_PARENTESISIZQUIERDO comparacion TOK_PARENTESISDERECHO {
		$$.tipo = $2.tipo;
		$$.es_direccion = $2.es_direccion;
		fprintf(yyout, ";R83:\t<exp> ::= ( <comparacion> )\n");}
		|
	 elemento_vector {
		$$.tipo = $1.tipo;
		$$.es_direccion = $1.es_direccion;
		fprintf(yyout, ";R85:\t<exp> ::= <elemento_vector>\n");}
	 	|
	 idf_llamada_funcion TOK_PARENTESISIZQUIERDO lista_expresiones TOK_PARENTESISDERECHO {
		INFO_SIMBOLO *info_nodo = NULL;
		/* Buscamos el identificador */
		info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
		if (info_nodo == NULL) {
			fprintf(yyout, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
			return TOK_ERROR;
		}
		if (info_nodo->num_parametros != num_parametros_llamada_actual){
            fprintf(stderr, "****Error semantico en lin %d: Numero incorrecto de parametros en llamada a funcion.\n", count_lines);
            return TOK_ERROR;
		}
		en_explist = 0;
		$$.tipo = info_nodo->tipo;
		$$.es_direccion = 0;
		llamarFuncion(yyout, $1.lexema, num_parametros_actual);
		fprintf(yyout, ";R88:\t<exp> ::= <identificador> ( <lista_expresiones> )\n");}
;

idf_llamada_funcion: TOK_IDENTIFICADOR {
	INFO_SIMBOLO *info_nodo = NULL;
	/* Buscamos el identificador */
	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo == NULL){
		fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).\n", count_lines, $1.lexema);
		return TOK_ERROR;
	}
	if (info_nodo->categoria != FUNCION){
		fprintf(stderr, "****Error semantico en lin %d: El identificador (%s) no es de categoria funcion.\n", count_lines, $1.lexema);
		return TOK_ERROR;
	}
	if (en_explist == 1){
		fprintf(stderr, "****Error semantico en lin %d: No esta permitido el uso de llamadas a funciones como parametros de otras funciones.\n", count_lines);
		return TOK_ERROR;
	}
	num_parametros_llamada_actual = 0;
	en_explist = 1;
	strcpy($$.lexema, $1.lexema);}
;

lista_expresiones: exp resto_lista_expresiones {num_parametros_llamada_actual++; fprintf(yyout, ";R89:\t<lista_expresiones> ::= <exp> <resto_lista_expresiones>\n");} |
				   /* lambda */ {fprintf(yyout, ";R90:\t<lista_expresiones> ::=\n");}
;

resto_lista_expresiones: TOK_COMA exp resto_lista_expresiones { num_parametros_llamada_actual++; fprintf(yyout, ";R91:\t<resto_lista_expresiones> ::= , <exp> <resto_lista_expresiones>\n");} |
						 /* lambda */ {fprintf(yyout, ";R92:\t<resto_lista_expresiones> ::=\n");}
;

comparacion: exp TOK_IGUAL exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}else{
					fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				igual(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R93:\t<comparacion> ::= <exp> == <exp>\n");}
			|
			exp TOK_DISTINTO exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}
				else{
					fprintf(stderr, "****Error semantico en lin %d: Comparacion con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				distinto(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R94:\t<comparacion> ::= <exp> != <exp>\n");}
			|
			 exp TOK_MENORIGUAL exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}else{
					fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				menor_igual(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R95:\t<comparacion> ::= <exp> <= <exp>\n");}
			|
			 exp TOK_MAYORIGUAL exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}
				else{
					fprintf(stderr, "****Error semantico en lin %d: Comparacion con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				mayor_igual(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R96:\t<comparacion> ::= <exp> >= <exp>\n");}
			|
			 exp TOK_MENOR exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}else{
					fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				menor(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R97:\t<comparacion> ::= <exp> < <exp>\n");}
			|
			 exp TOK_MAYOR exp {
				if ($1.tipo == INT && $3.tipo == INT){
					$$.tipo = BOOLEAN;
					$$.es_direccion = 0;
				}else{
					fprintf(stderr, "****Error semantico en lin %d: Operacion aritmetica con operandos boolean.\n", count_lines);
					return TOK_ERROR;
				}
				mayor(yyout, $1.es_direccion, $3.es_direccion, etiqueta);
				etiqueta++;
				fprintf(yyout, ";R98:\t<comparacion> ::= <exp> > <exp>\n");}
;

constante: constante_logica {$$.tipo = $1.tipo; $$.es_direccion = $1.es_direccion; fprintf(yyout, ";R99:\t<constante> ::= <constante_logica>\n");} |
		   constante_entera {$$.tipo = $1.tipo; $$.es_direccion = $1.es_direccion; fprintf(yyout, ";R100:\t<constante> ::= <constante_entera>\n");}
;

constante_logica: TOK_TRUE {$$.tipo = BOOLEAN; $$.es_direccion = 0; escribir_operando(yyout, "1", 0); fprintf(yyout, ";R102:\t<constante_logica> ::= true\n");} |
				  TOK_FALSE {$$.tipo = BOOLEAN; $$.es_direccion = 0; escribir_operando(yyout, "0", 0); fprintf(yyout, ";R103:\t<constante_logica> ::= false\n");}
;

constante_entera: TOK_CONSTANTE_ENTERA {
	$$.tipo = INT;
 	$$.es_direccion = 0;
	$$.valor_entero = $1.valor_entero;

	char value[20];
	sprintf(value, "%d", $1.valor_entero);
	escribir_operando(yyout, value, 0);

	fprintf(yyout, ";R104:\t<constante_entera> ::= <numero>\n");}
;

identificador: TOK_IDENTIFICADOR {

	INFO_SIMBOLO *info_nodo = NULL;

	/* Buscamos el identificador */
	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if (info_nodo != NULL){ /* Si ya existia tanto en el global como en el local... */
		fprintf(stderr, "****Error semantico en lin %d: Declaracion duplicada.\n", count_lines);
		return TOK_ERROR;
	}

	/* Si no, lo insertamos */
	info_nodo = init_info_symbol($1.lexema, VARIABLE, clase_actual, tipo_actual, tamanio_vector_actual, num_variables_locales_actual, pos_variable_local_actual, num_parametros_actual, pos_parametro_actual);
	ret = insertar(tabla_simbolos, info_nodo);
	if(ret == ERR){
        fprintf(stderr, "****Error semantico en lin %d: Acceso a variable no declarada (%s).", count_lines, $1.lexema);
    }

	/* Realizamos comprobaciones */
	info_nodo = buscarNodo(tabla_simbolos, $1.lexema);
	if(en_funcion == 1){

		if (info_nodo->clase != ESCALAR){ /* Si ya existia... */
			fprintf(stderr, "****Error semantico en lin %d: Variable local de tipo no escalar.\n", count_lines);
			return TOK_ERROR;
		}

		info_nodo->num_variables_locales = num_variables_locales_actual;
        info_nodo->num_parametros = num_parametros_actual;

		num_variables_locales_actual++;
		pos_variable_local_actual++;
	}
	else {

		if (info_nodo->clase == VECTOR){
			declarar_variable(yyout, $1.lexema, tipo_actual, tamanio_vector_actual);
		}
		else {
			declarar_variable(yyout, $1.lexema, tipo_actual, 1);
		}
	}
	fprintf(yyout, ";R108:\t<identificador> ::= TOK_IDENTIFICADOR\n");}
;

%%
void yyerror (char *s){

	if(flag_error == 1){
		fprintf(stderr, "****Error en [lin %d, col %d]: simbolo con una longitud superior a la permitida(%s)\n", count_lines, count_columns, yylval.atributos.lexema);
	}
	else if(flag_error == 2){
		fprintf(stderr, "****Error en [lin %d, col %d]: simbolo no permitido (%s)\n", count_lines, count_columns,yylval.atributos.lexema);
	}
	else{
		fprintf(stderr, "****Error sintactico en [lin %d, col %d])\n", count_lines, count_columns-yyleng);
	}

	flag_error = 0;
}