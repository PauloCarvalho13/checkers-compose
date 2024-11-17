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

fun directionOfMove(from: Square, to: Square, moves: Moves): Direction {
    val piece = moves[from] ?: return Direction.UNKNOWN
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index

    return when {
        // If the piece is a Queen and the move is diagonal
        piece is Queen && abs(rowDiff) == abs(colDiff) -> {
            when {
                rowDiff > 0 && colDiff > 0 -> Direction.DOWN_RIGHT
                rowDiff > 0 && colDiff < 0 -> Direction.DOWN_LEFT
                rowDiff < 0 && colDiff > 0 -> Direction.UP_RIGHT
                rowDiff < 0 && colDiff < 0 -> Direction.UP_LEFT
                else -> Direction.UNKNOWN // Invalid direction
            }
        }
        // If the piece is a white piece, only allows movement up-right or up-left
        piece.player == Player.WHITE && rowDiff == -1 && colDiff == 1 -> Direction.UP_RIGHT
        piece.player == Player.WHITE && rowDiff == -1 && colDiff == -1 -> Direction.UP_LEFT
        // If the piece is a black piece, only allows movement down-right or down-left
        piece.player == Player.BLACK && rowDiff == 1 && colDiff == 1 -> Direction.DOWN_RIGHT
        piece.player == Player.BLACK && rowDiff == 1 && colDiff == -1 -> Direction.DOWN_LEFT
        else -> Direction.UNKNOWN // Invalid direction
    }
}
