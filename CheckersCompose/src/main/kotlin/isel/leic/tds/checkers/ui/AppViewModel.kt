package isel.leic.tds.checkers.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.MongoDriver
import isel.leic.tds.checkers.storage.TextFileStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val REFRESH_DELAY_TIME = 5000L

enum class Action(val text: String) {
    START("Start"),
    JOIN("Join")
}

class AppViewModel(private val scope: CoroutineScope, driver: MongoDriver) {
    val storage = TextFileStorage<Name,Game>("games",GameSerializer)
    //val storage = MongoStorage<Name, Game>("games",driver, GameSerializer)

    var clash: Clash by mutableStateOf(Clash(storage))

    val hasClash:Boolean get() = clash is ClashRun
    val game: Game? get() = (clash as? ClashRun)?.game
    val board: Board? get() = game?.board
    var selectedMove: Pair<Square, Piece>? by mutableStateOf(null)
    val sidePlayer get() = (clash as? ClashRun)?.sidePlayer
    val isSideTurn get() = clash.isSideTurn

    val score get() = game?.score

    fun selectSquare(square: Square) {
        if (!hasClash || board !is BoardRun || !clash.isSideTurn) return

        if (square.black) {
            val previousMove = selectedMove
            val pieceOnSquare = (board as BoardRun).moves[square]

            when {
                previousMove != null && pieceOnSquare == null -> {
                    exec { play(previousMove.first, square) }
                    selectedMove = null
                    if(autoRefresh) waitingForOther()
                }
                previousMove != null && pieceOnSquare != null ->{
                    if(pieceOnSquare.player == sidePlayer) selectedMove = Pair(square, pieceOnSquare)
                }
                previousMove == null && pieceOnSquare != null -> {
                    if (pieceOnSquare.player == sidePlayer) selectedMove = Pair(square, pieceOnSquare)
                }
                else -> { /* Keep current state */ }
            }
        }
    }

    fun exit() {
        if (autoRefresh) cancelWaiting()
        clash.exit()
    }

    fun newBoard() = exec(Clash::newBoard)

    // UI State
    var scoreView by mutableStateOf(false)
        private set

    fun hideScore() { scoreView = false }

    fun showScore() { scoreView = true }

    var showTargets by mutableStateOf(true)
        private set

    fun changeShowTargets(value: Boolean){ showTargets = value }

    var autoRefresh by mutableStateOf(true)
        private set

    fun changeAutoRefresh(value: Boolean) { autoRefresh = value }

    var action: Action? by mutableStateOf(null)
        private set

    fun refresh() = exec( Clash::refresh )
    fun start() { action = Action.START }
    fun join() { action = Action.JOIN }
    fun cancelAction() { action = null }

    fun doAction(name: Name) {
        cancelWaiting()
        exec{
            when(action as Action) {
                Action.START -> start(name)
                Action.JOIN -> join(name)
            }
        }
        if (autoRefresh) waitingForOther()
        action = null
    }
    var message: String? by mutableStateOf(null)
        private set

    fun cancelMessage() { message = null }

    private fun exec( fx: Clash.()->Clash ) {
        try { clash = clash.fx() }
        catch(ex: Exception) {
            manageException(ex)
        }
    }

    private fun manageException(ex: Exception) {
        if (ex is IllegalArgumentException || ex is IllegalStateException){
            message = ex.message
            if (ex is GameDeletedException)
                clash = Clash(storage)
        }
        else throw ex
    }

    private var waitingJob by mutableStateOf<Job?>(null)

    private fun cancelWaiting(){
        waitingJob?.cancel()
        waitingJob = null
    }

    private fun waitingForOther(){
        if (isSideTurn) return
        waitingJob = scope.launch {
            while (true){
                delay(REFRESH_DELAY_TIME)
                try {
                    clash = clash.refresh()
                    if (isSideTurn) break
                }
                catch (ex: NoModificationException) { /* Do Nothing */ }
                catch (ex: Exception){
                    manageException(ex)
                    break
                }
            }
            waitingJob = null
        }
    }
}