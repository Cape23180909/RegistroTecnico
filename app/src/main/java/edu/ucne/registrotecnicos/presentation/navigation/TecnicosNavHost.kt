package edu.ucne.registrotecnicos.presentation.navigation

import TecnicoScreen
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.presentation.dashboard.DashboardScreen
import edu.ucne.registrotecnicos.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketViewModel

data class UiState(
    val mensajes: List<MensajeEntity>,
    val nombre: String,
    val rol: String,
    val descripcion: String
)

@Composable
fun TecnicosNavHost(
    navController: NavHostController,
    tecnicoViewModel: TecnicoViewModel,
    ticketViewModel: TicketViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        composable("tecnicoList") {
            val tecnicoList = tecnicoViewModel.tecnicoList.collectAsState().value
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnico ->
                    navController.navigate("tecnico/${tecnico.tecnicoId}")
                },
                onCreate = {
                    navController.navigate("tecnico/null")
                },
                onDelete = { tecnico ->
                    tecnicoViewModel.delete(tecnico)
                }
            )
        }

        composable("tecnico/{tecnicoId}") { backStackEntry ->
            val tecnicoIdParam = backStackEntry.arguments?.getString("tecnicoId")
            val tecnicoId = tecnicoIdParam?.toIntOrNull()
            val tecnico = tecnicoViewModel.getTecnicoById(tecnicoId)

            TecnicoScreen(
                tecnico = tecnico,
                agregarTecnico = { nombre, sueldo ->
                    if (tecnico == null) {
                        tecnicoViewModel.agregarTecnico(nombre, sueldo)
                    } else {
                        tecnicoViewModel.update(tecnico.copy(nombre = nombre, sueldo = sueldo))
                    }
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable("ticketList") {
            val ticketList = ticketViewModel.ticketList.collectAsState().value
            val tecnicosDisponibles = tecnicoViewModel.tecnicoList.collectAsState().value
            TicketListScreen(
                TicketList = ticketList,
                tecnicos = tecnicosDisponibles,
                onEdit = { ticket ->
                    navController.navigate("ticket/${ticket.ticketId}")
                },
                onCreate = {
                    navController.navigate("ticket/null")
                },
                onDelete = { ticket ->
                    ticketViewModel.delete(ticket)
                },
                onMessage = {
                    navController.navigate("mensaje")
                }
            )
        }

        composable("Ticket/{ticketId}") { backStackEntry ->
            val ticketIdParam = backStackEntry.arguments?.getString("ticketId")
            val ticketId = if (ticketIdParam == "null") null else ticketIdParam?.toIntOrNull()
            val ticket = ticketViewModel.getTicketById(ticketId)
            val tecnicosDisponibles = tecnicoViewModel.tecnicoList.collectAsState().value

            TicketScreen(
                ticket = ticket,
                tecnicosDisponibles = tecnicosDisponibles,
                saveTicket = { ticketId, fecha, cliente, asunto, descripcion, prioridad, tecnicoId ->
                    if (ticket == null) {
                        ticketViewModel.agregarTicket(
                            ticketId = null,
                            fecha = fecha,
                            cliente = cliente,
                            asunto = asunto,
                            descripcion = descripcion,
                            prioridad = prioridad,
                            tecnicoId = tecnicoId!!
                        )
                    } else {
                        ticketViewModel.update(
                            ticket.copy(
                                fecha = fecha,
                                cliente = cliente,
                                asunto = asunto,
                                descripcion = descripcion,
                                prioridad = prioridad,
                                tecnicoId = tecnicoId
                            )
                        )
                    }
                    navController.popBackStack()
                },

                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // Aquí agregamos la nueva ruta para MensajeScreen
        composable("mensaje") {
            var nombre by remember { mutableStateOf("") }
            var rol by remember { mutableStateOf("") }
            var descripcion by remember { mutableStateOf("") }
            var mensajes by remember { mutableStateOf(listOf<MensajeEntity>()) }

            // Ejemplo de carga inicial de mensajes (vacío por ahora)
            LaunchedEffect(Unit) {
                // Carga tus mensajes reales aquí o desde ViewModel
                mensajes = listOf()
            }

            MensajeScreen(
                uiState = UiState(
                    mensajes = mensajes,
                    nombre = nombre,
                    rol = rol,
                    descripcion = descripcion
                ),
                onNombreChange = { nombre = it },
                onRolChange = { rol = it },
                onDescripcionChange = { descripcion = it },
                onSave = {
                    val nuevoMensaje = MensajeEntity(
                        mensajeId = mensajes.size + 1,
                        nombre = nombre,
                        rol = rol,
                        descripcion = descripcion,
                        fecha = "2025-07-22 10:00:00" // fecha fija para ejemplo
                    )
                    mensajes = mensajes + nuevoMensaje
                    // limpiar campos
                    nombre = ""
                    rol = ""
                    descripcion = ""
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}