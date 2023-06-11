package Codigo



interface Dibujable {
    fun dibujar()
    fun informar() = println("Soy dibujable")
}
fun clasesImplementanInterfaz(){
    open class Vista(
        val id: Int,
        val alineacion: String
    ): Dibujable {
        override fun dibujar() = println("Soy la vista $id con alineación $alineacion")
    }

    class Boton(
        var esPulsable: Boolean,
        var texto: String,
        id : Int,
        alineacion: String
    ): Vista(id, alineacion){
        override fun dibujar() = when(esPulsable){
            true -> println("Soy la vista $id con alineación $alineacion y soy pulsable")
            else -> println("Soy la vista $id con alineación $alineacion y no soy pulsable")
        }
    }

    class Imagen(
        id: Int,
        alienacion: String
    ): Vista(id, alienacion)


    Boton(true, "Guardar", 1, "centrada").dibujar()
    Imagen(2, "izquierda").dibujar()
}

fun main(){
    // Combina las caracteristicas de Interfaz y de clase que tiene subclases y no se puede instanciar
    abstract class Vista(var id: Int){
        abstract fun dibujar()
        fun informar() = println("Extiendo de la vista abstracta")
    }

    class Boton(id: Int): Vista(id){
        override fun dibujar() = println("Soy el boton $id")
    }

    val btn = Boton(1)
    btn.informar()
    btn.dibujar()
}