package com.mabel.serenaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppForeground
import com.mabel.serenaapp.ui.theme.AppPrimary
import com.mabel.serenaapp.ui.theme.AppPrimaryLight
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

class EmpezarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fondo azul: íconos de la barra de estado en blanco
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            SerenaAppTheme {
                EmpezarScreen(
                    onSaltarClick = {
                        startActivity(Intent(this, LoginActivity::class.java))
                    },
                    onComenzarClick = {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                )
            }//SerenaAppTheme
        }
    }
}

@Composable
fun EmpezarScreen(
    onSaltarClick: () -> Unit,
    onComenzarClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppPrimary)
    ) {
        // Círculos decorativos del fondo
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 110.dp, y = 140.dp)
                .size(AppTheme.sizes.bigCircleSize)
                .background(AppPrimaryLight, CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 55.dp, y = 90.dp)
                .size(AppTheme.sizes.ringSize)
                .border(AppTheme.sizes.ringStroke, AppPrimaryLight, CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = AppTheme.spacing.screenMargin)
        ) {
            // Fila superior: puntos decorativos y "Saltar"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.regular),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.padding(top = AppTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.dotGap)
                ) {
                    repeat(4) {
                        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.dotGap)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(AppTheme.sizes.dotSize)
                                        .background(Color.White.copy(alpha = 0.6f), CircleShape)
                                )
                            }
                        }
                    }
                }
                TextButton(onClick = onSaltarClick) {
                    Text(
                        stringResource(R.string.empezar_saltar),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                }
            }

            // Ilustración: ocupa el espacio libre entre la fila superior y los textos
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.img_onboarding),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                stringResource(R.string.empezar_titulo),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
            Text(
                stringResource(R.string.empezar_contenido),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.large))

            Button(
                onClick = onComenzarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.sizes.buttonHeight),
                shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppAccent,
                    contentColor = AppForeground
                )
            ) {
                Text(
                    stringResource(R.string.empezar_boton),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.spacing.large))
        }
    }
}
