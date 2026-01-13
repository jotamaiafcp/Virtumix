package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    // Use graceful fallback to avoid crashes when the nav argument is missing
    private val recipeId: String = savedStateHandle["recipeId"] ?: ""

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    init {
        if (recipeId.isNotEmpty()) {
            fetchRecipeDetails(recipeId)
        } else {
            loadMockRecipeDetails("mock")
        }
    }

    private fun fetchRecipeDetails(id: String) {
        viewModelScope.launch {
            try {
                _recipe.value = RetrofitClient.instance.getRecipeDetails(id)
            } catch (e: Exception) {
                // Handle error
                 e.printStackTrace()
            }
        }
    }

    private fun loadMockRecipeDetails(id: String) {
        _recipe.value = Recipe(
            id = id,
            name = "Risotto de Cogumelos Bimby Style",
            image = "https://www.example.com/images/risotto.jpg",
            tags = listOf("prato-principal"),
            ingredients = emptyList(),
            steps = listOf(
                "Coloque a cebola no copo e pique. 10 segundos",
                "Adicione o azeite e os cogumelos. Refogue. 7 minutes",
                "Adicione o arroz e o caldo. Cozinhe sem o copo medidor. 15 minutes"
            )
        )
    }
}