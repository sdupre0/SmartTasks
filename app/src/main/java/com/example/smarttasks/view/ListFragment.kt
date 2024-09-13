package com.example.smarttasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarttasks.R
import com.example.smarttasks.databinding.FragmentListBinding
import com.example.smarttasks.model.Task
import com.example.smarttasks.util.simpleDate
import com.example.smarttasks.util.today
import com.example.smarttasks.viewmodel.ListViewModel
import java.time.LocalDate

class ListFragment: Fragment(), TaskClickListener {

    private lateinit var listViewModel: ListViewModel
    private var binding: FragmentListBinding? = null
    private val listAdapter = TaskListAdapter(arrayListOf())

    private val dateLiveDataObserver = Observer<LocalDate> { newDate ->
        binding?.date = newDate.simpleDate()
        listViewModel.refresh()
    }

    private val taskListDataObserver = Observer<List<Task>> { list ->
        list.let {
            binding?.taskList?.visibility = View.VISIBLE
            listAdapter.updateTaskList(it)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        binding?.loadingView?.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding?.listEmptyText?.visibility = View.GONE
            binding?.taskList?.visibility = View.GONE
        }
    }

    private val errorLiveDataObserver = Observer<Boolean> { isError ->
        binding?.listEmptyText?.visibility = if (isError) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel = ViewModelProvider(this)[ListViewModel::class.java]
        listViewModel.date.observe(viewLifecycleOwner, dateLiveDataObserver)
        listViewModel.tasks.observe(viewLifecycleOwner, taskListDataObserver)
        listViewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        listViewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)

        binding?.taskList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        binding?.date = today.simpleDate()
        binding?.listener = this

        binding?.swipeLayout?.setOnRefreshListener {
            binding?.taskList?.visibility = View.GONE
            binding?.listEmptyText?.visibility = View.GONE
            binding?.loadingView?.visibility = View.VISIBLE

            listViewModel.refresh()
            binding?.swipeLayout?.isRefreshing = false
        }

        listViewModel.date.value = today
    }

    override fun onClick(v: View) {
        if (v.id == R.id.nav_prev) listViewModel.previousDay()
        if (v.id == R.id.nav_next) listViewModel.nextDay()
    }
}