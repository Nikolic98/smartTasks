package com.example.smarttasks.api

import com.example.smarttasks.data.Task

data class TaskResponse(
    val tasks: List<Task>
)