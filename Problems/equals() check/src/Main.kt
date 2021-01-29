data class Client(val name: String, val age: Int, val balance: Int) {
    override fun equals(other: Any?): Boolean {
        other as Client
        if (name != other.name) return false
        if (age != other.age) return false
        return true
    }
}

fun main(args: Array<String>) {
    val n1 = readLine()!!
    val a1 = readLine()!!.toInt()
    val b1 = readLine()!!.toInt()

    val n2 = readLine()!!
    val a2 = readLine()!!.toInt()
    val b2 = readLine()!!.toInt()

    val c1 = Client(n1, a1, b1)
    val c2 = Client(n2, a2, b2)

    println(c1.equals(c2))
}