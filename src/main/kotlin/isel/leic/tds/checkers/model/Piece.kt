package isel.leic.tds.checkers.model


abstract class Piece(val player: Player){
    open fun canMove(from: Square, to: Square, moves: Moves): Boolean = false
    open fun canCapture(from: Square, to: Square, moves: Moves): Boolean = false
    open fun getPossibleCaptures(from: Square, moves: Moves): Moves = moves

    fun checkPiece(to: Square): Piece{
        return when(this){
            is Pawn -> {
                if(player == Player.WHITE && to.row.digit == '8'){
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