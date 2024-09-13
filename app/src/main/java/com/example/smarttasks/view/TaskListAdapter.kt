package com.example.smarttasks.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttasks.R
import com.example.smarttasks.databinding.ItemTaskBinding
import com.example.smarttasks.model.Task
import com.example.smarttasks.util.SharedPreferencesHelper
import com.example.smarttasks.util.Status
import com.example.smarttasks.util.setColor

class TaskListAdapter(private val taskList: ArrayList<Task>): RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>(), TaskClickListener {

    class TaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemTaskBinding>(inflater, R.layout.item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.task = taskList[position]
        holder.binding.listener = this

        val prefs = SharedPreferencesHelper(context)
        val status = prefs.getTaskStatus(holder.binding.task?.id!!)

        when (status) {
            Status.Unresolved -> {
                setTaskTextColor(context, holder, R.color.task_red)
                holder.binding.statusIcon.visibility = View.GONE
            }
            Status.CantResolve -> {
                setTaskTextColor(context, holder, R.color.task_red)
                holder.binding.statusIcon.visibility = View.VISIBLE
                holder.binding.cantIcon.visibility = View.VISIBLE
                holder.binding.resolvedIcon.visibility = View.GONE
            }
            Status.Resolved -> {
                setTaskTextColor(context, holder, R.color.task_green)
                holder.binding.statusIcon.visibility = View.VISIBLE
                holder.binding.resolvedIcon.visibility = View.VISIBLE
                holder.binding.cantIcon.visibility = View.GONE
            }
        }
    }

    fun updateTaskList(newTaskList: List<Task>) {
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyItemRangeChanged(0, taskList.size)
    }

    private fun setTaskTextColor(context: Context, holder: TaskViewHolder, colorId: Int) {
        holder.binding.taskTitle.setColor(context, colorId)
        holder.binding.taskDue.setColor(context, colorId)
        holder.binding.daysLeft.setColor(context, colorId)
    }

    override fun onClick(v: View) {
        for (task in taskList) {
            if (v.tag == task.id) {
                val action = ListFragmentDirections.actionDetail(task)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }
}