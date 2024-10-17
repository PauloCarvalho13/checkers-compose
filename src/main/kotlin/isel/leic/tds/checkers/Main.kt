package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.Game
import isel.leic.tds.checkers.ui.listOfCommands
import isel.leic.tds.checkers.ui.readLineCommand
import isel.leic.tds.checkers.ui.showBoard

const val BOARD_DIM = 6
const val HALF_BOARD_DIM = BOARD_DIM / 2
const val BOARD_SIZE = BOARD_DIM * BOARD_DIM

fun main() {
    var game: Game? = null

    val cmds = listOfCommands()

    while (true){
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd == null) println("Unknown Command")
        else try {
            game = cmd.execute(args, game)
            if (cmd.leave()) break
            game!!.showBoard()
        }catch (e: IllegalArgumentException){
            println(e.message)
        }catch (e:IllegalStateException){
            println(e.message)
        }
    }
}