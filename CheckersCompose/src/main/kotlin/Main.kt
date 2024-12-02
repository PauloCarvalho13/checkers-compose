import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.ui.*

const val BOARD_DIM = 8

@Composable
@Preview
private fun FrameWindowScope.BoardApp(vm: AppViewModel) {
    var game: Game by remember { mutableStateOf(Game().new()) }
    var selectedMove: Pair<Square, Piece>? by remember { mutableStateOf(null) }
    MaterialTheme {
        game.board?.let { board ->
            Column {
                GameView(board, selectedMove, onClickSquare = { square: Square ->
                    if(board is BoardRun) {
                        try {
                            val move = board.moves[square]
                            // see if the square clicked has a piece
                            if (move != null && move.player == board.turn) { // if not change selectedMove
                                selectedMove = Pair(square, move)
                            } else { // square selected doesn't have a piece
                                if (selectedMove != null) { // if not play, else ignore
                                    game = game.play(selectedMove!!.first, square)
                                    selectedMove = null // reset selectedMove
                                }
                            }
                        } catch (ex: Exception) {
                            println(ex.message)
                        }
                    }
                })
                StatusBarView(game)
            }
            if(vm.scoreView) ScoreDialog(vm.score, onClose = vm::hideScore)
            vm.action?.let { NameEdit(it, vm::cancelAction, vm::doAction) }
            vm.message?.let { Message(it, vm::cancelMessage) }
        }
    }
}

@Composable
fun FrameWindowScope.BoardMenu(vm: AppViewModel, onExit: ()->Unit){
    MenuBar {
        Menu("Game"){
            Item("New board", enabled = vm.hasClash, onClick = vm::newBoard)
            Item("Score",  enabled = vm.hasClash, onClick = vm::showScore)
            Item("Exit", onClick = onExit )
            Item("Refresh",  enabled = vm.hasClash, onClick = vm::refresh)
        }
        Menu("Clash") {
            Item("Start", onClick = vm::start)
            Item("Join", onClick = vm::join)
        }
    }
}

fun main() = application {
    val vm = remember { AppViewModel() } //ViewModel
    val onExit = { vm.exit(); exitApplication() }
    Window(
        onCloseRequest = onExit,
        state = WindowState(size = DpSize.Unspecified),
        title = "Checkers"
    ) {
        BoardMenu(vm, onExit)
        BoardApp(vm)
    }
}
