package com.mabel.serenaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.theme.AppPrimary
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fondo azul: íconos de la barra de estado en blanco
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            SerenaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppPrimary
                ) {
                    MainScreen(
                        onStartClick = {
                            startActivity(Intent(this@MainActivity, EmpezarActivity::class.java))
                        }
                    )
                }
            }//SerenaAppTheme
        }
    }
}

@Composable
fun MainScreen(
    onStartClick: () -> Unit
) {
    // Arriba: frase y autor
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(top = AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.frase),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            stringResource(R.string.autor),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }

    // Centro: logo, nombre y botón
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier.size(AppTheme.sizes.logoSizeLarge)
        )
        Text(
            stringResource(R.string.app_name_upper),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.large))
        // Componente reutilizable (ui/components/SerenaBoton.kt)
        SerenaBoton(
            texto = stringResource(R.string.empezar),
            onClick = onStartClick
        )
    }

    // Abajo: derechos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(bottom = AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            stringResource(R.string.derechos),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D5AFD)
@Composable
private fun MainScreenPreview() {
    SerenaAppTheme {
        MainScreen(onStartClick = {})
    }
}
