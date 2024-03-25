package com.example.todo_list_app.AddEditTodoViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_app.data.Todo
import com.example.todo_list_app.data.TodoRepository
import com.example.todo_list_app.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor (
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    var todo by mutableStateOf<Todo?>(null)
    private set     // can only change the value from within viewmodel

    var title by mutableStateOf("")
    private set

    var description by mutableStateOf("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init{
        // if the add is from existing todo edit, load from database
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1){  // clicked on existing one, so load from database
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let {todo->
                    title = todo.title
                    description = todo.description?:""
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent){
        when(event){
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()){
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "Title can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone?:false,
                            id = todo?.id   // if this is null, room will generate id
                        )
                    )
                    sendUiEvent(UiEvent.popBackStack)
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