package miguel.project.schoolmanagement.ui.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun DefaultDialog(
    title: String = "",
    message: String,
    confirmLabel: String,
    dismissLabel: String,
    confirmAction: () -> Unit,
    dismissAction: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(

                    onClick = {
                        confirmAction()
                        openDialog.value = false
                    }) {
                    Text(confirmLabel)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        dismissAction()
                        openDialog.value = false
                    }) {
                    Text(dismissLabel)
                }
            }
        )
    }
}
