package com.example.todo_list_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*In manifest <Application, Add
android:name=".ui.theme.TodoApp"
 */
@HiltAndroidApp
class TodoApp: Application() {
}