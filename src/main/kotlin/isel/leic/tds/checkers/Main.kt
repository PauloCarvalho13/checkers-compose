package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.Game
import isel.leic.tds.checkers.ui.listOfCommands
import isel.leic.tds.checkers.ui.readLineCommand
import isel.leic.tds.checkers.ui.show

const val BOARD_DIM = 8


fun main() {
    var game = Game()

    val cmds = listOfCommands()

    while (true){
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd == null) println("Unknown Command")
        else try {
            game = cmd.execute(args, game)
            if (cmd.leave()) break
            game.show()
        }catch (e: IllegalArgumentException){
            println(e.message)
        }catch (e:IllegalStateException){
            println(e.message)
        }
    }
}