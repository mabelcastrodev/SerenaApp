package com.mabel.serenaapp.models

import androidx.annotation.StringRes
import com.mabel.serenaapp.R

// Cada animo que se puede elegir en Mi Diario
data class Animo(
    val emoji: String,
    @StringRes val nombre: Int
)

val animos = listOf(
    Animo("😄", R.string.animo_feliz),
    Animo("😌", R.string.animo_calma),
    Animo("😐", R.string.animo_normal),
    Animo("😔", R.string.animo_triste),
    Animo("😣", R.string.animo_estres)
)


fun animoDe(posicion: Int): Animo = animos.getOrElse(posicion) { animos[2] }
