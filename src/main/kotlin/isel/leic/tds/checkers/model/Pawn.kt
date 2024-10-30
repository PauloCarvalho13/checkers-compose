package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import kotlin.math.absoluteValue

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1), //whiteMove
    UP_RIGHT(-1, 1), //whiteMove
    DOWN_LEFT(1, -1), //blackMove
    DOWN_RIGHT(1, 1), //blackMove
    UP_LEFT_CAP(2, -2),
    UP_RIGHT_CAP(2, 2),
    DOWN_LEFT_CAP(-2, -2),
    DOWN_RIGHT_CAP(-2, 2)
}

class Pawn(player: Player): Piece(player) {
    //PEAO -> Posição (4,d)
    //Pode jogar em (5,c) (5,e)

    // should check the from square is in moves
    override fun canMove(from: Square, to: Square, moves: Moves): Boolean {
        val directions = if (player == Player.WHITE) {
            listOf(Direction.UP_LEFT, Direction.UP_RIGHT)
        } else {
            listOf(Direction.DOWN_LEFT, Direction.DOWN_RIGHT)
        }
        //println("Posição atual: ${position.row.digit}, ${position.column.symbol}")

        // its already assured that the from position is valid, checked at Game.play()
        val move = directions.mapNotNull { direction ->
            from.move(direction)?.takeIf { newSquare ->
                newSquare == to && moves[newSquare] == null
            }
        }
        return move.isNotEmpty()
    }

    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {
        val captureDirections = if(player == Player.WHITE){
            listOf(Direction.DOWN_LEFT_CAP, Direction.DOWN_RIGHT_CAP)
        }else{
            listOf(Direction.UP_LEFT_CAP,Direction.UP_RIGHT_CAP)
        }

        //Valid Moves to Capture if there is a piece different then ours in the middle of our targetSquare
        val captureMoves = captureDirections.mapNotNull { direction ->
            val targetSquare = from.move(direction)
            val middleSquare = targetSquare?.let { from.getMiddleSquare(it) }

            // State of piece in MiddleSquare, if null there's no piece then we cant capture, but we can move
            // Needs to be different from the player who is playing
            // TargetSquare needs to be null, this means that the square is valid and is "empty" (no piece)
            if (middleSquare != null) {
                val middleSquarePiece = moves[middleSquare]
                val targetSquarePiece = moves[targetSquare]

                // Check if the middle square contains an opponent's piece and the target square is empty
                if (middleSquarePiece != null &&
                    middleSquarePiece.player != player &&
                    targetSquarePiece == null
                ) {
                    val fromPiece = moves[from]
                    if(fromPiece is Pawn)
                        targetSquare to Pawn(fromPiece.player)
                    else targetSquare to Queen(fromPiece!!.player) // we know that fromPiece.player is not null
                } else {
                    null
                }
            } else {
                null
            }
        }

        // the squares need to be ordered or the presentation funs will fail
        return captureMoves.toList().sortedBy { (square, _) -> square.index }.toMap()
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean =
        getPossibleCaptures(from, moves).containsKey(to)
}

