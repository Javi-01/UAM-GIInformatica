package Codigo
class Persona(
    val nombre: String,
    val edad: Int
){
    override fun toString(): String = "Codigo.Persona(nombre=$nombre, edad=$edad)"
    override fun equals(other: Any?): Boolean  =
         if (other == null || other !is Persona)
            false
        else
            other.edad == edad && other.nombre == nombre

    override fun hashCode(): Int = nombre.hashCode() * 31 + edad
}

data class PersonaData(val nombre: String, val edad: Int){
    var altura: Int = 12
}

fun main() {
    val uno = Persona("Ana", 25)
    val dos = Persona("Ana", 25)
    println(uno == dos)

    val tres = PersonaData("Ana", 25)
    // Copy un metodo para mutar una data class
    val cuatro = tres.copy(edad = 34)
    println(tres == cuatro)
    println(cuatro)

}