package isel.leic.tds.checkers.model

data class Game (val id: Int, val board: List<BoardPosition>, val turn: Player)

fun getInitialBoard(boardSize: Int): List<BoardPosition>{
    TODO("Leonel")
}

fun Game.play(from: Square, to: Square): Game{
    val piecePosition = board.first { it.square == from }
    val newPiecePosition = board.first { it.square == to }

    return if(isValidMove(piecePosition, newPiecePosition, this.turn)){
        TODO()
        turn.other
        this
    }else{
        println("Position Invalid")
        this

    }

}

fun Game.isValidMove(piecePosition:BoardPosition, newPiecePosition:BoardPosition, turn: Player)
    =   piecePosition.piece != null
        && piecePosition.piece.player == turn
        && newPiecePosition.square.black
        && newPiecePosition.piece == null