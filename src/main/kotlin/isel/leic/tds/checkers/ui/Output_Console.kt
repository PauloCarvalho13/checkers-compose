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
            val r = Row(BOARD_DIM - row)
            val c = Column(col)
            val square = "${r.digit}${c.symbol}".toSquare()
            if (square.black) {
                val boardPosition = board.playingPlaces.entries.first { it.key == square }
                if (boardPosition.value != null) {
                    val piece = boardPosition.value
                    val colourChar = piece?.player?.symbol

                    val pieceChar =
                        if (piece is Queen) colourChar?.uppercaseChar()
                        else colourChar
                    print("$pieceChar")
                } else {
                    print("-")
                }
            } else {
                print(" ")
            }
            if (col != BOARD_DIM - 1) {
                print(" ")
            }
        }
        println("|")
    }

    println(" +${"-".repeat(BOARD_DIM * 2 - 1)}+")

    println("Turn = ${turn.symbol}")
    println("Player = ${initPlayer.symbol}")
}