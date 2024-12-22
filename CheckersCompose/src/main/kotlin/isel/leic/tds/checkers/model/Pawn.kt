package isel.leic.tds.checkers.model

class Pawn(player: Player): Piece(player) {
    override val type: String = "P"

    // should check the start square is in moves
    override fun canMove(from: Square, to: Square, moves: Moves): Boolean {
        val direction = directionOfMove(from, to, moves)
        // its already assured that the start position is valid, checked at Game.play()
        return direction != Direction.UNKNOWN && from.move(direction) == to && moves[to] == null
    }

    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {
        val captureDirections = player.possibleDirections()

        //Valid Moves to Capture if there is a piece different then ours in the middle of our targetSquare
        val captureMoves = captureDirections.mapNotNull { direction ->
            val targetSquare = from.move(direction)
            val middleSquare = targetSquare?.let { from.getMiddleSquare(it,moves) }
            // State of piece in MiddleSquare, if null there's no piece then we cant capture, but we can move
            // Needs to be different from the player who is playing
            // TargetSquare needs to be null, this means that the square is valid and is "empty" (no piece)
            if (middleSquare != null) {
                val middleSquarePiece = moves[middleSquare]
                val targetSquarePiece = moves[targetSquare]

                // Check if the middle square contains an opponent's piece and the target square is empty
                if (middleSquarePiece != null &&
                    middleSquarePiece.player != player &&
                    targetSquarePiece == null &&
                    moves[from] != null
                ) {
                    targetSquare to moves[from]!!
                } else {
                    null
                }
            } else {
                null
            }
        }
        // the squares need to be ordered or the presentation functions will fail
        return captureMoves.sortedBy { (square, _) -> square.index }.toMap()
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean =
        getPossibleCaptures(from, moves).containsKey(to)
}

