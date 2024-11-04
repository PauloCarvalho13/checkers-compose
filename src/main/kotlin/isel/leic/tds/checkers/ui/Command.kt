package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.storage.Storage
import isel.leic.tds.checkers.model.*


data class Command(
    val argsSyntax: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>,game: Game)-> Game = { _,game -> game }
)


val startCommand = Command(
    argsSyntax = "<gameId>",
    execute = { args, _ ->
        require(args.size == 1 && args[0].isNotEmpty()) { "Missing GameId" }
        val game = Game(gameId = args[0], board = BoardRun(turn = Player.WHITE).init(), firstPlayer = Player.WHITE)
        game.show()
        game
    }
)

val playCommand = Command(
    argsSyntax = "<from> <to>",
    execute = { args, game ->
        check(game.board != null) { "Game not started" }
        require(args.size == 2) { "Missing position" }
        val newGame = game.play(args[0].toSquare(), args[1].toSquare())
        newGame.show()
        newGame
    }
)

val exitCommand = Command(
    toTerminate = true,
    execute = { _, game ->
        game
    }
)

val gridCommand = Command(
    execute = { _, game ->
        game.show()
        game
    }
)

val refreshCommand = Command(
    execute = { _, game ->
        println("Refreshing game")
        game.show()
        game
    }
)

val scoreCommand = Command(
    execute = { _, game ->
        game.showScore()
        game
    }
)

fun storageCommand(fx: (name: String, Game)->Game) = Command("<name>") { args, game ->
    val name = requireNotNull(args.firstOrNull()) {"Missing name"}
    fx(name, game)
}

fun listOfCommands(storage: Storage<String,Game>) = mapOf(
    "START" to startCommand,
    "PLAY" to playCommand,
    "EXIT" to exitCommand,
    "GRID" to gridCommand,
    "REFRESH" to refreshCommand,
    "SCORE" to scoreCommand,
    "SAVE" to  storageCommand { name, game -> game.also{
        if (storage.read(name)!=null) storage.update(name,game) else storage.create(name,game)
    } },
    "LOAD" to storageCommand {name, _ -> checkNotNull(storage.read(name)) }
)
