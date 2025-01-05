package isel.leic.tds.checkers.model

import BOARD_DIM

typealias Moves = Map<Square,Piece>

sealed class Board(val moves: Moves) {
    override fun equals(other: Any?): Boolean =
        other is Board && moves == other.moves
    override fun hashCode() = moves.hashCode()
}

class BoardRun(val turn: Player, squares: Moves = emptyMap()): Board(squares)
class BoardWin( val winner: Player, squares: Moves): Board(squares)

class InvalidMoveException: IllegalArgumentException()

private val piecesPerPlayer = mapOf(8 to 12, 6 to 6,  4 to 2)

fun Board.init(): BoardRun {
    check(this is BoardRun){"Game not started"}

    val piecesPerPlayer = piecesPerPlayer[BOARD_DIM]
    checkNotNull(piecesPerPlayer){"Invalid Board size"}

    val blackSquares = Square.values.filter { it.black }

    val squaresForPlayerBlack = blackSquares.take(piecesPerPlayer)
    val squaresForPlayerWhite = blackSquares.takeLast(piecesPerPlayer)

    val initialBoard =
        squaresForPlayerWhite.associateWith { Pawn(Player.WHITE) } +
                squaresForPlayerBlack.associateWith { Pawn(Player.BLACK) }

    return BoardRun(turn, initialBoard)
}

operator fun Board.get(square: Square): Piece? = moves[square]

fun Board.isValidMove(from: Square, to: Square): Boolean{
    check(this is BoardRun){"Game not started"}
    val piece = moves[from]?: return false
    return piece.player == turn  &&
            ( piece.canCapture(from, to, moves) ||
                    (piece.canMove(from, to, moves) && moves.getAllCaptures(piece.player).isEmpty()) )
}

fun Board.play(from: Square, to: Square): Board {

    check(isThereAvailableMoves()) { "Game Over no valid moves left, please forfeit" }

    if(!isValidMove(from, to)){
       throw InvalidMoveException()
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
    this.filter { it.value.player == player }
        .flatMap { (square, piece) ->
            val pieceCaptures = piece.getPossibleCaptures(square, this)
            if(!pieceCaptures.containsKey(square)) pieceCaptures.entries.map { it.toPair() }
            else listOf(square to piece) + pieceCaptures.entries.map { it.toPair() }
        }.toMap()

fun Moves.updateMoves(from: Square, to: Square, piece: Piece): Moves {
    val fromPiece = this[from] ?: return this

    val newMoves = this - from + (to to piece)

    // Check if the move results in a capture
    val capturedSquares = if (fromPiece.canCapture(from, to, this)) {
        val path = walkPath(from, to)
        // Filter the squares in the path that contain an opponent's piece
        path.filter { this[it]?.player != fromPiece.player } // Capture only if piece belongs to opponent
    } else {
        emptyList()
    }

    // Remove the captured squares from the newMoves
    val updatedMoves = capturedSquares.fold(newMoves) { moves, capturedSquare ->
        moves - capturedSquare // Remove the captured square from the moves map
    }

    return updatedMoves
}

fun Board.isThereAvailableMoves(): Boolean {
    check(this is BoardRun) { "Game Over" }
    return moves.filter { it.value.player == turn }.any {
        it.value.getPossibleMoves(it.key, moves).isNotEmpty()
                || it.value.getPossibleCaptures(it.key, moves).isNotEmpty()
    }
}