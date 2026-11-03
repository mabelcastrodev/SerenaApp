package com.mabel.serenaapp.models

data class Meditacion(
    val idmeditacion: Int = 0,
    val idcategoria: Int = 0,
    val categoria: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val duracion: Int = 0,       // en minutos
    val nivel: String = "",
    val autor: String = "",
    val destacado: Int = 0,      // 1 = aparece primero
    val imagen: String = ""      // ruta relativa: imagenes/meditaciones/m1.jpg
)
