package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.BOARD_SIZE
import isel.leic.tds.checkers.HALF_BOARD_DIM

data class Game (val id: Int, val board: List<BoardPosition>, val turn: Player)


private fun fillPos(colour: Player, row: Int, col: Int): BoardPosition =
    // even row
    if(row % 2 == 0){
        // even cols write
        if(col%2==0) BoardPosition(Square(Row(row), Column(col)), Piece(Type.PEAO, colour)) else BoardPosition(Square(Row(row), Column(col)), null)
    }else{
        // odd row
        // odd cols write
        if(col%2!= 0) BoardPosition(Square(Row(row), Column(col)), Piece(Type.PEAO, colour)) else BoardPosition(Square(Row(row), Column(col)), null)
    }

fun getInitialBoard(boardSize: Int): List<BoardPosition> =
    // 24ps - 8x8, 12ps - 6x6, 4 - 4x4
    List(BOARD_SIZE) {
        val row = (it / BOARD_DIM)
        val col = (it % BOARD_DIM)
        when (row) {
            // whites
            in 0 until HALF_BOARD_DIM -1 -> fillPos(Player.WHITE, row, col)

            // middle 2 rows
            in (HALF_BOARD_DIM - 1) until HALF_BOARD_DIM + 1 -> BoardPosition(Square(Row(row), Column(col)), null)

            // blacks
            else -> fillPos(Player.BLACK, row, col)
        }
    }

fun Game.isValidMove(piecePosition:BoardPosition, newPiecePosition:BoardPosition, turn: Player)
        =   piecePosition.piece != null
        && piecePosition.piece.player == turn
        && newPiecePosition.square.black
        && newPiecePosition.piece == null

fun Game.play(from: Square, to: Square): Game {
    val piecePosition = board.first { it.square == from }
    val newPiecePosition = board.first { it.square == to }

    return if (isValidMove(piecePosition, newPiecePosition, this.turn)) {
        //TODO()
        turn.other
        this
    } else {
        println("Position Invalid")
        this

    }
}