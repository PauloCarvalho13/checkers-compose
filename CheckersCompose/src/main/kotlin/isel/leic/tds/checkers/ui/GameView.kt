package isel.leic.tds.checkers.ui

import BOARD_DIM
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

data class BoardTheme(val squareColor: Color, val columnColor: Color)

@Composable
fun GameView(
    board: Board?,
    showTargets: Boolean,
    selectedMove: SelectedMove?,
    theme: Theme,
    onClickSquare: (Square) -> Unit,
    sidePlayer: Player = Player.WHITE
) {
    val boardTheme = GameTheme(Square(Row(0), Column(0)), theme)
    val moves = board?.moves ?: emptyMap()

    val playerCaptures = remember(board) {
        board?.let { b ->
            b.moves.entries
                .filter { it.value.player == sidePlayer }
                .flatMap { (square, piece) ->
                    piece.getPossibleCaptures(square, moves).keys // Get possible captures for each piece
                }
        } ?: emptyList()
    }

    val possibleMoves = remember(selectedMove){
        selectedMove?.let {
            it.piece.getPossibleMoves(it.square, moves)
        }?: emptyList()
    }

    Column(modifier = Modifier.size(BOARD_WITH + MARGIN_WIDTH).background(boardTheme.columnColor)) {
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

                        val squareColor = GameTheme(square, theme).squareColor

                        val isSelected = selectedMove?.square == square

                        val isPossibleMove = selectedMove != null &&
                                board is BoardRun &&
                                playerCaptures.isEmpty() &&
                                possibleMoves.contains(square)

                        val isPossibleCapture = selectedMove != null &&
                                board is BoardRun &&
                                selectedMove.piece.getPossibleCaptures(selectedMove.square, moves).containsKey(square)

                        SquareView(
                            piece = moves[square],
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

    if(board != null && board.winner() != null) EndGameDialog(board.winner()!!)
}

@Composable
fun GameTheme(square: Square, theme: Theme): BoardTheme {
    return when (theme) {
        Theme.DEFAULT -> BoardTheme(
            squareColor = if (square.black) SquareColors.DEFAULT_BLACK.color else SquareColors.DEFAULT_WHITE.color,
            columnColor = ColumnColors.DEFAULT.color
        )
        Theme.LIGHT -> BoardTheme(
            squareColor = if (square.black) SquareColors.LIGHT_BLACK.color else SquareColors.LIGHT_WHITE.color,
            columnColor = ColumnColors.LIGHT.color
        )
        Theme.COLORFUL -> BoardTheme(
            squareColor = if (square.black) SquareColors.COLORFUL_BLACK.color else SquareColors.COLORFUL_WHITE.color,
            columnColor = ColumnColors.COLORFUL.color
        )
    }
}

@Composable
@Preview
fun GamePreview() {
    val game = Game().new()
    GameView(game.board!!,true,null, Theme.LIGHT, { })
}
