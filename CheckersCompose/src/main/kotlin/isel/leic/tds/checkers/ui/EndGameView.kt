package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.*

@Composable
fun EndGameDialog(
    player: Player,
    onClose: ()->Unit = {},
) = AlertDialog(
    onDismissRequest = onClose,
    confirmButton = { },
    title = { Text("Game Finished", style = MaterialTheme.typography.h4) },
    text = { EndGameContent(player) }
)

@Composable
private fun EndGameContent(player: Player) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Player(player, modifier = Modifier.size(32.dp))
        Text(" Won The Game", style = MaterialTheme.typography.h5)
    }
}

@Composable
@Preview
fun EndGameDialogPreview()  {
    EndGameContent(
        player = Player.BLACK,
    )
}
