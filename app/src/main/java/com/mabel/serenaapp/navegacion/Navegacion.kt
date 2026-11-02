package com.mabel.serenaapp.navegacion

import android.app.Activity
import android.content.Intent
import com.mabel.serenaapp.DashboardActivity
import com.mabel.serenaapp.screens.categorias.CategoriasActivity
import com.mabel.serenaapp.screens.meditaciones.MeditacionesActivity
import com.mabel.serenaapp.ui.components.Seccion

/*
 * NAVEGACION DEL MENU INFERIOR
 *
 * Todas las pantallas con SerenaBarraNavegacion llaman a esta
 * funcion, asi el comportamiento es el mismo en todas:
 *
 *  - El Dashboard (INICIO) siempre queda en el fondo de la pila.
 *  - Las demas secciones se reemplazan entre si (finish), para
 *    que el boton "atras" del celular vuelva al Inicio y no
 *    recorra todas las pestanas que se abrieron.
 */
fun Activity.irASeccion(destino: Seccion, actual: Seccion) {
    if (destino == actual) return

    val pantalla = when (destino) {
        Seccion.INICIO -> DashboardActivity::class.java
        Seccion.CATEGORIAS -> CategoriasActivity::class.java
        Seccion.SESIONES -> MeditacionesActivity::class.java
        // Mi Diario y Perfil se agregan en la siguiente feature
        Seccion.DIARIO, Seccion.PERFIL -> return
    }

    val intent = Intent(this, pantalla)
    if (destino == Seccion.INICIO) {
        // Vuelve al Dashboard que ya estaba abierto
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    startActivity(intent)

    if (actual != Seccion.INICIO) {
        finish()
    }
}
