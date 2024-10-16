package isel.leic.tds.checkers.model

data class Game (val board: List<BoardPosition>, val turn: Player)

fun getInitialBoard(boardSize: Int): List<BoardPosition>{
    TODO("Leonel")
}

fun Game.play(from: Square, to: Square): Game{
    TODO()
}