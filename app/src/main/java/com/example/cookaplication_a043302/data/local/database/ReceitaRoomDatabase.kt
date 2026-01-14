package com.example.cookaplication_a043302.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cookaplication_a043302.data.local.dao.ReceitaDao
import com.example.cookaplication_a043302.data.local.dao.IngredienteDao
import com.example.cookaplication_a043302.data.local.dao.PassoDao
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd

@Database(
    entities = [ReceitaBd::class, IngredienteBd::class, PassoBd::class],
    version = 1,
    exportSchema = false
)
abstract class ReceitaRoomDatabase : RoomDatabase() {
    abstract fun receitaDao(): ReceitaDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun passoDao(): PassoDao

    companion object {
        @Volatile
        private var INSTANCE: ReceitaRoomDatabase? = null

        fun getInstance(context: Context): ReceitaRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReceitaRoomDatabase::class.java,
                        "receita_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
