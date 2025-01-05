package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    override val captureDirections get() = possibleDirections(true)
    override val moveDirections get() = possibleDirections(false)

    override val type: String get() = "Q"

    override fun canMove(from: Square, to: Square, moves: Moves): Boolean =
        getPossibleMoves(from, moves).contains(to)

    override fun getPossibleMoves(from: Square, moves: Moves): List<Square> {
        val possibleMoves = mutableListOf<Square>()

        moveDirections.forEach { direction ->
            var currentSquare = from

            while (true) {
                val nextSquare = currentSquare.move(direction) ?: break

                if (moves[nextSquare] == null) {
                    possibleMoves.add(nextSquare)
                    currentSquare = nextSquare
                } else {
                    // Stop if the square is occupied by any piece (either ally or opponent)
                    break
                }
            }
        }
        return possibleMoves
    }


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
        return captureDirections.fold(emptyMap()) { acc, direction ->
            acc + findCaptures(from, direction)
        }
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean
    = moves[from]?.let { to in it.getPossibleCaptures(from, moves) } ?: false
}