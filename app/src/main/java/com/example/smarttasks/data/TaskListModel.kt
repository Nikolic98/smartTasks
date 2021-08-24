package com.example.smarttasks.data

data class TaskListModel(
    var taskList: ArrayList<TaskBareInfo>
) {
    constructor() : this(arrayListOf())
}