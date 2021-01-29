fun isNumber(input: String): Any {
    val converted = input.toIntOrNull()
    return converted ?: input
}