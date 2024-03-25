package com.example.todo_list_app.AddEditTodoViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_list_app.utils.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
){
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.popBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)
            }){
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = viewModel.title,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.fillMaxWidth())

            TextField(
                value = viewModel.description,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                },

            )
        }
    }
}