package Main

fun main(){
    fun vocales(caracter: Char) : Boolean = caracter.lowercaseChar() in listOf('a', 'e', 'i', 'o', 'u')
    println("Alameda".count(::vocales))
}