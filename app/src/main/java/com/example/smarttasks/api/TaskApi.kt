package com.example.smarttasks.api

import retrofit2.http.GET

interface TaskApi {

    companion object {
        const val BASE_URL = "https://demo9094133.mockable.io/"
    }

    @GET("tasks")
    suspend fun getTasks(): TaskResponse
}