package miguel.project.foundation.ui.statemachine

import miguel.project.foundation.annotations.OpenForTesting
import miguel.project.domain.model.Post

@OpenForTesting
class MainStateMachine : StateMachineBlueprint<List<Post>?, String?>() {

    fun init() {
        state = StatesMain.initState()
        shareDataFlow.tryEmit(state)
    }
}

open class StatesMain(uiModel: List<Post>? = null, dataModel: String? = null) :
    StatesInitBlueprint<List<Post>?, String?>(uiModel, dataModel) {

    companion object {
        fun initState(): StateBlueprint<List<Post>?, String?> =
            StatesMain().createInitState()
    }

    override fun runAction(action: String, uiModel: List<Post>?, dataModel: String?):
            StateBlueprint<List<Post>?, String?> = this

    override fun createInitState(): StateBlueprint<List<Post>?, String?> = InitState()
}

const val mainInitState_actionGetPosts = "mainInitState_actionGetPosts"

open class InitState(
    uiModel: List<Post>? = null,
    dataModel: String? = null
) : StatesMain(uiModel, dataModel) {

    override fun runAction(
        action: String,
        uiModel: List<Post>?,
        dataModel: String?
    ): StateBlueprint<List<Post>?, String?> =
        when (action) {
            mainInitState_actionGetPosts -> GettingPostState(uiModel, dataModel)
            else -> this
        }
}

const val gettingPostState_actionGetPostsSuccess = "gettingPostState_actionGetPostsSuccess"
const val gettingPostState_actionGetPostsError= "gettingPostState_actionGetPostsError"

class GettingPostState(
    uiModel: List<Post>? = null,
    dataModel: String? = null
) : StatesMain(uiModel, dataModel) {

    override fun runAction(
        action: String,
        uiModel: List<Post>?,
        dataModel: String?
    ): StateBlueprint<List<Post>?, String?> =
        when (action) {
            gettingPostState_actionGetPostsSuccess -> GettingPostStateSuccess(uiModel, dataModel)
            gettingPostState_actionGetPostsError -> GettingPostStateError(uiModel, dataModel)
            else -> this
        }
}

class GettingPostStateSuccess(
    uiModel: List<Post>? = null,
    dataModel: String? = null
) : InitState(uiModel, dataModel)

class GettingPostStateError(
    uiModel: List<Post>? = null,
    dataModel: String? = null
) : InitState(uiModel, dataModel)