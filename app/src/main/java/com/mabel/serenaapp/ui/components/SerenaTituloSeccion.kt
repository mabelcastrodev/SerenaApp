package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaTituloSeccion
 *
 * Titulo de cada bloque ("Explora por categoria", "Mis entradas"...)
 * con un enlace opcional a la derecha ("Ver todas").
 */
@Composable
fun SerenaTituloSeccion(
    texto: String,
    modifier: Modifier = Modifier,
    textoAccion: String? = null,
    onAccion: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (textoAccion != null) {
            TextButton(onClick = onAccion) {
                Text(
                    textoAccion,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SerenaTituloSeccionPreview() {
    SerenaAppTheme {
        SerenaTituloSeccion(texto = "Explora por categoría", textoAccion = "Ver todas")
    }
}
