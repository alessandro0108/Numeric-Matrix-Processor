fun main() {
    val line = readLine()
    println("Elvis says: ${line ?: throw IllegalStateException()}")
}