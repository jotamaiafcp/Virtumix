package com.example.cookaplication_a043302.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * In-memory favoritos (poderia ser DataStore/Room no futuro).
 */
object FavoritesStore {
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    fun toggle(id: String) {
        val next = _favorites.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }
        _favorites.value = next
    }

    fun isFavorite(id: String): Boolean = _favorites.value.contains(id)
}
