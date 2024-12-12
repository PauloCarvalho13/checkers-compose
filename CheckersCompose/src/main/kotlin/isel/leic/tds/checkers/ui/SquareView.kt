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
            .then(if (isSelected) Modifier.border(3.dp, Color.Red) else Modifier)
    ) {
        when {
            piece != null -> RenderPiece(piece)
            showTargets -> RenderTarget(isPossibleMove, isPossibleCapture)
        }
    }
}

@Composable
private fun RenderPiece(piece: Piece) {
    val file = when (piece) {
        is Pawn -> if (piece.player == Player.WHITE) "pawn_white" else "pawn_black"
        is Queen -> if (piece.player == Player.WHITE) "queen_white" else "queen_black"
        else -> null
    }

    file?.let {
        Image(
            painter = painterResource("$it.png"),
            contentDescription = "Player $it",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun RenderTarget(isPossibleMove: Boolean, isPossibleCapture: Boolean) {
    val (color, size) = when {
        isPossibleMove -> Color.Green to CIRCLE_SIZE
        isPossibleCapture -> Color.Yellow to CIRCLE_SIZE
        else -> return
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .align(Alignment.Center)
                .background(color, shape = CircleShape)
        )
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