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
    require(fromPiece != null) { "No piece at the from position" }

    val captureBoard = getAllCaptures(this.turn)

    if (captureBoard.playingPlaces.isNotEmpty()) {
        if (!captureBoard.playingPlaces.containsKey(from)) {
            println("Captures available in ${captureBoard.playingPlaces.keys}, you must capture!")
            return this
        }
    }

    if (isValidMove(from, to, this.turn)) {

        //Checking the possible Moves from can do
        val possibleMoves = fromPiece.canMove(from, board)

        // If there is no Moves check if there is Captures
        if (possibleMoves.playingPlaces.containsKey(to)) {
            //Checking if the piece is a Pawn or a Queen
            val checkPiece = checkPiece(to, fromPiece)
            // Updating the board and changing the turn
            return this.copy(board = board.updateBoard(from, to, checkPiece), turn = turn.other)
        }else{

            val possibleCaptures = fromPiece.canCapture(from, board)
            // if there is no Captures -> error
            if(possibleCaptures.playingPlaces.isEmpty()){
                println("Invalid Move")
                println("Play Again!")
                return this // still this turn because something went wrong
            }else{
                //Checking if the Square to its in possibleCaptures and update board
                if (possibleCaptures.playingPlaces.containsKey(to)){
                    //Checking if the piece is a Pawn or a Queen
                    val checkPiece = checkPiece(to, fromPiece)

                    val updatedGame = this.copy(board = board.updateBoard(from, to, checkPiece))

                    if (checkPiece.canCapture(to, updatedGame.board).playingPlaces.isNotEmpty()) {
                        println("You can play again!")
                        return updatedGame // Turn doesn't change
                    } else {
                        // If there is no more Captures, change turn
                        return updatedGame.copy(turn = turn.other)
                    }
                }
            }
        }
    } else {
        println("Invalid Position")
        return this
    }
    return this
}

fun Game.checkPiece(to: Square, piece: Piece): Piece{
    return when(piece){
        is Pawn -> {
            if(piece.player == Player.WHITE && to.row.digit == '8'){
                Queen(Player.WHITE)
            }
            else if (piece.player == Player.BLACK && to.row.digit == '1'){
                Queen(Player.BLACK)
            }
            else{
                piece // There's no condition to change pawn to queen so it remains the same piece
            }
        }
        else -> piece // if it's already a Queen stays the same
    }
}

fun Game.getAllCaptures(player: Player): Board {
    val captures = buildMap<Square, Piece?> {
        board.playingPlaces.forEach { (square, piece) ->
            if (piece != null && piece.player == player) {
                val possibleCaptures = piece.canCapture(square, board)
                if (possibleCaptures.playingPlaces.isNotEmpty()) {
                    put(square, piece) // Adiciona a peça que pode capturar
                }
            }
        }
    }
    return Board(playingPlaces = captures)
}