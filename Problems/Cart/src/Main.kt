import java.lang.Exception

fun main() {
    val productType = readLine()!!
    val price = readLine()!!
    val product = getProduct(productType, price.toDouble())
    println(totalPrice(product))
}

fun getProduct(productType: String, price: Double): Product {
    return when (productType) {
        "headphones" -> Headphones(price)
        "smartphone" -> Smartphone(price)
        "tv" -> Tv(price)
        "laptop" -> Laptop(price)
        else -> throw Exception("Unknown product type")
    }
}

open class Product(val price: Double) {
    open fun getTax(): Double {
        return 1.0
    }
}

fun totalPrice(product: Product): Int {
    return (product.price * product.getTax()).toInt()
}

class Headphones(price: Double): Product(price) {
    override fun getTax(): Double {
        return 1.11
    }
}

class Smartphone(price: Double): Product(price) {
    override fun getTax(): Double {
        return 1.15
    }
}

class Tv(price: Double): Product(price) {
    override fun getTax(): Double {
        return 1.17
    }
}

class Laptop(price: Double): Product(price) {
    override fun getTax(): Double {
        return 1.19
    }
}