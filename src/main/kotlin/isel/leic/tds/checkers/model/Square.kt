package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.model.Square.Companion.values

class Square private constructor (val index: Int) {
    init {
        require( BOARD_DIM % 2 == 0)
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

