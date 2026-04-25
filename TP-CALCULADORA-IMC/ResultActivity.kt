package com.example.calculadoradeimc

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculadoradeimc.ui.theme.CalculadoraDeIMCTheme

class ResultActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Se recuperan los datos enviados desde la primera pantalla
        val name = intent.getStringExtra("name") ?: ""
        val imc = intent.getDoubleExtra("imc", 0.0)

        setContent {
            CalculadoraDeIMCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    ResultadoIMC(
                        name = name,
                        imc = imc,
                        context = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Esta función traduce el valor numérico del IMC a una categoría
fun clasificarIMC(imc: Double): String {
    return when {
        imc < 18.5 -> "Bajo peso"
        imc < 25 -> "Normal"
        imc < 30 -> "Sobrepeso"
        else -> "Obesidad"
    }
}

@Composable
fun ResultadoIMC(
    name: String,
    imc: Double,
    context: Context,
    modifier: Modifier = Modifier
) {

    // SharedPreferences permite guardar datos de forma persistente
    // Es decir, no se pierden al cerrar la app
    val prefs = context.getSharedPreferences("imc_history", Context.MODE_PRIVATE)

    // Se carga el historial guardado previamente
    var history by remember { mutableStateOf(loadHistory(prefs)) }

    // Mensaje opcional cuando el usuario mejora su IMC
    var mensaje by remember { mutableStateOf("") }

    // Se ejecuta una vez al entrar a la pantalla
    LaunchedEffect(Unit) {

        val anterior = history.firstOrNull()

        // Se guarda el nuevo valor en el historial
        history = saveAndGetHistory(prefs, imc)

        // Si el nuevo IMC es menor que el anterior, se muestra un mensaje
        if (anterior != null && imc < anterior) {
            mensaje = "Felicitaciones, bajaste tu IMC"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text("Hola $name")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Tu IMC es: %.2f".format(imc))
        Text("Clasificación: ${clasificarIMC(imc)}")

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(mensaje)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Historial (últimos 3):")

        history.forEach {
            Text("%.2f".format(it))
        }
    }
}

// Guarda el nuevo IMC y mantiene solo los últimos 3 valores
fun saveAndGetHistory(
    prefs: android.content.SharedPreferences,
    imc: Double
): List<Double> {

    val lista = loadHistory(prefs).toMutableList()

    // Se agrega el valor más reciente al inicio
    lista.add(0, imc)

    // Se limita el tamaño a 3 elementos
    val recortado = lista.take(3)

    // Se guarda como texto separado por comas
    prefs.edit()
        .putString("history", recortado.joinToString(","))
        .apply()

    return recortado
}

// Recupera el historial guardado previamente
fun loadHistory(
    prefs: android.content.SharedPreferences
): List<Double> {

    val saved = prefs.getString("history", "") ?: ""

    return if (saved.isEmpty()) emptyList()
    else saved.split(",").mapNotNull { it.toDoubleOrNull() }
}
