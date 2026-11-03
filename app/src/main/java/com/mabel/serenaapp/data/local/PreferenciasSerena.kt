package com.mabel.serenaapp.data.local

import android.content.Context
import com.google.gson.Gson
import com.mabel.serenaapp.models.EntradaDiario

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
