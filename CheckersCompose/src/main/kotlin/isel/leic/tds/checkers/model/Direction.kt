package isel.leic.tds.checkers.model

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1),
    UP_LEFT_CAP(-2, -2),
    UP_RIGHT_CAP(-2, 2),
    DOWN_LEFT_CAP(2, -2),
    DOWN_RIGHT_CAP(2, 2)
}


fun Piece.possibleDirections(capturing: Boolean): List<Direction> =
    if (this is Queen) listOf(Direction.UP_RIGHT, Direction.UP_LEFT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT)
    else when (this.player) {
        Player.WHITE ->
            if (capturing) listOf(Direction.UP_LEFT_CAP, Direction.UP_RIGHT_CAP)
            else listOf(Direction.UP_LEFT, Direction.UP_RIGHT)
        Player.BLACK ->
            if (capturing) listOf(Direction.DOWN_LEFT_CAP, Direction.DOWN_RIGHT_CAP)
            else listOf(Direction.DOWN_LEFT, Direction.DOWN_RIGHT)
    }