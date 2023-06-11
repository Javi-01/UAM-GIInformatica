package Codigo

fun count() {

    val contar: (String, (Char)-> Boolean) -> Int = { cadena, filtro ->
        var cont = 0
        for (c in cadena)
            if (filtro(c))
                cont++
        cont
    }

    println(contar("Alamenda") { it.lowercaseChar() == 'a' })
}

fun countSimple(){
    println("Alameda".count { it.lowercaseChar() == 'a' || it.lowercaseChar() =='e' })
}

//Listas normales o de distintos tipos (Any) -> ListOf
//Listas normales o de distintos tipos mutables (Any) -> MutableListOf
//Listas del mismo tipo y con elementos unicos (Unicidad) -> SetOf
//Listas del mismo tipo y con elementos unicos mutables (Unicidad) -> MutableSetOf
// Mapas con conjuntos de clave - valor -> mapOf<K, V>()
// Mapas con conjuntos de clave - valor mutables -> mutableMapOf<K, V>()
fun lists() {
    val cataluna = listOf("Barcelona", "Gerona", "Lérida", "Tarragona")
    println(cataluna.getOrNull(4) ?: "Ciudad no incluida")

    val castillaLaMancha = mutableListOf("Albacete", "Cuenca", "Ciudad Real", "Burgos", "Guadalajara")
    castillaLaMancha.remove("Burgos")
    castillaLaMancha.add("Toledo")

    println(castillaLaMancha)

    val extremadura = setOf("Badajoz", "Cáceres")

    println(extremadura)
    var codigo = mapOf(Pair('a', 'a'.toInt()), Pair('b', 'b'.toInt()))
    codigo.toMutableMap()

    codigo += Pair('c', 99)
    codigo += Pair('d', 100)
    println(codigo)

}

fun main() {

    print("Teclea un número entero: ")
    val numero: Int = readln().toInt()

    for (i in numero downTo   0 step 2){
        println(i)
    }

    for (i in numero until 20)
        print(i)

    for (i in -1 until 7 step 3)
        print("$i ")
}












