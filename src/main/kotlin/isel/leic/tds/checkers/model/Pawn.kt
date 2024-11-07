package isel.leic.tds.checkers.model

class Pawn(player: Player): Piece(player) {
    override val type: String
        get() = "P"

    // should check the from square is in moves
    override fun canMove(from: Square, to: Square, moves: Moves): Boolean {
        val direction = directionOfMove(from, to)
        // its already assured that the from position is valid, checked at Game.play()
        val newSquare = from.move(direction)
        return newSquare == to && moves[newSquare] == null
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
                    targetSquare to moves[from]!! // moves[from] was checked to not be null before
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

