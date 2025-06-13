package edu.ucne.registrotecnicos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrotecnicos.presentation.laboratorios.LaboratorioViewModel
import edu.ucne.registrotecnicos.presentation.mensaje.MensajeViewModel
import edu.ucne.registrotecnicos.presentation.navigation.TecnicosNavHost
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketViewModel
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RegistroTecnicosTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // ViewModels inyectados por Hilt
                val tecnicoViewModel: TecnicoViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val ticketViewModel: TicketViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val laboratorioViewModel: LaboratorioViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val mensajeViewModel: MensajeViewModel = androidx.hilt.navigation.compose.hiltViewModel()

                TecnicosNavHost(
                    navController = navController,
                    tecnicoViewModel = tecnicoViewModel,
                    ticketViewModel = ticketViewModel,
                    laboratorioViewModel = laboratorioViewModel,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    }
}