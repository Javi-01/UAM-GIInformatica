package Main

fun main() {
    print("Introduce tu peso en kg: ")
    val peso = readln().toIntOrNull()

    print("Introduce tu altura en cm: ")
    val altura = readln().toIntOrNull()

    var imc : Double

    if (peso != null && altura != null)
        imc = 10000.0 * peso / (altura * altura)
    else {
        println("Por favor, vuelva a intentarlo")
        return
    }

    val mensaje = if (imc < 16) "Delgadez severa";
    else if (imc < 17) "Delgadez moderada";
    else if (imc < 18.5) "Delgadez leve";
    else if (imc < 25) "Peso normal";
    else if (imc < 30) "Preobesidad";
    else if (imc < 35) "Obesidad leve";
    else if (imc < 40) "Obesidad media";
    else "Obesidad mórbida";

    println(mensaje)
}