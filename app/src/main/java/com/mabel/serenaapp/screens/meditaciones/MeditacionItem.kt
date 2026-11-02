package com.mabel.serenaapp.screens.meditaciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.data.remote.BASE_URL
import com.mabel.serenaapp.models.Meditacion
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaCard
import com.mabel.serenaapp.ui.components.SerenaEtiqueta
import com.mabel.serenaapp.ui.components.SerenaImagenRemota
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * Fila de la lista de sesiones: miniatura + datos.
 * Muestra 6 campos de la BD: titulo, descripcion, duracion,
 * categoria, nivel y autor.
 */
@Composable
fun MeditacionItem(
    meditacion: Meditacion,
    onClick: () -> Unit
) {
    SerenaCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        relleno = AppTheme.spacing.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SerenaImagenRemota(
                url = BASE_URL + meditacion.imagen,
                contentDescription = meditacion.titulo,
                modifier = Modifier
                    .size(AppTheme.sizes.thumbnailSize)
                    .clip(RoundedCornerShape(AppTheme.sizes.buttonCornerRadius))
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        meditacion.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.small))
                    Text(
                        stringResource(R.string.meditacion_duracion, meditacion.duracion),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    meditacion.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraSmall)) {
                    SerenaEtiqueta(texto = meditacion.categoria)
                    SerenaEtiqueta(texto = meditacion.nivel)
                }
                Text(
                    stringResource(R.string.sesiones_autor, meditacion.autor),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/*
 * Tarjeta grande para la sesion destacada (la primera de la lista).
 */
@Composable
fun MeditacionDestacada(
    meditacion: Meditacion,
    onClick: () -> Unit
) {
    SerenaCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        relleno = AppTheme.spacing.none
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.sizes.featuredImageHeight)
        ) {
            SerenaImagenRemota(
                url = BASE_URL + meditacion.imagen,
                contentDescription = meditacion.titulo,
                modifier = Modifier.fillMaxSize()
            )
            SerenaEtiqueta(
                texto = stringResource(R.string.sesiones_destacada),
                destacada = true,
                modifier = Modifier.padding(AppTheme.spacing.medium)
            )
        }
        Column(modifier = Modifier.padding(AppTheme.spacing.regular)) {
            Text(
                meditacion.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                meditacion.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraSmall)) {
                SerenaEtiqueta(texto = stringResource(R.string.meditacion_duracion, meditacion.duracion))
                SerenaEtiqueta(texto = meditacion.nivel)
                SerenaEtiqueta(texto = meditacion.categoria)
            }
            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
            SerenaBoton(
                texto = stringResource(R.string.sesiones_escuchar),
                onClick = onClick,
                icono = R.drawable.ic_play,
                anchoCompleto = true,
                pequeno = true
            )
        }
    }
}

private val meditacionEjemplo = Meditacion(
    1, 4, "Respiración", "Respiración 4-7-8",
    "Inhala cuatro, sostén siete, exhala ocho", 3, "Principiante", "Serena", 1
)

@Preview
@Composable
private fun MeditacionItemPreview() {
    SerenaAppTheme {
        Column {
            MeditacionDestacada(meditacion = meditacionEjemplo, onClick = {})
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            MeditacionItem(meditacion = meditacionEjemplo, onClick = {})
        }
    }
}
