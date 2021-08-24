package com.example.smarttasks.task.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttasks.R
import com.example.smarttasks.data.Task
import com.example.smarttasks.databinding.ItemTaskBinding
import com.example.smarttasks.task.ui.fragments.TaskFragment
import com.example.smarttasks.utils.StringKeys

class TaskAdapter(listener: TaskFragment) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface IItemClick {
        fun onItemClick(task: Task)
    }

    private var mTaskList: List<Task> = emptyList()
    private var mListener: IItemClick = listener

    fun setList(taskData: List<Task>) {
        mTaskList = taskData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = mTaskList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                dateLabelTv.text = StringKeys.DUE_DATE_TEXT
                daysLabelTv.text = StringKeys.DAYS_LEFT_TEXT
                taskTitleTv.text = task.title
                dateValueTv.text = task.formattedStartDate
                if (task.dueDate != null) {
                    daysValueTv.text = task.formattedEndDate
                    daysLabelTv.isVisible = true
                    daysValueTv.isVisible = true
                } else {
                    daysValueTv.visibility = View.INVISIBLE
                    daysLabelTv.visibility = View.INVISIBLE
                }

                when (task.taskStatus) {
                    0 -> {
                        taskStatusIv.visibility = View.GONE
                    }
                    1 -> {
                        taskStatusIv.visibility = View.VISIBLE
                        taskStatusIv.setImageResource(R.drawable.sign_resolved)
                    }
                    2 -> {
                        taskStatusIv.visibility = View.VISIBLE
                        taskStatusIv.setImageResource(R.drawable.unresolved_sign)
                    }
                }
                itemView.setOnClickListener { mListener.onItemClick(task) }
            }
        }
    }
}