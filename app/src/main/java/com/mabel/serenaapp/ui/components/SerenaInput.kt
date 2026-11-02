package com.mabel.serenaapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppDark
import com.mabel.serenaapp.ui.theme.AppDarkSurface
import com.mabel.serenaapp.ui.theme.AppMuted
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * COMPONENTE REUTILIZABLE: SerenaInput
 *
 * Campo de texto de la app. Lo usan Login, Registro, Perfil,
 * Mi Diario y el buscador de Sesiones.
 *
 *  - oscuro = true   -> estilo del Login (fondo #242644)
 *  - esPassword      -> oculta el texto y muestra el "ojito"
 *  - lineas > 1      -> campo grande (Mi Diario)
 *  - error           -> mensaje rojo debajo del campo
 */
@Composable
fun SerenaInput(
    valor: String,
    onValorCambio: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    oscuro: Boolean = false,
    esPassword: Boolean = false,
    lineas: Int = 1,
    error: String? = null,
    @DrawableRes iconoInicio: Int? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    var verPassword by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        TextField(
            value = valor,
            onValueChange = onValorCambio,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = lineas == 1,
            minLines = lineas,
            isError = error != null,
            textStyle = MaterialTheme.typography.bodyMedium,
            visualTransformation = if (esPassword && !verPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esPassword) KeyboardType.Password else tipoTeclado
            ),
            leadingIcon = if (iconoInicio != null) {
                {
                    Icon(
                        painter = painterResource(iconoInicio),
                        contentDescription = null
                    )
                }
            } else {
                null
            },
            trailingIcon = if (esPassword) {
                {
                    IconButton(onClick = { verPassword = !verPassword }) {
                        Icon(
                            painter = painterResource(
                                if (verPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                            ),
                            contentDescription = stringResource(R.string.login_show_password)
                        )
                    }
                }
            } else {
                null
            },
            shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
            colors = if (oscuro) coloresOscuros() else coloresClaros()
        )

        if (error != null) {
            Text(
                error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    start = AppTheme.spacing.small,
                    top = AppTheme.spacing.extraSmall
                )
            )
        }
    }
}

// Colores del Login y Registro (pantallas oscuras de la plantilla)
@Composable
private fun coloresOscuros(): TextFieldColors = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = AppDarkSurface,
    unfocusedContainerColor = AppDarkSurface,
    errorContainerColor = AppDarkSurface,
    errorTextColor = Color.White,
    cursorColor = AppAccent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedPlaceholderColor = AppMuted,
    unfocusedPlaceholderColor = AppMuted,
    errorPlaceholderColor = AppMuted,
    focusedTrailingIconColor = AppMuted,
    unfocusedTrailingIconColor = AppMuted,
    errorTrailingIconColor = AppMuted,
    focusedLeadingIconColor = AppMuted,
    unfocusedLeadingIconColor = AppMuted
)

// Colores de las pantallas claras: salen del tema, asi funcionan
// tambien en modo oscuro
@Composable
private fun coloresClaros(): TextFieldColors = TextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Preview
@Composable
private fun SerenaInputPreview() {
    SerenaAppTheme {
        Column(
            modifier = Modifier
                .background(AppDark)
                .padding(AppTheme.spacing.regular)
        ) {
            SerenaInput(valor = "", onValorCambio = {}, placeholder = "Usuario", oscuro = true)
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            SerenaInput(
                valor = "123",
                onValorCambio = {},
                placeholder = "Contraseña",
                oscuro = true,
                esPassword = true,
                error = "Mínimo 6 caracteres"
            )
        }
    }
}
