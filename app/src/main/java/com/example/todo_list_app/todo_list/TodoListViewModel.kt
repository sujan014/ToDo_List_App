package com.example.todo_list_app.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_app.data.Todo
import com.example.todo_list_app.data.TodoRepository
import com.example.todo_list_app.utils.Routes
import com.example.todo_list_app.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// to inject repository using dagger hilt, we do this in constructor using keyword shown below
// For viewmodels specifically,

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
): ViewModel() {
    // in this app, the only variable that can change is the list
    val todos = repository.getTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent= _uiEvent.receiveAsFlow()

    private var deletedTodo: Todo? = null

    // with implementation below we have one function for each event that handles them all
    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick ->{
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick ->{
                deletedTodo?.let {todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Todo deleted",
                        action = "Undo"
                    ))
                }
            }
            is TodoListEvent.OnDoneChangeClick -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(uievent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uievent)
        }
    }
}