package Main

import kotlin.math.absoluteValue

fun main(){
    class ValorAbsoluto {
        var valor = 0.0
            get () = field
            set (valor) {
                field = valor.absoluteValue
            }
    }
    val x = ValorAbsoluto()
    x.valor = -32.5

    println(x.valor)
}