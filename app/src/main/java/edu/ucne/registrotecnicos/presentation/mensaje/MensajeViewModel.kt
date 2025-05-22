package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.registrotecnicos.data.repository.MensajeRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadMensajes()
    }

    private fun loadMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect { mensajes ->
                _uiState.update { it.copy(mensajes = mensajes) }
            }
        }
    }

    fun onDescripcionChange(newValue: String) {
        _uiState.update { it.copy(descripcion = newValue) }
    }

    fun onNombreChange(newValue: String) {
        _uiState.update { it.copy(nombre = newValue) }
    }

    fun onRolChange(newValue: String) {
        _uiState.update { it.copy(rol = newValue) }
    }

    fun saveMensaje() {
        val state = _uiState.value

        if (state.descripcion.isBlank() || state.nombre.isBlank() || state.rol.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos los campos deben estar llenos.") }
            return
        }

        val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val nuevoMensaje = MensajeEntity(
            descripcion = state.descripcion,
            fecha = fechaActual,
            rol = state.rol,
            nombre = state.nombre
        )

        viewModelScope.launch {
            mensajeRepository.save(nuevoMensaje)
            _uiState.update {
                it.copy(
                    descripcion = "",
                    rol = "",
                    nombre = "",
                    successMessage = "Mensaje guardado con éxito",
                    errorMessage = null
                )
            }
        }
    }

    fun nuevoMensaje() {
        _uiState.update {
            it.copy(descripcion = "", rol = "", nombre = "", errorMessage = null, successMessage = null)
        }
    }
}

data class UiState(
    val mensajes: List<MensajeEntity> = emptyList(),
    val descripcion: String = "",
    val nombre: String = "",
    val rol: String = "",
    val successMessage: String? = null,
    val errorMessage: String? = null
)