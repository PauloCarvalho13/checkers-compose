package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.model.*


data class Commands(
    val argsSyntax: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>,game: Game)-> Game
)
val startCommand = Commands(
    argsSyntax = "<gameId>",
    execute = { args, _ ->
        require(args.size == 1 && args[0].isNotEmpty()) { "Missing GameId" }
        Game(gameId = args[0], board = BoardRun(turn = Player.WHITE).init(), firstPlayer = Player.WHITE)
    }
)
val playCommand = Commands(
    argsSyntax = "<from> <to>",
    execute = { args, game ->
        check(game.board != null) { "Game not started" }
        require(args.size == 2) { "Missing position" }
        game.play(args[0].toSquare(), args[1].toSquare())
    }
)
val exitCommand = Commands(
    toTerminate = true,
    execute = { _, game ->
        game
    }
)
val gridCommand = Commands(
    execute = { _, game ->
        game.show()
        game
    }
)
val refreshCommand = Commands(
    execute = { _, game ->
        println("Refreshing game")
        game
    }
)
val scoreCommand = Commands(
    execute = { _, game ->
        game.showScore()
        game
    }
)

fun listOfCommands() = mapOf(
    "START" to startCommand,
    "PLAY" to playCommand,
    "EXIT" to exitCommand,
    "GRID" to gridCommand,
    "REFRESH" to refreshCommand,
    "SCORE" to scoreCommand
)
