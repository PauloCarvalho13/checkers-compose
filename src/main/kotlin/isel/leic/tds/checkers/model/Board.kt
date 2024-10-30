package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM

typealias Moves = Map<Square,Piece>

sealed class Board(val moves: Moves)
class BoardRun(val turn: Player, val squares: Moves = emptyMap()): Board(squares)
class BoardWin( val winner: Player, val squares: Moves): Board(squares)


private val piecesPerPlayer = mapOf(8 to 12, 6 to 8,  4 to 2)

fun Board.init(): BoardRun {
    check(this is BoardRun){"Game not started"}

    val piecesPerPlayer = piecesPerPlayer[BOARD_DIM]
    checkNotNull(piecesPerPlayer){"Invalid Board size"}

    val blackSquares = Square.values.filter { it.black }

    val squaresForPlayerBlack = blackSquares.take(piecesPerPlayer)
    val squaresForPlayerWhite = blackSquares.takeLast(piecesPerPlayer)

    val initialBoard =
        squaresForPlayerWhite.associateWith { Pawn(Player.WHITE) } + squaresForPlayerBlack.associateWith { Pawn(Player.BLACK) }
    return BoardRun(turn = turn, initialBoard)
}

operator fun Board.get(square: Square): Piece? = moves[square]

fun Board.isValidMove(piecePosition: Square, newPiecePosition: Square): Boolean{
    check(this is BoardRun){"Game not started"}
    val piece = moves[piecePosition]?: return false
    return piece.player == turn  &&
            (piece.canMove(piecePosition, newPiecePosition, moves) ||
             piece.canCapture(piecePosition, newPiecePosition, moves))
}


fun Board.play(from: Square, to: Square): Board {
    check(this is BoardRun){ "Game Over" }
    // this is now treated as a BoardRun

    if(!isValidMove(from, to)){
        println("Invalid move")
        return this
    }

    val boardAfter = this.makePlay(from , to)
    val winner = winner()
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
    val groupedMoves: Map<Player, Boolean> = Player.entries.associateWith { player ->
        moves.values.any{ it.player == player }
    }

    val winningPlayer = groupedMoves.filter { !it.value }.keys.firstOrNull()

    return winningPlayer
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

    val middleSquare = from.getMiddleSquare(to)
    return if (middleSquare != null)
        newMoves - middleSquare // Remove the piece that was captured
    else
        newMoves
}
