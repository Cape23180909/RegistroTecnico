package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticket: TicketEntity?,
    tecnicosDisponibles: List<TecnicoEntity>,
    agregarTicket: (String, String, String, String, String, Int?) -> Unit,
    onCancel: () -> Unit
) {
    var fecha by remember { mutableStateOf(ticket?.Fecha ?: "") }
    var cliente by remember { mutableStateOf(ticket?.Cliente ?: "") }
    var asunto by remember { mutableStateOf(ticket?.Asunto ?: "") }
    var descripcion by remember { mutableStateOf(ticket?.Descripcion ?: "") }
    var prioridad by remember { mutableStateOf(ticket?.Prioridad ?: "") }
    var prioridadMenuExpanded by remember { mutableStateOf(false) }
    var tecnico by remember { mutableStateOf(ticket?.TecnicoId?.toString() ?: "") }
    var tecnicoMenuExpanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val prioridades = listOf("Alta", "Media", "Baja")
    val tecnicoSeleccionado = remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (ticket == null) "Registrar Ticket" else "Editar Ticket",
                        style = androidx.compose.ui.text.TextStyle(
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
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cliente,
                    onValueChange = { cliente = it },
                    label = { Text("Cliente") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = asunto,
                    onValueChange = { asunto = it },
                    label = { Text("Asunto") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Prioridad con ExposedDropdownMenu
                ExposedDropdownMenuBox(
                    expanded = prioridadMenuExpanded,
                    onExpandedChange = { prioridadMenuExpanded = !prioridadMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = prioridad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prioridad", color = Color.White) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = prioridadMenuExpanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = prioridadMenuExpanded,
                        onDismissRequest = { prioridadMenuExpanded = false }
                    ) {
                        prioridades.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item, color = Color.Black) },
                                onClick = {
                                    prioridad = item
                                    prioridadMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Campo Técnico con ExposedDropdownMenu
                ExposedDropdownMenuBox(
                    expanded = tecnicoMenuExpanded,
                    onExpandedChange = { tecnicoMenuExpanded = !tecnicoMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = tecnicoSeleccionado.value?.Nombre ?: "Seleccionar Técnico",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Técnico", color = Color.White) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = tecnicoMenuExpanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = tecnicoMenuExpanded,
                        onDismissRequest = { tecnicoMenuExpanded = false }
                    ) {
                        tecnicosDisponibles.forEach { tecnicoItem ->
                            DropdownMenuItem(
                                text = { Text(tecnicoItem.Nombre, color = Color.Black) },
                                onClick = {
                                    tecnicoSeleccionado.value = tecnicoItem
                                    tecnicoMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            when {
                                fecha.isBlank() -> error = "La fecha es requerida"
                                cliente.isBlank() -> error = "El cliente es requerido"
                                asunto.isBlank() -> error = "El asunto es requerido"
                                descripcion.isBlank() -> error = "La descripción es requerida"
                                prioridad.isBlank() -> error = "Debes seleccionar una prioridad"
                                tecnicoSeleccionado.value == null -> error = "Debes seleccionar un técnico"
                                else -> {
                                    error = null
                                    agregarTicket(
                                        fecha,
                                        cliente,
                                        asunto,
                                        descripcion,
                                        prioridad,
                                        tecnicoSeleccionado.value?.TecnicoId
                                    )
                                }
                            }
                        },
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

@Preview(showBackground = true)
@Composable
fun TicketScreenPreview() {
    TicketScreen(
        ticket = null,
        tecnicosDisponibles = listOf(
            TecnicoEntity(TecnicoId = 1, Nombre = "Juan Pérez"),
            TecnicoEntity(TecnicoId = 2, Nombre = "Ana Gómez")
        ),
        agregarTicket = { fecha, cliente, asunto, descripcion, prioridad, tecnicoId ->
            println("Nuevo ticket: $fecha, $cliente, $asunto, $descripcion, Prioridad: $prioridad, TécnicoId: $tecnicoId")
        },
        onCancel = { println("Cancelado") }
    )
}