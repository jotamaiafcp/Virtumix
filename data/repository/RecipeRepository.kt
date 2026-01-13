package com.example.cookaplication_a043302.data.repository

import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.model.UserRecipe
import com.example.cookaplication_a043302.data.model.toRecipe
import com.example.cookaplication_a043302.data.model.toUserRecipe
import com.example.cookaplication_a043302.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import android.util.Log

class RecipeRepository {
    
    private val supabase = SupabaseClient.client
    
    /**
     * Busca receitas criadas pelo utilizador no Supabase
     */
    suspend fun getUserRecipes(userId: String? = null): List<Recipe> {
        return try {
            val query = if (userId != null) {
                supabase.from("recipes").select {
                    filter {
                        eq("user_id", userId)
                    }
                }
            } else {
                supabase.from("recipes").select()
            }
            
            query.decodeList<UserRecipe>().map { it.toRecipe() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Cria nova receita no Supabase
     */
    suspend fun createRecipe(recipe: Recipe, userId: String): Recipe? {
        return try {
            val userRecipe = recipe.toUserRecipe(userId)
            Log.d("RecipeRepository", "Criando receita: ${userRecipe.name}")
            
            supabase.from("recipes").insert(userRecipe)
            
            Log.d("RecipeRepository", "Receita criada com sucesso")
            userRecipe.toRecipe()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Erro ao criar receita: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Atualiza receita existente
     */
    suspend fun updateRecipe(recipeId: String, recipe: Recipe): Boolean {
        return try {
            supabase.from("recipes")
                .update(recipe.toUserRecipe()) {
                    filter {
                        eq("id", recipeId)
                    }
                }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Remove receita
     */
    suspend fun deleteRecipe(recipeId: String): Boolean {
        return try {
            supabase.from("recipes")
                .delete {
                    filter {
                        eq("id", recipeId)
                    }
                }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Busca receita específica por ID
     */
    suspend fun getRecipeById(recipeId: String): Recipe? {
        return try {
            val result = supabase.from("recipes")
                .select {
                    filter {
                        eq("id", recipeId)
                    }
                }
                .decodeSingle<UserRecipe>()
            
            result.toRecipe()
        } catch (e: Exception) {
            null
        }
    }
}
