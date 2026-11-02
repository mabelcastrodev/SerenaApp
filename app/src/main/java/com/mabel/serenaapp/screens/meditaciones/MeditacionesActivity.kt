package com.mabel.serenaapp.screens.meditaciones

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.R
import com.mabel.serenaapp.models.Meditacion
import com.mabel.serenaapp.navegacion.irASeccion
import com.mabel.serenaapp.ui.components.Seccion
import com.mabel.serenaapp.ui.components.SerenaBarraNavegacion
import com.mabel.serenaapp.ui.components.SerenaCargando
import com.mabel.serenaapp.ui.components.SerenaEncabezado
import com.mabel.serenaapp.ui.components.SerenaError
import com.mabel.serenaapp.ui.components.SerenaInput
import com.mabel.serenaapp.ui.components.SerenaVacio
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

// Datos que recibe esta pantalla por el Intent
const val EXTRA_IDCATEGORIA = "idcategoria"
const val EXTRA_NOMBRE_CATEGORIA = "nombre_categoria"

/*
 * PANTALLA SESIONES
 *  - Desde el menu inferior: todas las sesiones (con menu abajo)
 *  - Desde una categoria:    solo las de esa categoria (con flecha)
 * Tocar una sesion abre su detalle.
 */
class MeditacionesActivity : ComponentActivity() {
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

        val viewModel = ViewModelProvider(this)[MeditacionesViewModel::class.java]
        val nombreCategoria = intent.getStringExtra(EXTRA_NOMBRE_CATEGORIA)

        setContent {
            SerenaAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                MeditacionesScreen(
                    uiState = uiState,
                    nombreCategoria = nombreCategoria,
                    onVolver = if (nombreCategoria != null) {
                        { finish() }
                    } else {
                        null
                    },
                    onReintentar = { viewModel.cargarMeditaciones() },
                    onMeditacionClick = { meditacion ->
                        val intent = Intent(this, MeditacionDetalleActivity::class.java)
                        intent.putExtra(EXTRA_IDMEDITACION, meditacion.idmeditacion)
                        startActivity(intent)
                    },
                    onNavegar = { seccion -> irASeccion(seccion, Seccion.SESIONES) }
                )
            }
        }
    }
}

@Composable
fun MeditacionesScreen(
    uiState: MeditacionesUIState,
    nombreCategoria: String?,
    onVolver: (() -> Unit)?,
    onReintentar: () -> Unit,
    onMeditacionClick: (Meditacion) -> Unit,
    onNavegar: (Seccion) -> Unit
) {
    // Texto del buscador (sobrevive al girar la pantalla)
    var busqueda by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            // El menu inferior solo va en la lista general
            if (onVolver == null) {
                SerenaBarraNavegacion(actual = Seccion.SESIONES, onSeleccion = onNavegar)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SerenaEncabezado(
                titulo = nombreCategoria ?: stringResource(R.string.sesiones_titulo),
                subtitulo = if (nombreCategoria != null) {
                    stringResource(R.string.sesiones_subtitulo_categoria)
                } else {
                    stringResource(R.string.sesiones_subtitulo)
                },
                onVolver = onVolver
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (uiState) {
                    MeditacionesUIState.Loading -> SerenaCargando()

                    is MeditacionesUIState.Error -> SerenaError(onReintentar = onReintentar)

                    is MeditacionesUIState.Success -> ListaMeditaciones(
                        meditaciones = uiState.meditaciones,
                        busqueda = busqueda,
                        onBusquedaCambio = { busqueda = it },
                        onMeditacionClick = onMeditacionClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ListaMeditaciones(
    meditaciones: List<Meditacion>,
    busqueda: String,
    onBusquedaCambio: (String) -> Unit,
    onMeditacionClick: (Meditacion) -> Unit
) {
    val buscando = busqueda.isNotBlank()

    // Filtro local por titulo o autor (no vuelve a llamar al servidor)
    val filtradas = remember(meditaciones, busqueda) {
        if (!buscando) {
            meditaciones
        } else {
            meditaciones.filter {
                it.titulo.contains(busqueda.trim(), ignoreCase = true) ||
                    it.autor.contains(busqueda.trim(), ignoreCase = true)
            }
        }
    }

    // La destacada va arriba en grande (solo si no se esta buscando)
    val destacada: Meditacion? = if (buscando) {
        null
    } else {
        filtradas.firstOrNull { it.destacado == 1 } ?: filtradas.firstOrNull()
    }
    val resto = filtradas.filter { it.idmeditacion != destacada?.idmeditacion }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppTheme.spacing.regular),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
    ) {
        item {
            SerenaInput(
                valor = busqueda,
                onValorCambio = onBusquedaCambio,
                placeholder = stringResource(R.string.sesiones_buscar),
                iconoInicio = R.drawable.ic_search
            )
        }

        when {
            meditaciones.isEmpty() -> item {
                SerenaVacio(mensaje = stringResource(R.string.sesiones_sin_datos))
            }

            filtradas.isEmpty() -> item {
                SerenaVacio(
                    mensaje = stringResource(R.string.sesiones_sin_resultados, busqueda),
                    textoBoton = stringResource(R.string.sesiones_limpiar),
                    onBoton = { onBusquedaCambio("") }
                )
            }

            else -> {
                if (buscando) {
                    item {
                        Text(
                            stringResource(R.string.sesiones_resultados, filtradas.size),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (destacada != null) {
                    item(key = "destacada") {
                        MeditacionDestacada(
                            meditacion = destacada,
                            onClick = { onMeditacionClick(destacada) }
                        )
                    }
                }
                items(resto, key = { it.idmeditacion }) { meditacion ->
                    MeditacionItem(
                        meditacion = meditacion,
                        onClick = { onMeditacionClick(meditacion) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MeditacionesScreenPreview() {
    SerenaAppTheme {
        MeditacionesScreen(
            uiState = MeditacionesUIState.Success(
                listOf(
                    Meditacion(1, 1, "Ansiedad", "Paz en tu día", "Aprende a meditar en solo 5 minutos", 5, "Principiante", "Serena", 1),
                    Meditacion(2, 1, "Ansiedad", "Soltar la preocupación", "Deja ir lo que no puedes controlar", 12, "Principiante", "Lucía Fernández", 0)
                )
            ),
            nombreCategoria = null,
            onVolver = null,
            onReintentar = {},
            onMeditacionClick = {},
            onNavegar = {}
        )
    }
}
