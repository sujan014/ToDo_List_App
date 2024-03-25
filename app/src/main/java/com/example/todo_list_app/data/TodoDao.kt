package com.example.todo_list_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Used to access our data
// Defines the ways, we want to access data

// Room will take care of implementations and generate code for us
@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    // Pass an SQL query
    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    // Flow - get realtime updates as soon as database changes
    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>
}