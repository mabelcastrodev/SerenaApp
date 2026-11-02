package com.mabel.serenaapp.models

/*
 * Una fila de la tabla "categorias" tal como la devuelve
 * categorias.php. Los nombres son iguales a las columnas para
 * que Gson los llene solo.
 */
data class Categoria(
    val idcategoria: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val total: Int = 0,          // cuantas sesiones tiene (COUNT en el PHP)
    val imagen: String = ""      // ruta relativa: imagenes/categorias/c1.jpg
)
