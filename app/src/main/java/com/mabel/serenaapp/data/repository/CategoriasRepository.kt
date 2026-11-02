package com.mabel.serenaapp.data.repository

import com.mabel.serenaapp.data.remote.CategoriasService
import com.mabel.serenaapp.data.remote.RetrofitClient
import com.mabel.serenaapp.models.Categoria

/*
 * El ViewModel no sabe de Retrofit: le pide los datos al
 * Repository y este se los pide al Service.
 */
class CategoriasRepository(
    private val service: CategoriasService = RetrofitClient.categoriasService
) {
    suspend fun getCategorias(): List<Categoria> = service.getCategorias()
}
