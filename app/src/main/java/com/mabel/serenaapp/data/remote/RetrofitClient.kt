package com.mabel.serenaapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Direccion del servidor en AlwaysData (tiene que terminar en /)
const val BASE_URL = "https://mabelcastro.alwaysdata.net/serena/"

/*
 * Un solo Retrofit para toda la app (object = singleton).
 * Cada tabla tiene su propio Service, igual que en Loki.
 */
object RetrofitClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val categoriasService: CategoriasService by lazy {
        retrofit.create(CategoriasService::class.java)
    }

    val meditacionesService: MeditacionesService by lazy {
        retrofit.create(MeditacionesService::class.java)
    }
}
