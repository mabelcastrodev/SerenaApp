package com.mabel.serenaapp.data.remote

import com.mabel.serenaapp.models.Meditacion
import retrofit2.http.GET
import retrofit2.http.Query

interface MeditacionesService {

    @GET("meditaciones.php")
    suspend fun getMeditaciones(
        @Query("idcategoria") idcategoria: Int?
    ): List<Meditacion>

    // meditaciones.php?idmeditacion=5  (devuelve una lista de 1)
    @GET("meditaciones.php")
    suspend fun getMeditacion(
        @Query("idmeditacion") idmeditacion: Int
    ): List<Meditacion>
}
