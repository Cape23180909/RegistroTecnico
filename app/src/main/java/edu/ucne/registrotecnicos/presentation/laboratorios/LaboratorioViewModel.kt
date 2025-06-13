package edu.ucne.registrotecnicos.presentation.laboratorios

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.repository.LaboratorioRepository
import edu.ucne.registrotecnicos.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class LaboratorioViewModel @Inject constructor(
    private val laboratorioRepository: LaboratorioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaboratorioUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLaboratorios()
    }

    fun validarCampos(): Boolean {
        val descripcion = _uiState.value.descripcion
        val monto = _uiState.value.monto

        return when {
            descripcion.isNullOrBlank() -> {
                _uiState.update { it.copy(inputError = "La descripción no puede estar vacía") }
                false
            }
            monto <= 0.0 -> {
                _uiState.update { it.copy(inputError = "El monto debe ser mayor que cero") }
                false
            }
            else -> {
                _uiState.update { it.copy(inputError = null) }
                true
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun create() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                laboratorioRepository.createLaboratorio(
                    LaboratorioDto(
                        laboratorioId = 0,
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getLaboratorios()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al guardar")
                }
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun update() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                laboratorioRepository.updateLaboratorio(
                    LaboratorioDto(
                        laboratorioId = currentState.laboratorioId ?: 0,
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getLaboratorios()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al actualizar")
                }
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getLaboratorios() {
        viewModelScope.launch {
            laboratorioRepository.getLaboratorios().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                laboratorios = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun setDescripcion(value: String) {
        _uiState.update { it.copy(descripcion = value) }
    }

    fun setMonto(value: Double) {
        _uiState.update { it.copy(monto = value) }
    }

    fun setLaboratorioId(id: Int) {
        _uiState.update { it.copy(laboratorioId = id) }
    }

    fun limpiarCampos() {
        _uiState.update {
            it.copy(
                laboratorioId = null,
                descripcion = null,
                monto = 0.0,
                inputError = null
            )
        }
    }
}

// UI State
data class LaboratorioUiState(
    val laboratorioId: Int? = null,
    val descripcion: String? = null,
    val monto: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val laboratorios: List<LaboratorioDto> = emptyList(),
    val inputError: String? = null
)