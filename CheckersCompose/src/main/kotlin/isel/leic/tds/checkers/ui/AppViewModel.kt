package isel.leic.tds.checkers.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.TextFileStorage

// TODO: Must be moved to ViewModel
enum class Action(val text: String) {
    START("Start"),
    JOIN("Join")
}

class AppViewModel {
    val storage = TextFileStorage<Name, Game>("games", GameSerializer)

    private var clash: Clash by mutableStateOf(Clash(storage))
    val hasClash:Boolean get() = clash is ClashRun
    val game: Game? get() = (clash as? ClashRun)?.game
    val board: Board? get() = game?.board

    var selectedMove: Pair<Square, Piece>? by mutableStateOf(null)

    val score get() = game?.score
    fun play(square: Square) {
        if (board is BoardRun) {
            try {
                board as BoardRun
                val move = (board as BoardRun).moves[square]
                // see if the square clicked has a piece
                if (move != null && move.player == (board as BoardRun).turn) { // if not change selectedMove
                    selectedMove = Pair(square, move)
                } else { // square selected doesn't have a piece
                    if (selectedMove != null) { // if not play, else ignore
                        exec { play(selectedMove!!.first, square) }
                        selectedMove = null // reset selectedMove
                    }
                }

            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

    val sidePlayer get() = (clash as? ClashRun)?.sidePlayer
    fun exit() { clash.exit() }
    fun newBoard() = exec(Clash::newBoard)

    // UI State
    var scoreView by mutableStateOf(false)
        private set

    fun showScore() { scoreView = true }
    fun hideScore() { scoreView = false }

    var action: Action? by mutableStateOf(null)
        private set

    fun refresh() = exec( Clash::refresh )
    fun start() { action = Action.START }
    fun join() { action = Action.JOIN }
    fun cancelAction() { action = null }
    fun doAction(name: Name) {
        exec{ when(action as Action) {
            Action.START -> start(name)
            Action.JOIN -> join(name)
        } }
        action = null
    }

    var message: String? by mutableStateOf(null)
        private set
    fun cancelMessage() { message = null }

    private fun exec( fx: Clash.()->Clash ) {
        try { clash = clash.fx() }
        catch(ex: Exception) {
            if (ex is IllegalArgumentException ||
                ex is IllegalStateException
            ) message = ex.message
            else throw ex
        }
    }

}