package com.mabel.serenaapp.models

data class EntradaDiario(
    val id: Long = 0,
    val animo: Int = 0,       // posicion en la lista "animos"
    val texto: String = "",
    val fecha: Long = 0       // milisegundos (System.currentTimeMillis)
)
