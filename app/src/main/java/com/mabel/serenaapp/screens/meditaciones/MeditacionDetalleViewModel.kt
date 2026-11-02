package com.mabel.serenaapp.screens.meditaciones

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabel.serenaapp.data.repository.MeditacionesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MeditacionDetalleViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = MeditacionesRepository()
    private val idmeditacion: Int = savedStateHandle[EXTRA_IDMEDITACION] ?: 0

    private val _uiState = MutableStateFlow<MeditacionDetalleUIState>(MeditacionDetalleUIState.Loading)
    val uiState: StateFlow<MeditacionDetalleUIState> = _uiState.asStateFlow()

    init {
        cargarMeditacion()
    }

    fun cargarMeditacion() {
        viewModelScope.launch {
            _uiState.value = MeditacionDetalleUIState.Loading
            _uiState.value = try {
                val meditacion = repository.getMeditacion(idmeditacion)
                if (meditacion != null) {
                    MeditacionDetalleUIState.Success(meditacion)
                } else {
                    MeditacionDetalleUIState.Error("No existe la sesion $idmeditacion")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("MeditacionDetalleVM", "Error al cargar la sesion", e)
                MeditacionDetalleUIState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
