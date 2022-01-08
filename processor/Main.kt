package processor

import java.util.Scanner
import kotlin.math.pow

class Matrix(private val numberOfRows: Int, private val numberOfColumns: Int) {
    val matrix = Array(numberOfRows) { DoubleArray(numberOfColumns) }
    fun add(anotherMatrix: Matrix): Matrix? {
        return if (numberOfRows != anotherMatrix.numberOfRows || numberOfColumns != anotherMatrix.numberOfColumns) {
            null
        } else {
            val resultMatrix = Matrix(numberOfRows, numberOfColumns)
            for (i in 0 until numberOfRows) {
                for (j in 0 until numberOfColumns) {
                    resultMatrix.matrix[i][j] = matrix[i][j] + anotherMatrix.matrix[i][j]
                }
            }
            resultMatrix
        }
    }
    fun multiplicationByNumber(c: Double): Matrix {
        val resultMatrix = Matrix(numberOfRows, numberOfColumns)
        for (i in 0 until numberOfRows) {
            for (j in 0 until numberOfColumns) {
                resultMatrix.matrix[i][j] = matrix[i][j] * c
            }
        }
        return resultMatrix
    }
    fun multiplicationByMatrix(anotherMatrix: Matrix): Matrix {
        val resultMatrix = Matrix(numberOfRows, anotherMatrix.numberOfColumns)
        for (i in 0 until numberOfRows) {
            for (j in 0 until anotherMatrix.numberOfColumns) {
                var sum = 0.0
                for (k in 0 until numberOfColumns) {
                    sum += matrix[i][k] * anotherMatrix.matrix[k][j]
                }
                resultMatrix.matrix[i][j] = sum
            }
        }
        return resultMatrix
    }
    fun transpose(): Matrix {
        val transposedMatrix = Matrix(numberOfColumns, numberOfRows)
        for (i in 0 until transposedMatrix.numberOfRows) {
            for (j in 0 until transposedMatrix.numberOfColumns)
                transposedMatrix.matrix[i][j] = matrix[j][i]
        }
        return transposedMatrix
    }
    fun transposeSideDiagonal(): Matrix {
        val transposedMatrix = Matrix(numberOfColumns, numberOfRows)
        for (i in 0 until transposedMatrix.numberOfRows) {
            for(j in 0 until transposedMatrix.numberOfColumns) {
                transposedMatrix.matrix[i][j] = matrix[numberOfRows-j-1][numberOfColumns-i-1]
            }
        }
        return transposedMatrix
    }
    fun transposeVerticalLine(): Matrix {
        val transposedMatrix = Matrix(numberOfRows, numberOfColumns)
        for (i in 0 until transposedMatrix.numberOfRows) {
            for(j in 0 until transposedMatrix.numberOfColumns) {
                transposedMatrix.matrix[i][j] = matrix[i][numberOfColumns-1-j]
            }
        }
        return transposedMatrix

    }
    fun transposeHorizontalLine(): Matrix {
        val transposedMatrix = Matrix(numberOfRows, numberOfColumns)
        for (i in 0 until transposedMatrix.numberOfRows) {
            for(j in 0 until transposedMatrix.numberOfColumns) {
                transposedMatrix.matrix[i][j] = matrix[numberOfRows-1-i][j]
            }
        }
        return transposedMatrix


    }
    fun determinant(): Double {
        var determinant = 0.0
            if (numberOfColumns == 2 && numberOfRows == 2) {
                determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
            } else {
                for (k in 0 until numberOfColumns) {
                    val minorMatrix = Matrix(numberOfRows-1, numberOfColumns-1)
                    for (i in 0 until minorMatrix.numberOfRows) {
                        for (j in 0 until minorMatrix.numberOfColumns) {
                            if (j < k) {
                                minorMatrix.matrix[i][j] = matrix[i + 1][j]
                            } else {
                                minorMatrix.matrix[i][j] = matrix[i + 1][j + 1]
                            }
                        }
                    }
                    determinant += (-1.0).pow(k) * matrix[0][k] * minorMatrix.determinant()
                }
            }
        return determinant
    }
    fun inverse(): Matrix {
        var invertedMatrix = Matrix(numberOfRows, numberOfColumns)
        val det = this.determinant()
        for (x in 0 until invertedMatrix.numberOfRows) {
            for (y in 0 until invertedMatrix.numberOfColumns) {
                val minorMatrix = Matrix(numberOfRows-1, numberOfColumns-1)
                for (i in 0 until minorMatrix.numberOfRows) {
                    for (j in 0 until minorMatrix.numberOfColumns) {
                        if (i < x && j < y) {
                            minorMatrix.matrix[i][j] = matrix[i][j]
                        } else if (i < x && j >= y) {
                            minorMatrix.matrix[i][j] = matrix[i][j+1]
                        } else if (i >= x && j < y) {
                            minorMatrix.matrix[i][j] = matrix[i+1][j]
                        } else {
                            minorMatrix.matrix[i][j] = matrix[i+1][j+1]
                        }
                    }
                }
                invertedMatrix.matrix[x][y] = minorMatrix.determinant() * (-1.0).pow(x+y)
            }
        }
        invertedMatrix = invertedMatrix.transpose().multiplicationByNumber(1/det)
        return invertedMatrix
    }
    fun print() {
        for (i in 0 until numberOfRows) {
            println(matrix[i].joinToString(separator = " "))
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    fun readMatrix(numberOfMatrix: String = ""): Matrix { //numberOfMatrix string just needed to add in sentences like "Enter size of FIRST matrix:"
        println("Enter size of $numberOfMatrix matrix")
        val rows = scanner.nextInt()
        val columns = scanner.nextInt()
        println("Enter $numberOfMatrix matrix")
        val matrix = Matrix(rows, columns)
        for (i in 0 until rows) {
            for (j in 0 until columns)
                matrix.matrix[i][j] = scanner.nextDouble()
        }
        return matrix

    }
    while (true) {
        println("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit")
        print("Your choice: ")

        when (readLine()!!.toInt()) {
            1 -> {
                val matrixA = readMatrix("first")
                val matrixB = readMatrix("second")
                println("The result is:")
                matrixA.add(matrixB)?.print()
            }
            2 -> {
                val matrix = readMatrix()
                val c = readLine()!!.toDouble()
                println("The result is:")
                matrix.multiplicationByNumber(c).print()
            }
            3 -> {
                val matrixA = readMatrix("first")
                val matrixB = readMatrix("second")
                println("The result is:")
                matrixA.multiplicationByMatrix(matrixB).print()
            }
            4 -> {
                println("1. Main diagonal\n" +
                        "2. Side diagonal\n" +
                        "3. Vertical line\n" +
                        "4. Horizontal line")
                print("Your choice: ")
                when(readLine()!!.toInt()) {

                    1 -> {
                        val matrix = readMatrix()
                        println("The result is:")
                        matrix.transpose().print()
                    }
                    2 -> {
                        val matrix = readMatrix()
                        println("The result is:")
                        matrix.transposeSideDiagonal().print()
                    }
                    3 -> {
                        val matrix = readMatrix()
                        println("The result is:")
                        matrix.transposeVerticalLine().print()
                    }
                    4 -> {
                        val matrix = readMatrix()
                        println("The result is:")
                        matrix.transposeHorizontalLine().print()
                    }
                }
            }
            5 -> {
                val matrix = readMatrix()
                println("The result is:")
                println(matrix.determinant())
            }
            6 -> {
                val matrix = readMatrix()
                println("The result is:")
                matrix.inverse().print()
            }
            0 -> { break }
        }
        println()
    }


}
