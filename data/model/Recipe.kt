package com.example.cookaplication_a043302.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo principal de uma receita
 * Baseado na API Low Carb Recipes (RapidAPI)
 */
data class Recipe(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String,
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    @SerializedName("prepareTime")
    val prepareTime: Int? = null,
    @SerializedName("cookTime")
    val cookTime: Int? = null,
    @SerializedName("servings")
    val servings: Int? = null,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient> = emptyList(),
    @SerializedName("steps")
    val steps: List<String> = emptyList(),
    @SerializedName("nutrients")
    val nutrients: Nutrients? = null,
    @SerializedName("servingSizes")
    val servingSizes: List<ServingSize>? = null
) {
    /**
     * Converte steps (strings) para CookingStep (com ordem e timer)
     */
    fun toCookingSteps(): List<CookingStep> {
        return steps.mapIndexed { index, instruction ->
            CookingStep(
                order = index + 1,
                instruction = instruction,
                timerSeconds = extractTimerFromInstruction(instruction)
            )
        }
    }

    /**
     * Tenta extrair tempo em segundos da instrução de texto
     */
    private fun extractTimerFromInstruction(instruction: String): Int? {
        // Procura por padrões como "20 seconds", "2 minutes", "1:30"
        val secondsPattern = Regex("""(\d+)\s*(?:seconds?|secs?|s)""", RegexOption.IGNORE_CASE)
        val minutesPattern = Regex("""(\d+)\s*(?:minutes?|mins?|m)""", RegexOption.IGNORE_CASE)
        val timePattern = Regex("""(\d+):(\d+)""")

        return when {
            secondsPattern.find(instruction) != null -> {
                secondsPattern.find(instruction)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            }
            minutesPattern.find(instruction) != null -> {
                val minutes = minutesPattern.find(instruction)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                minutes * 60
            }
            timePattern.find(instruction) != null -> {
                val match = timePattern.find(instruction)
                val minutes = match?.groupValues?.get(1)?.toIntOrNull() ?: 0
                val seconds = match?.groupValues?.get(2)?.toIntOrNull() ?: 0
                minutes * 60 + seconds
            }
            else -> null
        }
    }
}

/**
 * Resposta paginada da API
 */
data class RecipeResponse(
    @SerializedName("results")
    val results: List<Recipe> = emptyList()
)

/**
 * Modelo de Ingrediente com detalhes de quantidade e medidas
 */
data class Ingredient(
    @SerializedName("name")
    val name: String,
    @SerializedName("qty")
    val quantity: Double? = null,
    @SerializedName("units")
    val units: String? = null,
    @SerializedName("desc")
    val description: String? = null,
    @SerializedName("grams")
    val grams: Double? = null,
    @SerializedName("servingSize")
    val servingSize: ServingSize? = null,
    @SerializedName("scale")
    val scale: Double? = null
)

/**
 * Tamanho de porção com conversão de escala
 */
data class ServingSize(
    @SerializedName("qty")
    val quantity: Double? = null,
    @SerializedName("units")
    val units: String? = null,
    @SerializedName("grams")
    val grams: Double? = null,
    @SerializedName("scale")
    val scale: Double? = null,
    @SerializedName("originalWeight")
    val originalWeight: Double? = null,
    @SerializedName("originalWeightUnits")
    val originalWeightUnits: String? = null
)

/**
 * Informação nutricional completa
 */
data class Nutrients(
    @SerializedName("caloriesKCal")
    val caloriesKCal: Double? = null,
    @SerializedName("caloriesKJ")
    val caloriesKJ: Double? = null,
    @SerializedName("totalCarbs")
    val totalCarbs: Double? = null,
    @SerializedName("netCarbs")
    val netCarbs: Double? = null,
    @SerializedName("fiber")
    val fiber: Double? = null,
    @SerializedName("sugar")
    val sugar: Double? = null,
    @SerializedName("addedSugar")
    val addedSugar: Double? = null,
    @SerializedName("protein")
    val protein: Double? = null,
    @SerializedName("fat")
    val fat: Double? = null,
    @SerializedName("saturatedFat")
    val saturatedFat: Double? = null,
    @SerializedName("cholesterol")
    val cholesterol: Double? = null,
    @SerializedName("sodium")
    val sodium: Double? = null,
    @SerializedName("potassium")
    val potassium: Double? = null,
    @SerializedName("calcium")
    val calcium: Double? = null,
    @SerializedName("iron")
    val iron: Double? = null,
    @SerializedName("vitaminA")
    val vitaminA: Double? = null,
    @SerializedName("vitaminC")
    val vitaminC: Double? = null,
    @SerializedName("vitaminD")
    val vitaminD: Double? = null,
    @SerializedName("vitaminB12")
    val vitaminB12: Double? = null
)

/**
 * Passo de receita com tempo opcional em segundos vindo da API.
 */
data class RecipeStep(
    @SerializedName("instruction")
    val instruction: String,
    @SerializedName("timer_seconds")
    val timerSeconds: Int? = null
)

/**
 * Modelo para Step de cozinha com suporte a extração de tempo
 * O JSON fornece steps como strings simples
 * Esta classe permite extrair tempos automaticamente do texto
 */
data class CookingStep(
    val order: Int,
    val instruction: String,
    val timerSeconds: Int? = null
) {
    companion object {
        /**
         * Extrai tempo em segundos do texto da instrução
         * Reconhece: "20 seconds", "1 minute", "2 minutes", "1:30", etc
         */
        fun extractTimerSeconds(instruction: String): Int? {
            return when {
                // Padrão: "X seconds" ou "X second"
                instruction.contains(Regex("""\d+\s*seconds?""", RegexOption.IGNORE_CASE)) -> {
                    val match = Regex("""(\d+)\s*seconds?""", RegexOption.IGNORE_CASE).find(instruction)
                    match?.groupValues?.get(1)?.toIntOrNull()
                }
                // Padrão: "X minutes" ou "X minute"
                instruction.contains(Regex("""\d+\s*minutes?""", RegexOption.IGNORE_CASE)) -> {
                    val match = Regex("""(\d+)\s*minutes?""", RegexOption.IGNORE_CASE).find(instruction)
                    match?.groupValues?.get(1)?.toIntOrNull()?.let { it * 60 }
                }
                // Padrão: "X:XX" (formato MM:SS)
                instruction.contains(Regex("""\d+:\d+""")) -> {
                    val match = Regex("""(\d+):(\d+)""").find(instruction)
                    if (match != null) {
                        val minutes = match.groupValues[1].toIntOrNull() ?: 0
                        val seconds = match.groupValues[2].toIntOrNull() ?: 0
                        minutes * 60 + seconds
                    } else null
                }
                else -> null
            }
        }

        fun from(recipeStep: RecipeStep, order: Int): CookingStep {
            val detected = recipeStep.timerSeconds ?: extractTimerSeconds(recipeStep.instruction)
            return CookingStep(order = order, instruction = recipeStep.instruction, timerSeconds = detected)
        }
    }
}

/**
 * Extensão para converter List<RecipeStep> em List<CookingStep>
 */
fun List<RecipeStep>.toCookingSteps(): List<CookingStep> {
    return this.mapIndexed { index, step ->
        CookingStep.from(step, index + 1)
    }
}