package com.nabeelkhowaja.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todo")
class Todo(
    @ColumnInfo(name = "task")
    var task: String,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}