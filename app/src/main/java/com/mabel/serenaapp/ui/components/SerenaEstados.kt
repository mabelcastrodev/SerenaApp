package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTES REUTILIZABLES: estados de una pantalla con datos
 *
 * Cada pantalla que llama al servicio web pasa por tres estados
 * (UIState del ViewModel): Loading, Error y Success. Estos tres
 * componentes dibujan siempre igual esos estados en Dashboard,
 * Categorias, Sesiones y Detalle.
 */

// Estado Loading: circulo que gira
@Composable
fun SerenaCargando(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.extraLarge),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(AppTheme.sizes.progressSize),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Estado Error: aviso + boton Reintentar (usa SerenaBoton)
@Composable
fun SerenaError(
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    mensaje: String = stringResource(R.string.error_mensaje)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alerta),
            contentDescription = null,
            tint = AppAccent,
            modifier = Modifier.size(AppTheme.sizes.iconContainerMedium)
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.small))
        Text(
            stringResource(R.string.error_titulo),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Text(
            mensaje,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))
        SerenaBoton(
            texto = stringResource(R.string.reintentar),
            onClick = onReintentar,
            estilo = EstiloBoton.AZUL,
            pequeno = true
        )
    }
}

// Lista vacia: mensaje centrado (y boton opcional)
@Composable
fun SerenaVacio(
    mensaje: String,
    modifier: Modifier = Modifier,
    textoBoton: String? = null,
    onBoton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (textoBoton != null) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.regular))
            SerenaBoton(
                texto = textoBoton,
                onClick = onBoton,
                estilo = EstiloBoton.CONTORNO,
                pequeno = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SerenaEstadosPreview() {
    SerenaAppTheme {
        Column {
            SerenaCargando()
            SerenaError(onReintentar = {})
            SerenaVacio(mensaje = "Todavía no hay sesiones", textoBoton = "Limpiar búsqueda")
        }
    }
}
