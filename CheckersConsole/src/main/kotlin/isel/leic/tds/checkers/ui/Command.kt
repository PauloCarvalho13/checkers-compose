package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.model.*


data class Command(
    val argsSyntax: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>, clash: Clash)-> Clash = { _, clash -> clash }
)

val playCommand = Command(
    argsSyntax = "<from> <to>",
    execute = { args, clash  ->
        require(args.size == 2) { "Missing position" }
        clash.play(args[0].toSquare(), args[1].toSquare())
    }
)

val exitCommand = Command(
    toTerminate = true
)

val gridCommand = Command(
    execute = { _, clash ->
        clash.also { it.show() }
    }
)

val refreshCommand = Command(
    execute = { _, clash ->
        clash.refresh()
    }
)

val scoreCommand = Command(
    execute = { _, clash ->
        clash.also { it.showScore() }
    }
)

fun nameCmd(fx: Clash.(Name)-> Clash) = Command("<name>") { args, clash ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing name" }
    clash.fx(Name(arg))
}

fun listOfCommands() = mapOf(
    "START" to nameCmd(Clash::start),
    "PLAY" to playCommand,
    "EXIT" to exitCommand,
    "GRID" to gridCommand,
    "REFRESH" to refreshCommand,
    "SCORE" to scoreCommand
)
