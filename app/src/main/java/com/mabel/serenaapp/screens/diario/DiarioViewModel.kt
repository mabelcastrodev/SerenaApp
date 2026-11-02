package com.mabel.serenaapp.screens.diario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mabel.serenaapp.data.local.PreferenciasSerena
import com.mabel.serenaapp.models.EntradaDiario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
 * AndroidViewModel porque necesita el Context (application)
 * para abrir las SharedPreferences.
 * Las entradas sobreviven al girar la pantalla (ViewModel) y
 * tambien al cerrar la app (PreferenciasSerena).
 */
class DiarioViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencias = PreferenciasSerena(application)

    private val _entradas = MutableStateFlow(preferencias.leerDiario())
    val entradas: StateFlow<List<EntradaDiario>> = _entradas.asStateFlow()

    fun guardarEntrada(animo: Int, texto: String) {
        val ahora = System.currentTimeMillis()
        val nueva = EntradaDiario(id = ahora, animo = animo, texto = texto.trim(), fecha = ahora)
        // La mas reciente va primero
        actualizar(listOf(nueva) + _entradas.value)
    }

    fun eliminarEntrada(id: Long) {
        actualizar(_entradas.value.filter { it.id != id })
    }

    private fun actualizar(lista: List<EntradaDiario>) {
        _entradas.value = lista
        preferencias.guardarDiario(lista)
    }
}
