package edu.ucne.registrotecnicos.presentation.navigation

import TecnicoScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.presentation.dashboard.DashboardScreen
import edu.ucne.registrotecnicos.presentation.laboratorio.LaboratorioListScreen
import edu.ucne.registrotecnicos.presentation.laboratorio.LaboratorioScreen
import edu.ucne.registrotecnicos.presentation.laboratorio.LaboratorioViewModel
import edu.ucne.registrotecnicos.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class UiState(
    val mensajes: List<MensajeEntity>,
    val nombre: String,
    val rol: String,
    val descripcion: String
)

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TecnicosNavHost(
    navController: NavHostController,
    tecnicoViewModel: TecnicoViewModel,
    ticketViewModel: TicketViewModel,
    laboratorioViewModel: LaboratorioViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
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

        composable("mensaje") {
            MensajeScreen()
        }

        composable<Screen.LaboratorioList> {
            LaboratorioListScreen(
                goToLaboratorio = { laboratorioId ->
                    navController.navigate(Screen.Laboratorio(laboratorioId))
                },
                onDrawer = {
                    scope.launch { drawerState.open() }
                }
            )
        }

        composable<Screen.Laboratorio> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Laboratorio>()
            LaboratorioScreen(
                laboratorioId = args.laboratorioId,
                navController = navController
            )
        }
    }
}