package isel.leic.tds.checkers.model

import BOARD_DIM
import kotlin.math.abs

abstract class Piece(val player: Player){
    abstract val type: String
    abstract val captureDirections: List<Direction>
    abstract val moveDirections:List<Direction>

    override fun equals(other: Any?): Boolean = other is Piece && player == other.player
    override fun hashCode() = player.hashCode()

    abstract fun canMove(from: Square, to: Square, moves: Moves): Boolean
    abstract fun getPossibleMoves(from:Square, moves:Moves): List<Square>
    abstract fun canCapture(from: Square, to: Square, moves: Moves): Boolean
    abstract fun getPossibleCaptures(from: Square, moves: Moves): Moves


    fun checkPiece(to: Square): Piece{
        return when(this){
            is Pawn -> {
                if(player == Player.WHITE && to.row.digit == BOARD_DIM.digitToChar()){
                    Queen(Player.WHITE)
                }
                else if (player == Player.BLACK && to.row.digit == '1'){
                    Queen(Player.BLACK)
                }
                else{
                    this // There's no condition to change pawn to queen so it remains the same piece
                }
            }
            else -> this // if it's already a Queen stays the same
        }
    }
}

fun walkPath(from: Square, to: Square, step: Int = 1): List<Square> {
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index

    // Ensure the path is diagonal
    if (rowDiff == 0 || colDiff == 0 || abs(rowDiff) != abs(colDiff)) return emptyList()

    val rowStep = rowDiff / abs(rowDiff) * step
    val colStep = colDiff / abs(colDiff) * step

    val path = mutableListOf<Square>()
    var current = from

    while (true) {
        val nextRow = current.row.index + rowStep
        val nextCol = current.column.index + colStep

        if (nextRow == to.row.index && nextCol == to.column.index) break

        current = Square(nextRow.toRow(), nextCol.toColumn())
        path.add(current)
    }

    return path
}
