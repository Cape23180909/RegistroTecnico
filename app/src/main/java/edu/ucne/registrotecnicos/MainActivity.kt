package edu.ucne.registrotecnicos

import TecnicoScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.ucne.registrotecnicos.data.entities.TecnicoEntity
import edu.ucne.registrotecnicos.presentation.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.TecnicoViewModel
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecnicosListScreen()
        }
    }
}

@Composable
fun TecnicosListScreen(tecnicoViewModel: TecnicoViewModel = viewModel()) {
    var Formulario by remember { mutableStateOf(false) }
    var tecnicoToEdit by remember { mutableStateOf<TecnicoEntity?>(null) }
    if (Formulario) {
        TecnicoScreen(
            tecnico = tecnicoToEdit,
            agregarTecnico = { nombre, sueldo ->
                if (tecnicoToEdit == null) {
                    tecnicoViewModel.agregarTecnico(nombre, sueldo)
                } else {
                    tecnicoViewModel.updateTecnico(
                        tecnicoToEdit!!.copy(Nombre = nombre, Sueldo = sueldo)
                    )
                }
                Formulario = false
                tecnicoToEdit = null
            },
            onCancel = {
                Formulario = false
                tecnicoToEdit = null
            }
        )
    } else {
        TecnicoListScreen(
            tecnicoList = tecnicoViewModel.tecnicoList,
            onCreate = {
                tecnicoToEdit = null
                Formulario = true
            },
            onDelete = { tecnicoViewModel.deleteTecnico(it) },
            onEdit = {
                tecnicoToEdit = it
                Formulario = true
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RegistroTecnicosTheme {
        TecnicosListScreen()
    }
}