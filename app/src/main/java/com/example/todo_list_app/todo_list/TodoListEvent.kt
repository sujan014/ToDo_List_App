package com.example.todo_list_app.todo_list

import com.example.todo_list_app.data.Todo

sealed class TodoListEvent{
    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()
    data class OnDoneChangeClick(val todo: Todo, val isDone: Boolean): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val todo: Todo): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
}
