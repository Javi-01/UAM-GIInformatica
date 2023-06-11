package Codigo

fun clases1() {
    class Persona {
        private lateinit var nombre: String
        fun saludar(){
            println("Me llamo $nombre")
        }
    }

    val primo = Persona()
    // Primo y amigo apuntan a la misma direccion de memoria, si cambio una, se cambia otra
    val amigo = primo

    Persona().saludar()

}

fun clases2(){
    class Persona{
        var nombre = "Elena"

            get() {
                println("Metodo get invocado")
                return field.uppercase()
            }
            set(valor){
                println("Metodo set invocado")
                field = valor.trim()
            }
    }

    Persona().nombre
    Persona().nombre = "Juan"
}

fun main(){
    class Persona(var nombre: String, var esAmigo: Boolean) {
        init {
            println("Soy el constructor primario de $nombre")
        }
        constructor(nombre: String)
                : this(nombre, esAmigo = false){
                    println("Soy el constructor secundario de $nombre")
                }

        fun saludar(){
            if (esAmigo) println("Me llamo $nombre y somos amigos")
            else println("Me llamo $nombre y no somos amigos todavia")
        }
    }

    Persona("Juana").saludar()

}