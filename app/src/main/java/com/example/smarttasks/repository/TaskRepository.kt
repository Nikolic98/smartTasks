package com.example.smarttasks.repository

import com.example.smarttasks.api.RetrofitInstance
import com.example.smarttasks.api.TaskResponse

class TaskRepository {

    suspend fun getTasks(): TaskResponse {
        return RetrofitInstance.api.getTasks()
    }
}