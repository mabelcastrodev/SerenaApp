package com.mabel.serenaapp.screens.categorias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabel.serenaapp.data.repository.CategoriasRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
 * Lo usan dos pantallas: CategoriasActivity y el Dashboard
 * (los circulos de categorias).
 */
class CategoriasViewModel(
    private val repository: CategoriasRepository = CategoriasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriasUIState>(CategoriasUIState.Loading)
    val uiState: StateFlow<CategoriasUIState> = _uiState.asStateFlow()

    init {
        cargarCategorias()
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            _uiState.value = CategoriasUIState.Loading
            _uiState.value = try {
                CategoriasUIState.Success(repository.getCategorias())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CategoriasViewModel", "Error al cargar categorias", e)
                CategoriasUIState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
