package miguel.project.foundation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import miguel.project.domain.model.Post
import miguel.project.foundation.R
import miguel.project.foundation.ui.CardView
import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.GettingPostStateSuccess
import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.statemachine.StatesMain
import miguel.project.foundation.ui.statemachine.mainInitState_actionGetPosts
import miguel.project.foundation.ui.theme.FoundationProjectTheme
import miguel.project.foundation.ui.viewmodel.MainViewModel

@SuppressWarnings("unused")
@Composable
fun MainScreen(
    mainStateMachine: MainStateMachine,
    viewModel: MainViewModel,
    navigateToDashBoard: ((index: Int) -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        mainStateMachine.init()
        mainStateMachine.runAction(mainInitState_actionGetPosts)
    }
    val state = mainStateMachine.dataFlow.collectAsState(initial = StatesMain())
    MainContent(state.value as StatesMain, navigateToDashBoard)
}


@Composable
fun MainContent(state: StatesMain, navigateToDashBoard: ((index: Int) -> Unit)? = null) {
    when (state) {
        is GettingPostState -> {
            LoadingView()
        }
        is GettingPostStateSuccess -> {
            val postList = state.uiModel ?: emptyList()
            PostListView(postList, navigateToDashBoard)
        }
    }
}

@Composable
fun PostListView(postList: List<Post>, navigateToDashBoard: ((index: Int) -> Unit)? = null) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val smallHeight = dimensionResource(R.dimen.height_small)

    LazyColumn {
        itemsIndexed(postList) { index, post ->
            CardView(
                modifier = Modifier
                    .padding(top = smallPadding, start = smallPadding, end = smallPadding),
                onClick = {
                    navigateToDashBoard?.invoke(index)
                }
            ) {
                Column {
                    Text(
                        text = post.title ?: "",
                        color = MaterialTheme.colors.secondaryVariant,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Spacer(modifier = Modifier.height(smallHeight))

                    Text(
                        text = post.body ?: "",
                        style = MaterialTheme.typography.body2
                    )

                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val description = stringResource(R.string.progress_indicator_desccription)
        CircularProgressIndicator(modifier = Modifier.semantics {
            this.contentDescription = description
        }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoundationProjectTheme {
        MainContent(GettingPostState())
    }
}