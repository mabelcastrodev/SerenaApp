package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaEncabezado
 *
 * Barra superior blanca de la plantilla. Aparece en todas las
 * pantallas que estan despues del Login.
 *
 *  - Sin onVolver: muestra el logo (pantallas del menu inferior)
 *  - Con onVolver: muestra la flecha (pantallas de detalle)
 *  - Sin titulo:   muestra "SERENA" en azul (Dashboard)
 */
@Composable
fun SerenaEncabezado(
    modifier: Modifier = Modifier,
    titulo: String? = null,
    subtitulo: String? = null,
    onVolver: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = if (onVolver != null) AppTheme.spacing.extraSmall else AppTheme.spacing.regular,
                end = AppTheme.spacing.regular,
                top = AppTheme.spacing.medium,
                bottom = AppTheme.spacing.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onVolver != null) {
            IconButton(onClick = onVolver) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.login_back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.size(AppTheme.sizes.logoSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.small))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                titulo ?: stringResource(R.string.app_name_upper),
                style = if (titulo == null) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.titleLarge
                },
                color = if (titulo == null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitulo != null) {
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun SerenaEncabezadoPreview() {
    SerenaAppTheme {
        Column {
            SerenaEncabezado()
            SerenaEncabezado(titulo = "Categorías", subtitulo = "Elige cómo quieres sentirte")
            SerenaEncabezado(titulo = "Detalle", onVolver = {})
        }
    }
}
