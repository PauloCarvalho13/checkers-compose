package isel.leic.tds.checkers.model

enum class Type{
    DAMA, PEAO
}

data class Piece(val type: Type, val colour: Colour)