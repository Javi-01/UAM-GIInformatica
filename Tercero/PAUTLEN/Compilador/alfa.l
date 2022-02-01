/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

%{
    #include <stdio.h>
    #include "y.tab.h"

    int count_lines = 1;
    int count_columns = 1;
    int max_len = 100;
    int flag_error = 0; // = significa que no hay error y 1 que si

%}

%option noyywrap
SALTO_LINEA [\n\r]
ESPACIO " "
TABULACION [\t]

PUNTOYCOMA ";"
COMA ","
PARENTESISIZQUIERDO "("
PARENTESISDERECHO ")"
CORCHETEIZQUIERDO "["
CORCHETEDERECHO "]"
LLAVEIZQUIERDA "{"
LLAVEDERECHA "}"
ASIGNACION "="
MAS "+"
MENOS "-"
DIVISION "/"
ASTERISCO "*"
AND "&&"
OR "||"
NOT "!"
IGUAL "=="
DISTINTO "!="
MENORIGUAL "<="
MAYORIGUAL ">="
MENOR "<"
MAYOR ">"

MAIN "main"
INT "int"
BOOLEAN "boolean"
ARRAY "array"
FUNCTION "function"
IF "if"
ELSE "else"
WHILE "while"
SCANF "scanf"
PRINTF "printf"
RETURN "return"

IDENTIFICADOR [a-zA-Z][a-zA-Z0-9]*

CONSTANTE_ENTERA [0-9]+
TRUE "true"
FALSE "false"

COMENTARIO "//".*
%%

{ESPACIO} {count_columns += yyleng;}
{TABULACION} {count_columns += yyleng;}
{COMENTARIO} {count_lines += yyleng; count_columns=1;}
{SALTO_LINEA} {count_lines += yyleng; count_columns=1;}
{MAIN} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_MAIN;}
{INT} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_INT;}
{BOOLEAN} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_BOOLEAN;}
{ARRAY} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_ARRAY;}
{FUNCTION} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_FUNCTION;}
{IF} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_IF;}
{ELSE} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_ELSE;}
{WHILE} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_WHILE;}
{SCANF} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_SCANF;}
{PRINTF} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_PRINTF;}
{RETURN} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_RETURN;}
{PUNTOYCOMA} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_PUNTOYCOMA;}
{COMA} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_COMA;}
{PARENTESISIZQUIERDO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_PARENTESISIZQUIERDO;}
{PARENTESISDERECHO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_PARENTESISDERECHO;}
{CORCHETEIZQUIERDO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_CORCHETEIZQUIERDO;}
{CORCHETEDERECHO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_CORCHETEDERECHO;}
{LLAVEIZQUIERDA} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_LLAVEIZQUIERDA;}
{LLAVEDERECHA} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout, ";D:\t%s\n", yytext); return TOK_LLAVEDERECHA;}
{ASIGNACION} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_ASIGNACION;}
{MAS} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MAS;}
{MENOS} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MENOS;}
{DIVISION} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_DIVISION;}
{ASTERISCO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_ASTERISCO;}
{AND} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_AND;}
{OR} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_OR;}
{NOT} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_NOT;}
{IGUAL} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_IGUAL;}
{DISTINTO} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_DISTINTO;}
{MENORIGUAL} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MENORIGUAL;}
{MAYORIGUAL} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MAYORIGUAL;}
{MENOR} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MENOR;}
{MAYOR} { count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_MAYOR;}
{CONSTANTE_ENTERA} {count_columns += yyleng; yylval.atributos.valor_entero=atoi(yytext); fprintf(yyout,";D:\t%s\n",yytext); return TOK_CONSTANTE_ENTERA;}
{TRUE} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_TRUE;}
{FALSE} {count_columns += yyleng; strcpy(yylval.atributos.lexema, yytext);fprintf(yyout,";D:\t%s\n",yytext); return TOK_FALSE;}

{IDENTIFICADOR} {
    count_columns += yyleng;
    strcpy(yylval.atributos.lexema, yytext);

    if (yyleng > max_len) {
        flag_error = 1;
        strcpy(yylval.atributos.lexema, yytext);
        return TOK_ERROR;
    }

    fprintf(yyout,";D:\t%s\n",yytext);
    return TOK_IDENTIFICADOR;
};

.   {strcpy(yylval.atributos.lexema, yytext); flag_error = 2; return TOK_ERROR;}
%%