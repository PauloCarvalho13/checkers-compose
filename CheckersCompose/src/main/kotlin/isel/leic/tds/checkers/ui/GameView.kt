package isel.leic.tds.checkers.ui

import BOARD_DIM
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import isel.leic.tds.checkers.model.*

private val CELL_SIZE = 50.dp
private val LINE_WIDTH = 2.dp
val MARGIN_WIDTH = 12.dp
val GRID_WIDTH = CELL_SIZE * BOARD_DIM + LINE_WIDTH * (BOARD_DIM-1)
val BOARD_WITH  = GRID_WIDTH + (2 * MARGIN_WIDTH)


private val darkBrown = Color(139, 69, 19)

@Composable
fun GameView(
    board: Board?,
    showTargets: Boolean,
    selectedMove: Pair<Square, Piece>?,
    sidePlayer: Player = Player.WHITE,
    onClickSquare: (Square) -> Unit
) {
    Column(modifier = Modifier.size(BOARD_WITH + MARGIN_WIDTH).background(darkBrown)) {
        Column(
            modifier = Modifier.size(BOARD_WITH),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(MARGIN_WIDTH / 4))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(MARGIN_WIDTH + CELL_SIZE / 2))
                repeat(BOARD_DIM) { col ->
                    Text(
                        text = "${Column(col).symbol}",
                        modifier = Modifier.width(CELL_SIZE)
                    )
                }
            }

            Spacer(modifier = Modifier.size(MARGIN_WIDTH / 4))

            repeat(BOARD_DIM) { index ->
                val row = if (sidePlayer == Player.WHITE) index else BOARD_DIM - 1 - index
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(0.75 * MARGIN_WIDTH))
                    Text("${Row(row).digit}")
                    Spacer(modifier = Modifier.size(MARGIN_WIDTH / 2))

                    repeat(BOARD_DIM) { col ->
                        val square = Square(Row(row), Column(col))
                        val moves = board?.moves

                        val squareColor = if(square.black) Color.DarkGray else Color.Gray

                        val isSelected = selectedMove?.first == square
                        val isPossibleMove = selectedMove != null &&
                                board is BoardRun &&
                                selectedMove.second.getPossibleCaptures(selectedMove.first, moves ?: emptyMap()).isEmpty() &&
                                selectedMove.second.canMove(selectedMove.first, square, moves ?: emptyMap())

                        val isPossibleCapture = selectedMove != null &&
                                board is BoardRun &&
                                selectedMove.second.canCapture(selectedMove.first, square, moves ?: emptyMap())

                        SquareView(
                            piece = moves?.get(square),
                            showTargets = showTargets,
                            isSelected = isSelected,
                            isPossibleMove = isPossibleMove,
                            isPossibleCapture = isPossibleCapture,
                            onClick = { onClickSquare(square) },
                            modifier = Modifier.size(CELL_SIZE).background(squareColor)
                        )
                    }
                }
            }
        }
    }
}



@Composable
@Preview
fun GamePreview() {
    val game = Game().new()

    GameView(game.board!!,true,null, Player.WHITE) { }
}
