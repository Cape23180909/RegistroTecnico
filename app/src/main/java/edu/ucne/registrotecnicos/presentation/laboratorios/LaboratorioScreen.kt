package edu.ucne.registrotecnicos.presentation.laboratorio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.registrotecnicos.presentation.navigation.Screen

@Composable
fun LaboratorioScreen(
    laboratorioId: Int = 0,
    viewModel: LaboratorioViewModel = hiltViewModel(),
    navController: NavHostController
) {
    LaunchedEffect(laboratorioId) {
        if (laboratorioId != 0) {
            viewModel.limpiarCampos()
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaboratorioBodyScreen(
        uiState = uiState.value,
        onDescripcionChange = { viewModel.setDescripcion(it) },
        onMontoChange = { viewModel.setMonto(it.toDoubleOrNull() ?: 0.0) },
        saveLaboratorio = {
            if (viewModel.validarCampos()) {
                if (uiState.value.laboratorioId == null) {
                    viewModel.create()
                } else {
                    viewModel.update()
                }
                navController.navigate(Screen.LaboratorioList) {
                    popUpTo(Screen.LaboratorioList) { inclusive = true }
                }
            }
        },
        nuevoLaboratorio = { viewModel.limpiarCampos() },
        goBack = { navController.popBackStack() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratorioBodyScreen(
    uiState: LaboratorioUiState,
    onDescripcionChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    saveLaboratorio: () -> Unit,
    nuevoLaboratorio: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.laboratorioId == null) "Registrar Laboratorio" else "Editar Laboratorio",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFEDE7F6), Color(0xFF7E57C2))
                    )
                )
                .padding(padding)
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                OutlinedTextField(
                    value = uiState.descripcion ?: "",
                    onValueChange = onDescripcionChange,
                    label = { Text("Descripción del laboratorio") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = if (uiState.monto == 0.0) "" else uiState.monto.toString(),
                    onValueChange = onMontoChange,
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                uiState.inputError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { goBack() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { saveLaboratorio() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}