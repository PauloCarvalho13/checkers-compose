package isel.leic.tds.checkers.model

class Queen: Piece() {
    override fun canCapture(position: Square, board: Map<Square,Piece>): Map<Square,Piece> {
        return super.canCapture(position, board)
    }

    override fun canMove(position: Square, board: Map<Square,Piece>): Map<Square,Piece> {
        return super.canMove(position, board)
    }
}