package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.Clash
import isel.leic.tds.checkers.model.Game
import isel.leic.tds.checkers.model.GameSerializer
import isel.leic.tds.checkers.model.Name
import isel.leic.tds.checkers.storage.TextFileStorage
import isel.leic.tds.checkers.ui.listOfCommands
import isel.leic.tds.checkers.ui.readLineCommand


const val BOARD_DIM = 8


fun main() {
    val storage = TextFileStorage<Name,Game>("games", GameSerializer)
    var clash = Clash(storage)
    val cmds = listOfCommands()

    while (true){
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd == null) println("Unknown Command")
        else try {
            clash = cmd.execute(args, clash)
            if (cmd.toTerminate) break
        }catch (e: IllegalArgumentException){
            println(e.message)
        }catch (e:IllegalStateException){
            println(e.message)
        }
    }
}