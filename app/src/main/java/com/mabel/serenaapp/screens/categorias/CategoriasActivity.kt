package com.mabel.serenaapp.screens.categorias

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.R
import com.mabel.serenaapp.models.Categoria
import com.mabel.serenaapp.navegacion.irASeccion
import com.mabel.serenaapp.screens.meditaciones.EXTRA_IDCATEGORIA
import com.mabel.serenaapp.screens.meditaciones.EXTRA_NOMBRE_CATEGORIA
import com.mabel.serenaapp.screens.meditaciones.MeditacionesActivity
import com.mabel.serenaapp.ui.components.EstiloBoton
import com.mabel.serenaapp.ui.components.Seccion
import com.mabel.serenaapp.ui.components.SerenaBarraNavegacion
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaCard
import com.mabel.serenaapp.ui.components.SerenaCargando
import com.mabel.serenaapp.ui.components.SerenaEncabezado
import com.mabel.serenaapp.ui.components.SerenaError
import com.mabel.serenaapp.ui.components.SerenaVacio
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

/*
 * PANTALLA CATEGORIAS (maestro)
 * Lista las categorias del servicio web. Al tocar una se abre
 * Sesiones filtrada por esa categoria (detalle).
 */
class CategoriasActivity : ComponentActivity() {
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

        val viewModel = ViewModelProvider(this)[CategoriasViewModel::class.java]

        setContent {
            SerenaAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CategoriasScreen(
                    uiState = uiState,
                    onReintentar = { viewModel.cargarCategorias() },
                    onCategoriaClick = { categoria ->
                        val intent = Intent(this, MeditacionesActivity::class.java)
                        intent.putExtra(EXTRA_IDCATEGORIA, categoria.idcategoria)
                        intent.putExtra(EXTRA_NOMBRE_CATEGORIA, categoria.nombre)
                        startActivity(intent)
                    },
                    onVerTodasClick = { irASeccion(Seccion.SESIONES, Seccion.CATEGORIAS) },
                    onNavegar = { seccion -> irASeccion(seccion, Seccion.CATEGORIAS) }
                )
            }
        }
    }
}

@Composable
fun CategoriasScreen(
    uiState: CategoriasUIState,
    onReintentar: () -> Unit,
    onCategoriaClick: (Categoria) -> Unit,
    onVerTodasClick: () -> Unit,
    onNavegar: (Seccion) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            SerenaBarraNavegacion(actual = Seccion.CATEGORIAS, onSeleccion = onNavegar)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SerenaEncabezado(
                titulo = stringResource(R.string.categorias_titulo),
                subtitulo = stringResource(R.string.categorias_subtitulo)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (uiState) {
                    CategoriasUIState.Loading -> SerenaCargando()

                    is CategoriasUIState.Error -> SerenaError(onReintentar = onReintentar)

                    is CategoriasUIState.Success -> {
                        if (uiState.categorias.isEmpty()) {
                            SerenaVacio(mensaje = stringResource(R.string.categorias_sin_datos))
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(AppTheme.spacing.regular),
                                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
                            ) {
                                items(uiState.categorias, key = { it.idcategoria }) { categoria ->
                                    CategoriaItem(
                                        categoria = categoria,
                                        onClick = { onCategoriaClick(categoria) }
                                    )
                                }
                                item {
                                    TarjetaVerTodas(onVerTodasClick = onVerTodasClick)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Al final de la lista: atajo a todas las sesiones
@Composable
private fun TarjetaVerTodas(onVerTodasClick: () -> Unit) {
    SerenaCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.categorias_todas_titulo),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            stringResource(R.string.categorias_todas_desc),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
        SerenaBoton(
            texto = stringResource(R.string.categorias_todas_boton),
            onClick = onVerTodasClick,
            estilo = EstiloBoton.AZUL,
            anchoCompleto = true,
            pequeno = true
        )
    }
}

@Preview
@Composable
private fun CategoriasScreenPreview() {
    SerenaAppTheme {
        CategoriasScreen(
            uiState = CategoriasUIState.Success(
                listOf(
                    Categoria(1, "Ansiedad", "Prácticas cortas para calmar la mente", 6),
                    Categoria(2, "Dormir", "Sonidos y respiraciones suaves", 6)
                )
            ),
            onReintentar = {},
            onCategoriaClick = {},
            onVerTodasClick = {},
            onNavegar = {}
        )
    }
}
