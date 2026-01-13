package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.remote.RetrofitClient
import com.example.cookaplication_a043302.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.cookaplication_a043302.data.local.FavoritesStore

class DashboardViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags: StateFlow<Set<String>> = _selectedTags

    private val _availableTags = MutableStateFlow<Set<String>>(emptySet())
    val availableTags: StateFlow<Set<String>> = _availableTags
    
    private val _allRecipes = mutableListOf<Recipe>()
    private val _pageSize = 4
    
    private val recipeRepository = RecipeRepository()

    val favorites: StateFlow<Set<String>> = FavoritesStore.favorites

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                Log.d("DashboardViewModel", "Iniciando fetch de receitas...")
                
                // Buscar receitas do Supabase
                val userRecipes = recipeRepository.getUserRecipes()
                Log.d("DashboardViewModel", "Receitas do Supabase: ${userRecipes.size}")
                
                // Buscar receitas da API
                val apiRecipes = try {
                    RetrofitClient.instance.searchRecipes(name = "a", limit = 20)
                } catch (e: Exception) {
                    Log.e("DashboardViewModel", "Erro API", e)
                    emptyList()
                }
                
                Log.d("DashboardViewModel", "Receitas da API: ${apiRecipes.size}")
                
                // Combinar receitas (Supabase primeiro)
                _allRecipes.clear()
                _allRecipes.addAll(userRecipes)
                _allRecipes.addAll(apiRecipes)
                
                _availableTags.value = _allRecipes.flatMap { it.tags }.filter { it.isNotBlank() }.toSet()
                _currentIndex.value = 0
                updatePagedRecipes()
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Erro ao buscar receitas", e)
                _error.value = "Erro ao carregar receitas: ${e.message}"
                _isLoading.value = false
                loadMockRecipes()
            }
        }
    }
    
    private fun filteredRecipes(): List<Recipe> {
        val query = _searchQuery.value.trim().lowercase()
        val tags = _selectedTags.value
        return _allRecipes.filter { recipe ->
            val matchesQuery = query.isBlank() || recipe.name.lowercase().contains(query)
            val matchesTags = tags.isEmpty() || recipe.tags.any { tags.contains(it) }
            matchesQuery && matchesTags
        }
    }

    private fun updatePagedRecipes() {
        val data = filteredRecipes()
        val startIndex = _currentIndex.value.coerceAtMost((data.size - 1).coerceAtLeast(0))
        val endIndex = (startIndex + _pageSize).coerceAtMost(data.size)
        _recipes.value = if (data.isEmpty()) emptyList() else data.subList(startIndex, endIndex)
    }
    
    fun nextPage() {
        val dataSize = filteredRecipes().size
        if ((_currentIndex.value + _pageSize) < dataSize) {
            _currentIndex.value += _pageSize
            updatePagedRecipes()
        }
    }
    
    fun previousPage() {
        if (_currentIndex.value >= _pageSize) {
            _currentIndex.value -= _pageSize
            updatePagedRecipes()
        }
    }
    
    fun canGoNext(): Boolean {
        return (_currentIndex.value + _pageSize) < filteredRecipes().size
    }
    
    fun canGoPrevious(): Boolean {
        return _currentIndex.value > 0
    }
    
    fun getCurrentPageInfo(): String {
        val dataSize = filteredRecipes().size
        val currentPage = if (dataSize == 0) 0 else (_currentIndex.value / _pageSize) + 1
        val totalPages = if (dataSize == 0) 0 else (dataSize + _pageSize - 1) / _pageSize
        return "Página $currentPage de $totalPages"
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _currentIndex.value = 0
        updatePagedRecipes()
    }

    fun toggleTag(tag: String) {
        val next = _selectedTags.value.toMutableSet().apply {
            if (contains(tag)) remove(tag) else add(tag)
        }
        _selectedTags.value = next
        _currentIndex.value = 0
        updatePagedRecipes()
    }

    fun clearFilters() {
        _selectedTags.value = emptySet()
        _searchQuery.value = ""
        _currentIndex.value = 0
        updatePagedRecipes()
    }

    fun toggleFavorite(recipeId: String) {
        FavoritesStore.toggle(recipeId)
    }
    
    fun refreshRecipes() {
        fetchRecipes()
    }
    
    private fun loadMockRecipes() {
        _allRecipes.clear()
        _allRecipes.addAll(listOf(
            Recipe(
                id = "rec_123",
                name = "Risotto de Cogumelos",
                image = "https://www.example.com/images/risotto.jpg",
                tags = listOf("prato-principal", "italiano"),
                ingredients = emptyList(),
                steps = listOf(
                    "Coloque a cebola no copo e pique. 10 segundos",
                    "Adicione o azeite e os cogumelos. Refogue. 7 minutes",
                    "Adicione o arroz e o caldo. Cozinhe sem o copo medidor. 15 minutes"
                )
            )
        ))
        _availableTags.value = _allRecipes.flatMap { it.tags }.toSet()
        _currentIndex.value = 0
        updatePagedRecipes()
    }
}