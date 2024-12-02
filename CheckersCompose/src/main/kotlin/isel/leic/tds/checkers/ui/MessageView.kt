package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable



@Composable
fun Message(
    message: String,
    onClose: ()->Unit,
) = AlertDialog(
    onDismissRequest = onClose,
    confirmButton = {
        TextButton(onClick = onClose) { Text("Ok") }
    },
    text = { Text(message, style = MaterialTheme.typography.body1) }
)