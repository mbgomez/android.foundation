package miguel.project.schoolmanagement.ui.component

import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
    onDismiss: () -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                content = {
                    Text(text = data.message)
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss) {
                            Text(text = actionLabel)
                        }
                    }
                }
            )
        },
        modifier = modifier
    )
}
