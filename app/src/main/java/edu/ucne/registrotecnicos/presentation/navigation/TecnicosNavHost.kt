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

@Composable
fun TecnicosNavHost(
    navController: NavHostController,
    viewModel: TecnicoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") { //Aqui se dirije al componente principal de mi aplicacion
            DashboardScreen(navController = navController)
        }

        composable("tecnicoList") {
            val tecnicoList = viewModel.tecnicoList.collectAsState().value
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnico ->
                    navController.navigate("tecnico/${tecnico.TecnicoId}")
                },
                onCreate = {
                    navController.navigate("tecnico/null")
                },
                onDelete = { tecnico ->
                    viewModel.delete(tecnico)
                }
            )
        }

        composable("tecnico/{tecnicoId}") { backStackEntry ->
            val tecnicoIdParam = backStackEntry.arguments?.getString("tecnicoId")
            val tecnicoId = tecnicoIdParam?.toIntOrNull()
            val tecnico = viewModel.getTecnicoById(tecnicoId)

            TecnicoScreen(
                tecnico = tecnico,
                agregarTecnico = { nombre, sueldo ->
                    if (tecnico == null) {
                        viewModel.agregarTecnico(nombre, sueldo)
                    } else {
                        viewModel.update(tecnico.copy(Nombre = nombre, Sueldo = sueldo))
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