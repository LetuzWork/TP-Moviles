package com.example.calculadoradeimc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadoradeimc.ui.theme.CalculadoraDeIMCTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que el contenido use toda la pantalla
        enableEdgeToEdge()

        // Reemplaza el uso de layouts XML por Compose
        setContent {
            CalculadoraDeIMCTheme {

                // En Compose no se usa "this" directamente como contexto
                // LocalContext nos da acceso al contexto actual de la app
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Se llama al composable principal (formulario)
                    CalculadoraDeIMC(
                        name = "",
                        modifier = Modifier.padding(innerPadding),

                        // Esta función se ejecuta cuando el usuario toca el botón
                        onCalculate = { name, weight, height ->

                            // Se aplica la fórmula del IMC
                            val imc = weight / (height * height)

                            // Se crea un Intent para cambiar de pantalla
                            val intent = Intent(context, ResultActivity::class.java)

                            // Se envían los datos a la segunda activity
                            intent.putExtra("name", name)
                            intent.putExtra("imc", imc)

                            // Se inicia la nueva activity
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculadoraDeIMC(
    name: String,
    modifier: Modifier = Modifier,
    onCalculate: (String, Double, Double) -> Unit
) {

    // rememberSaveable permite que los datos no se pierdan si la pantalla se recrea
    // por ejemplo al rotar el dispositivo
    var userName by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }

    // Estado para mostrar errores de validación
    var error by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            // Permite hacer scroll si el contenido no entra en pantalla
            .verticalScroll(rememberScrollState())
    ) {

        Text("Calculadora de IMC")

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el nombre
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nombre") }
        )

        // Campo de texto para el peso
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") }
        )

        // Campo de texto para la altura
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altura (m)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            // Validación básica: ningún campo puede estar vacío
            if (userName.isBlank() || weight.isBlank() || height.isBlank()) {
                error = "Completar todos los campos"
            } else {
                try {
                    val w = weight.toDouble()
                    val h = height.toDouble()

                    // Se valida que la altura sea mayor a 0 para evitar errores matemáticos
                    if (h <= 0) {
                        error = "Altura inválida"
                    } else {
                        error = ""

                        // Si todo es correcto, se ejecuta la acción principal
                        onCalculate(userName, w, h)
                    }

                } catch (e: Exception) {
                    // Si el usuario ingresa texto en lugar de números
                    error = "Datos inválidos"
                }
            }
        }) {
            Text("Calcular IMC")
        }

        // Muestra el error solo si existe
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMain() {
    CalculadoraDeIMCTheme {
        CalculadoraDeIMC(
            name = "Test",
            onCalculate = { _, _, _ -> }
        )
    }
}
