package com.mabel.serenaapp.screens.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mabel.serenaapp.data.local.PreferenciasSerena
import com.mabel.serenaapp.utils.diaDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Todo lo que muestra la pantalla Perfil
data class PerfilUIState(
    val nombre: String = "",
    val correo: String = "",
    val totalEntradas: Int = 0,
    val diasConEntrada: Int = 0,
    val ultimoAnimo: Int? = null      // null = todavia no escribe nada
)

/*
 * Lee los datos guardados en el celular: el nombre y correo del
 * Registro y las estadisticas de Mi Diario (son datos reales,
 * no de ejemplo).
 */
class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencias = PreferenciasSerena(application)

    private val _uiState = MutableStateFlow(leerPerfil())
    val uiState: StateFlow<PerfilUIState> = _uiState.asStateFlow()

    fun guardarDatos(nombre: String, correo: String) {
        preferencias.nombre = nombre.trim()
        preferencias.correo = correo.trim()
        _uiState.value = leerPerfil()
    }

    private fun leerPerfil(): PerfilUIState {
        val diario = preferencias.leerDiario()
        return PerfilUIState(
            nombre = preferencias.nombre,
            correo = preferencias.correo,
            totalEntradas = diario.size,
            diasConEntrada = diario.map { diaDe(it.fecha) }.distinct().size,
            ultimoAnimo = diario.firstOrNull()?.animo
        )
    }
}
