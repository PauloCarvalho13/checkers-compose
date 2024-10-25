package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.model.*

abstract class Command(val argsSyntax: String = "") {
    open fun execute(args: List<String>, game: Game): Game = game
    open fun leave(): Boolean = false
}

object PlayCommand: Command("<from> <to>") {

    override fun execute(args: List<String>, game: Game): Game {
        check(game.board != null) { "Game not started" }
        require(args.size == 2) { "Missing position" }
        return game.play(args[0].toSquare(), args[1].toSquare())
    }
}

object StartCommand: Command("<gameId>") {

    override fun execute(args: List<String>, game: Game): Game {
        require(args.size == 1 && args[0].isNotEmpty()) { "Missing GameId" }
        return Game(gameId = args[0], board = BoardRun(turn = Player.WHITE).init() , firstPlayer = Player.WHITE)
    }
}

object GridCommand: Command(){

    override fun execute(args: List<String>, game: Game): Game {
        game.show()
        return game
    }
}

object RefreshCommand: Command(){
    override fun execute(args: List<String>, game: Game): Game {
        TODO("Not yet implemented")
    }
}

object ExitCommand: Command(){
    override fun leave(): Boolean  = true
}

object ScoreCommand: Command(){
    override fun execute(args: List<String>, game: Game): Game =
        game.also { it.showScore() }

}

fun listOfCommands() = mapOf(
    "START" to StartCommand,
    "PLAY" to PlayCommand,
    "EXIT" to ExitCommand,
    "GRID" to GridCommand,
    "REFRESH" to RefreshCommand,
    "SCORE" to ScoreCommand
)
