package com.example.smarttasks.task.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smarttasks.MainApplication
import com.example.smarttasks.R
import com.example.smarttasks.data.Task
import com.example.smarttasks.databinding.FragmentTaskBinding
import com.example.smarttasks.factory.TaskViewModelFactory
import com.example.smarttasks.repository.TaskRepository
import com.example.smarttasks.task.ui.activities.TaskDetailsActivity
import com.example.smarttasks.task.ui.adapters.TaskAdapter
import com.example.smarttasks.task.ui.viewModels.TaskViewModel
import com.example.smarttasks.utils.BundleData
import com.example.smarttasks.utils.StringKeys
import java.text.SimpleDateFormat
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task), TaskAdapter.IItemClick {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskViewModel

    private var mDateCounter: Date? = null
    private val mDateFormat = SimpleDateFormat(StringKeys.INPUT_DATE_FORMAT)
    private val mCalendar = Calendar.getInstance()
    private var mCurrentDay = String()
    private var mFirstNextDay = String()
    private var mFirstPrevDay = String()

    private var mMainList: List<Task> = emptyList()
    private val mAdapter = TaskAdapter(this)

    private val detailsResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadSavedList()
            viewModel.refreshListWithUpdatedData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskBinding.bind(view)
        MainApplication.setContext(requireActivity())

        setViewModel()
        setAdapter()
        setPageData()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, TaskViewModelFactory(TaskRepository())).get(TaskViewModel::class.java)
        loadSavedList()
    }

    private fun loadSavedList() {
        viewModel.addSavedList(MainApplication().getTaskList().taskList)
    }

    private fun setAdapter() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = mAdapter
        }
        viewModel.mTaskResponse.observe(viewLifecycleOwner, { response ->
            mMainList = response
            setDataForDate()
        })
    }

    private fun setPageData() {
        binding.apply {
            leftArrowIv.setOnClickListener(onClickLeftArrow)
            rightArrowIv.setOnClickListener(onClickRightArrow)
            setDayParameters()
            viewModel.getTasks()
        }
    }

    private fun setDayParameters() {
        mCurrentDay = mDateFormat.format(Date())
        mDateCounter = mDateFormat.parse(mCurrentDay)

        val calendar = Calendar.getInstance()

        calendar.time = mDateCounter
        calendar.add(Calendar.DATE, 1)
        mFirstNextDay = mDateFormat.format(calendar.time)

        calendar.time = mDateCounter
        calendar.add(Calendar.DATE, -1)
        mFirstPrevDay = mDateFormat.format(calendar.time)
    }

    private fun setDataForDate() {
        mDateCounter = mCalendar.time
        val newDate = mDateFormat.format(mDateCounter)
        if (newDate.equals(mCurrentDay)) {
            binding.dayTv.text = StringKeys.TODAY_TEXT
        } else if (newDate.equals(mFirstNextDay)) {
            binding.dayTv.text = StringKeys.TOMORROW_TEXT
        } else if (newDate.equals(mFirstPrevDay)) {
            binding.dayTv.text = StringKeys.YESTERDAY_TEXT
        } else {
            binding.dayTv.text = newDate
        }

        val pageList: ArrayList<Task> = arrayListOf()
        for (item in mMainList) {
            if (item.targetDate == mDateFormat.format(mDateCounter)) {
                pageList.add(item)
            }
        }
        binding.emptyView.root.isVisible = pageList.isEmpty()
        mAdapter.setList(pageList)
    }

    private val onClickLeftArrow = View.OnClickListener {
        mCalendar.time = mDateCounter
        mCalendar.add(Calendar.DATE, -1)
        setDataForDate()
    }

    private val onClickRightArrow = View.OnClickListener {
        mCalendar.time = mDateCounter
        mCalendar.add(Calendar.DATE, 1)
        setDataForDate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(task: Task) {
        val intent = Intent(activity, TaskDetailsActivity::class.java);
        val bundle = Bundle()
        bundle.putParcelable(BundleData.TASK_DATA, task)
        intent.putExtras(bundle)
        detailsResultLauncher.launch(intent)
    }
}