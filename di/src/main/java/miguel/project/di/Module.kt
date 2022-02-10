package miguel.project.domain.di


import miguel.project.data.datasource.DatabaseFactory
import miguel.project.data.datasource.remote.InternetConnectionChecker
import miguel.project.data.di.DAOProvider.providePostDao
import miguel.project.data.di.PostAPIFactory
import miguel.project.data.mapper.PostMapper
import miguel.project.data.repository.PostRepository
import miguel.project.domain.repository.IPostRepository
import miguel.project.domain.usecase.GetPostsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


val useCaseModule = module {
    factory { GetPostsUseCase(get()) }
}

val mappersModule = module {
    factory { PostMapper() }
}

val repositoryModule = module {
    factory<IPostRepository> {
        PostRepository(get(), get(), get(), get())
    }
}


val networkModule = module {
    factory { PostAPIFactory.POST_SERVICE }
    single { InternetConnectionChecker(androidContext()) }
}


val dataBaseModule = module {
    single { DatabaseFactory.getDBInstance(get()) }
    factory { providePostDao(get()) }

}

object KoinModules {

    val modules: List<Module> =
        listOf(
            repositoryModule,
            networkModule,
            dataBaseModule,
            useCaseModule,
            mappersModule
        )

}