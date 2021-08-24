package com.example.smarttasks.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smarttasks.repository.TaskRepository
import com.example.smarttasks.task.ui.viewModels.TaskViewModel

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(repository) as T
    }
}