package Entregables

fun main(){
    class Persona(var nombre: String, var edad: Int){
        fun saludar() = println("Me llamo $nombre y tengo $edad años")
    }

    val personas = listOf(
        Persona("Javier", 21),
        Persona("Pablo", 22),
        Persona("Marcos", 19),
        Persona("Lara", 18),
        Persona("Paula", 23),
    )

    val mayorEdad = personas.maxOfOrNull{
        it.edad
    }

    personas.forEach {
        if (it.edad == mayorEdad) it.saludar()
    }

}