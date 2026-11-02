package com.mabel.serenaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppForeground
import com.mabel.serenaapp.ui.theme.AppPrimary
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaBoton
 *
 * Es el boton de toda la app. Se usa en las 10 pantallas
 * (Main, Empezar, Login, Registro, Dashboard, Categorias,
 * Sesiones, Detalle, Perfil y Mi Diario) y tambien dentro de
 * otros componentes (SerenaModal y SerenaError).
 *
 * Los tres estilos salen de la plantilla:
 *  - AMARILLO: botones principales (Empezar, Iniciar ahora...)
 *  - AZUL:     acciones de formulario (Iniciar sesion, Guardar...)
 *  - CONTORNO: acciones secundarias (Cancelar, Cerrar sesion...)
 */
enum class EstiloBoton { AMARILLO, AZUL, CONTORNO }

@Composable
fun SerenaBoton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    estilo: EstiloBoton = EstiloBoton.AMARILLO,
    anchoCompleto: Boolean = false,
    pequeno: Boolean = false,
    @DrawableRes icono: Int? = null,
    habilitado: Boolean = true
) {
    val alto = if (pequeno) AppTheme.sizes.buttonHeightSmall else AppTheme.sizes.buttonHeight
    val forma = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius)

    val modificador = modifier
        .then(if (anchoCompleto) Modifier.fillMaxWidth() else Modifier)
        .height(alto)

    // Lo que va dentro del boton: icono opcional + texto
    val contenido: @Composable RowScope.() -> Unit = {
        if (icono != null) {
            Icon(
                painter = painterResource(icono),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.sizes.iconRegular)
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.small))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }

    when (estilo) {
        EstiloBoton.CONTORNO -> OutlinedButton(
            onClick = onClick,
            modifier = modificador,
            enabled = habilitado,
            shape = forma,
            border = BorderStroke(AppTheme.sizes.borderWidth, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            content = contenido
        )

        EstiloBoton.AMARILLO, EstiloBoton.AZUL -> Button(
            onClick = onClick,
            modifier = modificador,
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (estilo == EstiloBoton.AMARILLO) AppAccent else AppPrimary,
                contentColor = if (estilo == EstiloBoton.AMARILLO) AppForeground else Color.White
            ),
            content = contenido
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SerenaBotonPreview() {
    SerenaAppTheme {
        Column(modifier = Modifier.padding(AppTheme.spacing.regular)) {
            SerenaBoton(texto = "Empezar", onClick = {})
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            SerenaBoton(
                texto = "Iniciar sesión",
                onClick = {},
                estilo = EstiloBoton.AZUL,
                anchoCompleto = true
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            SerenaBoton(
                texto = "Cerrar sesión",
                onClick = {},
                estilo = EstiloBoton.CONTORNO,
                icono = R.drawable.ic_logout,
                pequeno = true
            )
        }
    }
}
