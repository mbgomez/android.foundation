package miguel.project.foundation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import miguel.project.foundation.ui.nav.FoundationNavGraph
import miguel.project.foundation.ui.theme.FoundationProjectTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoundationProjectTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState })
                    {
                        FoundationNavGraph(scaffoldState = scaffoldState)
                    }
                }
            }
        }
    }

}