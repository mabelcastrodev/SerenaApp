package com.mabel.serenaapp.data.remote

import com.mabel.serenaapp.models.Categoria
import retrofit2.http.GET

interface CategoriasService {

    @GET("categorias.php")
    suspend fun getCategorias(): List<Categoria>
}
