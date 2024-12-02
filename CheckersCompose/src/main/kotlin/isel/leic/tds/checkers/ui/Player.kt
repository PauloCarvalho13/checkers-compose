package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.Player

@Composable
fun Player(player: Player?, onClick: ()->Unit = {}, modifier: Modifier = Modifier.size(100.dp)) {
    if (player==null)
        Box(modifier.clickable(onClick = onClick))
    else {
        val file = when (player) {
            Player.WHITE -> "pawn_white"
            Player.BLACK -> "pawn_black"
        }
        Image(
            painter = painterResource("$file.png"),
            contentDescription = "Player $file",
            modifier = modifier
        )
    }
}

@Composable
@Preview
fun PlayerXPreview() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Player(Player.WHITE, modifier = Modifier.background(Color.Yellow).size(100.dp))
        Player(null)
        Player(Player.BLACK)
    }
}