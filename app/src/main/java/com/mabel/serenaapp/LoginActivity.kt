package com.mabel.serenaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.screens.registro.RegistroActivity
import com.mabel.serenaapp.ui.components.EstiloBoton
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaInput
import com.mabel.serenaapp.ui.components.SerenaModal
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppDark
import com.mabel.serenaapp.ui.theme.AppMuted
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            SerenaAppTheme {
                LoginScreen(
                    onVolverClick = {
                        finish()
                    },
                    onIniciarClick = {

                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    },
                    onRegistroClick = {
                        startActivity(Intent(this, RegistroActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onVolverClick: () -> Unit,
    onIniciarClick: () -> Unit,
    onRegistroClick: () -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var mostrarFaltan by rememberSaveable { mutableStateOf(false) }
    var mostrarOlvido by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppDark)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppTheme.spacing.screenMargin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flecha para volver
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onVolverClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.login_back),
                    tint = AppAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Image(
            painter = painterResource(R.drawable.img_login),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.sizes.loginIllustrationHeight),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Text(
            stringResource(R.string.login_title),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        // Componente reutilizable (ui/components/SerenaInput.kt)
        SerenaInput(
            valor = usuario,
            onValorCambio = { usuario = it },
            placeholder = stringResource(R.string.login_username),
            oscuro = true
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        SerenaInput(
            valor = password,
            onValorCambio = { password = it },
            placeholder = stringResource(R.string.login_password),
            oscuro = true,
            esPassword = true
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        // Componente reutilizable (ui/components/SerenaBoton.kt)
        SerenaBoton(
            texto = stringResource(R.string.login_button),
            onClick = {
                if (usuario.isBlank() || password.isBlank()) {
                    mostrarFaltan = true
                } else {
                    onIniciarClick()
                }
            },
            estilo = EstiloBoton.AZUL,
            anchoCompleto = true
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Text(
            stringResource(R.string.login_forgot),
            style = MaterialTheme.typography.labelMedium,
            color = AppAccent,
            modifier = Modifier.clickable { mostrarOlvido = true }
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.login_no_account),
                style = MaterialTheme.typography.bodyMedium,
                color = AppMuted
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
            Text(
                stringResource(R.string.login_register),
                style = MaterialTheme.typography.labelMedium,
                color = AppAccent,
                modifier = Modifier.clickable(onClick = onRegistroClick)
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))
    }

    // Componente reutilizable (ui/components/SerenaModal.kt)
    if (mostrarFaltan) {
        SerenaModal(
            titulo = stringResource(R.string.login_faltan_titulo),
            mensaje = stringResource(R.string.login_faltan_mensaje),
            textoConfirmar = stringResource(R.string.aceptar),
            onConfirmar = { mostrarFaltan = false },
            icono = R.drawable.ic_alerta
        )
    }
    if (mostrarOlvido) {
        SerenaModal(
            titulo = stringResource(R.string.login_olvido_titulo),
            mensaje = stringResource(R.string.login_olvido_mensaje),
            textoConfirmar = stringResource(R.string.login_register),
            onConfirmar = {
                mostrarOlvido = false
                onRegistroClick()
            },
            textoCancelar = stringResource(R.string.cerrar),
            onCancelar = { mostrarOlvido = false }
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    SerenaAppTheme {
        LoginScreen(onVolverClick = {}, onIniciarClick = {}, onRegistroClick = {})
    }
}
