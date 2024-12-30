package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    private val directions = listOf(
        Direction.UP_LEFT,
        Direction.UP_RIGHT,
        Direction.DOWN_LEFT,
        Direction.DOWN_RIGHT
    )

    override val type: String get() = "Q"

    override fun canMove(from: Square, to: Square, moves: Moves): Boolean {
        val direction = directionOfMove(from, to, moves)
        val path = walkPath(from, to)

        // Ensure the path is along the calculated direction and all squares except `to` are empty
        return path.all { square -> moves[square] == null } && direction != Direction.UNKNOWN
    }

    // gets all the possible captures of a piece
    override fun getPossibleCaptures(from: Square, moves: Moves): Moves {
        val fromPiece = moves[from] ?: return emptyMap()

        // Fun to verify captures in a specific direction
        tailrec fun findCaptures(
            startSquare: Square,
            direction: Direction,
            encounteredPieces: Int = 0,
            validMoves: MutableMap<Square, Piece> = mutableMapOf()
        ): Map<Square, Piece> {
            val nextSquare = startSquare.move(direction) ?: return validMoves

            return when (val pieceAtNextSquare = moves[nextSquare]) {
                null -> { // If the next square is empty
                    if (encounteredPieces == 1) {
                        // If an enemy piece was found before, this square is valid
                        validMoves[nextSquare] = fromPiece
                        // Continue searching beyond the captured piece
                        findCaptures(nextSquare, direction, encounteredPieces, validMoves)
                    } else {
                        // No piece encountered yet, keep searching
                        findCaptures(nextSquare, direction, encounteredPieces, validMoves)
                    }
                }
                else -> {
                    if (pieceAtNextSquare.player != fromPiece.player && encounteredPieces == 0) {
                        // First enemy piece found, continue searching
                        findCaptures(nextSquare, direction, 1, validMoves)
                    } else {
                        // More than one enemy piece found or a piece that's mine, stop
                        validMoves
                    }
                }
            }
        }

        // Search for every direction to get the possible captures
        return directions.fold(emptyMap()) { acc, direction ->
            acc + findCaptures(from, direction)
        }
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean
    = moves[from]?.let { to in it.getPossibleCaptures(from, moves) } ?: false
}