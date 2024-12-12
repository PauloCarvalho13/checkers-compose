package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.*

private val CIRCLE_SIZE = 40.dp

@Composable
fun SquareView(
    piece: Piece?,
    showTargets: Boolean,
    isSelected: Boolean = false,
    isPossibleMove: Boolean = false,
    isPossibleCapture: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.size(100.dp)
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, Color.Red)
                } else {
                    Modifier
                }
            )
    ) {
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
                modifier = Modifier.fillMaxSize() // Garante que a imagem se ajuste ao tamanho do quadrado
            )
        }else if(showTargets){
            when {
                isPossibleMove -> {
                    Box(
                        modifier = Modifier
                            .size(CIRCLE_SIZE)
                            .align(Alignment.Center)
                            .background(Color.Green, shape = CircleShape)
                    )
                }
                isPossibleCapture -> {
                    Box(
                        modifier = Modifier
                            .size(CIRCLE_SIZE)
                            .align(Alignment.Center)
                            .background(Color.Yellow, shape = CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SquarePreview() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SquareView(Pawn(Player.WHITE), isSelected = true, showTargets = true, modifier = Modifier.background(Color.Cyan).size(100.dp))
        SquareView(null, showTargets = true)
        SquareView(Pawn(Player.BLACK), showTargets = true,modifier = Modifier.size(100.dp))
    }
}