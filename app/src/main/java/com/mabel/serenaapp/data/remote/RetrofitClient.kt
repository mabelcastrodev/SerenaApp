package com.mabel.serenaapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://mabelcastro.alwaysdata.net/serena/"

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
