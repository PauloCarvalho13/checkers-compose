package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.*

@Composable
fun SquareView(piece: Piece?, onClick: ()->Unit = {}, modifier: Modifier = Modifier.size(100.dp)) {
    Box(modifier.clickable(onClick = onClick)) {
        if (piece != null) {
            val file = when (piece) {
                is Pawn -> when (piece.player) {
                    Player.WHITE -> "pawn_white"
                    Player.BLACK -> "pawn_black"
                }

                is Queen -> when (piece.player) {
                    Player.WHITE -> "queen_white"
                    Player.BLACK -> "queen_black"
                }

                else -> ""
            }
            Image(
                painter = painterResource("$file.png"),
                contentDescription = "Player $file",
                modifier = modifier
            )
        }
    }
}

@Composable
@Preview
fun SquarePreview() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SquareView(Pawn(Player.WHITE), modifier = Modifier.background(Color.Cyan).size(100.dp))
        SquareView(null)
        SquareView(Pawn(Player.BLACK), modifier = Modifier.size(100.dp))
    }
}