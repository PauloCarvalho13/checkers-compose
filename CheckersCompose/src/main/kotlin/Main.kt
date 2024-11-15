import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.ui.GameView
import isel.leic.tds.checkers.ui.StatusBarView

const val BOARD_DIM = 8

@Composable
@Preview
private fun BoardApp() {
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
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(size = DpSize.Unspecified)
    ) {
        BoardApp()
    }
}
