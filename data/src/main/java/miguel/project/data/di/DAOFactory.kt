package miguel.project.data.di

import miguel.project.data.datasource.FoundationDatabase
import miguel.project.data.datasource.local.PostDao

object DAOProvider{
    fun providePostDao(db: FoundationDatabase): PostDao {
        return db.getPostDao()
    }
}
