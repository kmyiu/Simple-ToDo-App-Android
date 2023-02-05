package com.kmyiu.simpletodoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kmyiu.simpletodoapp.MyApplication.Companion.dbName

@Entity(tableName = dbName)
data class TodoTask(
    @ColumnInfo(name = "title")
    val title: String = "",
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
)