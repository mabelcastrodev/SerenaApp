package com.mabel.serenaapp.screens.categorias

import com.mabel.serenaapp.models.Categoria

// Los tres estados posibles de la pantalla mientras llegan los datos
sealed interface CategoriasUIState {
    data object Loading : CategoriasUIState
    data class Success(val categorias: List<Categoria>) : CategoriasUIState
    data class Error(val mensaje: String) : CategoriasUIState
}
