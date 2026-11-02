package com.mabel.serenaapp

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mabel.serenaapp.ui.theme.AppAccent
import com.mabel.serenaapp.ui.theme.AppForeground
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "https://mabelcastro.alwaysdata.net/serena/"

// Si esta en true la app usa las sesiones de ejemplo
// en lugar de conectarse al servidor.
const val USAR_DATOS_DE_PRUEBA = false

data class Categoria(
    val idcategoria: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val total: Int = 0
)

data class Meditacion(
    val idmeditacion: Int = 0,
    val idcategoria: Int = 0,
    val categoria: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val duracion: Int = 0,
    val nivel: String = "",
    val autor: String = "",
    val destacado: Int = 0
)

interface ApiService {
    @GET("categorias.php")
    suspend fun getCategorias(): List<Categoria>

    @GET("meditaciones.php")
    suspend fun getMeditaciones(): List<Meditacion>
}

object RetrofitClient {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// Sesiones de ejemplo, por si el servidor no responde
private val meditacionesDePrueba = listOf(
    Meditacion(1, 1, "Ansiedad", "Paz en tu dia", "Aprende a meditar en solo 5 minutos", 5, "Principiante", "Serena", 1),
    Meditacion(2, 1, "Ansiedad", "Soltar la preocupacion", "Deja ir lo que no puedes controlar", 12, "Principiante", "Lucia Fernandez", 0),
    Meditacion(3, 2, "Dormir", "Lluvia sobre el bosque", "Sonido continuo de lluvia para dormir profundo", 45, "Principiante", "Serena", 0),
    Meditacion(4, 2, "Dormir", "Cuerpo pesado", "Relajacion desde los pies hasta la frente", 20, "Intermedio", "Diego Ramos", 0),
    Meditacion(5, 3, "Diario", "Mi semana en tres lineas", "Una pauta breve para escribir como te fue", 6, "Principiante", "Serena", 0),
    Meditacion(6, 4, "Respiracion", "Respiracion 4-7-8", "Inhala cuatro, manten siete, exhala ocho", 3, "Principiante", "Serena", 0),
    Meditacion(7, 5, "Concentracion", "Enfoque profundo", "Ruido blanco suave para estudiar", 60, "Intermedio", "Serena", 0),
    Meditacion(8, 6, "Gratitud", "Tres cosas buenas", "Reconoce tres momentos que valieron la pena", 5, "Principiante", "Lucia Fernandez", 0)
)

class SesionesActivity : ComponentActivity() {
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
                SesionesScreen(
                    onVolverClick = {
                        finish()
                    }
                )
            }//SerenaAppTheme
        }
    }
}

@Composable
fun SesionesScreen(
    onVolverClick: () -> Unit
) {
    // Lo que traemos de la base de datos
    var meditaciones by remember { mutableStateOf<List<Meditacion>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    // Se ejecuta una sola vez al abrir la pantalla
    LaunchedEffect(Unit) {
        try {
            if (USAR_DATOS_DE_PRUEBA) {
                meditaciones = meditacionesDePrueba
                error = true
            } else {
                meditaciones = RetrofitClient.api.getMeditaciones()
            }
        } catch (e: Exception) {
            error = true
            meditaciones = meditacionesDePrueba
        } finally {
            cargando = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            EncabezadoSesiones(onVolverClick = onVolverClick)

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

                    meditaciones.isEmpty() -> Text(
                        stringResource(R.string.dashboard_sin_datos),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(AppTheme.spacing.regular),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
                    ) {
                        items(meditaciones, key = { it.idmeditacion }) { meditacion ->
                            SesionCard(meditacion = meditacion)
                        }
                    }
                }
            }
        }
    }
}

// Barra superior con la flecha de volver y el titulo
@Composable
private fun EncabezadoSesiones(onVolverClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                end = AppTheme.spacing.regular,
                top = AppTheme.spacing.small,
                bottom = AppTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolverClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.login_back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Column {
            Text(
                stringResource(R.string.sesiones_titulo),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(R.string.sesiones_subtitulo),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SesionCard(meditacion: Meditacion) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cardCornerRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = AppTheme.sizes.cardElevation
        )
    ) {
        Column(modifier = Modifier.padding(AppTheme.spacing.regular)) {

            // Titulo y duracion
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    meditacion.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(AppTheme.spacing.small))
                Text(
                    stringResource(R.string.meditacion_duracion, meditacion.duracion),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppAccent
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))

            // Descripcion
            Text(
                meditacion.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

            // Categoria, nivel y autor
            Row(verticalAlignment = Alignment.CenterVertically) {
                Etiqueta(texto = meditacion.categoria)
                Spacer(modifier = Modifier.width(AppTheme.spacing.small))
                Etiqueta(texto = meditacion.nivel)
                Spacer(modifier = Modifier.width(AppTheme.spacing.small))
                Text(
                    stringResource(R.string.sesiones_autor, meditacion.autor),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(AppTheme.sizes.buttonCornerRadius)
            )
            .padding(
                horizontal = AppTheme.spacing.small,
                vertical = AppTheme.spacing.micro
            )
    )
}

/*
 * Barra amarilla que avisa que los datos no vienen del
 * servidor.
 */
@Composable
fun AvisoSinConexion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppAccent)
            .padding(
                horizontal = AppTheme.spacing.regular,
                vertical = AppTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alerta),
            contentDescription = null,
            tint = AppForeground,
            modifier = Modifier.size(AppTheme.sizes.iconRegular)
        )
        Spacer(modifier = Modifier.width(AppTheme.spacing.small))
        Text(
            stringResource(R.string.dashboard_sin_conexion),
            style = MaterialTheme.typography.bodySmall,
            color = AppForeground
        )
    }
}
