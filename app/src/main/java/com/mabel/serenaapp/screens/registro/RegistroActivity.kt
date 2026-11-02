package com.mabel.serenaapp.screens.registro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.R
import com.mabel.serenaapp.data.local.PreferenciasSerena
import com.mabel.serenaapp.ui.components.EstiloBoton
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaInput
import com.mabel.serenaapp.ui.components.SerenaModal
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppDark
import com.mabel.serenaapp.ui.theme.AppMuted
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme
import com.mabel.serenaapp.utils.LARGO_MINIMO_PASSWORD
import com.mabel.serenaapp.utils.correoValido

/*
 * PANTALLA REGISTRO
 * Mismo estilo oscuro del Login. Valida los 4 campos y, si todo
 * esta bien, guarda nombre y correo en el celular y muestra un
 * SerenaModal. Al aceptar vuelve al Login.
 */
class RegistroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val preferencias = PreferenciasSerena(this)

        setContent {
            SerenaAppTheme {
                RegistroScreen(
                    onVolverClick = { finish() },
                    onRegistrar = { nombre, correo ->
                        preferencias.nombre = nombre
                        preferencias.correo = correo
                    },
                    onIrAlLogin = { finish() }
                )
            }
        }
    }
}

@Composable
fun RegistroScreen(
    onVolverClick: () -> Unit,
    onRegistrar: (nombre: String, correo: String) -> Unit,
    onIrAlLogin: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

    // null = sin error
    var errorNombre by rememberSaveable { mutableStateOf<String?>(null) }
    var errorCorreo by rememberSaveable { mutableStateOf<String?>(null) }
    var errorPassword by rememberSaveable { mutableStateOf<String?>(null) }
    var errorConfirmar by rememberSaveable { mutableStateOf<String?>(null) }

    var mostrarModal by rememberSaveable { mutableStateOf(false) }

    // Los textos se leen aqui porque dentro de onClick no se puede
    // usar stringResource
    val textoErrorNombre = stringResource(R.string.error_nombre)
    val textoErrorCorreo = stringResource(R.string.error_correo)
    val textoErrorPassword = stringResource(R.string.error_password)
    val textoErrorConfirmar = stringResource(R.string.error_confirmar)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppDark)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppTheme.spacing.screenMargin)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onVolverClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.login_back),
                    tint = AppAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        Text(
            stringResource(R.string.registro_titulo),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.small))
        Text(
            stringResource(R.string.registro_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = AppMuted
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        // Cuatro veces el mismo componente, con distinta configuracion
        SerenaInput(
            valor = nombre,
            onValorCambio = { nombre = it; errorNombre = null },
            placeholder = stringResource(R.string.registro_nombre),
            oscuro = true,
            iconoInicio = R.drawable.ic_person,
            error = errorNombre
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
        SerenaInput(
            valor = correo,
            onValorCambio = { correo = it; errorCorreo = null },
            placeholder = stringResource(R.string.registro_correo),
            oscuro = true,
            tipoTeclado = KeyboardType.Email,
            error = errorCorreo
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
        SerenaInput(
            valor = password,
            onValorCambio = { password = it; errorPassword = null },
            placeholder = stringResource(R.string.registro_password),
            oscuro = true,
            esPassword = true,
            error = errorPassword
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
        SerenaInput(
            valor = confirmar,
            onValorCambio = { confirmar = it; errorConfirmar = null },
            placeholder = stringResource(R.string.registro_confirmar),
            oscuro = true,
            esPassword = true,
            error = errorConfirmar
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        SerenaBoton(
            texto = stringResource(R.string.registro_boton),
            onClick = {
                errorNombre = if (nombre.isBlank()) textoErrorNombre else null
                errorCorreo = if (!correoValido(correo)) textoErrorCorreo else null
                errorPassword = if (password.length < LARGO_MINIMO_PASSWORD) textoErrorPassword else null
                errorConfirmar = if (confirmar != password) textoErrorConfirmar else null

                val todoBien = errorNombre == null && errorCorreo == null &&
                    errorPassword == null && errorConfirmar == null
                if (todoBien) {
                    onRegistrar(nombre.trim(), correo.trim())
                    mostrarModal = true
                }
            },
            estilo = EstiloBoton.AZUL,
            anchoCompleto = true
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.registro_ya_tengo),
                style = MaterialTheme.typography.bodyMedium,
                color = AppMuted
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
            Text(
                stringResource(R.string.login_button),
                style = MaterialTheme.typography.labelMedium,
                color = AppAccent,
                modifier = Modifier.clickable(onClick = onIrAlLogin)
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))
    }

    if (mostrarModal) {
        SerenaModal(
            titulo = stringResource(R.string.registro_ok_titulo),
            mensaje = stringResource(R.string.registro_ok_mensaje, nombre.trim()),
            textoConfirmar = stringResource(R.string.registro_ok_boton),
            onConfirmar = {
                mostrarModal = false
                onIrAlLogin()
            },
            icono = R.drawable.ic_check
        )
    }
}

@Preview
@Composable
private fun RegistroScreenPreview() {
    SerenaAppTheme {
        RegistroScreen(onVolverClick = {}, onRegistrar = { _, _ -> }, onIrAlLogin = {})
    }
}
