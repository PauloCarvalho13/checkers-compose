package isel.leic.tds.checkers.model

typealias Score = Map<Player?, Int>

data class Game (
    val gameId: String = "",
    val board: Board? = null,
    val score : Score = (Player.entries + null).associateWith { 0 },
    val firstPlayer: Player = Player.entries.first()
)

private fun Game.advanceScore(player: Player?): Score =
    score - player + (player to checkNotNull(score[player]) + 1)

fun Game.new() = Game(
    board = BoardRun(turn = firstPlayer),
    score = if (board is BoardRun) advanceScore(board.turn.other) else score,
    firstPlayer = firstPlayer.other
)

fun Game.play(initPos: Square, finalPos: Square ): Game {
    checkNotNull(board){"No board"}
    if(board is BoardWin){
        return this
    }
    board as BoardRun
    val board = board.play(initPos, finalPos)
    return copy(
        board = board,
        score = when(board){
            is BoardWin  -> advanceScore(board.winner)
            is BoardRun -> score
        }

    )
}






