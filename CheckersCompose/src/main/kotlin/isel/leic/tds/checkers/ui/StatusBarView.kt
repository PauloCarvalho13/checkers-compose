package isel.leic.tds.checkers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import isel.leic.tds.checkers.model.*

@Composable
fun StatusBarView(clash: Clash) {
    Row(
        modifier = Modifier
            .width(BOARD_WITH + MARGIN_WIDTH)
            .background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when (clash) {
            is ClashRun -> {
                Text("Game: ${clash.id}")
                clash.sidePlayer.let { sidePlayer ->
                    Text("You: ${sidePlayer.name}", fontSize = 18.sp)
                    val message = when (clash.game.board) {
                        is BoardRun -> if (clash.game.board.turn == sidePlayer) "Your turn" else "Waiting.."
                        is BoardWin -> if (clash.game.board.winner == sidePlayer) "You WIN" else "You LOSE"
                        else -> ""
                    }
                    Text(message, fontSize = 18.sp)
                }
            }
            else -> {
                Box(modifier = Modifier.width(BOARD_WITH + MARGIN_WIDTH)) {
                    Text("Start a new game", fontSize = 18.sp)
                }
            }
        }
    }
}
