package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.BOARD_SIZE
import isel.leic.tds.checkers.HALF_BOARD_DIM

data class Game (val gameId: String, val board: Board, val turn: Player, val initPlayer: Player)

/*
fun getInitialBoard(): Map<Square,Piece> =
    List(BOARD_SIZE) {
        val square = Square(Row(it / BOARD_DIM), Column(it % BOARD_DIM))
        when (square.row.index) {
            // whites
            in 0 until HALF_BOARD_DIM -1 -> fillPos(square, Player.WHITE)

            // middle 2 rows
            in (HALF_BOARD_DIM - 1) until HALF_BOARD_DIM + 1 -> square

            // blacks
            else -> fillPos(square, Player.BLACK)
        }
    }
*/

fun Game.isValidMove(piecePosition:Square, newPiecePosition:Square, turn: Player)
        = board.playingPlaces[piecePosition] != null
        && board.playingPlaces[newPiecePosition] != null
        && board.playingPlaces[piecePosition]?.player == turn
        && board.playingPlaces[newPiecePosition]?.player != null

/*
fun Game.play(from: Square, to: Square): Game {

    if (isValidMove(from, to, this.turn)) {
        //I want to change the piece in the boardPosition (square, piece)
        //the pieceposition piece becomes null and newpiecePosition changes to pieceposition piece
        //in the board

        when(board.playingPlaces[from]){
            is Pawn ->
            is Queen ->
            else ->
        }






        val updatedBoard =
            if(canCapture(from)){
                Capture(to)

            }else{
                if (canMove(piecePosition)){
                      Move(newPiecePosition)
                } else {
                    this.board.also { turn.other }
                }
            }

        return this.copy(board = updatedBoard)
    } else {
        println("Position Invalid")
        return this

    }
}

//PEAO -> Posição (4,d)
//Pode jogar em (2,b) (6,f) (2,f) (6,b)


//DAMA ->
fun Game.canCapture(position: BoardPosition): Boolean{
    return when(position.piece?.type){
        Type.PEAO ->{}
        Type.DAMA ->{}
        else -> false
    }
}

fun Game.canMove(position: BoardPosition): Boolean{
    val updatedBoard = board.map { boardPosition ->
        when (boardPosition) {
            piecePosition -> boardPosition.copy(piece = null)
            newPiecePosition -> boardPosition.copy(piece = piecePosition.piece)
            else -> boardPosition
        }
    }
    TODO()
}

fun Game.Move(position: BoardPosition): List<BoardPosition>{
    TODO()
}

fun Game.Capture(position: BoardPosition): List<BoardPosition>{
    TODO()
}
*/