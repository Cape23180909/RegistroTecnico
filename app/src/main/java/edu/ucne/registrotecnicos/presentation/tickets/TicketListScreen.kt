package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity

@Composable
fun TicketListScreen(
    TicketList: List<TicketEntity>,
    tecnicos: List<TecnicoEntity>,
    onCreate: () -> Unit,
    onDelete: (TicketEntity) -> Unit,
    onEdit: (TicketEntity) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF5F5F5), Color(0xFF7E57C2))
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Lista de Tickets",
                style = TextStyle(
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF311B92),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(39.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                items(TicketList) { ticket ->
                    val tecnicoNombre = tecnicos.find { it.tecnicoId == ticket.tecnicoId }?.nombre ?: "No asignado"
                    TicketRow(ticket, tecnicoNombre, onDelete, onEdit)
                }
            }
        }
    }
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    tecnicoNombre: String,
    onDelete: (TicketEntity) -> Unit,
    onEdit: (TicketEntity) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(22.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Cliente: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ticket.cliente, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Fecha: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ticket.fecha, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Asunto: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ticket.asunto, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Descripcion: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ticket.descripcion, fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Prioridad: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ticket.prioridad ?: "No asignada", fontSize = 16.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Técnico: ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = tecnicoNombre, fontSize = 16.sp)
                }
            }

            Row {
                IconButton(onClick = { onEdit(ticket) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color(0xFF4CAF50))
                }
                IconButton(onClick = { onDelete(ticket) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketListScreenPreview() {
    val sampleTickets = remember {
        mutableStateListOf(
            TicketEntity(fecha = "2024-05-16", cliente = "Juan Pérez", asunto = "Revisión", descripcion = "Equipo no enciende", prioridad = "", tecnicoId = 1),
            TicketEntity(fecha = "2024-05-17", cliente = "María García", asunto = "Instalación", descripcion = "Configurar impresora", prioridad = "", tecnicoId = 2),
            TicketEntity(fecha = "2024-05-18", cliente = "Carlos López", asunto = "Mantenimiento", descripcion = "Limpieza de sistema", prioridad = "", tecnicoId = null)
        )
    }

    val sampleTecnicos = listOf(
        TecnicoEntity(tecnicoId = 1, nombre = "Pedro Sánchez"),
        TecnicoEntity(tecnicoId = 2, nombre = "Ana Martínez")
    )

    TicketListScreen(
        TicketList = sampleTickets,
        tecnicos = sampleTecnicos,
        onCreate = {
            sampleTickets.add(
                TicketEntity(
                    fecha = "2024-05-19",
                    cliente = "Nuevo Cliente",
                    asunto = "Consulta",
                    descripcion = "Consulta general",
                    prioridad = "",
                    tecnicoId = null
                )
            )
        },
        onDelete = { ticket -> sampleTickets.remove(ticket) },
        onEdit = { /* Simulación de edición */ }
    )
}