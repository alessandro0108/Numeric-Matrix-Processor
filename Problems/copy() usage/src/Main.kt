// do not change this data class
data class Box(val height: Int, val length: Int, val width: Int)

fun main(args: Array<String>) {
    // implement your code here
    val h = readLine()!!.toInt()
    val l1 = readLine()!!.toInt()
    val l2 = readLine()!!.toInt()
    val w = readLine()!!.toInt()

    val b1 = Box(h, l1, w)
    val b2 = b1.copy(length = l2)

    println(b1)
    println(b2)
}