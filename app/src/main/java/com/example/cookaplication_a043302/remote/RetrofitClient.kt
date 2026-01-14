package com.example.cookaplication_a043302.remote

import com.example.cookaplication_a043302.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base da API Low Carb Recipes (RapidAPI)
    private const val BASE_URL = "https://low-carb-recipes.p.rapidapi.com/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}