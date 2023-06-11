package Main

fun main() {

    val contar: (String, Char) -> Int = { cadena, ele ->
        var cont = 0
        for (c in cadena)
            if (c.lowercase() == ele.lowercase()) cont++
        cont
    }

    println(contar("sustancial", 's'))
}