package com.example.smarttasks.task.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import com.example.smarttasks.databinding.DialogCommentBinding
import com.example.smarttasks.task.ui.activities.TaskDetailsActivity
import com.example.smarttasks.utils.StringKeys

class CommentDialog(context: TaskDetailsActivity) : Dialog(context) {

    interface IComment {
        fun onSendWithComment(comment: String, buttonType: TaskDetailsActivity.CurrentButtonEnum)

        fun onSendWithoutComment()
    }

    private lateinit var binding: DialogCommentBinding
    private var mListener: IComment = context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPageData()
    }

    private fun setPageData() {
        binding.apply {
            noBtn.setOnClickListener(onClickNo)
            yesBtn.setOnClickListener(onClickYes)
            resolveBtn.setOnClickListener { sendComment(TaskDetailsActivity.CurrentButtonEnum.RESOLVE) }
            cantResolveBtn.setOnClickListener { sendComment(TaskDetailsActivity.CurrentButtonEnum.CANT_RESOLVE) }
            titleTv.text = StringKeys.COMMENT_DIALOG_TITLE
            noBtn.text = StringKeys.NO_TEXT
            yesBtn.text = StringKeys.YES_TEXT
            commentEt.hint = StringKeys.COMMENT_TEXT
            resolveBtn.text = StringKeys.RESOLVE_TEXT
            cantResolveBtn.text = StringKeys.CANT_RESOLVE_TEXT
        }
    }

    private val onClickNo = View.OnClickListener {
        mListener.onSendWithoutComment()
        dismiss()
    }

    private val onClickYes = View.OnClickListener {
        binding.apply {
            titleTv.visibility = View.INVISIBLE
            firstButtonContainerLl.isVisible = false
            secondPartContainerLl.isVisible = true
        }
    }

    private fun sendComment(buttonType: TaskDetailsActivity.CurrentButtonEnum) {
        binding.apply {
            if (!commentEt.text.toString().isEmpty()) {
                mListener.onSendWithComment(commentEt.text.toString(), buttonType)
                dismiss()
            }
        }
    }
}