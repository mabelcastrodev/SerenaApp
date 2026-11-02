package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppForeground
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaEtiqueta
 *
 * Etiqueta pequena (chip) para datos cortos: categoria, nivel,
 * duracion, "Destacada", numero de sesiones...
 * Con destacada = true sale en amarillo.
 */
@Composable
fun SerenaEtiqueta(
    texto: String,
    modifier: Modifier = Modifier,
    destacada: Boolean = false
) {
    val fondo: Color = if (destacada) AppAccent else MaterialTheme.colorScheme.surfaceVariant
    val color: Color = if (destacada) AppForeground else MaterialTheme.colorScheme.primary

    Text(
        texto,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        modifier = modifier
            .background(fondo, RoundedCornerShape(AppTheme.sizes.buttonCornerRadius))
            .padding(
                horizontal = AppTheme.spacing.small,
                vertical = AppTheme.spacing.micro
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun SerenaEtiquetaPreview() {
    SerenaAppTheme {
        Row(modifier = Modifier.padding(AppTheme.spacing.regular)) {
            SerenaEtiqueta(texto = "Dormir")
            Spacer(modifier = Modifier.width(AppTheme.spacing.small))
            SerenaEtiqueta(texto = "Destacada", destacada = true)
        }
    }
}
