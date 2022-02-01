/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#include "generacion.h"

void escribir_cabecera_bss(FILE *fpasm)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_cabecera_bss\n");
    fprintf(fpasm, "segment .bss\n");
    fprintf(fpasm, "\t__esp resd 1\n");
}

void escribir_subseccion_data(FILE *fpasm)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_subseccion_data\n");
    fprintf(fpasm, "segment .data\n");
    fprintf(fpasm, "\tmsg_error_indice_vector db \"Indice de vector fuera de rango\", 0\n");
    fprintf(fpasm, "\tmsg_error_division db \"Error division por 0\", 0\n");
}

void declarar_variable(FILE *fpasm, char *nombre, int tipo, int tamano)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }

    fprintf(fpasm, ";declarar_variable\n");
    if (tipo)
    {
        fprintf(fpasm, "\t_%s resd %d\n", nombre, tamano);
    }

    else
    {
        fprintf(fpasm, "\t_%s resd %d\n", nombre, tamano);
    }
}

void escribir_segmento_codigo(FILE *fpasm)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_segmento_codigo\n");
    fprintf(fpasm, "segment .text\n");
    fprintf(fpasm, "\tglobal main\n");
    fprintf(fpasm, "\textern malloc, free\n");
    fprintf(fpasm, "\textern scan_int, print_int, scan_boolean, print_boolean\n");
    fprintf(fpasm, "\textern print_endofline, print_blank, print_string\n");
}

void escribir_inicio_main(FILE *fpasm)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_inicio_main\n");
    fprintf(fpasm, "; -----------------------\n");
    fprintf(fpasm, "; PROCEDIMIENTO PRINCIPAL\n");
    fprintf(fpasm, "main:\n");
    fprintf(fpasm, "\tmov dword [__esp], esp\n");
}

void escribir_fin(FILE *fpasm)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_fin\n");
    fprintf(fpasm, "\n\tjmp near fin\n");
    fprintf(fpasm, "fin_error_division:\n");
    fprintf(fpasm, "\tpush dword msg_error_division\n");
    fprintf(fpasm, "\tcall print_string\n");
    fprintf(fpasm, "\tadd esp, 4\n");
    fprintf(fpasm, "\tcall print_endofline\n");
    fprintf(fpasm, "\tjmp near fin\n");
    fprintf(fpasm, "fin_indice_fuera_rango:\n");
    fprintf(fpasm, "\tpush dword msg_error_indice_vector\n");
    fprintf(fpasm, "\tcall print_string\n");
    fprintf(fpasm, "\tadd esp, 4\n");
    fprintf(fpasm, "\tcall print_endofline\n");
    fprintf(fpasm, "\tjmp near fin\n");

    fprintf(fpasm, "fin:\n");
    fprintf(fpasm, "\tmov esp, [__esp]\n");
    fprintf(fpasm, "\tret\n");
}

void suma_iterativa(FILE *fpasm, char *nombre1, char *nombre2, char *nombre3)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";suma_iterativa\n");
    fprintf(fpasm, "\tpush dword _%s\n", nombre1);
    fprintf(fpasm, "\tcall scan_int\n");
    fprintf(fpasm, "\tadd esp, 4\n");
    fprintf(fpasm, "\tpush dword [_%s]\n", nombre1);

    fprintf(fpasm, "\tpush dword _%s\n", nombre2);
    fprintf(fpasm, "\tcall scan_int\n");
    fprintf(fpasm, "\tadd esp, 4\n");
    fprintf(fpasm, "\tpush dword [_%s]\n", nombre2);

    fprintf(fpasm, "\tpop dword ebx\n");
    fprintf(fpasm, "\tpop dword eax\n\n");

    fprintf(fpasm, "inicio_bucle:\n");

    fprintf(fpasm, "\tcmp ebx, 0\n");
    fprintf(fpasm, "\tje fin\n");
    fprintf(fpasm, "\tadd eax, ebx\n");
    fprintf(fpasm, "\tmov [_%s], eax\n", nombre1);

    fprintf(fpasm, "\tpush dword [_%s]\n", nombre1);
    fprintf(fpasm, "\tcall print_int\n");
    fprintf(fpasm, "\tadd esp, 4\n");

    fprintf(fpasm, "\tcall print_endofline\n");

    fprintf(fpasm, "\tmov ebx, [_%s]\n", nombre1);

    fprintf(fpasm, "\tpush dword _%s\n", nombre2);
    fprintf(fpasm, "\tcall scan_int\n");
    fprintf(fpasm, "\tadd esp, 4\n");
    fprintf(fpasm, "\tpush dword [_%s]\n", nombre2);
    fprintf(fpasm, "\tpop dword ebx\n");

    fprintf(fpasm, "\tmov [_%s], ebx\n", nombre1);
    fprintf(fpasm, "\tmov eax, ebx\n");

    fprintf(fpasm, "\tjmp inicio_bucle\n");
    fprintf(fpasm, "fin:\n");
}

