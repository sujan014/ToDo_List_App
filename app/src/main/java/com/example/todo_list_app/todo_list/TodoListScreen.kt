package com.example.todo_list_app.todo_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_list_app.data.Todo
import com.example.todo_list_app.utils.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()  // automatically pass viewmodel for us, we don't need to create on our own
){
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true){
        // This block will be executed only once when this composable is executed
        viewModel.uiEvent.collect{event ->
            when(event){
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed){
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit    // we don't do anything
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(todos.value){todo ->
                TodoItem(
                    todo = todo,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(
                                TodoListEvent.OnTodoClick(todo)
                            )
                        }
                )
            }
        }
    }
}