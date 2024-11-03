package isel.leic.tds.checkers.model


class Queen(player: Player) : Piece(player) {
    private val directions = listOf(
        Direction.UP_LEFT,
        Direction.UP_RIGHT,
        Direction.DOWN_LEFT,
        Direction.DOWN_RIGHT
    )

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

        val fromPiece = moves[from] ?: return emptyMap<Square, Piece>()

        return directions.flatMap { direction ->
            generateSequence(from.move(direction)) { currentSquare ->
                // Move to the next square in the given direction
                currentSquare.move(direction)
            }
                .takeWhile { currentSquare ->
                    // Stop the sequence if we encounter a square that cannot be captured
                    //moves[currentSquare] == null || canCapture(from, currentSquare.move(direction) ?: return@takeWhile false, moves)
                    if (currentSquare == null) {
                        false // Stop if we reach the board boundary
                    } else {
                        val nextSquare = currentSquare.move(direction)
                        // Continue if the square is empty, or if the next square allows capture
                        moves[currentSquare] == null || (nextSquare != null && canCapture(from, nextSquare, moves))
                    }
                }
                .mapNotNull { currentSquare ->
                    // Only include squares that can actually be captured
                    val nextSquare = currentSquare.move(direction)
                    if (nextSquare != null && canCapture(from, nextSquare, moves)) nextSquare else null
                }
        }
            .distinct()
            .sortedBy { it.index }
            .associateWith { fromPiece }

       /* val possibleCaptures = mutableListOf<Square>()

        for (direction in directions) {
            var currentSquare = from.move(direction)

            while (currentSquare != null) {
                val nextSquare = currentSquare.move(direction)
                if (moves[currentSquare] == null) {
                    // If the current square is empty, continue moving in this direction
                    currentSquare = currentSquare.move(direction)
                } else if (nextSquare != null && canCapture(from, nextSquare, moves)) {
                    // If canCapture is true, add to possible captures and continue moving
                    possibleCaptures.add(nextSquare)
                    currentSquare = nextSquare
                } else {
                    // If the square is occupied and cannot be captured, stop in this direction
                    break
                }
            }
        }

        return possibleCaptures.toList().sortedBy { square -> square.index }.associateWith { fromPiece }*/
    }

    override fun canCapture(from: Square, to: Square, moves: Moves): Boolean {
        // get the piece that is moving, if its empty return false
        val fromPiece = moves[from] ?: return false

        // see if the target square is empty, if its not null return false
        moves[to]?.let { return false }

        // get the reverse direction to move to the previous square
        val reverseDirection = reverseDirectionOfMove(from, to)

        // see if the square before targetSquare has an opposing piece, if its empty return false
        val capturedSquare = to.move(reverseDirection) ?: return false

        val capturedPiece = moves[capturedSquare] ?: return false

        // check if its a piece from the same player, if it is, return false
        if(capturedPiece.player == fromPiece.player) return false

        val squareBeforeCaptured = capturedSquare.move(reverseDirection) ?: return false

        // see if the piece can move until the capturedPiece square, if it can, the capture is successful
        return (squareBeforeCaptured == from || canMove(from, squareBeforeCaptured, moves))
    }
}