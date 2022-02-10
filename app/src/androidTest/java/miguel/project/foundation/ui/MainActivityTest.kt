package miguel.project.foundation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import miguel.project.domain.model.Post
import miguel.project.foundation.R
import miguel.project.foundation.ui.activity.MainActivity
import miguel.project.foundation.ui.screen.MainContent
import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.GettingPostStateSuccess
import miguel.project.foundation.ui.theme.FoundationProjectTheme
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule

    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test_GettingPostState() {

        val state = GettingPostState()
        composeTestRule.setContent {

            FoundationProjectTheme {
                MainContent(state)
            }

        }
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val description = context.resources.getString(R.string.progress_indicator_desccription)
        composeTestRule.onNodeWithContentDescription(description).assertIsDisplayed()
    }

    @Test
    fun test_GettingPostStateSuccess() {
        val title1 = "title1"
        val body1 = "body1"
        val title2 = "title2"
        val body2 = "body2"

        val postUI1 = Post(title1, body1)
        val postUI2 = Post(title2, body2)

        val postList = listOf(postUI1, postUI2)

        val state = GettingPostStateSuccess(uiModel = postList)
        composeTestRule.setContent {

            FoundationProjectTheme {
                MainContent(state)
            }

        }

        composeTestRule.onNodeWithText(title1).assertIsDisplayed()
        composeTestRule.onNodeWithText(body1).assertIsDisplayed()
        composeTestRule.onNodeWithText(title2).assertIsDisplayed()
        composeTestRule.onNodeWithText(body2).assertIsDisplayed()
    }

}