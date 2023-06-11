package Codigo

class Student(var name: String) {
    var age: Int = 0
    var score: Int = 0
    fun greet() = println("Me llamo $name y tengo $age años")
    fun report() = println("Mi puntuacion es $score")
}
fun main() {
    // Ejecucion de bloque de codigo de un contexto determinado
    // let(it), apply(this), run, with (this), also (it)
    // with devuelve la ultima linea de la lambda

    val nombre = "datos.txt"
    nombre.apply {
        println(removeSuffix(".txt"))
    }

    Student("Elena").also {
        it.age = 21
        it.score = 78
        it.greet()
        it.report()
    }

    Student("Mario").apply {
        age = 21
        score = 78
        greet()
        report()
    }

    val ciudades = mutableListOf("Burgos", "Avila", "Pontevedra", "Santander")
    ciudades.filter { it.length > 5 }.let { println("Ciudades que pasan el filtro: "); println(it) }

}