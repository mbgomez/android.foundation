package miguel.project.foundation.di


import miguel.project.foundation.ui.statemachine.MainStateMachine
import miguel.project.foundation.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}

val stateMachineModule = module {
    single { MainStateMachine() }
}