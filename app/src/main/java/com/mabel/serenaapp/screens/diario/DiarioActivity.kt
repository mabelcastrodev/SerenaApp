package com.mabel.serenaapp.screens.diario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.R
import com.mabel.serenaapp.models.EntradaDiario
import com.mabel.serenaapp.models.animoDe
import com.mabel.serenaapp.models.animos
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
import com.mabel.serenaapp.ui.components.SerenaVacio
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme
import com.mabel.serenaapp.utils.formatearFecha

/*
 * PANTALLA MI DIARIO
 * Componentes: SerenaCard (animos y entradas), SerenaInput de
 * varias lineas, SerenaBoton y SerenaModal (aviso, confirmacion
 * y ver/eliminar una entrada).
 */
class DiarioActivity : ComponentActivity() {
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

        val viewModel = ViewModelProvider(this)[DiarioViewModel::class.java]

        setContent {
            SerenaAppTheme {
                val entradas by viewModel.entradas.collectAsStateWithLifecycle()

                DiarioScreen(
                    entradas = entradas,
                    onGuardar = { animo, texto -> viewModel.guardarEntrada(animo, texto) },
                    onEliminar = { id -> viewModel.eliminarEntrada(id) },
                    onNavegar = { seccion -> irASeccion(seccion, Seccion.DIARIO) }
                )
            }
        }
    }
}

@Composable
fun DiarioScreen(
    entradas: List<EntradaDiario>,
    onGuardar: (animo: Int, texto: String) -> Unit,
    onEliminar: (id: Long) -> Unit,
    onNavegar: (Seccion) -> Unit
) {
    // -1 = todavia no elige animo
    var animoElegido by rememberSaveable { mutableStateOf(-1) }
    var texto by rememberSaveable { mutableStateOf("") }
    var mostrarFalta by rememberSaveable { mutableStateOf(false) }
    var mostrarGuardada by rememberSaveable { mutableStateOf(false) }
    var idAbierta by rememberSaveable { mutableStateOf<Long?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            SerenaBarraNavegacion(actual = Seccion.DIARIO, onSeleccion = onNavegar)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SerenaEncabezado(
                titulo = stringResource(R.string.diario_titulo),
                subtitulo = stringResource(R.string.diario_subtitulo)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(AppTheme.spacing.regular),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
            ) {
                item {
                    NuevaEntrada(
                        animoElegido = animoElegido,
                        onAnimoClick = { animoElegido = it },
                        texto = texto,
                        onTextoCambio = { texto = it },
                        onGuardarClick = {
                            if (animoElegido < 0 || texto.isBlank()) {
                                mostrarFalta = true
                            } else {
                                onGuardar(animoElegido, texto)
                                animoElegido = -1
                                texto = ""
                                mostrarGuardada = true
                            }
                        }
                    )
                }

                item {
                    SerenaTituloSeccion(texto = stringResource(R.string.diario_mis_entradas))
                }

                if (entradas.isEmpty()) {
                    item { SerenaVacio(mensaje = stringResource(R.string.diario_vacio)) }
                } else {
                    items(entradas, key = { it.id }) { entrada ->
                        EntradaItem(entrada = entrada, onClick = { idAbierta = entrada.id })
                    }
                }
            }
        }
    }

    // ---- Modales (todos son SerenaModal) ----

    if (mostrarFalta) {
        SerenaModal(
            titulo = stringResource(R.string.diario_falta_titulo),
            mensaje = stringResource(R.string.diario_falta_mensaje),
            textoConfirmar = stringResource(R.string.aceptar),
            onConfirmar = { mostrarFalta = false },
            icono = R.drawable.ic_alerta
        )
    }

    if (mostrarGuardada) {
        SerenaModal(
            titulo = stringResource(R.string.diario_guardada_titulo),
            mensaje = stringResource(R.string.diario_guardada_mensaje),
            textoConfirmar = stringResource(R.string.aceptar),
            onConfirmar = { mostrarGuardada = false },
            icono = R.drawable.ic_check
        )
    }

    val entradaAbierta = entradas.firstOrNull { it.id == idAbierta }
    if (entradaAbierta != null) {
        val animo = animoDe(entradaAbierta.animo)
        SerenaModal(
            titulo = animo.emoji + " " + stringResource(animo.nombre),
            mensaje = formatearFecha(entradaAbierta.fecha) + "\n\n" + entradaAbierta.texto,
            textoConfirmar = stringResource(R.string.cerrar),
            onConfirmar = { idAbierta = null },
            textoCancelar = stringResource(R.string.diario_eliminar),
            onCancelar = {
                onEliminar(entradaAbierta.id)
                idAbierta = null
            },
            // Tocar fuera del modal solo lo cierra, nunca elimina
            onDescartar = { idAbierta = null }
        )
    }
}

// Tarjeta para escribir una entrada nueva
@Composable
private fun NuevaEntrada(
    animoElegido: Int,
    onAnimoClick: (Int) -> Unit,
    texto: String,
    onTextoCambio: (String) -> Unit,
    onGuardarClick: () -> Unit
) {
    SerenaCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.diario_pregunta),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        // Cinco SerenaCard pequenas; la elegida tiene borde azul
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
        ) {
            animos.forEachIndexed { posicion, animo ->
                SerenaCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(AppTheme.sizes.moodSize),
                    onClick = { onAnimoClick(posicion) },
                    seleccionada = posicion == animoElegido,
                    relleno = AppTheme.spacing.extraSmall
                ) {
                    Text(
                        animo.emoji,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }

        if (animoElegido >= 0) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            Text(
                stringResource(animoDe(animoElegido).nombre),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        SerenaInput(
            valor = texto,
            onValorCambio = onTextoCambio,
            placeholder = stringResource(R.string.diario_placeholder),
            lineas = 5
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        SerenaBoton(
            texto = stringResource(R.string.diario_guardar),
            onClick = onGuardarClick,
            estilo = EstiloBoton.AZUL,
            icono = R.drawable.ic_check,
            anchoCompleto = true
        )
    }
}

// Una entrada guardada: emoji, fecha y las primeras lineas
@Composable
private fun EntradaItem(
    entrada: EntradaDiario,
    onClick: () -> Unit
) {
    val animo = animoDe(entrada.animo)
    SerenaCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(animo.emoji, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.width(AppTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(animo.nombre),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    formatearFecha(entrada.fecha),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(AppTheme.spacing.small))
        Text(
            entrada.texto,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun DiarioScreenPreview() {
    SerenaAppTheme {
        DiarioScreen(
            entradas = listOf(
                EntradaDiario(1, 1, "Hoy medité cinco minutos antes del examen y me sentí mejor.", 1_761_900_000_000),
                EntradaDiario(2, 4, "Mucho trabajo en la semana.", 1_761_800_000_000)
            ),
            onGuardar = { _, _ -> },
            onEliminar = {},
            onNavegar = {}
        )
    }
}
