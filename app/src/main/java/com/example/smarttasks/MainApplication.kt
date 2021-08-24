package com.example.smarttasks

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.smarttasks.data.Task
import com.example.smarttasks.data.TaskBareInfo
import com.example.smarttasks.data.TaskListModel
import com.example.smarttasks.utils.SharedPrefKeys
import com.google.gson.Gson

class MainApplication : Application() {

    companion object {
        private lateinit var context: Context
        fun setContext(con: Context) {
            context = con
        }
    }

    fun getTaskList(): TaskListModel {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SharedPrefKeys.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(SharedPrefKeys.TASK_LIST_MODEL, "")
        Gson().fromJson(json, TaskListModel::class.java)
        return if (json.equals("", ignoreCase = true)) {
            TaskListModel()
        } else Gson().fromJson(json, TaskListModel::class.java)
    }

    fun saveToTaskList(taskModel: Task) {
        val listModel = MainApplication().getTaskList()
        val taskToBeAdded = TaskBareInfo(taskModel.id, taskModel.taskStatus, taskModel.comment)

        if (listModel.taskList.isEmpty()) {
            listModel.taskList.add(taskToBeAdded)
        } else {
            val index = listModel.taskList.indexOfFirst { it.id == taskModel.id }
            if (index != -1) {
                listModel.taskList[index] = taskToBeAdded
            } else {
                listModel.taskList.add(taskToBeAdded)
            }
        }

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SharedPrefKeys.USER_SHARED_PREFERENCES, MODE_PRIVATE)
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putString(SharedPrefKeys.TASK_LIST_MODEL, Gson().toJson(listModel))
        prefsEditor.apply()
    }
}