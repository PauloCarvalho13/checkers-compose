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




    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean =
        moves[from]?.let { to in it.getPossibleCaptures(from, moves) } ?: false
}