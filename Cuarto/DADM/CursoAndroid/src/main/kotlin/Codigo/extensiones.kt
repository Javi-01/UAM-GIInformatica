package Codigo


fun String.ultimo(): Char = this[this.length - 1]
fun String.vocales(): Int = lowercase().count {
    it in listOf('a', 'e', 'i', 'o', 'u')
}

fun main() {
    println("Javier".ultimo())
    println("Javier".vocales())

    open class Vista2
    fun Vista2.dibujar() = println("Soy una vista")
    class Boton : Vista2()
    fun Boton.dibujar() = println("Soy un botón dibujado con una extensión")


    var vista =Vista2()

    vista.dibujar()
    vista = Boton()
    vista.dibujar()
    class Vista1(val id: Int)
    // No tiene ni por que estar en el cuerpo de la clase, son como funciones estaticas
    fun Vista1.mostrar() = println("Soy la vista con id = $id")

    Vista1(1).mostrar()

}