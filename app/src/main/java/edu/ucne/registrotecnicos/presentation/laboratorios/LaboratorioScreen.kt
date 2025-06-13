package edu.ucne.registrotecnicos.presentation.laboratorios

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.registrotecnicos.presentation.navigation.Screen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    )
                )
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Registrar Laboratorio",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = goBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            uiState.inputError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "DescripciÃ³n") },
                value = uiState.descripcion ?: "",
                onValueChange = onDescripcionChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Monto") },
                value = if (uiState.monto == 0.0) "" else uiState.monto.toString(),
                onValueChange = onMontoChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = nuevoLaboratorio
                ) {
                    Text("Nuevo")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = saveLaboratorio
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar")
                }
            }
        }
    }
}