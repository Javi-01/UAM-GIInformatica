#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>

#define ARRAY_SIZE 2048
#define NUMBER_OF_TRIALS 100000

/*
 * Statically allocate our arrays.  Compilers can
 * align them correctly.
 */
static double a[ARRAY_SIZE], b[ARRAY_SIZE], c;

int main(int argc, char *argv[])
{
    int i, t, n;
    struct timeval fin, ini;

    double m = 1.0001;
    n = atoi(argv[1]);

    /* Populate A and B arrays */
    for (i = 0; i < ARRAY_SIZE; i++)
    {
        b[i] = i;
        a[i] = i + 1;
    }

    gettimeofday(&ini, NULL);
    /* Perform an operation a number of times */
    for (t = 0; t < n; t++)
    {
        for (i = 0; i < ARRAY_SIZE; i++)
        {
            c += m * a[i] + b[i];
        }
    }
    gettimeofday(&fin, NULL);

    printf("Execution time: %f\n", ((fin.tv_sec * 1000000 + fin.tv_usec) - (ini.tv_sec * 1000000 + ini.tv_usec)) * 1.0 / 1000000.0);
    printf("El valor de c en simple2 es: %lf\n", c);

    return 0;
}
