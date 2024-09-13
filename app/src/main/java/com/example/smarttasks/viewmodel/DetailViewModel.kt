package com.example.smarttasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val comment by lazy { MutableLiveData<String>()  }
}