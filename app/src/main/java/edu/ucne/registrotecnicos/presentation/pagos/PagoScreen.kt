package edu.ucne.registrotecnicos.presentation.pagos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import edu.ucne.registrotecnicos.presentation.pago.PagoUiState
import edu.ucne.registrotecnicos.presentation.pago.PagoViewModel

@Composable
fun PagoScreen(
    PagoId: Int = 0,
    viewModel: PagoViewModel = hiltViewModel(),
    navController: NavHostController
) {
    LaunchedEffect(PagoId) {
        if (PagoId != 0) {
            viewModel.limpiarCampos()
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PagoBodyScreen(
        uiState = uiState.value,
        onDescripcionChange = { viewModel.setDescripcion(it) },
        onMontoChange = { viewModel.setMonto(it.toDoubleOrNull() ?: 0.0) },
        savePago = {
            if (viewModel.validarCampos()) {
                if (uiState.value.pagoId == null) {
                    viewModel.create()
                } else {
                    viewModel.update()
                }
                navController.navigate(Screen.PagoList) {
                    popUpTo(Screen.PagoList) { inclusive = true }
                }
            }
        },
        nuevoPago = { viewModel.limpiarCampos() },
        goBack = { navController.popBackStack() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoBodyScreen(
    uiState: PagoUiState,
    onDescripcionChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    savePago: () -> Unit,
    nuevoPago: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.pagoId == null) "Registrar Pago" else "Editar Pago",
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
                    label = { Text("Descripción del pago") },
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
                        onClick = { savePago() },
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