void escribir_operando(FILE *fpasm, char *nombre, int es_variable)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir_operando\n");
    if (es_variable)
    {
        fprintf(fpasm, "\tpush dword _%s\n", nombre);
    }
    else
    {
        fprintf(fpasm, "\tpush dword %s\n", nombre);
    }
}

void asignar(FILE *fpasm, char *nombre, int es_variable)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";asignar\n");
    if (es_variable)
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
        fprintf(fpasm, "\tmov dword [_%s], eax\n", nombre);
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword [_%s], eax\n", nombre);
    }
}

void leer(FILE *fpasm, char *nombre, int tipo)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";leer\n");
    if (tipo)
    {
        fprintf(fpasm, "\tpush dword _%s\n", nombre);
        fprintf(fpasm, "\tcall scan_boolean\n");
        fprintf(fpasm, "\tadd esp, 4\n");
    }
    else
    {
        fprintf(fpasm, "\tpush dword _%s\n", nombre);
        fprintf(fpasm, "\tcall scan_int\n");
        fprintf(fpasm, "\tadd esp, 4\n");
    }
}

void escribir(FILE *fpasm, int es_variable, int tipo)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribir\n");
    if (!es_variable)
    {
        if (tipo)
        {
            fprintf(fpasm, "\tcall print_boolean\n");
            fprintf(fpasm, "\tadd esp, 4\n");
        }
        else
        {
            fprintf(fpasm, "\tcall print_int\n");
            fprintf(fpasm, "\tadd esp, 4\n");
        }
    }
    else
    {
        if (tipo)
        {
            fprintf(fpasm, "\tpop eax\n");
            fprintf(fpasm, "\tmov eax, [eax]\n");
            fprintf(fpasm, "\tpush dword eax\n");
            fprintf(fpasm, "\tcall print_boolean\n");
            fprintf(fpasm, "\tadd esp, 4\n");
        }
        else
        {
            fprintf(fpasm, "\tpop eax\n");
            fprintf(fpasm, "\tmov eax, [eax]\n");
            fprintf(fpasm, "\tpush dword eax\n");
            fprintf(fpasm, "\tcall print_int\n");
            fprintf(fpasm, "\tadd esp, 4\n");
        }
    }
    fprintf(fpasm, "\tcall print_endofline\n");
}

void sumar(FILE *fpasm, int es_variable_1, int es_variable_2)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";sumar\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov ebx, [ebx]\n");
    }

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tadd eax, ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void restar(FILE *fpasm, int es_variable_1, int es_variable_2)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";restar\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }
    fprintf(fpasm, "\tsub eax, ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void multiplicar(FILE *fpasm, int es_variable_1, int es_variable_2)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";multiplicar\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }
    fprintf(fpasm, "\timul ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void dividir(FILE *fpasm, int es_variable_1, int es_variable_2)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";dividir\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov ebx, [ebx]\n");
    }
    fprintf(fpasm, "\tcmp ebx, 0\n");
    fprintf(fpasm, "\tje near fin_error_division\n");

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tcdq\n");
    fprintf(fpasm, "\tidiv ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void o(FILE *fpasm, int es_variable_1, int es_variable_2)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";o\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov ebx, [ebx]\n");
    }

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tor eax, ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void y(FILE *fpasm, int es_variable_1, int es_variable_2)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";y\n");
    if (!es_variable_2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov ebx, [ebx]\n");
    }

    if (!es_variable_1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tand eax, ebx\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void cambiar_signo(FILE *fpasm, int es_variable)
{

    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";cambiar_signo\n");
    if (!es_variable)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tneg eax\n");
    fprintf(fpasm, "\tpush dword eax\n");
}

