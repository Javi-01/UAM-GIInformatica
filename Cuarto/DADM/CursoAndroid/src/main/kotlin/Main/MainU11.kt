package Main

fun main() {
    println("Comunidades autónomas de España:\n" +
        "1. Andalucía\n" +
        "2. Aragón\n" +
        "3. Principado de Asturias\n" +
        "4. Islas Baleares\n" +
        "5. Canarias\n" +
        "6. Cantabria\n" +
        "7. Castilla-La Mancha\n" +
        "8. Castilla y León\n" +
        "9. Cataluña\n" +
        "10. Comunidad Valenciana\n" +
        "11. Extremadura\n" +
        "12. Galicia\n" +
        "13. La Rioja\n" +
        "14. Comunidad de Madrid\n" +
        "15. Región de Murcia\n" +
        "16. Comunidad Foral de Navarra\n" +
        "17. País Vasco\n\n"
    )

    val comunidadesProvincias: Map<String, List<String>> = mapOf(
        Pair("Andalucía", listOf("Almería", "Cádiz", "Córdoba", "Granada", "Huelva", "Jaén", "Málaga", "Sevilla")),
        Pair("Aragón", listOf("Huesca", "Teruel", "Zaragoza")),
        Pair("Principado de Asturias", listOf("Oviedo")),
        Pair("Islas Baleares", listOf("Palma")),
        Pair("Canarias", listOf("Islas Baleares", "Santa Cruz de Tenerife")),
        Pair("Cantabria", listOf("Cantabria")),
        Pair("Castilla-La Mancha", listOf("Albacete", "Ciudad Real", "Cuenca", "Guadalajara", "Toledo")),
        Pair("Castilla y León", listOf("Ávila", "Burgos", "León", "Palencia", "Salamanca", "Segovia", "Soria", "Valladolid", "Zamora")),
        Pair("Cataluña", listOf("Barcelona", "Gerona", "Lérida", "Tarragona")),
        Pair("Comunidad Valenciana", listOf("Alicante", "Castellón", "Valencia")),
        Pair("Extremadura", listOf("Badajoz", "Cáceres")),
        Pair("Galicia", listOf("La Coruña", "Lugo", "Orense", "Pontevedra")),
        Pair("La Rioja", listOf("La Rioja")),
        Pair("Comunidad de Madrid", listOf("Madrid")),
        Pair("Región de Murcia", listOf("Región de Murcia")),
        Pair("Comunidad Foral de Navarra", listOf("Navarra")),
        Pair("País Vasco", listOf("Álava", "Guipúzcoa", "Vizcaya"))
    )



    println("Elige una comunidad Autónoma: ")
    val seleccion = readln().toInt()

    val comunidades = when(seleccion){
        1 -> comunidadesProvincias["Andalucía"]
        2 -> comunidadesProvincias["Aragón"]
        3 -> comunidadesProvincias["Principado de Asturias"]
        4 -> comunidadesProvincias["Islas Baleares"]
        5 -> comunidadesProvincias["Canarias"]
        6 -> comunidadesProvincias["Cantabria"]
        7 -> comunidadesProvincias["Castilla-La Mancha"]
        8 -> comunidadesProvincias["Castilla y León"]
        9 -> comunidadesProvincias["Cataluña"]
        10 -> comunidadesProvincias["Comunidad Valenciana"]
        11-> comunidadesProvincias["Extremadura"]
        12 -> comunidadesProvincias["Galicia"]
        13 -> comunidadesProvincias["La Rioja"]
        14 -> comunidadesProvincias["Comunidad de Madrid"]
        15 -> comunidadesProvincias["Región de Murcia"]
        16 -> comunidadesProvincias["Comunidad Foral de Navarra"]
        17 -> comunidadesProvincias["País Vasco"]
        else -> "Ninguna comunidad autónoma seleccionada"
    }

    println(comunidades)
}