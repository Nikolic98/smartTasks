package com.example.smarttasks.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TaskApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TaskApi by lazy {
        retrofit.create(TaskApi::class.java)
    }
}