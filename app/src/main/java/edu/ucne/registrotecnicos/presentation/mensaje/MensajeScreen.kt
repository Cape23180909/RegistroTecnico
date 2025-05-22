package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity

@Composable
fun MensajeCard(mensaje: MensajeEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Nombre: ${mensaje.nombre}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Rol: ${mensaje.rol}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Descripción: ${mensaje.descripcion}",
                style = MaterialTheme.typography.bodySmall
            )
            mensaje.fecha?.let {
                Text(
                    text = "Fecha: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MensajeScreen(
    uiState: edu.ucne.registrotecnicos.presentation.navigation.UiState,
    onNombreChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.mensajes) { mensaje ->
                MensajeCard(mensaje = mensaje)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.rol,
            onValueChange = onRolChange,
            label = { Text("Rol") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción del Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Mensaje")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMensajeScreen() {
    val sampleMensajes = listOf(
        MensajeEntity(
            mensajeId = 1,
            descripcion = "Mensaje de prueba número uno",
            fecha = "2025-07-21 15:30:00",
            rol = "Administrador",
            nombre = "Carlos López"
        ),
        MensajeEntity(
            mensajeId = 2,
            descripcion = "Mensaje de prueba número dos",
            fecha = "2025-07-21 16:45:00",
            rol = "Técnico",
            nombre = "Ana Pérez"
        )
    )

    val uiState = UiState(
        mensajes = sampleMensajes,
        descripcion = "Mensaje temporal",
        nombre = "Nombre temporal",
        rol = "Rol temporal"
    )

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.mensajes) { mensaje ->
                    MensajeCard(mensaje)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = {},
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.rol,
                onValueChange = {},
                label = { Text("Rol") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = {},
                label = { Text("Descripción del Mensaje") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar Mensaje")
            }
        }
    }
}