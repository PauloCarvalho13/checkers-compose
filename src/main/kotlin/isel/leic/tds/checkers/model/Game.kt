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

fun Game.isValidMove(piecePosition: Square, newPiecePosition: Square, turn: Player)
        =  board.playingPlaces[piecePosition] != null
        && board.playingPlaces[piecePosition]?.player == turn
        && newPiecePosition.black
        && board.playingPlaces[newPiecePosition] == null


fun Game.play(from: Square, to: Square): Game {
    val fromPiece = board.playingPlaces[from]
    require(fromPiece != null) { "No piece at the starting position" }

    if (isValidMove(from, to, this.turn)) {

        //Checking the possible Moves from can do
        val possibleMoves = fromPiece.canMove(from, board)

        // If there is no Moves check if there is Captures
        if (possibleMoves.playingPlaces.containsKey(to)) {
            return this.copy(board = board.updateBoard(from, to, fromPiece), turn = turn.other)
        }else{
            val possibleCaptures = fromPiece.canCapture(from, board)
            // if there is no Captures -> error
            if(possibleCaptures.playingPlaces.isEmpty()){
                println("NO CAPTURES AND NO MOVES")
                return this // still this turn because something went wrong
            }else{
                //Checking if the Square to its in possibleCaptures and update board
                if (possibleCaptures.playingPlaces.containsKey(to)){
                    val updatedGame = this.copy(board = board.updateBoard(from, to, fromPiece))

                    if (fromPiece.canCapture(to, updatedGame.board).playingPlaces.isNotEmpty()) {
                        println("Multiple captures possible!")
                        return updatedGame // Turn doesn't change
                    } else {
                        // If there is no more Captures, change turn
                        return updatedGame.copy(turn = turn.other)
                    }
                }
            }
        }
    } else {
        println("Position Invalid")
        return this
    }
    return this
}

