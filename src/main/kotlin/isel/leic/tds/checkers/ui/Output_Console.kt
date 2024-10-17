package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.model.*

fun Game.showBoard(){

    print("  ")
    repeat(BOARD_DIM) { col -> print("${'a' + col} ") }
    println()
    println(" +${"-".repeat(BOARD_DIM * 2 - 1)}+")

    (BOARD_DIM downTo 1).forEach { row ->

        print("$row|")

        repeat(BOARD_DIM) { col ->
            val square = Square(Row(row - 1), Column(col))
            val boardPosition = board.find { it.square == square }

            if (boardPosition?.piece != null) {
                val piece = boardPosition.piece
                val colourChar = piece.colour.symbol

                val pieceChar =
                    if (piece.type == Type.DAMA) colourChar.uppercaseChar()
                    else colourChar
                print("$pieceChar")
                if(col != BOARD_DIM - 1){
                    print(" ")
                }

            } else {
                if (square.black) {
                    print("-")
                } else {
                    print(" ")
                }
                if(col != BOARD_DIM -1){
                    print(" ")
                }
            }
        }
        println("|")
    }

    println(" +${"-".repeat(BOARD_DIM * 2 - 1)}+")

    println("Turn = ${turn.symbol}")
    println("Player = ${initPlayer.symbol}")
}