package isel.leic.tds.checkers.model

import kotlin.math.abs

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1),
    UP_LEFT_CAP(2, -2),
    UP_RIGHT_CAP(2, 2),
    DOWN_LEFT_CAP(-2, -2),
    DOWN_RIGHT_CAP(-2, 2),
    UNKNOWN(0,0);

}

fun Player.possibleDirections(): List<Direction> =
    when {
        this == Player.WHITE -> listOf(Direction.DOWN_LEFT_CAP, Direction.DOWN_RIGHT_CAP)
        else -> listOf(Direction.UP_LEFT_CAP,Direction.UP_RIGHT_CAP)
    }

fun directionOfMove(from: Square, to: Square, moves: Moves): Direction {
    val piece = moves[from] ?: return Direction.UNKNOWN
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index

    return when {
        // Queen's diagonal movement
        piece is Queen && abs(rowDiff) == abs(colDiff) -> diagonalDirection(rowDiff, colDiff)
        // White player's movement
        piece.player == Player.WHITE && rowDiff == -1 -> diagonalDirection(rowDiff, colDiff)
        // Black player's movement
        piece.player == Player.BLACK && rowDiff == 1 -> diagonalDirection(rowDiff, colDiff)
        else -> Direction.UNKNOWN
    }
}

private fun diagonalDirection(rowDiff: Int, colDiff: Int): Direction
    = when {
        rowDiff > 0 && colDiff > 0 -> Direction.DOWN_RIGHT
        rowDiff > 0 && colDiff < 0 -> Direction.DOWN_LEFT
        rowDiff < 0 && colDiff > 0 -> Direction.UP_RIGHT
        rowDiff < 0 && colDiff < 0 -> Direction.UP_LEFT
        else -> Direction.UNKNOWN
    }

