package com.kmyiu.simpletodoapp.database

import androidx.room.*
import com.kmyiu.simpletodoapp.MyApplication.Companion.dbName
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoTaskDao {
    @Query("SELECT * FROM $dbName")
    fun getAll(): Flow<List<TodoTask>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todoTask: TodoTask)

    @Query("UPDATE $dbName SET title = :title WHERE uid = :uid")
    suspend fun editByUid(uid: Int, title: String)

    @Delete
    suspend fun delete(todoTask: TodoTask)

    @Query("DELETE FROM $dbName WHERE uid = :uid")
    suspend fun deleteByUid(uid: Int)

    @Query("DELETE FROM $dbName")
    suspend fun deleteAll()
}