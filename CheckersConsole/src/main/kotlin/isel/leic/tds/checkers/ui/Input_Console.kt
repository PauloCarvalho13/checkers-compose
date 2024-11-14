package isel.leic.tds.checkers.ui

data class LineCommand(val name: String, val args: List<String>)

tailrec fun readLineCommand(): LineCommand {
    print("> ")
    val line = readln().trim().split(' ')
    return if (line.isNotEmpty())
        LineCommand(line[0].uppercase(),line.drop(1))
    else readLineCommand()
}