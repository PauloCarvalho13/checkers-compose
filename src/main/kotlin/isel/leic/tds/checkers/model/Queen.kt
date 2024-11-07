package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    private val directions = listOf(
        Direction.UP_LEFT,
        Direction.UP_RIGHT,
        Direction.DOWN_LEFT,
        Direction.DOWN_RIGHT
    )

    override val type: String
        get() = "Q"

    override fun canMove(from: Square, to:  Square, moves: Moves): Boolean {
        val direction = directionOfMove(from, to)
        val validSquares = mutableListOf<Square>()
        var currentSquare: Square? = from

        while (true) {
            currentSquare = currentSquare?.move(direction) ?: return false
            if (moves[currentSquare] != null || currentSquare == to) {
                break
            }
            validSquares.add(currentSquare)
        }

        // get the last square and move it, so getting the to Square or an occupied square
        val toSquareOnBoard = if(validSquares.isEmpty()) from.move(direction)
                              else validSquares.last().move(direction)

        // check if that square is empty and that it is the same as to
        return moves[toSquareOnBoard] == null && toSquareOnBoard == to
    }

    // gets all the possible captures of a piece
    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {
        // canCapture for every direction
        // but does it check for every square
        // from my position, do canCapture to from.move(direction) until it reaches the end of the board for every direction
        val fromPiece = moves[from] ?: return emptyMap()

        // Helper function for recursive traversal in a direction
        tailrec fun findCaptures(
            currentSquare: Square?,
            direction: Direction,
            captures: MutableList<Square> = mutableListOf()
        ): List<Square> {
            // Base case: stop if the current square is null (board boundary) or if conditions aren't met
            if (currentSquare == null) return captures

            val nextSquare = currentSquare.move(direction)
            return if (moves[currentSquare] == null || (nextSquare != null && canCapture(from, nextSquare, moves))) {
                // Accumulate valid captures and continue
                if (nextSquare != null && canCapture(from, nextSquare, moves)) {
                    captures.add(nextSquare)
                }
                findCaptures(nextSquare, direction, captures)
            } else {
                captures
            }
        }

        // Accumulate captures for all directions
        return directions.flatMap { direction ->
            findCaptures(from.move(direction), direction)
        }
            .sortedBy { it.index }
            .associateWith { fromPiece }
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean {
        // get the piece that is moving, if its empty return false
        val fromPiece = moves[from] ?: return false

        // see if the target square is empty, if it's not null return false
        moves[to]?.let { return false }

        // get the reverse direction to move to the previous square
        val reverseDirection = directionOfMove(to, from)

        // see if the square before targetSquare has an opposing piece, if its empty return false
        val capturedSquare = to.move(reverseDirection) ?: return false

        val capturedPiece = moves[capturedSquare] ?: return false

        // check if it's a piece from the same player, if it is, return false
        if(capturedPiece.player == fromPiece.player) return false

        val squareBeforeCaptured = capturedSquare.move(reverseDirection) ?: return false

        // see if the piece can move until the capturedPiece square, if it can, the capture is successful
        return (squareBeforeCaptured == from || canMove (from, squareBeforeCaptured, moves))
    }
}