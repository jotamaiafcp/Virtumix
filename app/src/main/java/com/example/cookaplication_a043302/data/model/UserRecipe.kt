package com.example.cookaplication_a043302.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


fun UserRecipe.toRecipe(): Recipe {
    return Recipe(
        id = id ?: "",
        name = name,
        description = description,
        image = imageUrl ?: "",
        tags = tags,
        prepareTime = prepareTime,
        cookTime = cookTime,
        servings = servings,
        ingredients = ingredients.map { 
            Ingredient(name = it, quantity = null, units = null)
        },
        steps = steps
    )
}

/**
 * Extensão para converter Recipe em UserRecipe
 */
fun Recipe.toUserRecipe(userId: String? = null): UserRecipe {
    return UserRecipe(
        id = id,
        name = name,
        description = description,
        imageUrl = image,
        tags = tags,
        prepareTime = prepareTime,
        cookTime = cookTime,
        servings = servings,
        ingredients = ingredients.map { "${it.quantity ?: ""} ${it.units ?: ""} ${it.name}".trim() },
        steps = steps,
        userId = userId
    )
}

/**
 * Receita armazenada no Supabase (criada pelo utilizador)
 */
@Serializable
data class UserRecipe(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    @SerialName("prepare_time")
    val prepareTime: Int? = null,
    @SerialName("cook_time")
    val cookTime: Int? = null,
    val servings: Int? = null,
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
