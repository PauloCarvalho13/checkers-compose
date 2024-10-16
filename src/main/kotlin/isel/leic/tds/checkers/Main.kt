package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.Game
import isel.leic.tds.checkers.ui.listOfCommands
import isel.leic.tds.checkers.ui.readLineCommand

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
        }catch (e: IllegalArgumentException){
            println(e.message)
        }catch (e:IllegalStateException){
            println(e.message)
        }
    }
}