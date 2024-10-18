package isel.leic.tds.checkers.model

class Pawn(player: Player) : Piece(player) {
    override fun canMove(position: Square, board: Map<Square,Piece>): Map<Square,Piece> {
        return super.canMove(position, board)
    }

    override fun canCapture(position: Square, board: Map<Square,Piece>): Map<Square,Piece> {
        return super.canCapture(position, board)
    }
}