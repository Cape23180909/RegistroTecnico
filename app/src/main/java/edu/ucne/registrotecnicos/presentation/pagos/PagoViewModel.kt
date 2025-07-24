package edu.ucne.registrotecnicos.presentation.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.repository.PagoRepository
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.PagoDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagoViewModel @Inject constructor(
    private val pagoRepository: PagoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPagos()
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

    fun create() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                pagoRepository.createPago(
                    PagoDto(
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getPagos()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al guardar")
                }
            }
        }
    }

    fun update() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                pagoRepository.updatePago(
                    currentState.pagoId ?: 0,
                    PagoDto(
                        pagoId = currentState.pagoId ?: 0,
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getPagos()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al actualizar")
                }
            }
        }
    }

    fun getPagos() {
        viewModelScope.launch {
            pagoRepository.getPagos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                pagos = result.data ?: emptyList(),
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
                    else -> {}
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

    fun setPagoId(id: Int) {
        _uiState.update { it.copy(pagoId = id) }
    }

    fun limpiarCampos() {
        _uiState.update {
            it.copy(
                pagoId = null,
                descripcion = null,
                monto = 0.0,
                inputError = null
            )
        }
    }
}

data class PagoUiState(
    val pagoId: Int? = null,
    val descripcion: String? = null,
    val monto: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val pagos: List<PagoDto> = emptyList(),
    val inputError: String? = null
)