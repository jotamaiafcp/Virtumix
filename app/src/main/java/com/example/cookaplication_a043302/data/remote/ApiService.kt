package com.example.cookaplication_a043302.data.remote

import com.example.cookaplication_a043302.data.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Endpoint /search retorna um array direto de receitas
    @GET("search")
    suspend fun searchRecipes(
        @Query("name") name: String = "food",
        @Query("limit") limit: Int = 20
    ): List<Recipe>

    @GET("recipes/{id}")
    suspend fun getRecipeDetails(@Path("id") recipeId: String): Recipe
}