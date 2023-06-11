package Main

fun main() {
    class Persona{
        lateinit var nombre: String
        lateinit var apellido: String
        lateinit var telefono: String
        fun asignar(n: String, a: String, t:String){
            nombre = n
            apellido = a
            telefono = t
        }
    }

    val p1 = Persona()
    p1.asignar("Javier", "Fraile", "620602602")
    val p2 = Persona()
    p2.asignar("Antony", "Iglesias", "621622222")
    val p3 = Persona()
    p3.asignar("Carlos", "Perez", "221948544")
    val p4 = Persona()
    p4.asignar("Pedro", "Iglesias", "620122122")

    val personas = listOf<Persona>(p1, p2, p3, p4)

    println("¿A quien buscas? Escribe su apellido: ")
    val apellido = readln()

    personas.forEach { persona ->
        if (persona.apellido == apellido){
            println("${persona.nombre} ${persona.apellido} ${persona.telefono}")
        }
    }
}