package isel.leic.tds.checkers.model

import BOARD_DIM

const val FIRST_CHAR_CODE = 97 // a

@JvmInline
value class Column(val index: Int) {
    init {
        require(index in 0 until BOARD_DIM) { "Invalid column index: $index" }
    }

    val symbol: Char
        get() = (FIRST_CHAR_CODE + index).toChar()


    companion object {
        val values: List<Column> = List(BOARD_DIM) { Column(it) }
    }
}

fun Char.toColumnOrNull(): Column? {
    // Only allow lowercase characters to be converted to columns
    if (this !in 'a' until 'a' + BOARD_DIM) return null
    val index = this.code - FIRST_CHAR_CODE
    return if (index in 0 until BOARD_DIM) Column(index) else null
}

fun Int.toColumn(): Column = Column.values[this]