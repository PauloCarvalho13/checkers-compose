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

    override fun canMove(from: Square, to: Square, moves: Moves): Boolean {
        val direction = directionOfMove(from, to, moves)

        tailrec fun findPathToSquare(currentSquare: Square?): Boolean =
            when {
                currentSquare == null -> false
                currentSquare == to -> true
                moves[currentSquare] != null -> false
                else -> findPathToSquare(currentSquare.move(direction))
            }
        // Check if the to Square has a piece
        if(moves[to] != null) return false

        return findPathToSquare(from.move(direction))
    }

    // gets all the possible captures of a piece
    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {
        val fromPiece = moves[from] ?: return emptyMap()

        // Fun to verify captures in a specific direction
        tailrec fun findCaptures(
            startSquare: Square,
            direction: Direction,
            encounteredPieces: Int = 0
        ): Map<Square, Piece> {
            val nextSquare = startSquare.move(direction) ?: return emptyMap()

            return when (val pieceAtNextSquare = moves[nextSquare]) {
                null -> { //if nextSquare its empty
                    if (encounteredPieces == 1) {
                        // if an enemy piece was found before
                        mapOf(nextSquare to fromPiece)
                    } else {
                        //No piece was found so keep searching
                        findCaptures(nextSquare, direction, encounteredPieces)
                    }
                }
                else -> {
                    //if there's a piece nextSquare
                    if (pieceAtNextSquare.player != fromPiece.player && encounteredPieces == 0) {
                        // first enemy piece found
                        findCaptures(nextSquare, direction, encounteredPieces + 1)
                    } else {
                        // More than one enemy piece found or a piece that's mine
                        emptyMap()
                    }
                }
            }
        }
        // Search for every direction to get the possible captures
        return directions.fold(emptyMap()) { acc, direction ->
            acc + findCaptures(from, direction)
        }
    }




    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean {
        val fromPiece = moves[from] ?: return false
        val direction = directionOfMove(from, to, moves)

        if (direction == Direction.UNKNOWN) return false

        // Fun to validate each capture in a direction
        tailrec fun validateCapture(
            currentSquare: Square?,
            encounteredPieces: Int = 0
        ): Boolean {
            if (currentSquare == null) return false
            if (currentSquare == to) {
                // To needs to be empty and can only found one enemy piece
                return encounteredPieces == 1 && moves[currentSquare] == null
            }

            return when (val pieceAtCurrentSquare = moves[currentSquare]) {
                // if the square its empty, keep searching in the same direction and verify each square
                null -> validateCapture(currentSquare.move(direction), encounteredPieces)
                else -> {
                    if (pieceAtCurrentSquare.player != fromPiece.player && encounteredPieces == 0) {
                        // First enemy piece found
                        validateCapture(currentSquare.move(direction), encounteredPieces + 1)
                    } else {
                        false // More than one enemy piece found or a piece that's mine
                    }
                }
            }
        }
        // Start validating each square after from and in the movement direction
        return validateCapture(from.move(direction))
    }
}