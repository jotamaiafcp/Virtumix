package com.example.cookaplication_a043302.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookaplication_a043302.data.local.dao.ReceitaDao
import com.example.cookaplication_a043302.data.local.dao.IngredienteDao
import com.example.cookaplication_a043302.data.local.dao.PassoDao
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd
import com.example.cookaplication_a043302.data.local.entities.ReceitaComDetalhes
import kotlinx.coroutines.*

const val APP_TAG = "cookaplication"

class ReceitaRepository(
    private val receitaDao: ReceitaDao,
    private val ingredienteDao: IngredienteDao,
    private val passoDao: PassoDao
) {
    val allReceitas: LiveData<List<ReceitaBd>> = receitaDao.getAllReceitas()
    val searchResults = MutableLiveData<List<ReceitaBd>>()
    val receitaComDetalhes = MutableLiveData<ReceitaComDetalhes>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Receita operations
    fun insertReceita(receita: ReceitaBd, ingredientes: List<IngredienteBd>, passos: List<PassoBd>) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val receitaId = receitaDao.insertReceita(receita).toInt()

                // Insert ingredientes com receitaId
                ingredientes.forEach { ingrediente ->
                    ingredienteDao.insertIngrediente(ingrediente.copy(receitaId = receitaId))
                }

                // Insert passos com receitaId
                passos.forEach { passo ->
                    passoDao.insertPasso(passo.copy(receitaId = receitaId))
                }

                Log.i(APP_TAG, "Receita '$receitaId' criada com sucesso")
            } catch (e: Exception) {
                Log.e(APP_TAG, "ERRO ao criar Receita: ${e.message}")
            }
        }
    }

    fun updateReceita(receita: ReceitaBd) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                receitaDao.updateReceita(receita)
                Log.i(APP_TAG, "Receita '${receita.titulo}' atualizada com sucesso")
            } catch (e: Exception) {
                Log.e(APP_TAG, "ERRO ao atualizar Receita: ${e.message}")
            }
        }
    }

    fun deleteReceita(receita: ReceitaBd) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                // Apagar ingredientes e passos automaticamente (CASCADE)
                receitaDao.deleteReceita(receita)
                Log.i(APP_TAG, "Receita '${receita.titulo}' apagada com sucesso")
            } catch (e: Exception) {
                Log.e(APP_TAG, "ERRO ao apagar Receita: ${e.message}")
            }
        }
    }

    fun deleteReceitaByTitulo(titulo: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                receitaDao.deleteReceitaByTitulo(titulo)
                Log.i(APP_TAG, "Receita '$titulo' apagada com sucesso")
            } catch (e: Exception) {
                Log.e(APP_TAG, "ERRO ao apagar Receita: ${e.message}")
            }
        }
    }

    fun findReceitaById(id: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            receitaComDetalhes.value = asyncFindReceitaById(id).await()
        }
    }

    private fun asyncFindReceitaById(id: Int): Deferred<ReceitaComDetalhes?> =
        coroutineScope.async(Dispatchers.IO) {
            val receita = receitaDao.findReceitaById(id) ?: return@async null
            val ingredientes = ingredienteDao.getIngredientesByReceitaId(id)
            val passos = passoDao.getPassosByReceitaId(id)
            return@async ReceitaComDetalhes(receita, ingredientes, passos)
        }

    fun searchReceitas(termo: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncSearchReceitas(termo).await()
        }
    }

    private fun asyncSearchReceitas(termo: String): Deferred<List<ReceitaBd>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async receitaDao.searchReceitas(termo).value ?: emptyList()
        }

    fun getReceitasByCategorias(categoria: String): LiveData<List<ReceitaBd>> {
        return receitaDao.getReceitasByCategorias(categoria)
    }

    // Ingrediente operations
    suspend fun insertIngrediente(ingrediente: IngredienteBd) {
        try {
            ingredienteDao.insertIngrediente(ingrediente)
        } catch (e: Exception) {
            Log.e(APP_TAG, "ERRO ao criar Ingrediente: ${e.message}")
        }
    }

    suspend fun deleteIngrediente(ingrediente: IngredienteBd) {
        try {
            ingredienteDao.deleteIngrediente(ingrediente)
        } catch (e: Exception) {
            Log.e(APP_TAG, "ERRO ao apagar Ingrediente: ${e.message}")
        }
    }

    // Passo operations
    suspend fun insertPasso(passo: PassoBd) {
        try {
            passoDao.insertPasso(passo)
        } catch (e: Exception) {
            Log.e(APP_TAG, "ERRO ao criar Passo: ${e.message}")
        }
    }

    suspend fun deletePasso(passo: PassoBd) {
        try {
            passoDao.deletePasso(passo)
        } catch (e: Exception) {
            Log.e(APP_TAG, "ERRO ao apagar Passo: ${e.message}")
        }
    }
}
