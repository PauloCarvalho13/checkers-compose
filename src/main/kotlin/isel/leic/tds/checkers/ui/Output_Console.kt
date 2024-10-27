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
            val r = Row(BOARD_DIM - row)
            val c = Column(col)
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

        print("|")
        when(row){
            8 -> {
                if (this is BoardRun)
                    print("  Turn = ${turn.symbol}")
            }
            7 -> {
                if(this is BoardRun)
                    print("  Player = ${turn.symbol} (this is turn cuz we are yet to save the which player it is)")
            }
        }
        println()
    }

    println(" +${"-".repeat(BOARD_DIM * 2 - 1)}+")

    print("  ")
    repeat(BOARD_DIM) { col -> print("${'a' + col} ") }
    println()
    println("")
}

fun Game.show(){
    // command is only called once a game has been started already, so is running
    check(board is BoardRun) { "not running" }
    board.show()
}

fun Game.showScore(){
    score.entries.forEach{ (key, value) ->
        println("${key?: "Draw"} = $value")
    }
}