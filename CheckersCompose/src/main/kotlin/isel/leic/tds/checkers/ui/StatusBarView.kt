package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.leic.tds.checkers.model.*

@Composable
fun StatusBarView(game: Game) {
    Row(
        modifier = Modifier.width(GRID_WIDTH).background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val (state,player) = when(game.board) {
            is BoardRun -> "Turn: " to game.board.turn
            is BoardWin -> "Winner: " to game.board.winner
            else -> "" to Player.WHITE // todo idk what to put
        }
        Text(state, fontSize = 32.sp)
        SquareView(Pawn(player), modifier = Modifier.size(32.dp))
    }
}

@Composable
@Preview
fun StatusBarPreview() {
    val game = Game().new()
    StatusBarView(game)
}