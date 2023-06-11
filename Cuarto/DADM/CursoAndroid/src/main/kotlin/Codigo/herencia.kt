package Codigo

open class Vista(val id: Int)

class Boton(
    var pulsado: Boolean,
    var texto: String,
    id: Int
) : Vista(id)

fun main() {
    val boton = Boton(true, "Guardar", 1)

    if (boton.pulsado)
        print("Se ha pulsado la vista con id ${boton.id}")
}