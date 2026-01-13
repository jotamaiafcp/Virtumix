package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import android.util.Log

data class DayPlan(
    val dayOfWeek: DayOfWeek,
    val recipe: Recipe? = null
)

class PlannerViewModel : ViewModel() {
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes
    
    private val _dayPlans = MutableStateFlow<Map<DayOfWeek, Recipe?>>(
        mapOf(
            DayOfWeek.MONDAY to null,
            DayOfWeek.TUESDAY to null,
            DayOfWeek.WEDNESDAY to null,
            DayOfWeek.THURSDAY to null,
            DayOfWeek.FRIDAY to null,
            DayOfWeek.SATURDAY to null,
            DayOfWeek.SUNDAY to null
        )
    )
    val dayPlans: StateFlow<Map<DayOfWeek, Recipe?>> = _dayPlans
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("PlannerViewModel", "Iniciando fetch de receitas...")
                val recipes = RetrofitClient.instance.searchRecipes(name = "a", limit = 20)
                Log.d("PlannerViewModel", "Receitas carregadas: ${recipes.size}")
                _allRecipes.value = recipes
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("PlannerViewModel", "Erro ao buscar receitas", e)
                _isLoading.value = false
                loadMockRecipes()
            }
        }
    }
    
    private fun loadMockRecipes() {
        _allRecipes.value = listOf(
            Recipe(
                id = "mock_1",
                name = "Risotto de Cogumelos",
                image = "https://www.example.com/images/risotto.jpg",
                tags = listOf("prato-principal", "italiano"),
                ingredients = emptyList(),
                steps = listOf(
                    "Coloque a cebola no copo e pique. 10 segundos",
                    "Adicione o azeite e os cogumelos. Refogue. 7 minutes",
                    "Adicione o arroz e o caldo. Cozinhe. 15 minutes"
                )
            )
        )
    }
    
    fun assignRecipeToDay(dayOfWeek: DayOfWeek, recipe: Recipe?) {
        val currentPlans = _dayPlans.value.toMutableMap()
        currentPlans[dayOfWeek] = recipe
        _dayPlans.value = currentPlans
        Log.d("PlannerViewModel", "Receita atribuída a ${dayOfWeek.name}: ${recipe?.name ?: "Nenhuma"}")
    }
    
    fun getRecipeForDay(dayOfWeek: DayOfWeek): Recipe? {
        return _dayPlans.value[dayOfWeek]
    }
}
