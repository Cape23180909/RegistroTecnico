package edu.ucne.registrotecnicos.presentation.navigation

import TecnicoScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    viewModel: TecnicoViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            val tecnicoList = viewModel.tecnicoList.collectAsState().value

            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnico ->
                    navHostController.navigate(Screen.Tecnico(tecnico.TecnicoId))
                },
                onCreate = {
                    navHostController.navigate(Screen.Tecnico(null))
                },
                onDelete = { tecnico ->
                    viewModel.delete(tecnico)
                }
            )
        }

        composable<Screen.Tecnico> { backStackEntry ->
            val tecnicoId = backStackEntry.toRoute<Screen.Tecnico>().tecnicoId
            val tecnico = viewModel.getTecnicoById(tecnicoId)

            TecnicoScreen(
                tecnico = tecnico,
                agregarTecnico = { nombre, sueldo ->
                    if (tecnico == null) {
                        viewModel.agregarTecnico(nombre, sueldo)
                    } else {
                        viewModel.update(tecnico.copy(Nombre = nombre, Sueldo = sueldo))
                    }
                    navHostController.popBackStack()
                },
                onCancel = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}