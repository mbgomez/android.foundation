package miguel.project.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import miguel.project.data.datasource.local.PostDao
import miguel.project.data.datasource.local.PostTable

const val databaseName = "FoundationDatabase"

@Database(
    entities = [PostTable::class],
    version = 1,
    exportSchema = false
)
abstract class FoundationDatabase : RoomDatabase() {

    abstract fun getPostDao(): PostDao

}

object DatabaseFactory {
    fun getDBInstance(context: Context) =
        Room.databaseBuilder(context, FoundationDatabase::class.java, databaseName)
            .build()
}
