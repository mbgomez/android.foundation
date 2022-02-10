package miguel.project.foundation.viewmodel

import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.viewmodel.MainViewModel
import miguel.project.domain.usecase.GetPostsUseCase
import miguel.project.common_test.extension.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTests {
    private lateinit var postsUseCase: GetPostsUseCase
    private lateinit var mainStateMachine: MainStateMachine
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    @Before
    fun setup() {
        postsUseCase = mockk(relaxed = true)
        mainStateMachine = mockk(relaxed = true)
        mainViewModel = spyk(MainViewModel(postsUseCase, mainStateMachine))
    }

    @Test
    fun gettingPostState_run() = runBlockingTest {

        val state = GettingPostState()
        val stateFlow = MutableStateFlow(state)

        coEvery { mainStateMachine.dataFlow } returns stateFlow
        coEvery { mainViewModel.getPosts() } just runs
        mainViewModel.initStateObserver()
        coVerify (exactly = 1) { mainViewModel.getPosts() }
    }
}