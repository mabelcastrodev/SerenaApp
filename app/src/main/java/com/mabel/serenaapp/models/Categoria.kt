package com.mabel.serenaapp.models

data class Categoria(
    val idcategoria: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val total: Int = 0,          // cuantas sesiones tiene (COUNT en el PHP)
    val imagen: String = ""      // ruta relativa: imagenes/categorias/c1.jpg
)
