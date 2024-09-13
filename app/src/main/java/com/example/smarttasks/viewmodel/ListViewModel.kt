package com.example.smarttasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smarttasks.model.Task
import com.example.smarttasks.model.TaskApiService
import com.example.smarttasks.model.TaskResponse
import com.example.smarttasks.util.simpleDate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val allTasks = ArrayList<Task>()
    val date by lazy { MutableLiveData<LocalDate>() }
    val tasks by lazy { MutableLiveData<List<Task>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()
    private val api: TaskApiService = TaskApiService()

    fun refresh() {
        loading.value = true
        if (allTasks.isEmpty()) {
            getTasks()
        } else {
            val filteredList = allTasks.filter { it.targetDate == date.value?.simpleDate() }
            tasks.value = filteredList.sortedWith(compareBy<Task> { -it.priority }.thenBy { it.dueDate })
            if (tasks.value.isNullOrEmpty()) {
                loadError.value = true
            }
            loading.value = false
            loadError.value = false
        }
    }

    private fun getTasks() {
        disposable.add(
            api.getTasks().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<TaskResponse>() {
                    override fun onSuccess(response: TaskResponse) {
                        loadError.value = false
                        loading.value = false
                        allTasks.addAll(response.tasks)
                        val filteredList = allTasks.filter { it.targetDate == date.value?.simpleDate() }
                        tasks.value = filteredList.sortedWith(compareBy<Task> { -it.priority }.thenBy { it.dueDate })
                        if (tasks.value.isNullOrEmpty()) {
                            loadError.value = true
                        }
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ListViewModel", "getTasks() callback error")
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                        tasks.value = null
                    }
                })
        )
    }

    fun previousDay() {
        date.value = date.value?.minusDays(1)
    }

    fun nextDay() {
        date.value = date.value?.plusDays(1)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}