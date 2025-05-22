package edu.ucne.registrotecnicos.presentation.mensaje

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
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
    var error by remember { mutableStateOf<String?>(null) }

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
                .padding(bottom = 16.dp)
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
                    .background(Color.Black),
                singleLine = false
            )

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

            // Mensaje de error si existe
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 96.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(text = "Cancelar")
                }

                Button(
                    onClick = {
                        when {
                            uiState.nombre.isBlank() -> error = "El nombre es requerido"
                            uiState.descripcion.isBlank() -> error = "El mensaje es requerido"
                            else -> {
                                error = null
                                onSave()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(text = "Guardar")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MensajeCard(mensaje: MensajeEntity) {
    // Formateador para parsear la fecha original
    val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    // Formateador para mostrar la fecha
    val displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Intentamos parsear y formatear la fecha, si falla usamos el texto original
    val fechaFormateada = try {
        val fechaParseada = LocalDateTime.parse(mensaje.fecha, originalFormatter)
        fechaParseada.format(displayFormatter)
    } catch (e: DateTimeParseException) {
        mensaje.fecha // fallback en caso de error
    }

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
                text = "Descripción: ${mensaje.descripcion}",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fecha: $fechaFormateada",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            when (mensaje.rol) {
                                "Owner" -> Color.Green
                                "Operator" -> Color.Blue
                                else -> Color.Gray
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mensaje.rol,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMensajeScreen() {
    val sampleMensajes = listOf(
        MensajeEntity(
            mensajeId = 1,
            descripcion = "Mensaje de prueba número uno",
            fecha = "22-5-2025",
            rol = "Owner",
            nombre = "Carlos Reyes"
        ),
        MensajeEntity(
            mensajeId = 2,
            descripcion = "Mensaje de prueba número dos",
            fecha = "22-5-2025",
            rol = "Operator",
            nombre = "Juan Pérez"
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