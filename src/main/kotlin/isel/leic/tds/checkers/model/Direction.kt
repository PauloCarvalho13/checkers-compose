package isel.leic.tds.checkers.model

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1), //whiteMove
    UP_RIGHT(-1, 1), //whiteMove
    DOWN_LEFT(1, -1), //blackMove
    DOWN_RIGHT(1, 1), //blackMove
    UP_LEFT_CAP(2, -2),
    UP_RIGHT_CAP(2, 2),
    DOWN_LEFT_CAP(-2, -2),
    DOWN_RIGHT_CAP(-2, 2);

}

fun directionOfMove(from: Square, to: Square): Direction{
    val rowDiff = from.row.index - to.row.index
    val colDiff = from.column.index - to.column.index
    return when{
        rowDiff > 0 && colDiff < 0 -> Direction.UP_RIGHT
        rowDiff > 0 && colDiff > 0 -> Direction.UP_LEFT
        rowDiff < 0 && colDiff < 0 -> Direction.DOWN_RIGHT
        else -> Direction.DOWN_LEFT
    }
}

fun reverseDirectionOfMove(from: Square, to: Square): Direction{
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index
    return when {
        rowDiff > 0 && colDiff < 0 -> Direction.UP_RIGHT
        rowDiff > 0 && colDiff > 0 -> Direction.UP_LEFT
        rowDiff < 0 && colDiff < 0 -> Direction.DOWN_RIGHT
        else -> Direction.DOWN_LEFT
    }
}
