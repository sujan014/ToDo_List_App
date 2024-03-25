package com.example.todo_list_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_list_app.AddEditTodoViewModel.AddEditTodoScreen
import com.example.todo_list_app.todo_list.TodoListScreen
import com.example.todo_list_app.ui.theme.ToDo_List_AppTheme
import com.example.todo_list_app.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

// This annotation comes from dagger-hilt. It is necessary if we want to inject dependency in an Android component
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDo_List_AppTheme {
                // A surface container using the 'background' color from the theme

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ){
                    composable(Routes.TODO_LIST){
                        TodoListScreen(
                            onNavigate = {navController.navigate(it.route)}
                        )
                    }
                    composable(
                        Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                        arguments = listOf(
                            navArgument(name = "todoId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ){
                        AddEditTodoScreen(onPopBackStack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun EntryPoint(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.TODO_LIST
    ){
        composable(Routes.TODO_LIST){
            TodoListScreen(
                onNavigate = {navController.navigate(it.route)}
            )
        }
        composable(
            Routes.ADD_EDIT_TODO + "?todoId={todoId}",
            arguments = listOf(
                navArgument(name = "todoId"){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            AddEditTodoScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDo_List_AppTheme {
        Greeting("Android")
    }
}