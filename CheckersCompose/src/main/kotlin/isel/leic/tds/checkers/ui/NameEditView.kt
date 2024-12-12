package isel.leic.tds.checkers.ui

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
import androidx.compose.ui.text.input.ImeAction
import isel.leic.tds.checkers.model.Name

@Composable
fun NameEdit(
    action: Action,
    onCancel: ()->Unit,
    onAction: (Name)->Unit
) {
    var txt by remember{ mutableStateOf("") }
    AlertDialog(
        title = { Text("Name to ${action.text}", style = MaterialTheme.typography.h4) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                enabled = Name.isValid(txt),
                onClick = { onAction(Name(txt)) }
            ) { Text(action.text) }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        },
        text = {
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
                            onAction(Name(txt))
                        }
                    }
                ),
                singleLine = true
            )
        }
    )
}
