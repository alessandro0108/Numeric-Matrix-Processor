package processor

import java.util.*
import kotlin.math.pow

const val ADDITION = "1"
const val SCALAR_MULT = "2"
const val MATRIX_MULT = "3"
const val TRANSPOSE = "4"
const val TRANSPOSE_MAIN_DIAGONAL = "41"
const val TRANSPOSE_SIDE_DIAGONAL = "42"
const val TRANSPOSE_VERTICAL = "43"
const val TRANSPOSE_HORIZONTAL = "44"
const val DETERMINANT = "5"
const val INVERSE = "6"
const val EXIT = "0"

val scanner = Scanner(System.`in`)

fun main() {
    var choice: String = ""
    do {
        try {
            choice = chooseOperation()
            when (choice) {
                ADDITION -> matrixSumCommand()
                SCALAR_MULT -> matrixScalarMultiplicationCommand()
                MATRIX_MULT -> matrixByMatrixMultiplicationCommand()
                TRANSPOSE_MAIN_DIAGONAL,
                    TRANSPOSE_SIDE_DIAGONAL,
                    TRANSPOSE_VERTICAL,
                    TRANSPOSE_HORIZONTAL -> matrixTransposeCommand(choice)
                DETERMINANT -> matrixDeterminantCommand()
                INVERSE -> matrixInverseCommand()
                EXIT -> break
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    } while (choice != EXIT)
}

private fun chooseOperation(): String {
    println("1. Add matrices")
    println("2. Multiply matrix by a constant")
    println("3. Multiply matrices")
    println("4. Transpose matrix")
    println("5. Calculate a determinant")
    println("6. Inverse matrix")
    println("0. Exit")
    print("Your choice: ")
    var choice = readLine()!!
    if (choice == TRANSPOSE) {
        println("1. Main diagonal")
        println("2. Side diagonal")
        println("3. Vertical line")
        println("4. Horizontal line")
        print("Your choice: ")
        choice += readLine()!!
    }
    return choice
}

private fun matrixSumCommand() {
    val matrix1 = readMatrix(scanner)
    val matrix2 = readMatrix(scanner)
    if (matrix1.size != matrix2.size || matrix1[0].size != matrix2[0].size) {
        throw Exception("The operation cannot be performed.")
    } else {
        val matrixSum = sumTwoMatrices(matrix1, matrix2)
        printMatrix(matrixSum)
    }
}

private fun matrixScalarMultiplicationCommand() {
    val matrix = readMatrix(scanner)
    //val number = scanner.nextInt()
    val number = scanner.nextDouble()
    val matrixMult = multiplyMatrixByScalar(matrix, number)
    printMatrix(matrixMult)
}

private fun matrixByMatrixMultiplicationCommand() {
    val matrix1 = readMatrix(scanner)
    val matrix2 = readMatrix(scanner)
    if (matrix1[0].size != matrix2.size) {
        throw Exception("The operation cannot be performed.")
    } else {
        val matrixSum = multiplyTwoMatrices(matrix1, matrix2)
        printMatrix(matrixSum)
    }
}

private fun matrixTransposeCommand(transpositionType: String) {
    val matrix = readMatrix(scanner)
    val transposer = Transposer.create(matrix, transpositionType)
    printMatrix(transposer.transpose())
}

private fun matrixDeterminantCommand() {
    val matrix = readMatrix(scanner)
    if (matrix.size != matrix[0].size) {
        throw  Exception("The operation cannot be performed.")
    }
    val determinant = det(matrix)
    printResult(determinant)
}

private fun matrixInverseCommand() {
    val matrix = readMatrix(scanner)
    if (matrix.size != matrix[0].size) {
        throw  Exception("The operation cannot be performed.")
    }
    val inverseMatrix = inverse(matrix)
    printMatrix(inverseMatrix)
}

private fun readMatrix(scanner: Scanner): Array<DoubleArray> {
    val m = scanner.nextInt()
    val n = scanner.nextInt()
    val matrix = Array(m, {DoubleArray(n)}) //arrayOf<IntArray>()
    for (i in 0 until m) {
        val row = DoubleArray(n)
        matrix[i] = row
        for (j in 0 until n) {
            row[j] = scanner.nextDouble()
        }
    }
    return matrix
}

private fun printMatrix(matrix: Array<DoubleArray>) {
    println("The result is:")
    for (i in 0 until matrix.size) {
        for (j in 0 until matrix[0].size) {
            print("${matrix[i][j]} ")
        }
        println()
    }
}

private fun printResult(result: Any) {
    println("The result is:")
    println(result)
    println()
}

private fun sumTwoMatrices(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>): Array<DoubleArray> {
    val m = matrix1.size
    val n = matrix1[0].size
    val matrixResult = Array(m, {DoubleArray(n)}) //arrayOf<IntArray>()
    for (i in 0 until m) {
        val row = DoubleArray(n)
        matrixResult[i] = row
        for (j in 0 until n) {
            row[j] = matrix1[i][j] + matrix2[i][j]
        }
    }
    return matrixResult
}

private fun multiplyMatrixByScalar(matrix1: Array<DoubleArray>, number: Double): Array<DoubleArray> {
    val m = matrix1.size
    val n = matrix1[0].size
    val matrixResult = Array(m, {DoubleArray(n)})
    for (i in 0 until m) {
        val row = DoubleArray(n)
        matrixResult[i] = row
        for (j in 0 until n) {
            row[j] = matrix1[i][j] * number
        }
    }
    return matrixResult
}

/* matrix1: m x n
 * matrix2: n x k
 * result : m x k
 */
private fun multiplyTwoMatrices(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>): Array<DoubleArray> {
    val m = matrix1.size
    val k = matrix2[0].size
    val matrixResult = Array(m, {DoubleArray(k)})
    for (i in 0 until m) {
        for (j in 0 until k) {
            val p: Double = calculateDotProduct(matrix1, i, matrix2, j)
            matrixResult[i][j] = p
        }
    }
    return matrixResult
}

/* matrix1: row length = n
 * matrix2: column length = n
 */
fun calculateDotProduct(matrix1: Array<DoubleArray>, rowIndex: Int, matrix2: Array<DoubleArray>, columnIndex: Int): Double {
    val n = matrix1[0].size // = matrix2.size
    var dotProduct = 0.0
    for (k in 0 until n) {
        dotProduct += matrix1[rowIndex][k] * matrix2[k][columnIndex]
    }
    return dotProduct
}

open class Transposer {
    var matrix = Array(1, {DoubleArray(1)})
    var m = 0   // number of rows
    var n = 0   // number of columns
    constructor(_matrix: Array<DoubleArray>) {
        matrix = _matrix
        m = matrix.size
        n = matrix[0].size
    }
    companion object {
        fun create(matrix: Array<DoubleArray>, transpositionType: String): Transposer {
            val transposer = when (transpositionType) {
                TRANSPOSE_MAIN_DIAGONAL -> MainDiagonalTransposer(matrix)
                TRANSPOSE_SIDE_DIAGONAL -> SideDiagonalTransposer(matrix)
                TRANSPOSE_VERTICAL -> VerticalTransposer(matrix)
                TRANSPOSE_HORIZONTAL -> HorizontalTransposer(matrix)
                else -> throw Exception("Invalid transposition type")
            }
            return transposer
        }
    }
    open fun transpose(): Array<DoubleArray> {
        // default:
        return matrix
    }
}

class MainDiagonalTransposer(matrix: Array<DoubleArray>) : Transposer(matrix) {
    override fun transpose(): Array<DoubleArray> {
        // if the original matrix is [m x n], the result matrix is [n x m]
        // because it's the transpose along a diagonal
        val matrixResult = Array(n, {DoubleArray(m)})
        for (i in 0 until m) {
            for (j in 0 until n) {
                // the first row becomes the first column, and so on,
                // so we simply exchange the indexes
                matrixResult[j][i] = matrix[i][j]
            }
        }
        return matrixResult
    }
}

class SideDiagonalTransposer(matrix: Array<DoubleArray>) : Transposer(matrix) {
    override fun transpose(): Array<DoubleArray> {
        // if the original matrix is [m x n], the result matrix is [n x m]
        // because it's the transpose along a diagonal
        val matrixResult = Array(n, {DoubleArray(m)})
        for (i in 0 until m) {
            for (j in 0 until n) {
                // the first row becomes the last column, and so on
                // [n - j - 1] means the last row when j = 0,
                // and when j = n - 1, it means the first row.
                // The same is for the column index.
                matrixResult[n - j - 1][m - i - 1] = matrix[i][j]
            }
        }
        return matrixResult
    }
}

class VerticalTransposer(matrix: Array<DoubleArray>) : Transposer(matrix) {
    override fun transpose(): Array<DoubleArray> {
        // if the original matrix is [m x n], the result matrix is [m x n]
        // because it's the transpose along a vertical line
        val matrixResult = Array(m, {DoubleArray(n)})
        for (i in 0 until m) {
            for (j in 0 until n) {
                // the first column becomes the last one and so on.
                matrixResult[i][n - j - 1] = matrix[i][j]
            }
        }
        return matrixResult
    }
}

class HorizontalTransposer(matrix: Array<DoubleArray>) : Transposer(matrix) {
    override fun transpose(): Array<DoubleArray> {
        // if the original matrix is [m x n], the result matrix is [m x n]
        // because it's the transpose along a horizontal line
        val matrixResult = Array(m, {DoubleArray(n)})
        for (i in 0 until m) {
            for (j in 0 until n) {
                // the first row becomes the last one, and so on
                matrixResult[m - i - 1][j] = matrix[i][j]
            }
        }
        return matrixResult
    }
}

private fun det(matrix: Array<DoubleArray>): Double {
    var determinant: Double = 0.0
    when (matrix.size) {
        1 -> {
            determinant = matrix[0][0]
        }
        2 -> {
            determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        }
        else -> {
            val i = 0
            for (j in matrix.indices) {
                determinant += matrix[i][j] * getCofactor(matrix, i, j)
            }
        }
    }
    return determinant
}

private fun getCofactor(matrix: Array<DoubleArray>, i: Int, j: Int) =
        (-1.0).pow(i + j) * det(getSubmatrix(matrix, i, j))

/*
 * gets the sub-matrix of matrix excluding the i-th row and the j-th column
 */
fun getSubmatrix(matrix: Array<DoubleArray>, i: Int, j: Int): Array<DoubleArray> {
//    println("SUB!")
    val m = matrix.size
    val n = matrix[0].size
    val matrixResult = Array(m - 1, {DoubleArray(n - 1)})
    for (x in 0 until m) {
        if (x != i) {
            val row = DoubleArray(n)
            if (x < i)
                matrixResult[x] = row
            else
                matrixResult[x - 1] = row
            for (y in 0 until n) {
                if (y != j) {
                    if (y < j)
                        row[y] = matrix[x][y]
                    else
                        row[y - 1] = matrix[x][y]
                }
            }
        }
    }
//    println("fineSUB")
    return matrixResult
}

private fun inverse(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val det = det(matrix)
    if (det == 0.0) {
        throw Exception("This matrix doesn't have an inverse.")
    }
    val adjugateMatrix = getAdjugateMatrix(matrix)

    val matrixResult = multiplyMatrixByScalar (adjugateMatrix, 1 / det)
    return matrixResult
}

private fun getAdjugateMatrix(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val matrixOfCofactors = getMatrixOfCofactors(matrix)
    val transposer = MainDiagonalTransposer(matrixOfCofactors)
    val adjugateMatrix = transposer.transpose()
    return adjugateMatrix
}

private fun getMatrixOfCofactors(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val m = matrix.size // matrix is m x m
    val matrixOfCofactors = Array(m, { DoubleArray(m) })
    for (i in 0 until m) {
        val row = DoubleArray(m)
        matrixOfCofactors[i] = row
        for (j in 0 until m) {
            row[j] = getCofactor(matrix, i, j)
        }
    }
    return matrixOfCofactors
}