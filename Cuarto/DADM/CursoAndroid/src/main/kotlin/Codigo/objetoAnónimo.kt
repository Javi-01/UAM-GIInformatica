package Codigo

interface BotonListener{
    fun onClick()
}
fun objetosAnonimos(){
    object: BotonListener {
        var nombre: String = "objeto anónimo"
        fun informar() = println("Soy un $nombre")
        override fun onClick() {
            println("$nombre pulsado")
        }
    }

    fun agregarEscuchador(e: BotonListener){}

    agregarEscuchador(object : BotonListener {
        override fun onClick() {
            println("El boton ha sido pulsado")
        }
    })
}

class BotonPulsable {
    lateinit var escuchador: BotonListener
    fun agregaListener(e: BotonListener) { escuchador = e}
    fun pulsar() = escuchador.onClick()
}

fun main() {
    val btn = BotonPulsable()
    btn.agregaListener(object : BotonListener {
        override fun onClick() {
            println("EL boton se ha pulsado")
        }
    })
    btn.pulsar()
}