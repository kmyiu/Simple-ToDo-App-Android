package com.kmyiu.simpletodoapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kmyiu.simpletodoapp.MyApplication.Companion.dbName
import com.kmyiu.simpletodoapp.database.TodoTask
import com.kmyiu.simpletodoapp.database.TodoTaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TodoTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoTaskDao(): TodoTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    dbName
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.todoTaskDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more data, just add them.
         */
        suspend fun populateDatabase(todoTaskDao: TodoTaskDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            todoTaskDao.deleteAll()
            todoTaskDao.insert(TodoTask( "Item #1"))
            todoTaskDao.insert(TodoTask( "Item #2"))
            todoTaskDao.insert(TodoTask( "Item #3"))
            todoTaskDao.insert(TodoTask( "Item #4"))
            todoTaskDao.insert(TodoTask( "Item #5"))
            todoTaskDao.insert(TodoTask( "Item #6"))
            todoTaskDao.insert(TodoTask( "Get it all done"))
        }
    }
}