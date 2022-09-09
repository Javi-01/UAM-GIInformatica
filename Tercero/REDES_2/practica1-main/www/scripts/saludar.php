<?php
if($argc != 2) {
    echo "Error - Introduce unicamente 1 argumento\n";
    exit(1);
}

$nombre = $argv[1];

echo "Hola, $nombre!\n";