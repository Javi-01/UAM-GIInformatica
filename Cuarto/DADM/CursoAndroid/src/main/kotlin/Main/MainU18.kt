package Main

fun main() {
    open class Vista(
        val id: Int,
        val alineacion: String
    )

    class Boton : Vista {
        constructor(id: Int, alineacion: String): super(id, alineacion)
        constructor(id: Int): super(id, "centrada")
    }

    val vista: Boton = Boton(1)
    println("La vista con id ${vista.id} está ${vista.alineacion}")
}