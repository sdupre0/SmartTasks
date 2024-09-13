package com.example.smarttasks.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.smarttasks.R
import com.example.smarttasks.databinding.FragmentDetailBinding
import com.example.smarttasks.model.Task
import com.example.smarttasks.util.SharedPreferencesHelper
import com.example.smarttasks.util.Status
import com.example.smarttasks.util.setColor
import com.example.smarttasks.viewmodel.DetailViewModel
import androidx.lifecycle.Observer

class DetailFragment: Fragment(), TaskClickListener {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var context: Context
    private lateinit var task: Task
    private lateinit var prefs: SharedPreferencesHelper

    private val commentLiveDataObserver = Observer<String> { comment ->
        if (comment.isNotEmpty()) {
            binding.commentBox.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            task = DetailFragmentArgs.fromBundle(it).task
        }

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.comment.observe(viewLifecycleOwner, commentLiveDataObserver)

        context = view.context
        prefs = SharedPreferencesHelper(context)
        val status = prefs.getTaskStatus(task.id!!)

        binding.task = task
        binding.status = status
        binding.comment = prefs.getTaskComment(task.id!!)
        binding.listener = this

        setTaskDetailTextColor(status, context)
    }

    private fun setTaskDetailTextColor(status: Status, context: Context) {
        when (status) {
            Status.Unresolved -> {
                binding.taskTitle.setColor(context, R.color.task_red)
                binding.taskDue.setColor(context, R.color.task_red)
                binding.daysLeft.setColor(context, R.color.task_red)
                binding.taskStatus.setColor(context, R.color.task_orange)
            }
            Status.CantResolve -> {
                binding.taskTitle.setColor(context, R.color.task_red)
                binding.taskDue.setColor(context, R.color.task_red)
                binding.daysLeft.setColor(context, R.color.task_red)
                binding.taskStatus.setColor(context, R.color.task_red)
            }
            Status.Resolved -> {
                binding.taskTitle.setColor(context, R.color.task_green)
                binding.taskDue.setColor(context, R.color.task_green)
                binding.daysLeft.setColor(context, R.color.task_green)
                binding.taskStatus.setColor(context, R.color.task_green)
            }
        }
    }

    private fun closeTask(status: Status, comment: String) {
        binding.status = status
        prefs.saveTaskStatus(task.id!!, status)
        if (comment.isNotEmpty()) {
            detailViewModel.comment.value = comment
            prefs.saveTaskComment(task.id!!, comment)
        }
        setTaskDetailTextColor(status, context)
        binding.buttonsBar.visibility = View.GONE
        if (status == Status.Resolved) {
            binding.resolvedImg.visibility = View.VISIBLE
        } else {
            binding.cantImg.visibility = View.VISIBLE
        }
    }

    private fun showCommentDialogs(status: Status) {
        val commentEdit = EditText(context)
        val commentDialog = AlertDialog.Builder(requireContext())
            .setView(commentEdit)
            .setPositiveButton("Close Task") { _,_ ->
                closeTask(status, commentEdit.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(true)
            .create()

        val queryDialog = AlertDialog.Builder(requireContext())
            .setMessage("Would you like to leave a comment?")
            .setPositiveButton("Yes") { _,_ -> commentDialog.show() }
            .setNegativeButton("No") { _,_ ->
                closeTask(status, "")
            }
            .setCancelable(true)
            .create()

        queryDialog.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nav_prev -> {
                val action = DetailFragmentDirections.actionList()
                Navigation.findNavController(v).navigate(action)
            }
            R.id.resolve_btn -> showCommentDialogs(Status.Resolved)
            R.id.cant_resolve_btn -> showCommentDialogs(Status.CantResolve)
        }
    }
}