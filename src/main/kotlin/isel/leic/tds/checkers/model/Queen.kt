package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    private val directions = listOf(
        Direction.UP_LEFT,
        Direction.UP_RIGHT,
        Direction.DOWN_LEFT,
        Direction.DOWN_RIGHT
    )
    override fun canCapture(position: Square, board: Board): Board{
        /*val posCaptures = super.canCapture(position, board)
        val captureMoves = directions.flatMap { direction ->
            val middleSquare = position.move(direction)
            val targetSquare = middleSquare?.move(direction)

            if (middleSquare != null && targetSquare != null &&
                board.playingPlaces[middleSquare]?.player != this.player &&
                board.playingPlaces[targetSquare] == null) {
                listOf(targetSquare to this)
            } else {
                emptyList()
            }
        }.toMap()

        return Board(posCaptures.playingPlaces + captureMoves)

         */
        return board
    }

    override fun canMove(position: Square, board: Board): Board {

        /*val validMoves = directions.flatMap { direction ->
            generateSequence(position) { it.move(direction) }
                .takeWhile {
                    it != null &&
                    (board.playingPlaces[it] == null ||
                     board.playingPlaces[it]!!.player != this.player)
                }
                .drop(1)
                .toList()
        }
        return Board(validMoves.associateWith { this })

         */
        return board
    }
}