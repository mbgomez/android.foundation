package miguel.project.foundation.ui.statemachine

import miguel.project.foundation.annotations.OpenForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@OpenForTesting
class StateMachineBlueprint<K, T> {

    lateinit var state: StateBlueprint<K, T>

    protected final val shareDataFlow = MutableSharedFlow<StateBlueprint<K, T>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val dataFlow: Flow<StateBlueprint<K, T>> = shareDataFlow.distinctUntilChanged()

    fun runAction(action: String, uiModel: K? = null, dataModel: T? = null) {

        val newUiModel = uiModel ?: state.uiModel
        val newDataModel = dataModel ?: state.dataModel

        val newState = state.runAction(action, newUiModel, newDataModel)
        if (state != newState) {
            state = newState
            shareDataFlow.tryEmit(state)
        }
    }

    @ExperimentalCoroutinesApi
    fun test()= callbackFlow<String> {

    }
}


@SuppressWarnings("unchecked")
sealed class StateBlueprint<K, T>(
    val uiModel: K?,
    val dataModel: T?
) {
    abstract fun runAction(action: String, uiModel: K? = null, dataModel: T? = null):
            StateBlueprint<K, T>

    override fun toString(): String {
        return super.toString() + " - dataUI:$uiModel - dataModel:$dataModel"
    }

    override fun equals(other: Any?): Boolean = other?.let {
        if (it::class != this::class) {
            false
        } else {
            val otherState = it as StateBlueprint<K, T>
            otherState.uiModel == this.uiModel && otherState.dataModel == this.dataModel
        }
    } ?: false

    override fun hashCode(): Int {
        var result = uiModel?.hashCode() ?: 0
        result = 31 * result + (dataModel?.hashCode() ?: 0)
        return result
    }
}

abstract class StatesInitBlueprint<K, T>(dataUI: K, dataModel: T) :
    StateBlueprint<K, T>(dataUI, dataModel) {
    abstract fun createInitState(): StateBlueprint<K, T>
}
