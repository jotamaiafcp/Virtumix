package com.example.cookaplication_a043302.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "weekly_plan")
data class PlannerEntry(

    @PrimaryKey val dayOfWeek: Int,
    val recipeId: String,
    val recipeName: String? = null,
    val recipeImage: String? = null
) {
    companion object {
        fun from(day: DayOfWeek, recipeId: String, recipeName: String?, recipeImage: String?): PlannerEntry {
            return PlannerEntry(dayOfWeek = day.value, recipeId = recipeId, recipeName = recipeName, recipeImage = recipeImage)
        }
    }
}
