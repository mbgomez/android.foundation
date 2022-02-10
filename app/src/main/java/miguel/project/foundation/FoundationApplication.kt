package miguel.project.foundation

import android.app.Application
import miguel.project.domain.di.KoinModules
import miguel.project.foundation.di.stateMachineModule
import miguel.project.foundation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class FoundationApplication : Application() {

    private val appComponent: MutableList<Module> =
        mutableListOf(
            viewModelModule,
            stateMachineModule
        )

    override fun onCreate() {
        super.onCreate()
        appComponent.addAll(KoinModules.modules)
        startKoin {
            androidContext(applicationContext)
            modules(appComponent)
        }
    }
}