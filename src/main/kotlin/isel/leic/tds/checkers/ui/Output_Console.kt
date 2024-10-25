package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.model.*

fun Board.show() {
    print("  ")
    println()

    println(" +${"-".repeat(BOARD_DIM * 2 - 1)}+")

    (BOARD_DIM downTo 1).forEach { row ->

        print("$row|")

        repeat(BOARD_DIM) { col ->
            val r = Row(row - 1)
            val c = Column(BOARD_DIM - col - 1)
            val square = Square(r, c)

            if (square.black) {

                val piece = moves[square]

                if (piece != null) {
                    val colourChar = piece.player.symbol

                    val pieceChar = if (piece is Queen) colourChar.uppercaseChar() else colourChar
                    print(pieceChar)
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

    print("  ")
    repeat(BOARD_DIM) { col -> print("${'a' + col} ") }
    println()
}

fun Game.show() = board?.show()

fun Game.showScore(){
    score.entries.forEach{ (key, value) ->
        println("${key?: "Draw"} = $value")
    }
}