package com.mabel.serenaapp.utils

import android.util.Patterns
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
 * Funciones pequenas que usan varias pantallas.
 */

// Registro y Perfil
fun correoValido(correo: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()

const val LARGO_MINIMO_PASSWORD = 6

// Mi Diario y Perfil
private val formatoFecha: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.forLanguageTag("es-PE"))

fun formatearFecha(milisegundos: Long): String =
    Instant.ofEpochMilli(milisegundos).atZone(ZoneId.systemDefault()).format(formatoFecha)

fun diaDe(milisegundos: Long): LocalDate =
    Instant.ofEpochMilli(milisegundos).atZone(ZoneId.systemDefault()).toLocalDate()
