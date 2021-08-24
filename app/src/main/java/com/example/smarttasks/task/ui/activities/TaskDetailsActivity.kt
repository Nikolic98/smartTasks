package com.example.smarttasks.task.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.smarttasks.MainApplication
import com.example.smarttasks.R
import com.example.smarttasks.data.Task
import com.example.smarttasks.databinding.ActivityTaskDetailsBinding
import com.example.smarttasks.task.ui.dialogs.CommentDialog
import com.example.smarttasks.utils.BundleData
import com.example.smarttasks.utils.StringKeys

class TaskDetailsActivity : AppCompatActivity(), CommentDialog.IComment {

    enum class CurrentButtonEnum {
        RESOLVE, CANT_RESOLVE
    }

    private lateinit var binding: ActivityTaskDetailsBinding
    private lateinit var mTask: Task
    private lateinit var mCurrentActiveButton: CurrentButtonEnum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainApplication.setContext(this)
        getBundleData()
    }

    private fun getBundleData() {
        val bundle = intent.extras
        if (bundle != null) {
            mTask = bundle.getParcelable(BundleData.TASK_DATA)!!
            setTextData();
        } else {
            onBackPressed()
        }
    }

    private fun setTextData() {
        binding.apply {
            titleTv.text = StringKeys.TASK_DETAILS_TITLE_TEXT
            dateLabelTv.text = StringKeys.DUE_DATE_TEXT
            daysLabelTv.text = StringKeys.DAYS_LEFT_TEXT
            commentLabelTv.text = StringKeys.COMMENT_TEXT
            resolveBtn.text = StringKeys.RESOLVE_TEXT
            cantResolveBtn.text = StringKeys.CANT_RESOLVE_TEXT
            taskTitleTv.text = mTask.title
            dateValueTv.text = mTask.formattedStartDate
            if (mTask.dueDate != null) {
                daysValueTv.text = mTask.formattedEndDate
                daysValueTv.isVisible = true
                daysLabelTv.isVisible = true
            } else {
                daysValueTv.visibility = View.INVISIBLE
                daysLabelTv.visibility = View.INVISIBLE
            }
            descriptionTv.text = mTask.description

            setTaskStatus()
            setCommentText()

            resolveBtn.setOnClickListener(onClickResolve)
            cantResolveBtn.setOnClickListener(onClickCantResolve)
            backArrowIv.setOnClickListener { finish() }
        }
    }

    private fun setCommentText() {
        binding.apply {
            if (mTask.comment != null && mTask.comment!!.isNotEmpty()) {
                commentContainerLl.isVisible = true
                commentTv.text = mTask.comment
            }
        }
    }

    private fun setTaskStatus() {
        binding.apply {
            when (mTask.taskStatus) {
                0 -> {
                    taskStatusTv.text = StringKeys.UNRESOLVED_TEXT
                    taskTitleTv.setTextColor(resources.getColor(R.color.app_red))
                    daysValueTv.setTextColor(resources.getColor(R.color.app_red))
                    dateValueTv.setTextColor(resources.getColor(R.color.app_red))
                    taskStatusTv.setTextColor(resources.getColor(R.color.task_status_default))
                    statusContainerLl.visibility = View.VISIBLE
                    checkIv.visibility = View.GONE
                }
                1 -> {
                    taskStatusTv.text = StringKeys.RESOLVED_TEXT
                    taskTitleTv.setTextColor(resources.getColor(R.color.app_green))
                    daysValueTv.setTextColor(resources.getColor(R.color.app_green))
                    dateValueTv.setTextColor(resources.getColor(R.color.app_green))
                    taskStatusTv.setTextColor(resources.getColor(R.color.app_green))
                    statusContainerLl.visibility = View.GONE
                    checkIv.visibility = View.VISIBLE
                    checkIv.setImageResource(R.drawable.sign_resolved)
                }
                2 -> {
                    taskStatusTv.text = StringKeys.UNRESOLVED_TEXT
                    taskTitleTv.setTextColor(resources.getColor(R.color.app_red))
                    daysValueTv.setTextColor(resources.getColor(R.color.app_red))
                    dateValueTv.setTextColor(resources.getColor(R.color.app_red))
                    taskStatusTv.setTextColor(resources.getColor(R.color.app_red))
                    statusContainerLl.visibility = View.GONE
                    checkIv.visibility = View.VISIBLE
                    checkIv.setImageResource(R.drawable.unresolved_sign)
                }
            }
        }
    }

    private val onClickResolve = View.OnClickListener {
        mCurrentActiveButton = CurrentButtonEnum.RESOLVE
        CommentDialog(this).show()
    }

    private val onClickCantResolve = View.OnClickListener {
        mCurrentActiveButton = CurrentButtonEnum.CANT_RESOLVE
        CommentDialog(this).show()
    }

    private fun updateTaskStatus() {
        when (mCurrentActiveButton) {
            CurrentButtonEnum.RESOLVE -> {
                mTask.taskStatus = 1
            }
            CurrentButtonEnum.CANT_RESOLVE -> {
                mTask.taskStatus = 2
            }
        }
        MainApplication().saveToTaskList(mTask)
        setTaskStatus()
        setResult(RESULT_OK, intent)
    }

    override fun onSendWithComment(comment: String, buttonType: CurrentButtonEnum) {
        mCurrentActiveButton = buttonType
        mTask.comment = comment
        updateTaskStatus()
        setCommentText()
    }

    override fun onSendWithoutComment() {
        updateTaskStatus()
    }
}