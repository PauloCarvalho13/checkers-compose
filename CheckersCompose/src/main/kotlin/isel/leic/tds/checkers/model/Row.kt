package isel.leic.tds.checkers.model

import BOARD_DIM


@JvmInline
value class Row(val index: Int) {
    init {
        require(index in 0 until BOARD_DIM) { "Invalid row index: $index" }
    }

    val digit: Char
        get() = ('1' + (BOARD_DIM - 1 - index))

    companion object {
        val values: List<Row> = List(BOARD_DIM) { Row(it) }
    }
}

fun Char.toRowOrNull(): Row? {
    val index = BOARD_DIM - (this - '1') - 1
    return if (index in 0 until BOARD_DIM) Row(index) else null
}

fun Int.toRow(): Row = Row.values[this]