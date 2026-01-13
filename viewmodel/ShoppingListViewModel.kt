package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cookaplication_a043302.data.local.ShoppingListStore
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.data.model.ShoppingItem
import kotlinx.coroutines.flow.StateFlow

class ShoppingListViewModel : ViewModel() {
    val items: StateFlow<List<ShoppingItem>> = ShoppingListStore.items

    fun addItem(name: String, quantity: String? = null) {
        if (name.isNotBlank()) ShoppingListStore.addItem(name.trim(), quantity?.takeIf { it.isNotBlank() })
    }

    fun toggleChecked(id: String) = ShoppingListStore.toggleChecked(id)

    fun remove(id: String) = ShoppingListStore.remove(id)

    fun clear() = ShoppingListStore.clear()

    fun importFromRecipes(recipes: List<Recipe>) = ShoppingListStore.importFromRecipes(recipes)
}
