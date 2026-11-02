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

/*
 * SavedStateHandle trae los extras del Intent: asi el ViewModel
 * sabe de que categoria tiene que pedir las sesiones.
 * Si no llega ninguna (0) se piden todas.
 */
class MeditacionesViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = MeditacionesRepository()
    private val idcategoria: Int = savedStateHandle[EXTRA_IDCATEGORIA] ?: 0

    private val _uiState = MutableStateFlow<MeditacionesUIState>(MeditacionesUIState.Loading)
    val uiState: StateFlow<MeditacionesUIState> = _uiState.asStateFlow()

    init {
        cargarMeditaciones()
    }

    fun cargarMeditaciones() {
        viewModelScope.launch {
            _uiState.value = MeditacionesUIState.Loading
            _uiState.value = try {
                MeditacionesUIState.Success(repository.getMeditaciones(idcategoria))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("MeditacionesViewModel", "Error al cargar sesiones", e)
                MeditacionesUIState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
