package Codigo

import java.io.File
import java.io.FileNotFoundException


data class Pais(var nombre: String, var cerveza: Int, var vevidaEspirituosa: Int, var vino: Int)
fun main() {
    val paises: MutableList<Pais> = mutableListOf()
    try {
        val lines = File("data/drinks.txt").readLines()
        lines.forEach {
            val slices = it.split(",")
            paises.add(Pais(slices[0], slices[1].toInt(), slices[2].toInt(), slices[3].toInt()))
        }
    } catch (e: FileNotFoundException) {
        println("Fichero no encontrado, intentalo de nuevo")
    }

    fun selector (pais: Pais) : Int = maxOf(pais.vino, pais.cerveza, pais.vevidaEspirituosa)

    println("${paises.sortedByDescending { selector(it) }.subList(0,10).
    forEach { println("${it.nombre} : ${it.cerveza}, ${it.vevidaEspirituosa}, ${it.vino}")}}"
    )
}