package Entregables

class Animal(var nombre: String, var edad: Int)


fun main() {
    var animales = mutableListOf<Animal>()

    animales.apply {
        println("Añadiendo animales")
        add(Animal("perro", 12))
        add(Animal("gato", 10))
        add(Animal("vaca", 15))
        println("Ordenando animales")
        sortWith { o1, o2 -> if (o1.edad < o2.edad) -1 else if (o1.edad > o2.edad) 1 else 0 }
    }

    animales.forEach {
        println("${it.nombre} : ${it.edad}")
    }
}