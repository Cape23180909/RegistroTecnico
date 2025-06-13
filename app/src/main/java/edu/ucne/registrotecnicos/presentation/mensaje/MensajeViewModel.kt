package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ucne.edu.registrotecnicos.data.repository.MensajeRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository,
    private val tecnicoRepository: TecnicoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadMensajes()
        loadTecnicos()
    }

    private fun loadMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect { mensajes ->
                _uiState.value = _uiState.value.copy(mensajes = mensajes)
            }
        }
    }

    private fun loadTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.value = _uiState.value.copy(tecnicos = tecnicos)
            }
        }
    }

    fun onDescripcionChange(newDescripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = newDescripcion)
    }

    fun onTecnicoIdChange(newTecnicoId: String?) {
        _uiState.value = _uiState.value.copy(tecnicoId = newTecnicoId)
    }

    fun saveMensaje() {
        val currentState = _uiState.value
        if (!currentState.tecnicoId.isNullOrEmpty() && currentState.descripcion.isNotEmpty()) {
            // Si quieres guardar el técnico, pero MensajeEntity no tiene ese campo, aquí podrías guardarlo en otro lugar.
            // Por ahora, solo guardaremos descripcion y fecha como String.

            val fechaActual = Date() // Fecha actual
            val fechaStr = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", fechaActual).toString()

            val nuevoMensaje = MensajeEntity(
                descripcion = currentState.descripcion,
                fecha = fechaStr,
                // Rol y nombre podrían venir de alguna otra fuente o ser vacíos
                rol = "",
                nombre = ""
            )
            viewModelScope.launch {
                mensajeRepository.saveMensaje(nuevoMensaje)
                _uiState.value = _uiState.value.copy(
                    successMessage = "Mensaje guardado con éxito",
                    descripcion = "",
                    tecnicoId = null,
                    fecha = System.currentTimeMillis(),
                    errorMessage = null
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Debe llenar todos los campos.")
        }
    }

    fun nuevoMensaje() {
        _uiState.value = _uiState.value.copy(
            descripcion = "",
            tecnicoId = null,
            fecha = System.currentTimeMillis(),
            successMessage = null,
            errorMessage = null
        )
    }
}

data class UiState(
    val mensajes: List<MensajeEntity> = emptyList(),
    val tecnicos: List<TecnicoEntity> = emptyList(),
    val descripcion: String = "",
    val tecnicoId: String? = null,
    val tecnicoSeleccionado: Int? = null, // Campo para el técnico seleccionado
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val fecha: Long = System.currentTimeMillis()
)