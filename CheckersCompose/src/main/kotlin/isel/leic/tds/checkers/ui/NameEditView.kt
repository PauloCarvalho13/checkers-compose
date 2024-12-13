package isel.leic.tds.checkers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.Name

@Composable
fun NameEdit(
    action: Action,
    onCancel: ()->Unit,
    onAction: (Name/*, Int*/)->Unit
) {
    var txt by remember{ mutableStateOf("") }
    // TODO
    var boardDim by remember { mutableStateOf(8) }
    val boardSizes = listOf(4, 6, 8)
    AlertDialog(
        title = { Text("Name to ${action.text}", style = MaterialTheme.typography.h4) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                enabled = Name.isValid(txt),
                onClick = { onAction(Name(txt)/*, boardDim*/) }
            ) { Text(action.text) }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = txt,
                    onValueChange = { newValue ->
                        txt = newValue.filter { it != '\n' }
                    },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (Name.isValid(txt)) {
                                onAction(Name(txt)/*, boardDim*/)
                            }
                        }
                    ),
                    singleLine = true
                )
             if (action.name == "START"){
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Select Board Size:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        boardSizes.forEach { size ->
                            TextButton(
                                onClick = { boardDim = size },
                                enabled = boardDim != size
                            ) {
                                Text(
                                    text = "$size x $size",
                                    color = if (boardDim == size) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
