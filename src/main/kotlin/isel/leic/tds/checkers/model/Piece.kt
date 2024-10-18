package isel.leic.tds.checkers.model


abstract class Piece(val player: Player){
    open fun canMove(position: Square, board: Map<Square,Piece>): Map<Square,Piece> = board
    open fun canCapture(position: Square, board: Map<Square,Piece>): Map<Square,Piece> = board
}