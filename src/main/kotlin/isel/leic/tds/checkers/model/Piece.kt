package isel.leic.tds.checkers.model


abstract class Piece(val player: Player){
    open fun canMove(from: Square, to: Square, moves: Moves): Boolean = false
    open fun canCapture(from: Square, to: Square, moves: Moves): Boolean = false
    open fun getPossibleCaptures(from: Square, moves: Moves): Moves = moves
}