package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookaplication_a043302.data.model.Ingredient
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.util.UUID

class AddRecipeViewModel : ViewModel() {
    private val repository = RecipeRepository()
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving
    
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun saveRecipe(
        name: String,
        description: String,
        imageUrl: String,
        tags: List<String>,
        prepareTime: Int?,
        cookTime: Int?,
        servings: Int?,
        ingredientsText: String,
        stepsText: String
    ) {
        viewModelScope.launch {
            try {
                _isSaving.value = true
                _error.value = null
                
                val ingredients = ingredientsText.lines()
                    .filter { it.isNotBlank() }
                    .map { Ingredient(name = it.trim()) }
                
                val steps = stepsText.lines()
                    .filter { it.isNotBlank() }
                    .map { it.trim() }
                
                val recipe = Recipe(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    description = description.takeIf { it.isNotBlank() },
                    image = imageUrl,
                    tags = tags,
                    prepareTime = prepareTime,
                    cookTime = cookTime,
                    servings = servings,
                    ingredients = ingredients,
                    steps = steps
                )
                
                Log.d("AddRecipeViewModel", "Iniciando save: $name")
                Log.d("AddRecipeViewModel", "ID: ${recipe.id}")
                Log.d("AddRecipeViewModel", "Tags: $tags")
                Log.d("AddRecipeViewModel", "Ingredients: $ingredients")
                Log.d("AddRecipeViewModel", "Steps: $steps")
                val result = repository.createRecipe(recipe, userId = "guest")
                
                Log.d("AddRecipeViewModel", "Resultado: $result")
                if (result != null) {
                    _saveSuccess.value = true
                } else {
                    _error.value = "Erro ao guardar receita no servidor"
                    Log.e("AddRecipeViewModel", "Resultado nulo")
                }
                
                _isSaving.value = false
            } catch (e: Exception) {
                Log.e("AddRecipeViewModel", "Erro ao guardar receita", e)
                _error.value = "Erro: ${e.message ?: "Erro desconhecido"}"
                _isSaving.value = false
            }
        }
    }
    
    fun resetState() {
        _saveSuccess.value = false
        _error.value = null
    }
}
