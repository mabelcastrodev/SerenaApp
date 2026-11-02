package com.mabel.serenaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppDark
import com.mabel.serenaapp.ui.theme.AppDarkSurface
import com.mabel.serenaapp.ui.theme.AppMuted
import com.mabel.serenaapp.ui.theme.AppPrimary
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fondo oscuro: íconos de la barra de estado en blanco
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
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }
                )
            }//SerenaAppTheme
        }
    }
}

@Composable
fun LoginScreen(
    onVolverClick: () -> Unit,
    onIniciarClick: () -> Unit,
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }

    val colorCampo = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = AppDarkSurface,
        unfocusedContainerColor = AppDarkSurface,
        cursorColor = AppAccent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedPlaceholderColor = AppMuted,
        unfocusedPlaceholderColor = AppMuted,
        focusedTrailingIconColor = AppMuted,
        unfocusedTrailingIconColor = AppMuted
    )

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

        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.login_username)) },
            singleLine = true,
            shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
            colors = colorCampo
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.login_password)) },
            singleLine = true,
            visualTransformation = if (verPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Icon(
                        painter = painterResource(
                            if (verPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                        ),
                        contentDescription = stringResource(R.string.login_show_password)
                    )
                }
            },
            shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
            colors = colorCampo
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        Button(
            onClick = onIniciarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.sizes.buttonHeight),
            shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                stringResource(R.string.login_button),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Text(
            stringResource(R.string.login_forgot),
            style = MaterialTheme.typography.labelMedium,
            color = AppAccent
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
                color = AppAccent
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))
    }
}
