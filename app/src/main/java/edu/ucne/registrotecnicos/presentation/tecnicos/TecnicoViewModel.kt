package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.repository.TecnicoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TecnicoViewModel @Inject constructor(
    private val repository: TecnicoRepository
) : ViewModel() {

    // Exponemos el listado de técnicos como StateFlow
    val tecnicoList: StateFlow<List<TecnicoEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Agregar un nuevo técnico
    fun agregarTecnico(nombre: String, sueldo: Double) {
        val tecnico = TecnicoEntity(
            tecnicoId = null,
            nombre = nombre,
            sueldo = sueldo
        )
        saveTecnico(tecnico)
    }

    // Guardar o actualizar un técnico
    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.save(tecnico)
        }
    }

    // Eliminar un técnico
    fun delete(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.delete(tecnico)
        }
    }

    fun update(tecnico: TecnicoEntity) {
        saveTecnico(tecnico)
    }

    // Buscar técnico por ID en la lista actual
    fun getTecnicoById(id: Int?): TecnicoEntity? {
        return tecnicoList.value.find { it.tecnicoId == id }
    }
}