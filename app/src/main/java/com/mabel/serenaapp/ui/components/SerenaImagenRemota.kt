package com.mabel.serenaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.mabel.serenaapp.R

/*
 * COMPONENTE REUTILIZABLE: SerenaImagenRemota
 *
 * Muestra una imagen que esta en el servidor (AlwaysData) usando
 * Coil. Mientras carga, o si falla, se ve img_placeholder.
 * En el Preview de Android Studio no hay internet, por eso ahi
 * se dibuja directamente el placeholder (LocalInspectionMode).
 *
 * Recibe la URL completa: BASE_URL + el campo "imagen" de la BD.
 */
@Composable
fun SerenaImagenRemota(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    // Si la BD todavia no tiene imagen, la URL termina en "/"
    val sinImagen = url.isBlank() || url.endsWith("/")

    if (LocalInspectionMode.current || sinImagen) {
        Image(
            painter = painterResource(R.drawable.img_placeholder),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            contentScale = contentScale
        )
    }
}
