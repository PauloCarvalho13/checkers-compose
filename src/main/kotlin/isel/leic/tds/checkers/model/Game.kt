package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.BOARD_SIZE
import isel.leic.tds.checkers.HALF_BOARD_DIM

data class Game (val gameId: String, val board: Board, val turn: Player, val initPlayer: Player )

fun getInitialBoard(): Board =
    Board(
       playingPlaces = buildMap {
           repeat(BOARD_SIZE){ i ->
               val square = Square(Row(i / BOARD_DIM), Column(i % BOARD_DIM))

               // only save when its black square
               if(square.black){
                   when (square.row.index) {
                       // blacks
                       in 0 until HALF_BOARD_DIM -1 -> put(square, Pawn(Player.BLACK))

                       // middle 2 rows
                       in (HALF_BOARD_DIM - 1) until HALF_BOARD_DIM + 1 -> put(square, null)

                       // whites
                       else -> put(square, Pawn(Player.WHITE))
                   }
               }
           }
       }
    )

fun Game.isValidMove(piecePosition: Map.Entry<Square, Piece?>, newPiecePosition: Map.Entry<Square, Piece?>, turn: Player)
        =   piecePosition.value != null
        && piecePosition.value!!.player == turn
        && newPiecePosition.key.black
        && newPiecePosition.value == null


fun Game.play(from: Square, to: Square): Game {
    val piecePosition = board.playingPlaces.entries.first { it.key == from }
    val newPiecePosition = board.playingPlaces.entries.first { it.key == to }

    if (isValidMove(piecePosition, newPiecePosition, this.turn)) {
        //I want to change the piece in the boardPosition (square, piece)
        //the pieceposition piece becomes null and newpiecePosition changes to pieceposition piece
        //in the board
        //val checkCapture = canCapture(piecePosition)

        val updatedBoard = board.playingPlaces.mapValues { (square, piece) ->
            when (square) {
                piecePosition.key -> null
                newPiecePosition.key -> newPiecePosition.value
                else -> piece
            }
        }
        //Verify if newPiece has more captures so it can still play

        val updatedGame = this.copy(board = Board(updatedBoard))

    } else {
        println("Position Invalid")
        return this

    }
    return this
}

//PEAO -> Posição (4,d)
//Pode jogar em (2,b) (6,f) (2,f) (6,b)

/*
//DAMA ->
fun Game.canCapture(position: BoardPosition): Boolean{
    return when(position.piece?.type){
        Type.PEAO ->{}
        Type.DAMA ->{}
        else -> false
    }
}*/

fun Game.canMove(position: Board): Boolean{
    TODO()
}

fun Game.Move(){
    TODO()
}

fun Game.Capture(){
    TODO()
}
