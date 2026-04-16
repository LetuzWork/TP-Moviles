package com.example.movilestp2ifts182026c1
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    var nombre by mutableStateOf("")
    var nota1 by mutableStateOf("")
    var nota2 by mutableStateOf("")
    var resultado by mutableStateOf("")
    var error by mutableStateOf("")

    fun calcularPromedio() {
        if (nombre.isBlank()) {
            error = "El nombre no puede estar vacío"
            resultado = ""
            return
        }

        val n1 = nota1.toDoubleOrNull()
        val n2 = nota2.toDoubleOrNull()

        if (n1 == null || n2 == null) {
            error = "Las notas deben ser numéricas"
            resultado = ""
            return
        }

        val promedio = (n1 + n2) / 2
        resultado = "Alumno: $nombre - Promedio: %.2f".format(promedio)
        error = ""
    }

    fun agregarParticipacion() {
        val n1 = nota1.toDoubleOrNull()
        val n2 = nota2.toDoubleOrNull()

        if (nombre.isBlank() || n1 == null || n2 == null) {
            error = "Primero ingresá datos válidos"
            resultado = ""
            return
        }

        val participacion = Random.nextDouble(0.0, 1.0)
        val promedioFinal = ((n1 + n2) / 2) + participacion

        resultado = "Alumno: $nombre - Final con participación: %.2f".format(promedioFinal)
        error = ""
    }
}