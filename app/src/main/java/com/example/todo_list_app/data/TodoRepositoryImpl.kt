package com.example.todo_list_app.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dao: TodoDao
): TodoRepository {
    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo = todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo = todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id = id)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }
}