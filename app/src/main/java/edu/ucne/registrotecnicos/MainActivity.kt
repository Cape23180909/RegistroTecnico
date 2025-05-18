package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TareaDb
import edu.ucne.registrotecnicos.data.local.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.local.repository.TicketRepository
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoViewModel
import edu.ucne.registrotecnicos.presentation.navigation.TecnicosNavHost
import edu.ucne.registrotecnicos.presentation.tickets.TicketViewModel
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tareaDb: TareaDb
    private lateinit var tecnicosRepository: TecnicoRepository
    private lateinit var tecnicosViewModel: TecnicoViewModel
    private lateinit var ticketRepository: TicketRepository
    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización de la base de datos
        tareaDb = Room.databaseBuilder(
            applicationContext,
            TareaDb::class.java,
            "Tarea.db"
        ).fallbackToDestructiveMigration()
            .build()

        tecnicosRepository = TecnicoRepository(tareaDb.TecnicoDao())
        tecnicosViewModel = TecnicoViewModel(tecnicosRepository)

        ticketRepository = TicketRepository(tareaDb.TicketDao())
        ticketViewModel = TicketViewModel(ticketRepository)

        setContent {
            RegistroTecnicosTheme {
                val navController = rememberNavController()
                TecnicosNavHost(
                    navController = navController,
                    tecnicoViewModel = tecnicosViewModel,
                    ticketViewModel = ticketViewModel
                )
            }
        }
    }
}