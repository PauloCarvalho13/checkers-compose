package isel.leic.tds.checkers.model


abstract class Piece(val player: Player){
    open fun canMove(position: Square, board: Board): Board = board
    open fun canCapture(position: Square, board: Board): Board = board
}