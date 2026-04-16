package com.example.movilestp2ifts182026c1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movilestp2ifts182026c1.ui.theme.MovilesTP2IFTS182026C1Theme
import androidx.activity.viewModels
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovilesTP2IFTS182026C1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PromedioAlumno(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PromedioAlumno( viewModel: MainViewModel, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Promedio de Alumno",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.nombre = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.nota1,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    viewModel.nota1 = it
                }
            },
            label = { Text("Nota 1") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.nota2,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    viewModel.nota2 = it
                }
            },
            label = { Text("Nota 2") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.calcularPromedio() }) {
            Text("Calcular")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.agregarParticipacion() }) {
            Text("Agregar participación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.error.isNotEmpty()) {
            Text(
                text = viewModel.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (viewModel.resultado.isNotEmpty()) {
            Text(
                text = viewModel.resultado,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPromedioAlumno() {
    MovilesTP2IFTS182026C1Theme {
        PromedioAlumno(viewModel = MainViewModel())
    }
}
