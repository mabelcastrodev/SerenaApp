package com.mabel.serenaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaModal
 *
 * Ventana emergente de la app. Se usa para confirmar acciones
 * (cuenta creada, datos guardados, entrada guardada), para
 * avisar errores de formulario y para preguntar antes de algo
 * importante (cerrar sesion, eliminar una entrada).
 *
 *  - Con un boton:  solo textoConfirmar / onConfirmar
 *  - Con dos:       se agrega textoCancelar / onCancelar
 *  - onDescartar:   que pasa al tocar fuera del modal o "atras"
 *                   (por defecto lo mismo que Cancelar)
 *
 * Los botones del modal son SerenaBoton (componente dentro de
 * otro componente).
 */
@Composable
fun SerenaModal(
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    onConfirmar: () -> Unit,
    @DrawableRes icono: Int? = null,
    textoCancelar: String? = null,
    onCancelar: () -> Unit = onConfirmar,
    onDescartar: () -> Unit = onCancelar
) {
    AlertDialog(
        onDismissRequest = onDescartar,
        icon = if (icono != null) {
            {
                Icon(
                    painter = painterResource(icono),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppTheme.sizes.modalIconSize)
                )
            }
        } else {
            null
        },
        title = {
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            SerenaBoton(
                texto = textoConfirmar,
                onClick = onConfirmar,
                pequeno = true
            )
        },
        dismissButton = if (textoCancelar != null) {
            {
                SerenaBoton(
                    texto = textoCancelar,
                    onClick = onCancelar,
                    estilo = EstiloBoton.CONTORNO,
                    pequeno = true
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(AppTheme.sizes.cardCornerRadius),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun SerenaModalPreview() {
    SerenaAppTheme {
        SerenaModal(
            titulo = "¿Cerrar sesión?",
            mensaje = "Tendrás que volver a ingresar tus datos.",
            textoConfirmar = "Sí, salir",
            onConfirmar = {},
            icono = R.drawable.ic_logout,
            textoCancelar = "Cancelar"
        )
    }
}
