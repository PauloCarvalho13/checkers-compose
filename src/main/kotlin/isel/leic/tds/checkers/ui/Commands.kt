package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.model.*

abstract class Command(val argsSyntax: String = "") {
    open fun execute(args: List<String>, game: Game?): Game? = game
    open fun leave(): Boolean = false
}

object PlayCommand: Command("<from> <to>") {

    override fun execute(args: List<String>, game: Game?): Game {
        check(game != null) { "Game not started" }
        require(args.size == 2) { "Missing position" }
        return game.play(args[0].toSquare(), args[1].toSquare())
    }
}

object StartCommand: Command("<gameId>") {

    override fun execute(args: List<String>, game: Game?): Game {
        require(args.size == 1 && args[0].isNotEmpty()) { "Missing GameId" }
        return Game(gameId = args[0], board = getInitialBoard(), turn = Player.WHITE, initPlayer = Player.WHITE)
    }
}

object GridCommand: Command(){

    override fun execute(args: List<String>, game: Game?): Game {
        check(game != null) { "Game yet to be started" }
        return game
    }
}

object RefreshCommand: Command(){
    override fun execute(args: List<String>, game: Game?): Game {
        TODO("Not yet implemented")
    }
}

object ExitCommand: Command(){
    override fun leave(): Boolean  = true
}
fun listOfCommands() = mapOf(
    "START" to StartCommand,
    "PLAY" to PlayCommand,
    "EXIT" to ExitCommand,
    "GRID" to GridCommand,
    "REFRESH" to RefreshCommand
)
