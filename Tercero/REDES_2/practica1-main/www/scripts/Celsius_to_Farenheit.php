<?php
if($argc != 2) {
    echo "Error - Introduce unicamente 1 argumento\n";
    exit(1);
}

try {
    $temp = (float) $argv[1];
    $celsius = (float) ($argv[1] * 9 / 5 + 32);
    echo "$argv[1] grados Celsius son $celsius grados Farenheit\n";
} catch (Exception $e) {
    echo "El parámetro introducido debe ser un número para poder ser convertido\n";
}