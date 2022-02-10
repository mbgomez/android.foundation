package miguel.project.foundation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import miguel.project.common.model.Status
import miguel.project.foundation.ui.statemachine.GettingPostState
import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.statemachine.gettingPostState_actionGetPostsError
import miguel.project.foundation.ui.statemachine.gettingPostState_actionGetPostsSuccess
import miguel.project.domain.usecase.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(
    private val postsUseCase: GetPostsUseCase,
    private val mainStateMachine: MainStateMachine
) : ViewModel() {

    init {
        initStateObserver()
    }

    fun initStateObserver() {
        viewModelScope.launch {
            mainStateMachine.dataFlow.flowOn(Dispatchers.IO).collect { state ->
                when (state) {
                    is GettingPostState -> getPosts()
                    else -> {
                        //Do not handle unknown states
                    }
                }
            }
        }
    }

    fun getPosts() {
        viewModelScope.launch {
            postsUseCase.run().catch {
                mainStateMachine.runAction(gettingPostState_actionGetPostsError)
            }.collect {
                if(it.status == Status.SUCCESS){
                    mainStateMachine.runAction(gettingPostState_actionGetPostsSuccess, uiModel = it.data)
                } else if(it.status == Status.ERROR){
                    mainStateMachine.runAction(gettingPostState_actionGetPostsError)
                }

            }
        }
    }

}