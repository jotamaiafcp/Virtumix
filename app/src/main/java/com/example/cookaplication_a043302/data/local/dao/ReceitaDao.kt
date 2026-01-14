package com.example.cookaplication_a043302.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd

@Dao
interface ReceitaDao {
    @Insert
    suspend fun insertReceita(receita: ReceitaBd): Long

    @Update
    suspend fun updateReceita(receita: ReceitaBd)

    @Delete
    suspend fun deleteReceita(receita: ReceitaBd)

    @Query("SELECT * FROM receitas WHERE receitaId = :id")
    suspend fun findReceitaById(id: Int): ReceitaBd?

    @Query("SELECT * FROM receitas WHERE titulo = :titulo")
    suspend fun findReceitaByTitulo(titulo: String): ReceitaBd?

    @Query("DELETE FROM receitas WHERE titulo = :titulo")
    suspend fun deleteReceitaByTitulo(titulo: String)

    @Query("SELECT * FROM receitas ORDER BY criadoEm DESC")
    fun getAllReceitas(): LiveData<List<ReceitaBd>>

    @Query("SELECT * FROM receitas WHERE categoria = :categoria ORDER BY titulo ASC")
    fun getReceitasByCategorias(categoria: String): LiveData<List<ReceitaBd>>

    @Query("SELECT * FROM receitas WHERE titulo LIKE '%' || :termo || '%' ORDER BY titulo ASC")
    fun searchReceitas(termo: String): LiveData<List<ReceitaBd>>
}

@Dao
interface IngredienteDao {
    @Insert
    suspend fun insertIngrediente(ingrediente: IngredienteBd): Long

    @Update
    suspend fun updateIngrediente(ingrediente: IngredienteBd)

    @Delete
    suspend fun deleteIngrediente(ingrediente: IngredienteBd)

    @Query("SELECT * FROM ingredientes WHERE ingredienteId = :id")
    suspend fun findIngredienteById(id: Int): IngredienteBd?

    @Query("SELECT * FROM ingredientes WHERE receitaId = :receitaId ORDER BY nome ASC")
    suspend fun getIngredientesByReceitaId(receitaId: Int): List<IngredienteBd>

    @Query("DELETE FROM ingredientes WHERE receitaId = :receitaId")
    suspend fun deleteIngredientesByReceitaId(receitaId: Int)
}

@Dao
interface PassoDao {
    @Insert
    suspend fun insertPasso(passo: PassoBd): Long

    @Update
    suspend fun updatePasso(passo: PassoBd)

    @Delete
    suspend fun deletePasso(passo: PassoBd)

    @Query("SELECT * FROM passos WHERE passoId = :id")
    suspend fun findPassoById(id: Int): PassoBd?

    @Query("SELECT * FROM passos WHERE receitaId = :receitaId ORDER BY ordem ASC")
    suspend fun getPassosByReceitaId(receitaId: Int): List<PassoBd>

    @Query("DELETE FROM passos WHERE receitaId = :receitaId")
    suspend fun deletePassosByReceitaId(receitaId: Int)
}
