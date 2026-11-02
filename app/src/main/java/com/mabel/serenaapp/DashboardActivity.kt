package com.mabel.serenaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppForeground
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme


private val categoriasDePrueba = listOf(
    Categoria(1, "Ansiedad", "Practicas cortas para calmar la mente", 6),
    Categoria(2, "Dormir", "Respiraciones suaves para dormir mejor", 6),
    Categoria(3, "Diario", "Escribe como te sentiste hoy", 5),
    Categoria(4, "Respiracion", "Ejercicios guiados de 3 a 10 minutos", 5),
    Categoria(5, "Concentracion", "Sesiones para estudiar sin distraerte", 5),
    Categoria(6, "Gratitud", "Reconoce lo bueno que ya tienes", 6)
)

// Secciones de la app
sealed interface AppRoute {
    data object Inicio : AppRoute
    data object Paginas : AppRoute
    data object Elementos : AppRoute
    data object Chat : AppRoute
    data object Ajustes : AppRoute
}

// Cada opcion del menu inferior
data class BottomNavItem(
    val route: AppRoute,
    @StringRes val labelRes: Int,
    @DrawableRes val icono: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(AppRoute.Inicio, R.string.bottom_nav_home, R.drawable.ic_home),
    BottomNavItem(AppRoute.Paginas, R.string.bottom_nav_pages, R.drawable.ic_layers),
    BottomNavItem(AppRoute.Elementos, R.string.bottom_nav_elements, R.drawable.ic_folder),
    BottomNavItem(AppRoute.Chat, R.string.bottom_nav_chat, R.drawable.ic_chat),
    BottomNavItem(AppRoute.Ajustes, R.string.bottom_nav_settings, R.drawable.ic_settings)
)

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
        setContent {
            SerenaAppTheme {
                DashboardScreen(
                    onVerSesionesClick = {
                        startActivity(Intent(this, SesionesActivity::class.java))
                    },
                    onNavigate = { route ->
                        when (route) {
                            AppRoute.Inicio -> {}
                            AppRoute.Paginas -> {}
                            AppRoute.Elementos -> {
                                startActivity(Intent(this, SesionesActivity::class.java))
                            }
                            AppRoute.Chat -> {}
                            AppRoute.Ajustes -> {}
                        }
                    }
                )
            }//SerenaAppTheme
        }
    }
}

@Composable
fun DashboardScreen(
    onVerSesionesClick: () -> Unit,
    onNavigate: (AppRoute) -> Unit
) {
    // Lo que traemos de la base de datos
    var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    // Se ejecuta una sola vez al abrir la pantalla
    LaunchedEffect(Unit) {
        try {
            if (USAR_DATOS_DE_PRUEBA) {
                categorias = categoriasDePrueba
                error = true
            } else {
                categorias = RetrofitClient.api.getCategorias()
            }
        } catch (e: Exception) {
            // Si el servidor no responde, la app igual muestra
            // algo y avisa arriba que no vienen de la base.
            error = true
            categorias = categoriasDePrueba
        } finally {
            cargando = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            SerenaBottomNavigation(onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            EncabezadoSerena()

            if (error) {
                AvisoSinConexion()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when {
                    cargando -> CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(AppTheme.sizes.progressSize),
                        color = MaterialTheme.colorScheme.primary
                    )

                    else -> ContenidoDashboard(
                        categorias = categorias,
                        onVerSesionesClick = onVerSesionesClick
                    )
                }
            }
        }
    }
}

// Barra superior blanca con el logo, igual que la plantilla
@Composable
private fun EncabezadoSerena() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                horizontal = AppTheme.spacing.regular,
                vertical = AppTheme.spacing.medium
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.size(AppTheme.sizes.logoSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.small))
            Text(
                stringResource(R.string.app_name_upper),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconContainerSmall)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = stringResource(R.string.menu),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(AppTheme.sizes.iconMedium)
            )
        }
    }
}

@Composable
private fun ContenidoDashboard(
    categorias: List<Categoria>,
    onVerSesionesClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = AppTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.regular)
    ) {
        item {
            HeroBanner(onIniciarClick = onVerSesionesClick)
        }

        if (categorias.isNotEmpty()) {
            item {
                TituloSeccion(stringResource(R.string.dashboard_categorias_title))
            }
            item {
                TarjetaCategorias(categorias = categorias)
            }
        }

        item {
            TarjetaSonidos(
                onVerSesionesClick = onVerSesionesClick,
                modifier = Modifier.padding(horizontal = AppTheme.spacing.regular)
            )
        }
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = AppTheme.spacing.regular)
    )
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
            BotonAmarillo(
                text = stringResource(R.string.dashboard_hero_action),
                onClick = onIniciarClick
            )
        }
    }
}

// Tarjeta blanca con los circulos de cada categoria
@Composable
private fun TarjetaCategorias(categorias: List<Categoria>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.regular),
        shape = RoundedCornerShape(AppTheme.sizes.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                CategoriaItem(categoria = categoria)
            }
        }
    }
}

@Composable
private fun CategoriaItem(categoria: Categoria) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            BotonAmarillo(
                text = stringResource(R.string.dashboard_banner_action),
                onClick = onVerSesionesClick
            )
        }
    }
}

// Menu inferior de la plantilla
@Composable
private fun SerenaBottomNavigation(
    selectedRoute: AppRoute = AppRoute.Inicio,
    onNavigate: (AppRoute) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == selectedRoute,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(item.icono),
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.sizes.iconMedium)
                    )
                },
                label = {
                    Text(
                        stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

// Boton amarillo pequeño de la plantilla
@Composable
private fun BotonAmarillo(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(AppTheme.sizes.buttonHeightSmall),
        shape = RoundedCornerShape(AppTheme.sizes.buttonCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppAccent,
            contentColor = AppForeground
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
