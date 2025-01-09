package isel.leic.tds.checkers.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.MongoDriver
import isel.leic.tds.checkers.storage.MongoStorage
import isel.leic.tds.checkers.storage.TextFileStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val REFRESH_DELAY_TIME = 1000L

enum class Action(val text: String) {
    START("Start"),
    JOIN("Join")
}

enum class Theme {
    DEFAULT, LIGHT, COLORFUL
}

data class SelectedMove(val square: Square, val piece: Piece)

class AppViewModel(private val scope: CoroutineScope, driver: MongoDriver) {
    val storage = TextFileStorage<Name,Game>("games",GameSerializer)
    //val storage = MongoStorage<Name, Game>("games",driver, GameSerializer)

    var clash: Clash by mutableStateOf(Clash(storage))
    val hasClash:Boolean get() = clash is ClashRun

    val game: Game? get() = (clash as? ClashRun)?.game
    val board: Board? get() = game?.board

    var selectedMove: SelectedMove? by mutableStateOf(null)

    val sidePlayer get() = (clash as? ClashRun)?.sidePlayer
    val isSideTurn get() = clash.isSideTurn


    private val moves get() = board?.moves ?: emptyMap()

    val playerCaptures get() =
        board?.let { b ->
            b.moves.entries
                .filter { it.value.player == sidePlayer }
                .flatMap { (square, piece) ->
                    piece.getPossibleCaptures(square, moves).keys // Get possible captures for each player piece
                }
        } ?: emptyList()

    val possibleMoves get() = selectedMove?.let {
            it.piece.getPossibleMoves(it.square, moves)
    }?: emptyList()


    val score get() = game?.score

    fun selectSquare(square: Square) {
        if (isWaiting || !hasClash || board !is BoardRun || !clash.isSideTurn) return

        if (!square.black) return

        val boardRun = board as BoardRun
        val pieceOnSquare: Piece? = boardRun.moves[square]
        val previousMove = selectedMove

        when {

            previousMove != null && pieceOnSquare == null -> {
                exec { play(previousMove.square, square) }

                if (autoRefresh) waitingForOther()
            }

            pieceOnSquare != null && pieceOnSquare.player == sidePlayer -> {
                selectedMove = SelectedMove(square, pieceOnSquare)
            }

        }
    }


    fun exit() {
        if (autoRefresh) cancelWaiting()
        clash.exit()
    }

    fun newBoard() = exec(Clash::newBoard)

    // Ui Theme
    var selectedTheme by mutableStateOf(Theme.DEFAULT)
        private set

    fun changeTheme(theme: Theme) {
        selectedTheme = if (selectedTheme == theme) Theme.DEFAULT else theme
    }

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
        try {
            clash = clash.fx()
            selectedMove = null
        }
        catch(ex: Exception) {
            manageException(ex)
        }
    }

    private fun manageException(ex: Exception) {
        if (ex is InvalidMoveException) return
        if (ex is IllegalArgumentException || ex is IllegalStateException){
            message = ex.message
            if (ex is GameDeletedException)
                clash = Clash(storage)
        }
        else throw ex
    }

    private var waitingJob by mutableStateOf<Job?>(null)

    private val isWaiting get() = waitingJob != null

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