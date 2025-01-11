package isel.leic.tds.checkers.model

class Pawn(player: Player): Piece(player) {
    override val type: String = "P"

    private val captureDirections get() = possibleDirections(true)
    private val moveDirections: List<Direction> get() = possibleDirections(false)

    override fun canMove(from: Square, to: Square, moves: Moves): Boolean =
        getPossibleMoves(from, moves).contains(to)

    override fun getPossibleMoves(from: Square, moves: Moves): List<Square> {

        moves[from] ?: return emptyList()

        val possibleMoves = mutableListOf<Square>()

        moveDirections.forEach { direction ->
            val targetSquare = from.move(direction)
            if (targetSquare != null && moves[targetSquare] == null) possibleMoves.add(targetSquare)
        }
        return possibleMoves
    }



    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {

        // Valid Moves to Capture if there is a piece different then ours in the middle of our targetSquare
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

