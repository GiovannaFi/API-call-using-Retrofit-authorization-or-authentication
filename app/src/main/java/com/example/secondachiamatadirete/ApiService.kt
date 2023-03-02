package com.example.secondachiamatadirete

import retrofit2.http.GET

interface ApiService {
    @GET("/statistics")
    suspend fun getDetails() : Data
}