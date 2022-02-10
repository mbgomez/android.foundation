package miguel.project.foundation.statemachine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import miguel.project.domain.model.Post
import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.GettingPostStateError
import miguel.project.foundation.ui.statemachine.GettingPostStateSuccess
import miguel.project.foundation.ui.statemachine.InitState
import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.statemachine.gettingPostState_actionGetPostsError
import miguel.project.foundation.ui.statemachine.gettingPostState_actionGetPostsSuccess
import miguel.project.foundation.ui.statemachine.mainInitState_actionGetPosts
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import miguel.project.common_test.extension.test
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class MainStateMachineTest {

    private val stateMachine = MainStateMachine()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testLoginInitState() = runBlockingTest {
        val expectedState = InitState()
        val observableTest =
            stateMachine.dataFlow.test(this)
        stateMachine.init()
        Assert.assertEquals(expectedState, stateMachine.state)
        observableTest.assertValues(expectedState).finish()
    }

    @Test
    fun testLoginInitState_mainInitState_actionGetPosts() = runBlockingTest {
        val currentState = InitState()
        val dataModel = ""
        val uiModel: List<Post> = mockk(relaxed = true)
        val expectedState = GettingPostState(uiModel = uiModel, dataModel = dataModel)
        val newState = currentState.runAction(
            mainInitState_actionGetPosts,
            uiModel = uiModel,
            dataModel = dataModel
        )
        Assert.assertEquals(expectedState, newState)
    }

    @Test
    fun testLoginInitState_ActionUnknown() = runBlockingTest {
        val currentState = InitState()
        val newState = currentState.runAction("")
        Assert.assertEquals(currentState, newState)
    }

    @Test
    fun testGettingPostState_gettingPostState_actionGetPostsSuccess() = runBlockingTest {
        val currentState = GettingPostState()
        val dataModel = ""
        val uiModel: List<Post> = mockk(relaxed = true)
        val expectedState = GettingPostStateSuccess(uiModel = uiModel, dataModel = dataModel)
        val newState = currentState.runAction(
            gettingPostState_actionGetPostsSuccess,
            uiModel = uiModel,
            dataModel = dataModel
        )
        Assert.assertEquals(expectedState, newState)
    }

    @Test
    fun testGettingPostState_gettingPostState_actionGetPostsError() = runBlockingTest {
        val currentState = GettingPostState()
        val dataModel = ""
        val uiModel: List<Post> = mockk(relaxed = true)
        val expectedState = GettingPostStateError(uiModel = uiModel, dataModel = dataModel)
        val newState = currentState.runAction(
            gettingPostState_actionGetPostsError,
            uiModel = uiModel,
            dataModel = dataModel
        )
        Assert.assertEquals(expectedState, newState)
    }


    @Test
    fun testGettingPostState_ActionUnknown() = runBlockingTest {
        val currentState = GettingPostState()
        val newState = currentState.runAction("")
        Assert.assertEquals(currentState, newState)
    }

}
