package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
        game.firstPlayer.let {
            Text("You: ", fontSize = 32.sp)
            SquareView(Pawn(it),modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(32.dp))
        }
        val (state,player) = when(game.board) {
            is BoardRun -> "Turn: " to game.board.turn
            is BoardWin -> "Winner: " to game.board.winner
            null -> "No board" to null
        }
        Text(state, fontSize = 32.sp)
        player?.let { SquareView(Pawn(player), modifier = Modifier.size(32.dp)) }
    }
}

@Composable
@Preview
fun StatusBarPreview() {
    val game = Game().new()
    StatusBarView(game)
}