package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    private val directions = listOf(
        Direction.UP_LEFT,
        Direction.UP_RIGHT,
        Direction.DOWN_LEFT,
        Direction.DOWN_RIGHT
    )

    override fun canMove(from: Square, to:  Square, moves: Moves): Boolean {
        val validMoves = directions.flatMap { direction ->
            generateSequence(from) { it.move(direction) }
                .takeWhile {
                    moves[it] == null ||
                            moves[it]!!.player != this.player
                }
                .drop(1)
        }

        return validMoves.isNotEmpty()
    }

    override fun getPossibleCaptures(from: Square, moves: Moves): Moves{
        val captureMoves = directions.flatMap { direction ->
            val middleSquare = from.move(direction)
            val targetSquare = middleSquare?.move(direction)

            if (middleSquare != null && targetSquare != null &&
                moves[middleSquare]?.player != this.player &&
                moves[targetSquare] == null) {
                listOf(targetSquare to this)
            } else {
                emptyList()
            }
        }.toMap()

        return captureMoves.toList().sortedBy { (square, _) -> square.index }.toMap()
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean =
        getPossibleCaptures(from, moves).containsKey(to)
}