void no(FILE *fpasm, int es_variable, int cuantos_no)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";no\n");
    if (!es_variable)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tcmp eax, 0\n");
    fprintf(fpasm, "\tje cambio1_%d\n", cuantos_no);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp fin_no%d\n", cuantos_no);
    fprintf(fpasm, "cambio1_%d:\n", cuantos_no);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_no%d:\n", cuantos_no);
}

void igual(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";igual\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tje near igual%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_igual%d\n", etiqueta);
    fprintf(fpasm, "igual%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_igual%d:\n", etiqueta);
}

void distinto(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";distinto\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tjne near distinto%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_distinto%d\n", etiqueta);
    fprintf(fpasm, "distinto%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_distinto%d:\n", etiqueta);
}

void menor_igual(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";menor_igual\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tjle near si_menor_igual%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_menor_igual%d\n", etiqueta);
    fprintf(fpasm, "si_menor_igual%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_menor_igual%d:\n", etiqueta);
}

void mayor_igual(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";mayor_igual\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tjge near si_mayor_igual%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_mayor_igual%d\n", etiqueta);
    fprintf(fpasm, "si_mayor_igual%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_mayor_igual%d:\n", etiqueta);
}

void menor(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";menor\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tjl near si_menor%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_menor%d\n", etiqueta);
    fprintf(fpasm, "si_menor%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_menor%d:\n", etiqueta);
}

void mayor(FILE *fpasm, int es_variable1, int es_variable2, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";mayor\n");
    if (!es_variable2)
    {
        fprintf(fpasm, "\tpop dword ebx\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword ebx\n");
        fprintf(fpasm, "\tmov dword ebx, [ebx]\n");
    }

    if (!es_variable1)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }

    fprintf(fpasm, "\tcmp eax, ebx\n");
    fprintf(fpasm, "\tjg near si_mayor%d\n", etiqueta);
    fprintf(fpasm, "\tpush dword 0\n");
    fprintf(fpasm, "\tjmp near fin_mayor%d\n", etiqueta);
    fprintf(fpasm, "si_mayor%d:\n", etiqueta);
    fprintf(fpasm, "\tpush dword 1\n");
    fprintf(fpasm, "fin_mayor%d:\n", etiqueta);
}

void ifthenelse_inicio(FILE *fpasm, int exp_es_variable, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; ifthenelse_inicio %d\n", etiqueta);
    fprintf(fpasm, "\tpop eax\n");
    fprintf(fpasm, "\tcmp eax, 0\n");
    fprintf(fpasm, "\tje near fin_then%d\n", etiqueta);
}

void ifthen_inicio(FILE *fpasm, int exp_es_variable, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; ifthen_inicio %d\n", etiqueta);
    if (!exp_es_variable)
    {
        fprintf(fpasm, "\tpop eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov dword eax, [eax]\n");
    }
    fprintf(fpasm, "\tcmp eax, 0\n");
    fprintf(fpasm, "\tje near fin_then%d\n", etiqueta);
}

void ifthenelse_fin(FILE *fpasm, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; ifthenelse_fin %d\n", etiqueta);
    fprintf(fpasm, "fin_ifelse%d:\n", etiqueta);
}

void ifthenelse_fin_then(FILE *fpasm, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; ifthenelese_fin_then %d\n", etiqueta);
    fprintf(fpasm, "\tjmp near fin_ifelse%d\n", etiqueta);
    fprintf(fpasm, "fin_then%d:\n", etiqueta);
}

void while_inicio(FILE *fpasm, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; while_inicio %d\n", etiqueta);
    fprintf(fpasm, "inicio_while%d:\n", etiqueta);
}

void while_exp_pila(FILE *fpasm, int exp_es_variable, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; while_exp_pila %d\n", etiqueta);
    if (!exp_es_variable)
    {
        fprintf(fpasm, "\tpop eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tcmp eax, 0\n");
    fprintf(fpasm, "\tje near fin_while%d\n", etiqueta);
}

void while_fin(FILE *fpasm, int etiqueta)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, "; while_fin %d\n", etiqueta);
    fprintf(fpasm, "\tjmp near inicio_while%d\n", etiqueta);
    fprintf(fpasm, "fin_while%d:\n", etiqueta);
}

