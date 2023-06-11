package Main

fun main() {

    for (i in 7 downTo   0 step 2){
        println(i)
    }

    for (i in 'm' downTo 'g' step 2) {
        println(i)
    }

    ('a'..'f').forEach(::print)

}
