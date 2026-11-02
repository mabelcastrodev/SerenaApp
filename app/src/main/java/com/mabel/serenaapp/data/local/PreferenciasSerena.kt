package com.mabel.serenaapp.data.local

import android.content.Context
import com.google.gson.Gson
import com.mabel.serenaapp.models.EntradaDiario

/*
 * DATOS LOCALES (en el celular)
 *
 * Guarda con SharedPreferences lo que no esta en el servidor:
 * el nombre y correo del registro, y las entradas de Mi Diario.
 * Las entradas se guardan como texto JSON usando Gson (la misma
 * libreria que usa Retrofit). La contrasena NO se guarda.
 */
class PreferenciasSerena(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
    private val gson = Gson()

    var nombre: String
        get() = prefs.getString(CLAVE_NOMBRE, "") ?: ""
        set(valor) = prefs.edit().putString(CLAVE_NOMBRE, valor).apply()

    var correo: String
        get() = prefs.getString(CLAVE_CORREO, "") ?: ""
        set(valor) = prefs.edit().putString(CLAVE_CORREO, valor).apply()

    fun leerDiario(): List<EntradaDiario> {
        val json = prefs.getString(CLAVE_DIARIO, null) ?: return emptyList()
        return try {
            gson.fromJson(json, Array<EntradaDiario>::class.java)?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun guardarDiario(entradas: List<EntradaDiario>) {
        prefs.edit().putString(CLAVE_DIARIO, gson.toJson(entradas)).apply()
    }

    private companion object {
        const val ARCHIVO = "serena_preferencias"
        const val CLAVE_NOMBRE = "nombre"
        const val CLAVE_CORREO = "correo"
        const val CLAVE_DIARIO = "diario"
    }
}
