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

    val movesAfter = this.makePlay(from , to)
    val nextTurn = if(moves != movesAfter) turn.other else turn
    val winner = winner()
    return when {
        winner != null -> BoardWin(winner, movesAfter)
        else -> BoardRun(nextTurn, movesAfter)
    }
}

fun Board.makePlay(from: Square, to: Square): Moves {
    check(this is BoardRun){"Game not started"}

    val fromPiece = moves[from]
    requireNotNull(fromPiece) {"Invalid piece"}

    val captureMoves = getAllCaptures(turn)

    if(captureMoves.moves.isNotEmpty()){
        if(!captureMoves.moves.containsKey(to)){
            println("Captures available in ${captureMoves.moves.entries}, you must capture!")
            return moves
        }

    }else{
        //Checking if the Square to its in possibleCaptures and update board
        // Checking if the piece is a Pawn or a Queen
        val checkPiece = checkPiece(to, fromPiece)

        val updatedMoves = moves.updateMoves(from, to, checkPiece)

        if (checkPiece.getPossibleCaptures(to, updatedMoves).isNotEmpty()) {
            println("You can play again!")
            return updatedMoves // Turn doesn't change
        } else {
            // If there is no more Captures, change turn
            return updatedMoves
        }
    }

    if (fromPiece.canMove(from, to, moves)) {
        //Checking if the piece is a Pawn or a Queen
        val checkPiece = checkPiece(to, fromPiece)
        // Updating the board and changing the turn
        return moves.updateMoves(from, to, checkPiece)
    }else{
        // if there is no Captures -> error
        if(!fromPiece.canCapture(from, to, moves)){
            println("Invalid Move")
            println("Play Again!")
            return moves // still this turn because something went wrong
        }else{

        }
    }
    return moves
}

// TODO
fun Board.winner(): Player? {
    return null
}


fun Moves.hasNoValidPlay(): Boolean{
    return true
}


fun Board.getAllCaptures(player: Player): Board {
    val captures = buildMap<Square, Piece> {
        moves
            .filter { it.value.player == player }
            .forEach { (square, piece) ->
                val pieceCaptures = piece.getPossibleCaptures(square, moves)
                pieceCaptures.forEach { (square, piece) ->
                    put(square, piece)
                }
        }
    }
    return BoardRun(player, captures)
}

fun Board.checkPiece(to: Square, piece: Piece): Piece{
    return when(piece){
        is Pawn -> {
            if(piece.player == Player.WHITE && to.row.digit == '8'){
                Queen(Player.WHITE)
            }
            else if (piece.player == Player.BLACK && to.row.digit == '1'){
                Queen(Player.BLACK)
            }
            else{
                piece // There's no condition to change pawn to queen so it remains the same piece
            }
        }
        else -> piece // if it's already a Queen stays the same
    }
}

fun Moves.updateMoves(from: Square, to: Square, piece: Piece): Moves {
    val newMoves = this - from + (to to piece)

    val middleSquare = from.getMiddleSquare(to)
    return if (middleSquare != null)
        newMoves - middleSquare // Remove the piece that was captured
    else
        newMoves
}
