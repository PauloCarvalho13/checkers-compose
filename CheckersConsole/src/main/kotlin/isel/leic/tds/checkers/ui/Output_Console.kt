package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.model.*

fun Clash.showScore() = (this as? ClashRun)?.game?.showScore()

fun Clash.show() = (this as? ClashRun)?.game?.show(sidePlayer)

fun Board.show(player: Player) {
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
            BOARD_DIM -> {
                if (this is BoardRun)
                    print("  Turn = ${this.turn}")
            }
            BOARD_DIM-1 -> {
                if(this is BoardRun)
                    print("  Player = ${player.name} ")
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

fun Game.show(player: Player ){
    checkNotNull(board){"No board created"}
    board.show(player)
    if (board is BoardWin)
        println("Game is Over. ${board.winner} won")
}

fun Game.showScore(){
    score.entries.forEach{ (key, value) ->
        println("${key?: "Draw"} = $value")
    }
}