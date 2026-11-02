package com.mabel.serenaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * Las cinco secciones del menu inferior. Cada una tiene su texto
 * y su icono, en el mismo orden en que se dibujan.
 */
enum class Seccion(
    @StringRes val etiqueta: Int,
    @DrawableRes val icono: Int
) {
    INICIO(R.string.nav_inicio, R.drawable.ic_home),
    CATEGORIAS(R.string.nav_categorias, R.drawable.ic_layers),
    SESIONES(R.string.nav_sesiones, R.drawable.ic_folder),
    DIARIO(R.string.nav_diario, R.drawable.ic_edit),
    PERFIL(R.string.nav_perfil, R.drawable.ic_person)
}

/*
 * COMPONENTE REUTILIZABLE: SerenaBarraNavegacion
 *
 * Menu inferior de la plantilla. Es el mismo en Dashboard,
 * Categorias, Sesiones, Mi Diario y Perfil; solo cambia cual
 * esta marcada (actual). Que Activity se abre lo decide cada
 * pantalla con onSeleccion (ver navegacion/Navegacion.kt).
 */
@Composable
fun SerenaBarraNavegacion(
    actual: Seccion,
    onSeleccion: (Seccion) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        Seccion.entries.forEach { seccion ->
            NavigationBarItem(
                selected = seccion == actual,
                onClick = { onSeleccion(seccion) },
                icon = {
                    Icon(
                        painter = painterResource(seccion.icono),
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.sizes.iconMedium)
                    )
                },
                label = {
                    Text(
                        stringResource(seccion.etiqueta),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Preview
@Composable
private fun SerenaBarraNavegacionPreview() {
    SerenaAppTheme {
        SerenaBarraNavegacion(actual = Seccion.INICIO, onSeleccion = {})
    }
}
