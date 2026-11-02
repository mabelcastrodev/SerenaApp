package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaCard
 *
 * Tarjeta blanca redondeada de la plantilla. Es la base de
 * casi todo el contenido: categorias, sesiones, detalle,
 * perfil, estadisticas y entradas del diario.
 *
 *  - onClick       -> si se pasa, la tarjeta se puede tocar
 *  - seleccionada  -> borde azul (animo elegido en Mi Diario)
 *  - relleno       -> 0.dp cuando la tarjeta lleva una imagen
 *                     de borde a borde
 */
@Composable
fun SerenaCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    seleccionada: Boolean = false,
    relleno: Dp = AppTheme.spacing.regular,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(AppTheme.sizes.cardCornerRadius)
    val colores = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
    val elevacion = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.cardElevation)
    val borde = if (seleccionada) {
        BorderStroke(AppTheme.sizes.selectedBorder, MaterialTheme.colorScheme.primary)
    } else {
        null
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion,
            border = borde
        ) {
            Column(modifier = Modifier.padding(relleno), content = contenido)
        }
    } else {
        Card(
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion,
            border = borde
        ) {
            Column(modifier = Modifier.padding(relleno), content = contenido)
        }
    }
}

@Preview
@Composable
private fun SerenaCardPreview() {
    SerenaAppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(AppTheme.spacing.regular)
        ) {
            SerenaCard {
                Text("Respiración 4-7-8", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Inhala cuatro, sostén siete, exhala ocho",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            SerenaCard(onClick = {}, seleccionada = true) {
                Text("Tarjeta seleccionada", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}
