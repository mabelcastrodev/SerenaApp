package com.mabel.serenaapp.screens.meditaciones

import com.mabel.serenaapp.models.Meditacion

sealed interface MeditacionesUIState {
    data object Loading : MeditacionesUIState
    data class Success(val meditaciones: List<Meditacion>) : MeditacionesUIState
    data class Error(val mensaje: String) : MeditacionesUIState
}

// El detalle muestra una sola meditacion
sealed interface MeditacionDetalleUIState {
    data object Loading : MeditacionDetalleUIState
    data class Success(val meditacion: Meditacion) : MeditacionDetalleUIState
    data class Error(val mensaje: String) : MeditacionDetalleUIState
}
