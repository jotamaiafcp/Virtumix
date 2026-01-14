package com.example.cookaplication_a043302.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookaplication_a043302.data.local.database.ReceitaRoomDatabase
import com.example.cookaplication_a043302.data.repository.ReceitaRepository
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd
import com.example.cookaplication_a043302.data.local.entities.ReceitaComDetalhes

class ReceitaViewModel(application: Application) : ViewModel() {
    val allReceitas: LiveData<List<ReceitaBd>>
    val searchResults: MutableLiveData<List<ReceitaBd>>
    val receitaComDetalhes: MutableLiveData<ReceitaComDetalhes>
    
    private val repository: ReceitaRepository

    init {
        val database = ReceitaRoomDatabase.getInstance(application)
        val receitaDao = database.receitaDao()
        val ingredienteDao = database.ingredienteDao()
        val passoDao = database.passoDao()

        repository = ReceitaRepository(receitaDao, ingredienteDao, passoDao)

        allReceitas = repository.allReceitas
        searchResults = repository.searchResults
        receitaComDetalhes = repository.receitaComDetalhes
    }

    fun insertReceita(receita: ReceitaBd, ingredientes: List<IngredienteBd> = emptyList(), passos: List<PassoBd> = emptyList()) {
        repository.insertReceita(receita, ingredientes, passos)
    }

    fun updateReceita(receita: ReceitaBd) {
        repository.updateReceita(receita)
    }

    fun deleteReceita(receita: ReceitaBd) {
        repository.deleteReceita(receita)
    }

    fun deleteReceitaByTitulo(titulo: String) {
        repository.deleteReceitaByTitulo(titulo)
    }

    fun findReceitaById(id: Int) {
        repository.findReceitaById(id)
    }

    fun searchReceitas(termo: String) {
        repository.searchReceitas(termo)
    }

    fun getReceitasByCategorias(categoria: String): LiveData<List<ReceitaBd>> {
        return repository.getReceitasByCategorias(categoria)
    }
}

class ReceitaViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReceitaViewModel(application) as T
    }
}
