package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookaplication_a043302.data.model.CookingStep
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class ExecutionViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val recipeId: String = savedStateHandle["recipeId"] ?: ""

    private val _steps = MutableStateFlow<List<CookingStep>>(emptyList())

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    val currentStep: StateFlow<CookingStep?> = _steps.map { steps ->
        steps.getOrNull(_currentStepIndex.value)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    val totalSteps: StateFlow<Int> = _steps.map { steps ->
        steps.size
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        if (recipeId.isNotEmpty()) {
            fetchRecipeSteps(recipeId)
        } else {
            loadMockSteps()
        }
    }

    private fun fetchRecipeSteps(id: String) {
        viewModelScope.launch {
            try {
                val recipe = RetrofitClient.instance.getRecipeDetails(id)
                _steps.value = recipe.toCookingSteps()
            } catch (e: Exception) {
                // Fallback to mock if fetch fails
                loadMockSteps()
            }
        }
    }

    private fun loadMockSteps() {
        _steps.value = listOf(
            CookingStep(1, "Coloque a cebola no copo e pique. 10 segundos", 10),
            CookingStep(2, "Adicione o azeite e os cogumelos. Refogue. 7 minutes", 420),
            CookingStep(3, "Adicione o arroz e o caldo. Cozinhe. 15 minutes", 900),
            CookingStep(4, "O seu risotto está pronto! Bom apetite.", null)
        )
    }

    fun nextStep() {
        if (_currentStepIndex.value < _steps.value.size - 1) {
            _currentStepIndex.value++
        }
    }

    fun previousStep() {
        if (_currentStepIndex.value > 0) {
            _currentStepIndex.value--
        }
    }
}