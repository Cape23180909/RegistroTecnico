package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.presentation.navigation.UiState

@Composable
fun MensajeScreen(
    uiState: UiState,
    onNombreChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var selectedRole by remember { mutableStateOf("Operator") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF5F5F5), Color(0xFF7E57C2))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 16.dp) // Evita que el botón choque con íconos del sistema
        ) {

            // Lista de mensajes existentes
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(uiState.mensajes) { mensaje ->
                    MensajeCard(mensaje = mensaje)
                }
            }

            // Espaciador entre la lista y el formulario
            Spacer(modifier = Modifier.height(16.dp))

            // Opciones de rol
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                RoleButton(text = "Operator", isSelected = selectedRole == "Operator") {
                    selectedRole = "Operator"
                    onRolChange("Operator")
                }
                Spacer(modifier = Modifier.width(8.dp))
                RoleButton(text = "Owner", isSelected = selectedRole == "Owner") {
                    selectedRole = "Owner"
                    onRolChange("Owner")
                }
            }

            // Campo de nombre
            TextField(
                value = uiState.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Black), // Agregamos esto para hacer el fondo blanco
                singleLine = false
            )

            // Espaciador
            Spacer(modifier = Modifier.height(16.dp))

            // Área de mensaje
            TextField(
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false
            )

            // Espaciador reducido
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de guardar
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 96.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Guardar Mensaje",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// Componente personalizado para mostrar un mensaje
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

// Componente personalizado para los botones de rol
@Composable
fun RoleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Circle(
                isSelected = isSelected,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Círculo para indicar si el rol está seleccionado
@Composable
fun Circle(isSelected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
    )
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
        MensajeScreen(
            uiState = uiState,
            onNombreChange = {},
            onRolChange = {},
            onDescripcionChange = {},
            onSave = {},
            onBack = {}
        )
    }
}