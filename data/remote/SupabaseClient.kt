package com.example.cookaplication_a043302.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    // TODO: Substitui com as tuas credenciais do Supabase
    // Vai a https://supabase.com/dashboard -> Project Settings -> API
    private const val SUPABASE_URL = "https://edpxgryftsgsmvjdozxm.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_UaoziM7hMagqxEwjnmji2Q_1AbsrUCZ"

    val client by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Storage)
        }
    }
}
