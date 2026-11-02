package com.mabel.serenaapp.data.repository

import com.mabel.serenaapp.data.remote.MeditacionesService
import com.mabel.serenaapp.data.remote.RetrofitClient
import com.mabel.serenaapp.models.Meditacion

class MeditacionesRepository(
    private val service: MeditacionesService = RetrofitClient.meditacionesService
) {
    // idcategoria = 0 significa "todas"
    suspend fun getMeditaciones(idcategoria: Int = 0): List<Meditacion> =
        service.getMeditaciones(if (idcategoria > 0) idcategoria else null)

    // Se busca por id en la respuesta: asi funciona aunque el PHP
    // del servidor todavia no tenga el filtro idmeditacion
    suspend fun getMeditacion(idmeditacion: Int): Meditacion? =
        service.getMeditacion(idmeditacion).firstOrNull { it.idmeditacion == idmeditacion }
}
