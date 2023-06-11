package Codigo
object Demo {
    var nombre: String = "Codigo.Demo"
    fun informar() = println("Soy un objeto y me llamo $nombre")
}

class Estudiante(
    var nombre: String,
    var nota1: Double,
    var nota2: Double
)

object Calificaciones{
    var estudiantes = mutableListOf<Estudiante>()
    fun calcular(){
        estudiantes.forEach {
            println("${it.nombre} -> ${it.nota1*0.4 + it.nota2*0.6}")
        }
    }
}

object ComparadorEstudiantes: Comparator<Estudiante> {
    override fun compare(o1: Estudiante?, o2: Estudiante?) : Int{
        val diff = 0.4 * (o1!!.nota1 - o2!!.nota2) +
                0.6 * (o1.nota2 - o2.nota2)
        return if (diff < 0) -1
        else 1
    }
}

class Ejemplo {
    companion object Informe {
        fun informar() = println("Objeto acompañante")
    }
}

// No se necesita instanciar como si una clase, y solo hay una instancia
fun ejemplosPrimeraParte() {

    Demo.nombre = "Elena"
    Demo.informar()

    Calificaciones.estudiantes.add(Estudiante("Elena", 2.4, 7.6))
    Calificaciones.estudiantes.add(Estudiante("Pedro", 5.8, 8.3))
    Calificaciones.calcular()

    val est1 = Estudiante("Elena", 2.4, 7.6)
    val est2 = Estudiante("Pedro", 5.8, 8.3)
    val lista = mutableListOf(est1, est2)

    lista.sortWith(ComparadorEstudiantes)
    lista.forEach { println("${it.nombre} -> ${it.nota1}") }

    Ejemplo.informar()

}

// Metodos es factoria son buenos para instanciar objetos de la clase privada
// De esta manera, no se necesita hacer una instancia de la clase Codigo.EstudianteOtro
class EstudianteOtro private constructor(
    var nombre: String,
    var calificacion: Double
) {
    companion object {
        fun nuevoEstudianteFinal(
            nombre: String,
            nota: Double
        ) = EstudianteOtro(nombre, nota)

        fun nuevoEstudianteContinua(
            nombre: String,
            nota1: Double,
            nota2: Double
        ) = EstudianteOtro(nombre, 0.4 * nota1 + 0.6 * nota2)

        fun nuevoEstudianteContinuaTres(
            nombre: String,
            nota1: Double,
            nota2: Double,
            nota3: Double
        ) = EstudianteOtro(nombre, 0.3 * nota1 + 0.3 * nota2 + 0.4 * nota3)
    }
}
fun main(){
    val juan = EstudianteOtro.nuevoEstudianteFinal("Juan", 7.2)
    val elena = EstudianteOtro.nuevoEstudianteContinua("Elena", 5.6, 7.8)
    val paula = EstudianteOtro.nuevoEstudianteContinuaTres("Paula", 3.6, 4.5, 7.1)

    println("${juan.nombre} tiene un ${juan.calificacion}")
    println("${elena.nombre} tiene un ${elena.calificacion}")
    println("${paula.nombre} tiene un ${paula.calificacion}")

}