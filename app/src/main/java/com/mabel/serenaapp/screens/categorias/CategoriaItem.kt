package com.mabel.serenaapp.screens.categorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.data.remote.BASE_URL
import com.mabel.serenaapp.models.Categoria
import com.mabel.serenaapp.ui.components.SerenaCard
import com.mabel.serenaapp.ui.components.SerenaEtiqueta
import com.mabel.serenaapp.ui.components.SerenaImagenRemota
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * Tarjeta de una categoria: SerenaCard + SerenaImagenRemota +
 * SerenaEtiqueta (tres componentes reutilizables juntos).
 */
@Composable
fun CategoriaItem(
    categoria: Categoria,
    onClick: () -> Unit
) {
    SerenaCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        relleno = AppTheme.spacing.none
    ) {
        SerenaImagenRemota(
            url = BASE_URL + categoria.imagen,
            contentDescription = categoria.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.sizes.cardImageHeight)
        )
        Column(modifier = Modifier.padding(AppTheme.spacing.regular)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    categoria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(AppTheme.spacing.small))
                SerenaEtiqueta(
                    texto = stringResource(R.string.categoria_sesiones, categoria.total)
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))
            Text(
                categoria.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun CategoriaItemPreview() {
    SerenaAppTheme {
        CategoriaItem(
            categoria = Categoria(2, "Dormir", "Sonidos y respiraciones suaves para quedarte dormida", 6),
            onClick = {}
        )
    }
}
