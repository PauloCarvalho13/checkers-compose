package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.BOARD_SIZE
import isel.leic.tds.checkers.HALF_BOARD_DIM

data class Game (val gameId: String, val board: List<BoardPosition>, val turn: Colour, val initPlayer: Colour )


private fun fillPos(square: Square, colour: Colour): BoardPosition =
    // even row
    if(square.row.index % 2 == 0){
        // even cols write
        val piece = if(square.column.index %2==0) Piece(Type.PEAO, colour) else null
        BoardPosition(square, piece)
    }else{
    // odd row
        // odd cols write
        val piece = if(square.column.index %2!= 0) Piece(Type.PEAO, colour) else null
        BoardPosition(square, piece)
    }

fun getInitialBoard(): List<BoardPosition> =
    List(BOARD_SIZE) {
        val square = Square(Row(it / BOARD_DIM), Column(it % BOARD_DIM))
        when (square.row.index) {
            // whites
            in 0 until HALF_BOARD_DIM -1 -> fillPos(square, Colour.WHITE)

            // middle 2 rows
            in (HALF_BOARD_DIM - 1) until HALF_BOARD_DIM + 1 -> BoardPosition(square, null)

            // blacks
            else -> fillPos(square, Colour.BLACK)
        }
    }

fun Game.isValidMove(piecePosition:BoardPosition, newPiecePosition:BoardPosition, turn: Colour)
        =   piecePosition.piece != null
        && piecePosition.piece.colour == turn
        && newPiecePosition.square.black
        && newPiecePosition.piece == null


fun Game.play(from: Square, to: Square): Game {
    val piecePosition = board.first { it.square == from }
    val newPiecePosition = board.first { it.square == to }

    if (isValidMove(piecePosition, newPiecePosition, this.turn)) {
        //I want to change the piece in the boardPosition (square, piece)
        //the pieceposition piece becomes null and newpiecePosition changes to pieceposition piece
        //in the board
        //val checkCapture = canCapture(piecePosition)

        val updatedBoard = board.map { boardPosition ->
            when (boardPosition) {
                piecePosition -> boardPosition.copy(piece = null)
                newPiecePosition -> boardPosition.copy(piece = piecePosition.piece)
                else -> boardPosition
            }
        }
        //Verify if newPiece has more captures so it can still play

        val updatedGame = this.copy(board = updatedBoard)

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

fun Game.canMove(position: BoardPosition): Boolean{
    TODO()
}

fun Game.Move(){
    TODO()
}

fun Game.Capture(){
    TODO()
}
