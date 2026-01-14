package com.example.cookaplication_a043302.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

object ViewModelFactory {
    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val savedStateHandle = extras.createSavedStateHandle()
            
            return when {
                modelClass.isAssignableFrom(RecipeDetailViewModel::class.java) -> {
                    RecipeDetailViewModel(savedStateHandle) as T
                }
                modelClass.isAssignableFrom(ExecutionViewModel::class.java) -> {
                    ExecutionViewModel(savedStateHandle) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
}