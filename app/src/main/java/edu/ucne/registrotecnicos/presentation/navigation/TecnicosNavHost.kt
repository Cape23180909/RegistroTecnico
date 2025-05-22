package edu.ucne.registrotecnicos.presentation.navigation

import TecnicoScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrotecnicos.presentation.dashboard.DashboardScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketViewModel

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
        composable("dashboard") { //Aqui se dirije al componente principal de mi aplicacion
            DashboardScreen(navController = navController)
        }

        composable("tecnicoList") {
            val tecnicoList =  tecnicoViewModel.tecnicoList.collectAsState().value
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
            val tecnico =  tecnicoViewModel.getTecnicoById(tecnicoId)

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
    }
}