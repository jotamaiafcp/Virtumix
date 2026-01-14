package com.example.cookaplication_a043302.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

// Tabela Receitas
@Entity(
    tableName = "receitas",
    indices = [Index(value = ["titulo"], unique = true)]
)
data class ReceitaBd(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receitaId")
    val idReceita: Int = 0,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "descricao")
    val descricao: String,

    @ColumnInfo(name = "categoria")
    val categoria: String = "",

    @ColumnInfo(name = "pessoasServidas")
    val pessoasServidas: Int = 1,

    @ColumnInfo(name = "tempoPreparacao")
    val tempoPreparacao: Int = 0, // em minutos

    @ColumnInfo(name = "tempoCozimento")
    val tempoCozimento: Int = 0, // em minutos

    @ColumnInfo(name = "dificuldade")
    val dificuldade: String = "Médio", // Fácil, Médio, Difícil

    @ColumnInfo(name = "criadoEm")
    val criadoEm: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "atualizadoEm")
    val atualizadoEm: Long = System.currentTimeMillis()
)

// Tabela Ingredientes
@Entity(
    tableName = "ingredientes",
    foreignKeys = [
        ForeignKey(
            entity = ReceitaBd::class,
            parentColumns = ["receitaId"],
            childColumns = ["receitaId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("receitaId")]
)
data class IngredienteBd(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredienteId")
    val idIngrediente: Int = 0,

    @ColumnInfo(name = "receitaId")
    val receitaId: Int,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "quantidade")
    val quantidade: Double,

    @ColumnInfo(name = "unidade")
    val unidade: String // g, ml, colher, chávena, etc.
)

// Tabela Passos
@Entity(
    tableName = "passos",
    foreignKeys = [
        ForeignKey(
            entity = ReceitaBd::class,
            parentColumns = ["receitaId"],
            childColumns = ["receitaId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("receitaId")]
)
data class PassoBd(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "passoId")
    val idPasso: Int = 0,

    @ColumnInfo(name = "receitaId")
    val receitaId: Int,

    @ColumnInfo(name = "ordem")
    val ordem: Int,

    @ColumnInfo(name = "instrucao")
    val instrucao: String,

    @ColumnInfo(name = "duracao")
    val duracao: Int = 0 // em minutos, opcional
)

// DTO para retornar Receita com seus Ingredientes e Passos
data class ReceitaComDetalhes(
    val receita: ReceitaBd,
    val ingredientes: List<IngredienteBd> = emptyList(),
    val passos: List<PassoBd> = emptyList()
)
