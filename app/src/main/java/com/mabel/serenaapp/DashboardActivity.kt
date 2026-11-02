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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.models.Categoria
import com.mabel.serenaapp.navegacion.irASeccion
import com.mabel.serenaapp.screens.categorias.CategoriasUIState
import com.mabel.serenaapp.screens.categorias.CategoriasViewModel
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
import com.mabel.serenaapp.ui.components.SerenaTituloSeccion
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

// El icono que le toca a cada categoria, en el mismo orden
// en que estan en la base de datos
private val iconosCategorias = listOf(
    R.drawable.ic_heart,
    R.drawable.ic_moon,
    R.drawable.ic_edit,
    R.drawable.ic_layers,
    R.drawable.ic_folder,
    R.drawable.ic_chat
)

/*
 * PANTALLA INICIO (Dashboard)
 * Usa el mismo CategoriasViewModel que CategoriasActivity.
 */
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Los iconos de las barras se adaptan al modo claro u oscuro
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

                DashboardScreen(
                    uiState = uiState,
                    onReintentar = { viewModel.cargarCategorias() },
                    onCategoriaClick = { categoria ->
                        val intent = Intent(this, MeditacionesActivity::class.java)
                        intent.putExtra(EXTRA_IDCATEGORIA, categoria.idcategoria)
                        intent.putExtra(EXTRA_NOMBRE_CATEGORIA, categoria.nombre)
                        startActivity(intent)
                    },
                    onNavegar = { seccion -> irASeccion(seccion, Seccion.INICIO) }
                )
            }//SerenaAppTheme
        }
    }
}

@Composable
fun DashboardScreen(
    uiState: CategoriasUIState,
    onReintentar: () -> Unit,
    onCategoriaClick: (Categoria) -> Unit,
    onNavegar: (Seccion) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            SerenaBarraNavegacion(actual = Seccion.INICIO, onSeleccion = onNavegar)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            // Sin titulo: muestra logo + SERENA, como la plantilla
            SerenaEncabezado()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = AppTheme.spacing.large),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.regular)
            ) {
                item {
                    HeroBanner(onIniciarClick = { onNavegar(Seccion.SESIONES) })
                }

                item {
                    SerenaTituloSeccion(
                        texto = stringResource(R.string.dashboard_categorias_title),
                        textoAccion = stringResource(R.string.dashboard_ver_todas),
                        onAccion = { onNavegar(Seccion.CATEGORIAS) },
                        modifier = Modifier.padding(horizontal = AppTheme.spacing.regular)
                    )
                }

                // Si las categorias fallan, el resto del Inicio se sigue viendo
                item {
                    when (uiState) {
                        CategoriasUIState.Loading -> SerenaCargando()
                        is CategoriasUIState.Error -> SerenaError(onReintentar = onReintentar)
                        is CategoriasUIState.Success -> TarjetaCategorias(
                            categorias = uiState.categorias,
                            onCategoriaClick = onCategoriaClick
                        )
                    }
                }

                item {
                    TarjetaDiario(
                        onClick = { onNavegar(Seccion.DIARIO) },
                        modifier = Modifier.padding(horizontal = AppTheme.spacing.regular)
                    )
                }

                item {
                    TarjetaSonidos(
                        onVerSesionesClick = { onNavegar(Seccion.SESIONES) },
                        modifier = Modifier.padding(horizontal = AppTheme.spacing.regular)
                    )
                }
            }
        }
    }
}

// Imagen grande de la plantilla
@Composable
private fun HeroBanner(onIniciarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.sizes.heroBannerHeight)
    ) {
        Image(
            painter = painterResource(R.drawable.img_hero_paz),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Capa oscura para que el texto se lea bien
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.dashboard_hero_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            Text(
                stringResource(R.string.dashboard_hero_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.regular))
            SerenaBoton(
                texto = stringResource(R.string.dashboard_hero_action),
                onClick = onIniciarClick,
                pequeno = true
            )
        }
    }
}

// Tarjeta blanca con los circulos de cada categoria
@Composable
private fun TarjetaCategorias(
    categorias: List<Categoria>,
    onCategoriaClick: (Categoria) -> Unit
) {
    SerenaCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.regular),
        relleno = AppTheme.spacing.none
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = AppTheme.spacing.large,
                vertical = AppTheme.spacing.large
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
        ) {
            items(categorias, key = { it.idcategoria }) { categoria ->
                CirculoCategoria(
                    categoria = categoria,
                    onClick = { onCategoriaClick(categoria) }
                )
            }
        }
    }
}

@Composable
private fun CirculoCategoria(
    categoria: Categoria,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppTheme.sizes.cardCornerRadius))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconContainerLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    iconosCategorias[(categoria.idcategoria - 1).mod(iconosCategorias.size)]
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppTheme.sizes.iconLarge)
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.spacing.small))
        Text(
            categoria.nombre,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            stringResource(R.string.categoria_sesiones, categoria.total),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Acceso rapido a Mi Diario (SerenaCard que se puede tocar)
@Composable
private fun TarjetaDiario(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SerenaCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(AppTheme.sizes.iconContainerMedium)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppTheme.sizes.iconRegular)
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.dashboard_diario_titulo),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.dashboard_diario_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.spacing.small))
            SerenaBoton(
                texto = stringResource(R.string.dashboard_diario_accion),
                onClick = onClick,
                estilo = EstiloBoton.AZUL,
                pequeno = true
            )
        }
    }
}

// Tarjeta oscura con imagen, tal como esta en la plantilla
@Composable
private fun TarjetaSonidos(
    onVerSesionesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.sizes.bannerHeight)
            .clip(RoundedCornerShape(AppTheme.sizes.cardCornerRadius))
    ) {
        Image(
            painter = painterResource(R.drawable.img_banner_sonidos),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.spacing.large),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.dashboard_banner_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.small))
            Text(
                stringResource(R.string.dashboard_banner_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.regular))
            SerenaBoton(
                texto = stringResource(R.string.dashboard_banner_action),
                onClick = onVerSesionesClick,
                pequeno = true
            )
        }
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    SerenaAppTheme {
        DashboardScreen(
            uiState = CategoriasUIState.Success(
                listOf(
                    Categoria(1, "Ansiedad", "", 6),
                    Categoria(2, "Dormir", "", 6),
                    Categoria(3, "Diario", "", 5)
                )
            ),
            onReintentar = {},
            onCategoriaClick = {},
            onNavegar = {}
        )
    }
}
