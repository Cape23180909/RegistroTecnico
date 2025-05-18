package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticket: TicketEntity?,
    agregarTicket: (String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var fecha by remember { mutableStateOf(ticket?.Fecha ?: "") }
    var cliente by remember { mutableStateOf(ticket?.Cliente ?: "") }
    var asunto by remember { mutableStateOf(ticket?.Asunto ?: "") }
    var descripcion by remember { mutableStateOf(ticket?.Descripcion ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

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
                                else -> {
                                    error = null
                                    agregarTicket(fecha, cliente, asunto, descripcion)
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
        agregarTicket = { fecha, cliente, asunto, descripcion ->
            println("Nuevo ticket: $fecha, $cliente, $asunto, $descripcion")
        },
        onCancel = { println("Cancelado") }
    )
}