package com.example.cookaplication_a043302.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base da API Low Carb Recipes (RapidAPI)
    private const val BASE_URL = "https://low-carb-recipes.p.rapidapi.com/"
    private const val RAPIDAPI_HOST = "low-carb-recipes.p.rapidapi.com"
    private const val RAPIDAPI_KEY = "74ab3691a5mshf34e8ba4f7e4291p15230fjsn8b7f3af106d3"

    private val httpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("x-rapidapi-host", RAPIDAPI_HOST)
                    .header("x-rapidapi-key", RAPIDAPI_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
