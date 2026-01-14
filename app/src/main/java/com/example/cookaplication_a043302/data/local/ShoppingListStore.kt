package com.example.cookaplication_a043302.data.local

import com.example.cookaplication_a043302.data.model.ShoppingItem
import com.example.cookaplication_a043302.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Armazena lista de compras em memória. Trocar para DataStore/Room numa próxima iteração.
 */
object ShoppingListStore {
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items

    fun addItem(name: String, quantity: String? = null) {
        val newItem = ShoppingItem(id = UUID.randomUUID().toString(), name = name, quantity = quantity)
        _items.value = _items.value + newItem
    }

    fun toggleChecked(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id) item.copy(checked = !item.checked) else item
        }
    }

    fun remove(id: String) {
        _items.value = _items.value.filterNot { it.id == id }
    }

    fun clear() {
        _items.value = emptyList()
    }

    fun importFromRecipes(recipes: List<Recipe>) {
        val newItems = recipes
            .flatMap { it.ingredients }
            .mapNotNull { ingredient ->
                ingredient.name.takeIf { it.isNotBlank() }?.let { name ->
                    val qty = listOfNotNull(ingredient.quantity?.toString(), ingredient.units).joinToString(" ").ifBlank { null }
                    ShoppingItem(id = UUID.randomUUID().toString(), name = name, quantity = qty)
                }
            }
        _items.value = (_items.value + newItems).distinctBy { it.name.lowercase() }
    }
}
