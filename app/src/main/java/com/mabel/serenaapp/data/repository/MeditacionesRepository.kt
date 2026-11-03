package com.mabel.serenaapp.data.repository

import com.mabel.serenaapp.data.remote.MeditacionesService
import com.mabel.serenaapp.data.remote.RetrofitClient
import com.mabel.serenaapp.models.Meditacion

class MeditacionesRepository(
    private val service: MeditacionesService = RetrofitClient.meditacionesService
) {

    suspend fun getMeditaciones(idcategoria: Int = 0): List<Meditacion> =
        service.getMeditaciones(if (idcategoria > 0) idcategoria else null)

    suspend fun getMeditacion(idmeditacion: Int): Meditacion? =
        service.getMeditacion(idmeditacion).firstOrNull { it.idmeditacion == idmeditacion }
}
