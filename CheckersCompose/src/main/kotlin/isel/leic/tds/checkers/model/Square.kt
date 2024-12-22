package isel.leic.tds.checkers.model

import BOARD_DIM
import isel.leic.tds.checkers.model.Square.Companion.values

class Square private constructor (val index: Int) {
    init {
        require(BOARD_DIM % 2 == 0)
    }
    val row get() = Row(index / BOARD_DIM)
    val column get() = Column(index % BOARD_DIM)

    val black: Boolean get() = (row.index + column.index) % 2 != 0

    override fun toString(): String = "${row.digit}${column.symbol}"

    companion object {
        // List of all valid squares for the current board dimension
        val values: List<Square> = List(BOARD_DIM * BOARD_DIM) {
            Square(it)
        }

    }
}

fun Square.move(direction: Direction): Square? {
    val newRow = this.row.index + direction.row
    val newColumn = this.column.index + direction.column

    return if (newRow in 0..<BOARD_DIM && newColumn in 0..<BOARD_DIM) {
        Square(Row(newRow), Column(newColumn))
    } else {
        null
    }
}

fun Square.getMiddleSquare(to: Square, moves: Moves): Square? {
    // Calculate differences
    val rowDiff = to.row.index - this.row.index
    val colDiff = to.column.index - this.column.index

    val rowStep = when {
        rowDiff < 0 -> -1  // Moving upwards
        rowDiff > 0 -> 1   // Moving downwards
        else -> 0
    }

    val colStep = when {
        colDiff < 0 -> -1  // Moving left
        colDiff > 0 -> 1   // Moving right
        else -> 0
    }

    // function to walk along the path
    tailrec fun walk(currentSquare: Square): Square? {
        // Calculate the next square in the direction of the movement
        val nextRow = currentSquare.row.index + rowStep
        val nextCol = currentSquare.column.index + colStep

        val nextSquare = Square(Row(nextRow), Column(nextCol))

        // If we have reached the destination square, return null
        if (nextRow == to.row.index && nextCol == to.column.index) {
            return null
        }

        val pieceAtMiddle = moves[nextSquare]
        if (pieceAtMiddle != null) {
            return nextSquare  // Return the square if a piece is found
        }

        // Continue until a piece is found, or we reach the destination square
        return walk(nextSquare)
    }

    return walk(this)
}


fun Square(row: Row, column: Column): Square
        = values[row.index * BOARD_DIM + column.index % BOARD_DIM]

fun String.toSquareOrNull(): Square? {
    if (this.length != 2) return null
    val row = this[0].toRowOrNull()
    val column = this[1].toColumnOrNull()

    return if (row != null && column != null) Square(row, column) else null
}

fun String.toSquare(): Square
    = this.toSquareOrNull() ?: throw IllegalArgumentException("Invalid square string format")

