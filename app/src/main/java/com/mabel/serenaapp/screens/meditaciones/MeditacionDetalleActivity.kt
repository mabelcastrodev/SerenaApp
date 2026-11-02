package com.mabel.serenaapp.screens.meditaciones

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mabel.serenaapp.R
import com.mabel.serenaapp.data.remote.BASE_URL
import com.mabel.serenaapp.models.Meditacion
import com.mabel.serenaapp.ui.components.EstiloBoton
import com.mabel.serenaapp.ui.components.SerenaBoton
import com.mabel.serenaapp.ui.components.SerenaCard
import com.mabel.serenaapp.ui.components.SerenaCargando
import com.mabel.serenaapp.ui.components.SerenaEncabezado
import com.mabel.serenaapp.ui.components.SerenaError
import com.mabel.serenaapp.ui.components.SerenaEtiqueta
import com.mabel.serenaapp.ui.components.SerenaImagenRemota
import com.mabel.serenaapp.ui.components.SerenaModal
import com.mabel.serenaapp.ui.theme.AppTheme
import com.mabel.serenaapp.ui.theme.SerenaAppTheme

const val EXTRA_IDMEDITACION = "idmeditacion"

/*
 * PANTALLA DETALLE DE UNA SESION
 * Recibe el id por el Intent; el ViewModel lo lee con
 * SavedStateHandle y pide meditaciones.php?idmeditacion=N
 */
class MeditacionDetalleActivity : ComponentActivity() {
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

        val viewModel = ViewModelProvider(this)[MeditacionDetalleViewModel::class.java]

        setContent {
            SerenaAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                MeditacionDetalleScreen(
                    uiState = uiState,
                    onVolver = { finish() },
                    onReintentar = { viewModel.cargarMeditacion() },
                    onVerCategoria = { meditacion ->
                        val intent = Intent(this, MeditacionesActivity::class.java)
                        intent.putExtra(EXTRA_IDCATEGORIA, meditacion.idcategoria)
                        intent.putExtra(EXTRA_NOMBRE_CATEGORIA, meditacion.categoria)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MeditacionDetalleScreen(
    uiState: MeditacionDetalleUIState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onVerCategoria: (Meditacion) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SerenaEncabezado(
                titulo = stringResource(R.string.detalle_titulo),
                onVolver = onVolver
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (uiState) {
                    MeditacionDetalleUIState.Loading -> SerenaCargando()

                    is MeditacionDetalleUIState.Error -> SerenaError(onReintentar = onReintentar)

                    is MeditacionDetalleUIState.Success -> DetalleContenido(
                        meditacion = uiState.meditacion,
                        onVerCategoria = { onVerCategoria(uiState.meditacion) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetalleContenido(
    meditacion: Meditacion,
    onVerCategoria: () -> Unit
) {
    var mostrarModal by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.spacing.regular)
    ) {
        SerenaImagenRemota(
            url = BASE_URL + meditacion.imagen,
            contentDescription = meditacion.titulo,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.sizes.detailImageHeight)
                .clip(RoundedCornerShape(AppTheme.sizes.cardCornerRadius))
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
            SerenaEtiqueta(texto = meditacion.categoria)
            if (meditacion.destacado == 1) {
                SerenaEtiqueta(texto = stringResource(R.string.sesiones_destacada), destacada = true)
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        Text(
            meditacion.titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        // Tres datos cortos, cada uno en su SerenaCard
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
            DatoSesion(
                icono = R.drawable.ic_clock,
                etiqueta = stringResource(R.string.detalle_duracion),
                valor = stringResource(R.string.meditacion_duracion, meditacion.duracion),
                modifier = Modifier.weight(1f)
            )
            DatoSesion(
                icono = R.drawable.ic_nivel,
                etiqueta = stringResource(R.string.detalle_nivel),
                valor = meditacion.nivel,
                modifier = Modifier.weight(1f)
            )
            DatoSesion(
                icono = R.drawable.ic_person,
                etiqueta = stringResource(R.string.detalle_instructor),
                valor = meditacion.autor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.regular))

        SerenaCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.detalle_acerca),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))
            Text(
                meditacion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        SerenaBoton(
            texto = stringResource(R.string.detalle_comenzar),
            onClick = { mostrarModal = true },
            icono = R.drawable.ic_play,
            anchoCompleto = true
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        SerenaBoton(
            texto = stringResource(R.string.detalle_mas_categoria, meditacion.categoria),
            onClick = onVerCategoria,
            estilo = EstiloBoton.CONTORNO,
            anchoCompleto = true
        )
    }

    if (mostrarModal) {
        SerenaModal(
            titulo = stringResource(R.string.detalle_modal_titulo),
            mensaje = stringResource(
                R.string.detalle_modal_mensaje,
                meditacion.titulo,
                meditacion.duracion
            ),
            textoConfirmar = stringResource(R.string.detalle_modal_boton),
            onConfirmar = { mostrarModal = false },
            icono = R.drawable.ic_check,
            textoCancelar = stringResource(R.string.cancelar),
            onCancelar = { mostrarModal = false }
        )
    }
}

@Composable
private fun DatoSesion(
    @DrawableRes icono: Int,
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    SerenaCard(modifier = modifier, relleno = AppTheme.spacing.medium) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppTheme.sizes.iconRegular)
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            valor,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun MeditacionDetallePreview() {
    SerenaAppTheme {
        MeditacionDetalleScreen(
            uiState = MeditacionDetalleUIState.Success(
                Meditacion(
                    4, 2, "Dormir", "Lluvia sobre el bosque",
                    "Sonido continuo de lluvia para dormir profundo", 45,
                    "Principiante", "Serena", 1
                )
            ),
            onVolver = {},
            onReintentar = {},
            onVerCategoria = {}
        )
    }
}
