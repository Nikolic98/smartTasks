package com.example.smarttasks.task.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttasks.data.Task
import com.example.smarttasks.data.TaskBareInfo
import com.example.smarttasks.repository.TaskRepository
import com.example.smarttasks.utils.StringKeys
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val mTaskResponse = MutableLiveData<List<Task>>()
    private var mSavedList: List<TaskBareInfo> = emptyList()

    fun getTasks() {
        viewModelScope.launch {
            val response = repository.getTasks().tasks.sortedWith(compareByDescending<Task> { it.priority }.thenBy { it.targetDate })
            val formattedList = formatTime(response)
            mTaskResponse.value = updateList(formattedList)
        }
    }

    fun addSavedList(savedList: List<TaskBareInfo>) {
        this.mSavedList = savedList
    }

    fun refreshListWithUpdatedData() {
        mTaskResponse.value = updateList(mTaskResponse.value!!)
    }

    private fun formatTime(taskList: List<Task>): List<Task> {
        val inputFormat = SimpleDateFormat(StringKeys.INPUT_DATE_FORMAT)
        val outFormat = SimpleDateFormat(StringKeys.EXPORT_DATE_FORMAT)
        for (task in taskList) {
            val targetDate: Date = inputFormat.parse(task.targetDate)
            val startDate = outFormat.format(targetDate)
            task.formattedStartDate = startDate
            if (task.dueDate != null) {
                val dueDate: Date = inputFormat.parse(task.dueDate)
                val diff: Long = dueDate.getTime() - targetDate.getTime()
                task.formattedEndDate = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()
            }
        }
        return taskList
    }

    private fun updateList(oldList: List<Task>): List<Task> {
        for (oldItem in oldList) {
            for (savedItem in mSavedList) {
                if (oldItem.id == savedItem.id) {
                    oldItem.comment = savedItem.comment
                    oldItem.taskStatus = savedItem.taskStatus
                }
            }
        }
        return oldList
    }
}