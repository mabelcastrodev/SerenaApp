package com.mabel.serenaapp.screens.perfil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.MainActivity
import com.mabel.serenaapp.R
import com.mabel.serenaapp.models.animoDe
import com.mabel.serenaapp.navegacion.irASeccion
import com.mabel.serenaapp.ui.components.EstiloBoton
import com.mabel.serenaapp.ui.components.Seccion
import com.mabel.serenaapp.ui.components.SerenaBarraNavegacion
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaCard
import com.mabel.serenaapp.ui.components.SerenaEncabezado
import com.mabel.serenaapp.ui.components.SerenaInput
import com.mabel.serenaapp.ui.components.SerenaModal
import com.mabel.serenaapp.ui.components.SerenaTituloSeccion
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme
import com.mabel.serenaapp.utils.correoValido

/*
 * PANTALLA PERFIL
 * Datos del usuario, estadisticas de Mi Diario, formulario para
 * editar (SerenaInput) y cerrar sesion con confirmacion
 * (SerenaModal de dos botones).
 */
class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        val viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]

        setContent {
            SerenaAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                PerfilScreen(
                    uiState = uiState,
                    onGuardar = { nombre, correo -> viewModel.guardarDatos(nombre, correo) },
                    onCerrarSesion = {
                        // Vuelve a la portada y borra todas las pantallas anteriores
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    },
                    onNavegar = { seccion -> irASeccion(seccion, Seccion.PERFIL) }
                )
            }
        }
    }
}

@Composable
fun PerfilScreen(
    uiState: PerfilUIState,
    onGuardar: (nombre: String, correo: String) -> Unit,
    onCerrarSesion: () -> Unit,
    onNavegar: (Seccion) -> Unit
) {
    // El formulario empieza con lo guardado
    var nombre by rememberSaveable(uiState.nombre) { mutableStateOf(uiState.nombre) }
    var correo by rememberSaveable(uiState.correo) { mutableStateOf(uiState.correo) }
    var errorNombre by rememberSaveable { mutableStateOf<String?>(null) }
    var errorCorreo by rememberSaveable { mutableStateOf<String?>(null) }
    var mostrarGuardado by rememberSaveable { mutableStateOf(false) }
    var mostrarSalir by rememberSaveable { mutableStateOf(false) }

    val textoErrorNombre = stringResource(R.string.error_nombre)
    val textoErrorCorreo = stringResource(R.string.error_correo)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            SerenaBarraNavegacion(actual = Seccion.PERFIL, onSeleccion = onNavegar)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SerenaEncabezado(
                titulo = stringResource(R.string.perfil_titulo),
                subtitulo = stringResource(R.string.perfil_subtitulo)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.spacing.regular),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
            ) {
                TarjetaUsuario(nombre = uiState.nombre, correo = uiState.correo)

                // Tres estadisticas, cada una en una SerenaCard
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
                    Estadistica(
                        valor = uiState.totalEntradas.toString(),
                        etiqueta = stringResource(R.string.perfil_stat_entradas),
                        modifier = Modifier.weight(1f)
                    )
                    Estadistica(
                        valor = uiState.diasConEntrada.toString(),
                        etiqueta = stringResource(R.string.perfil_stat_dias),
                        modifier = Modifier.weight(1f)
                    )
                    Estadistica(
                        valor = uiState.ultimoAnimo?.let { animoDe(it).emoji } ?: "—",
                        etiqueta = stringResource(R.string.perfil_stat_animo),
                        modifier = Modifier.weight(1f)
                    )
                }

                SerenaTituloSeccion(texto = stringResource(R.string.perfil_editar))

                SerenaCard(modifier = Modifier.fillMaxWidth()) {
                    SerenaInput(
                        valor = nombre,
                        onValorCambio = { nombre = it; errorNombre = null },
                        placeholder = stringResource(R.string.registro_nombre),
                        iconoInicio = R.drawable.ic_person,
                        error = errorNombre
                    )
                    Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
                    SerenaInput(
                        valor = correo,
                        onValorCambio = { correo = it; errorCorreo = null },
                        placeholder = stringResource(R.string.registro_correo),
                        tipoTeclado = KeyboardType.Email,
                        error = errorCorreo
                    )
                    Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
                    SerenaBoton(
                        texto = stringResource(R.string.perfil_guardar),
                        onClick = {
                            errorNombre = if (nombre.isBlank()) textoErrorNombre else null
                            errorCorreo = if (!correoValido(correo)) textoErrorCorreo else null
                            if (errorNombre == null && errorCorreo == null) {
                                onGuardar(nombre, correo)
                                mostrarGuardado = true
                            }
                        },
                        estilo = EstiloBoton.AZUL,
                        anchoCompleto = true
                    )
                }

                SerenaBoton(
                    texto = stringResource(R.string.perfil_cerrar_sesion),
                    onClick = { mostrarSalir = true },
                    estilo = EstiloBoton.CONTORNO,
                    icono = R.drawable.ic_logout,
                    anchoCompleto = true
                )
            }
        }
    }

    if (mostrarGuardado) {
        SerenaModal(
            titulo = stringResource(R.string.perfil_guardado_titulo),
            mensaje = stringResource(R.string.perfil_guardado_mensaje),
            textoConfirmar = stringResource(R.string.aceptar),
            onConfirmar = { mostrarGuardado = false },
            icono = R.drawable.ic_check
        )
    }

    if (mostrarSalir) {
        SerenaModal(
            titulo = stringResource(R.string.perfil_salir_titulo),
            mensaje = stringResource(R.string.perfil_salir_mensaje),
            textoConfirmar = stringResource(R.string.perfil_salir_si),
            onConfirmar = {
                mostrarSalir = false
                onCerrarSesion()
            },
            icono = R.drawable.ic_logout,
            textoCancelar = stringResource(R.string.cancelar),
            onCancelar = { mostrarSalir = false }
        )
    }
}

// Avatar con la inicial + nombre y correo
@Composable
private fun TarjetaUsuario(nombre: String, correo: String) {
    val nombreVisible = nombre.ifBlank { stringResource(R.string.perfil_nombre_defecto) }
    val correoVisible = correo.ifBlank { stringResource(R.string.perfil_correo_defecto) }

    SerenaCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(AppTheme.sizes.avatarSize)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    nombreVisible.trim().take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.spacing.regular))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    nombreVisible,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    correoVisible,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun Estadistica(
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    SerenaCard(modifier = modifier, relleno = AppTheme.spacing.medium) {
        Text(
            valor,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PerfilScreenPreview() {
    SerenaAppTheme {
        PerfilScreen(
            uiState = PerfilUIState("Mabel Castro", "mabel@correo.com", 4, 3, 1),
            onGuardar = { _, _ -> },
            onCerrarSesion = {},
            onNavegar = {}
        )
    }
}
