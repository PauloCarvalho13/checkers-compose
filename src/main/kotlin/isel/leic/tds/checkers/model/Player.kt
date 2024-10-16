package isel.leic.tds.checkers.model

enum class Player {
    BLACK, WHITE;
    val symbol get() = if (this == WHITE) 'w' else 'b'
    val other get() = if (this == WHITE) BLACK else WHITE
}