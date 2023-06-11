package Main

fun main() {
    print("Por favor, introduce un correo electrónico: ")

    readLine()?.let {
        if (it.contains("@"))
            println("Bienvenido, $it")
        else
            println("Lo siento, tendrás que probar otra vez")
    }
}