void escribir_elemento_vector(FILE *fpasm, char *nombre_vector, int tam_max, int exp_es_direccion)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }

    fprintf(fpasm, "\n\tpop dword eax");

    if (exp_es_direccion == 1)
    {
        fprintf(fpasm, "\n\tmov dword eax, [eax]");
    }

    fprintf(fpasm, "\n\tcmp eax, 0");
    fprintf(fpasm, "\n\tjl near msg_error_indice_vector");

    fprintf(fpasm, "\n\tcmp eax, %d", tam_max - 1);
    fprintf(fpasm, "\n\tjg near msg_error_indice_vector");

    fprintf(fpasm, "\n\tmov dword edx, _%s", nombre_vector);
    fprintf(fpasm, "\n\tlea eax, [edx + eax*4]");
    fprintf(fpasm, "\n\tpush dword eax");
}

void declararFuncion(FILE *fd_asm, char *nombre_funcion, int num_var_loc)
{
    if (!fd_asm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fd_asm, ";declararFuncion\n");
    fprintf(fd_asm, "_%s:\n", nombre_funcion);
    fprintf(fd_asm, "\tpush ebp\n");
    fprintf(fd_asm, "\tmov ebp, esp\n");
    fprintf(fd_asm, "\tsub esp, %d\n", 4 * num_var_loc);
}

void retornarFuncion(FILE *fd_asm, int es_variable)
{
    if (!fd_asm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fd_asm, ";retornarFuncion\n");
    if (!es_variable)
    {
        fprintf(fd_asm, "\tpop eax\n");
    }
    else
    {
        fprintf(fd_asm, "\tpop eax\n");
        fprintf(fd_asm, "\tmov dword eax, [eax]\n");
    }
    fprintf(fd_asm, "\tmov esp, ebp\n");
    fprintf(fd_asm, "\tpop ebp\n");
    fprintf(fd_asm, "\tret\n");
}

void escribirParametro(FILE *fpasm, int pos_parametro, int num_total_parametros)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribirParametro\n");
    int d_ebp = 4 * (1 + (num_total_parametros - pos_parametro));
    fprintf(fpasm, "\tlea eax, [ebp + %d]\n", d_ebp);
    fprintf(fpasm, "\tpush dword eax\n");
}

void escribirVariableLocal(FILE *fpasm, int posicion_variable_local)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";escribirVariableLocal\n");
    int d_ebp = 4 * posicion_variable_local;
    fprintf(fpasm, "\tlea eax, [ebp - %d]\n", d_ebp);
    fprintf(fpasm, "\tpush dword eax\n");
}

void asignarDestinoEnPila(FILE *fpasm, int es_variable)
{
    if (!fpasm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fpasm, ";asignarDestinoEnPila\n");
    fprintf(fpasm, "\tpop dword ebx\n");

    if (!es_variable)
    {
        fprintf(fpasm, "\tpop dword eax\n");
    }
    else
    {
        fprintf(fpasm, "\tpop dword eax\n");
        fprintf(fpasm, "\tmov eax, [eax]\n");
    }
    fprintf(fpasm, "\tmov dword [ebx], eax\n");
}

void operandoEnPilaAArgumento(FILE *fd_asm, int es_variable)
{
    if (!fd_asm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fd_asm, ";operandoEnPilaAArgumento\n");
    if (es_variable)
    {
        fprintf(fd_asm, "\tpop eax\n");
        fprintf(fd_asm, "\tmov eax, [eax]\n");
        fprintf(fd_asm, "\tpush eax\n");
    }
}

/* Cambios respecto a la P1 por problemas de violacion de segmento
y por similitud al codigo de ejemplo que nos da el profe */
void llamarFuncion(FILE *fd_asm, char *nombre_funcion, int num_argumentos)
{
    if (!fd_asm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fd_asm, ";llamarFuncion\n");
    fprintf(fd_asm, "\tcall _%s\n", nombre_funcion);
    limpiarPila(fd_asm, num_argumentos);
}

/* Cambios respecto a la P1 por problemas de violacion de segmento
y por similitud al codigo de ejemplo que nos da el profe */
void limpiarPila(FILE *fd_asm, int num_argumentos)
{
    if (!fd_asm)
    {
        printf("Error en fopen");
        return;
    }
    fprintf(fd_asm, ";limpiarPila\n");
    fprintf(fd_asm, "\tadd esp, %d\n", 4 * num_argumentos);
    fprintf(fd_asm, "\tpush dword eax\n");
}
