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
