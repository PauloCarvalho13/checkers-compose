package isel.leic.tds.checkers.ui

import BOARD_DIM
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.*

private val CELL_SIZE = 50.dp
private val LINE_WIDTH = 2.dp
val GRID_WIDTH = CELL_SIZE * BOARD_DIM + LINE_WIDTH * (BOARD_DIM-1)

private val lightBrown = Color(196, 164, 132)
private val darkBrown = Color(139, 69, 19)
private val lightRed = Color(255, 100, 100)
private val lightYellow = Color(255, 222, 33)
private val lightBlue = Color(173, 216, 255)

@Composable
fun GameView(board: Board, selectedMove: Pair<Square, Piece>?, onClickSquare: (Square) -> Unit) {
    Column(
        modifier = Modifier.size(GRID_WIDTH).background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(BOARD_DIM) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(BOARD_DIM) { col ->
                    val square = Square(Row(row), Column(col))
                    SquareView(
                        // não está a fazer a jogada apropriadamente
                        piece = board.moves[square],
                        onClick= { onClickSquare(square) },
                        modifier = Modifier.size(CELL_SIZE).background(
                            when {
                                !square.black -> lightBrown
                                selectedMove == null -> darkBrown
                                selectedMove.second.canCapture(selectedMove.first, square, board.moves) -> lightYellow
                                selectedMove.second.canMove(selectedMove.first, square, board.moves) -> lightBlue
                                selectedMove.first == square &&
                                        board is BoardRun &&
                                        board.turn == selectedMove.second.player
                                             -> lightRed
                                else -> darkBrown
                            }
                    ))
                }
            }
        }
    }
}

@Composable
@Preview
fun GamePreview() {
    val game = Game().new()
        .play("3e".toSquare(), "4f".toSquare())
        .play("6f".toSquare(), "5g".toSquare())

    GameView(game.board!!, null, { })
}
