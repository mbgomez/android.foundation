package miguel.project.foundation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import miguel.project.foundation.R
import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.GettingPostStateSuccess
import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.statemachine.StatesMain
import miguel.project.foundation.ui.statemachine.mainInitState_actionGetPosts
import miguel.project.foundation.ui.viewmodel.MainViewModel

@SuppressWarnings("unused")
@Composable
fun DetailScreen(mainStateMachine: MainStateMachine, viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        mainStateMachine.init()
        mainStateMachine.runAction(mainInitState_actionGetPosts)
    }
    val state = mainStateMachine.dataFlow.collectAsState(initial = StatesMain())
    DetailContent(state.value as StatesMain)
}

@Composable
fun DetailContent(state: StatesMain) {

    val smallHeight = dimensionResource(R.dimen.height_small)
    when (state) {
        is GettingPostState -> {
            LoadingView()
        }
        is GettingPostStateSuccess -> {
            Column {
                Text(
                    text = "THIS IS THE DETAIL SCREEN",
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(smallHeight))
                val postList = state.uiModel ?: emptyList()
                PostListView(postList)
            }
        }
    }
}