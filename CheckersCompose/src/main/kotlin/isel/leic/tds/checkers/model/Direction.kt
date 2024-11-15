package isel.leic.tds.checkers.model

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1), //whiteMove
    UP_RIGHT(-1, 1), //whiteMove
    DOWN_LEFT(1, -1), //blackMove
    DOWN_RIGHT(1, 1), //blackMove
    UP_LEFT_CAP(2, -2),
    UP_RIGHT_CAP(2, 2),
    DOWN_LEFT_CAP(-2, -2),
    DOWN_RIGHT_CAP(-2, 2),
    UNKNOWN(0,0);

}

fun directionOfMove(from: Square, to: Square, moves: Moves): Direction{
    val piece = moves[from]?.player
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index
    // Determine the direction of the move based on piece type
    //check if its black or white
    //if its white only can move to up_right or up_left
    //if its black only can move to down_right or down_left
    return when {
        piece == Player.WHITE && rowDiff == -1 && colDiff == 1 -> Direction.UP_RIGHT
        piece == Player.WHITE && rowDiff == -1 && colDiff == -1 -> Direction.UP_LEFT
        piece == Player.BLACK && rowDiff == 1 && colDiff == 1 -> Direction.DOWN_RIGHT
        piece == Player.BLACK && rowDiff == 1 && colDiff == -1 -> Direction.DOWN_LEFT
        else -> Direction.UNKNOWN // Invalid direction
    }
}
