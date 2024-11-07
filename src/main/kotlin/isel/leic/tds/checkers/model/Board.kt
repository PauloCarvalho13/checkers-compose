package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM

typealias Moves = Map<Square,Piece>

sealed class Board(val moves: Moves)
class BoardRun(val turn: Player, squares: Moves = emptyMap()): Board(squares)
class BoardWin( val winner: Player, squares: Moves): Board(squares)

private val piecesPerPlayer = mapOf(8 to 12, 6 to 6,  4 to 2)

fun Board.init(): BoardRun {
    check(this is BoardRun){"Game not started"}

    val piecesPerPlayer = piecesPerPlayer[BOARD_DIM]
    checkNotNull(piecesPerPlayer){"Invalid Board size"}

    val blackSquares = Square.values.filter { it.black }

    val squaresForPlayerBlack = blackSquares.take(piecesPerPlayer)
    val squaresForPlayerWhite = blackSquares.takeLast(piecesPerPlayer)

    val initialBoard =
        squaresForPlayerWhite.associateWith { Pawn(Player.WHITE) } + squaresForPlayerBlack.associateWith { Pawn(Player.BLACK) }

    var board = BoardRun(turn = turn, initialBoard)

    val moves = listOf(
        Pair("3e".toSquare(), "4f".toSquare()),
        Pair("6f".toSquare(), "5g".toSquare()),
        Pair("2f".toSquare(), "3e".toSquare()),
        Pair("7g".toSquare(), "6f".toSquare()),
        Pair("3c".toSquare(), "4b".toSquare()),
        Pair("6b".toSquare(), "5c".toSquare()),
        Pair("2d".toSquare(), "3c".toSquare()),
        Pair("7a".toSquare(), "6b".toSquare()),
        Pair("4f".toSquare(), "5e".toSquare()),
        Pair("6d".toSquare(), "4f".toSquare()),
        Pair("4f".toSquare(), "2d".toSquare()),
        Pair("1c".toSquare(), "3e".toSquare()),
        Pair("5g".toSquare(), "4f".toSquare()),
        Pair("4b".toSquare(), "6d".toSquare()),
        Pair("4f".toSquare(), "2d".toSquare()),
        Pair("3g".toSquare(), "4f".toSquare()),
        Pair("7e".toSquare(), "5c".toSquare()),
        Pair("2h".toSquare(), "3g".toSquare()),
        Pair("2d".toSquare(), "1c".toSquare()),
        Pair("3c".toSquare(), "4b".toSquare()),
        Pair("1c".toSquare(), "5g".toSquare()),
        Pair("4b".toSquare(), "6d".toSquare()),
        Pair("7c".toSquare(), "5e".toSquare()),
        Pair("3g".toSquare(), "4f".toSquare()),
        Pair("5g".toSquare(), "3e".toSquare()),
        Pair("1e".toSquare(), "2d".toSquare()),
        Pair("3e".toSquare(), "1c".toSquare()),
        Pair("3a".toSquare(), "4b".toSquare()),
        Pair("1c".toSquare(), "3a".toSquare()),
        Pair("3a".toSquare(), "5c".toSquare()),
        Pair("1g".toSquare(), "2f".toSquare()),
        Pair("5c".toSquare(), "1g".toSquare()),
        Pair("1a".toSquare(), "2b".toSquare()),
        Pair("5e".toSquare(), "4d".toSquare()),
        Pair("2b".toSquare(), "3c".toSquare()),
        //Pair("4d".toSquare(), "2b".toSquare()),
    )

    for ((start, end) in moves) {
        board = board.play(start, end) as BoardRun
    }

    return board
}

operator fun Board.get(square: Square): Piece? = moves[square]

fun Board.isValidMove(from: Square, to: Square): Boolean{
    check(this is BoardRun){"Game not started"}
    val piece = moves[from]?: return false
    return piece.player == turn  &&
            (piece.canMove(from, to, moves) ||
             piece.canCapture(from, to, moves))
}


fun Board.play(from: Square, to: Square): Board {
    check(this is BoardRun){ "Game Over" }
    // this is now treated as a BoardRun

    if(!isValidMove(from, to)){
        println("Invalid move")
        return this
    }

    val boardAfter = this.makePlay(from , to)
    val winner = boardAfter.winner()
    return when {
        winner != null -> BoardWin(winner, boardAfter.moves)
        else -> boardAfter
    }
}

fun Board.makePlay(from: Square, to: Square): BoardRun {
    check(this is BoardRun){"Game not started"}

    val fromPiece = moves[from]
    requireNotNull(fromPiece) {"Invalid piece"}

    // get all possible captures
    val captureMoves = moves.getAllCaptures(turn)

    // Checking if the piece is a Pawn or a Queen
    val checkPiece = fromPiece.checkPiece(to)

    // update moves with the new piece
    // we already know it's a valid move and updateMoves() already solves both captures and simple moves, so no need to check which it is
    val updatedMoves = moves.updateMoves(from, to, checkPiece)

    // check if there are any captures available
    if(captureMoves.isNotEmpty()){
        // if there are but the position to is not in one of them, print the available captures and return moves as it was
        if(!captureMoves.containsKey(to)){
            println("Captures available in ${captureMoves.entries.map { it.key.toString() }}, you must capture!")
            return BoardRun(turn, moves)
        }

        // if there are still captures available print this information
        if (checkPiece.getPossibleCaptures(to, updatedMoves).isNotEmpty()) {
            println("You can play again!")
            return BoardRun(turn, updatedMoves)
        }
    }

    // if it's a simple move, return updatedMoves with change of turn
    return BoardRun(turn.other, updatedMoves)
}

fun Board.winner(): Player? {
    val white = moves.values.any { it.player == Player.WHITE }
    val black = moves.values.any { it.player == Player.BLACK }

    return when{
        white && !black -> Player.WHITE
        !white && black -> Player.BLACK
        else -> null
    }
}

fun Moves.getAllCaptures(player: Player): Moves =
    this
        .filter { it.value.player == player }
        .flatMap { (square, piece) ->
            val pieceCaptures = piece.getPossibleCaptures(square, this)
            if(!pieceCaptures.containsKey(square)) pieceCaptures.entries.map { it.toPair() }
            else listOf(square to piece) + pieceCaptures.entries.map { it.toPair() }
        }
        .toMap()

fun Moves.updateMoves(from: Square, to: Square, piece: Piece): Moves {
    val newMoves = this - from + (to to piece)

    val fromPiece = this[from] ?: return this

    val capturedSquare = if(fromPiece.canCapture(from, to, this)){
        val reverseDirection = directionOfMove(to, from)
        to.move(reverseDirection)
    } else null

    return if (capturedSquare != null)
        newMoves - capturedSquare // Remove the piece that was captured
    else
        newMoves
}
