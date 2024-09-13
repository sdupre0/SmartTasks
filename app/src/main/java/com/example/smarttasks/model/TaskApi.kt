package com.example.smarttasks.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface TaskApi {
    @GET(".")
    fun getTasks(): Single<TaskResponse>
}