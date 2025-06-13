package edu.ucne.registrotecnicos.presentation.laboratorios

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LaboratorioListScreen(
    viewModel: LaboratorioViewModel = hiltViewModel(),
    goToLaboratorio: (Int) -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLaboratorios()
    }

    LaboratorioListBodyScreen(
        uiState = uiState,
        goToLaboratorio = goToLaboratorio,
        onDrawer = onDrawer,
        onRefresh = viewModel::getLaboratorios
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratorioListBodyScreen(
    uiState: LaboratorioUiState,
    goToLaboratorio: (Int) -> Unit,
    onDrawer: () -> Unit,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text("Laboratorios") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                FloatingActionButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
                FloatingActionButton(onClick = { goToLaboratorio(0) }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar laboratorio")
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.laboratorios) { laboratorio ->
                    LaboratorioRow(laboratorio, goToLaboratorio)
                }
            }
        }
    }
}

@Composable
private fun LaboratorioRow(
    item: LaboratorioDto,
    goToLaboratorio: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { goToLaboratorio(item.laboratorioId) }
            .padding(16.dp)
    ) {
        Text(text = "ID: ${item.laboratorioId}")
        Text(text = "Descripción: ${item.descripcion ?: "Sin descripción"}")
        Text(text = "Monto: RD$${item.monto}")
    }
    Divider()
}