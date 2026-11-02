package com.mabel.serenaapp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class AppSpacing(

    // Espaciado general
    val none: Dp = 0.dp,
    val micro: Dp = 2.dp,
    val tiny: Dp = 3.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val regular: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 48.dp,

    // Margen lateral de las pantallas Empezar y Login
    val screenMargin: Dp = 40.dp
)

data class AppComponentSizes(

    // Logo
    val logoSizeLarge: Dp = 150.dp,
    val logoSizeSmall: Dp = 30.dp,

    // Iconos
    val iconRegular: Dp = 22.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 28.dp,

    // Contenedores de iconos (Circulos de fondo)
    val iconContainerSmall: Dp = 36.dp,
    val iconContainerMedium: Dp = 48.dp,
    val iconContainerLarge: Dp = 64.dp,

    // Elevacion de las tarjetas
    val cardElevation: Dp = 2.dp,

    // Indicador de carga ( Tamaño del circulo que gira )
    val progressSize: Dp = 44.dp,

    // Botones
    val buttonHeight: Dp = 52.dp,
    val buttonHeightSmall: Dp = 40.dp,
     //Redondez del boton
    val buttonCornerRadius: Dp = 8.dp,

    // Cards
    val cardCornerRadius: Dp = 12.dp,

    // Ilustración del login
    val loginIllustrationHeight: Dp = 220.dp,

    // Hero / banner del dashboard
    val heroBannerHeight: Dp = 220.dp,
    val bannerHeight: Dp = 220.dp,

    // Decoración del fondo azul (Empezar)
    val dotSize: Dp = 4.dp,
    val dotGap: Dp = 6.dp,
    val ringSize: Dp = 100.dp,
    val ringStroke: Dp = 20.dp,
    val bigCircleSize: Dp = 320.dp
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
val LocalAppComponentSizes = staticCompositionLocalOf { AppComponentSizes() }
