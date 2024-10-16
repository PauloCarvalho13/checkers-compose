package isel.leic.tds.checkers.model

enum class Player {
    BLACK, WHITE;

    val other get() = if (this == WHITE) BLACK else WHITE
}