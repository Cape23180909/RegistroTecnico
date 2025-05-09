package edu.ucne.registrotecnicos.presentation

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import edu.ucne.registrotecnicos.data.entities.TecnicoEntity


class TecnicoViewModel() : ViewModel(), Parcelable {
    private var nextId = 1
    val tecnicoList = mutableStateListOf<TecnicoEntity>()

    constructor(parcel: Parcel) : this() {
        nextId = parcel.readInt()
    }

    fun agregarTecnico(nombre: String, sueldo: Double) {
        tecnicoList.add(
            TecnicoEntity(
                TecnicoId = nextId++,
                Nombre = nombre,
                Sueldo = sueldo
            )
        )
    }

    fun updateTecnico(updatedTecnico: TecnicoEntity) {
        val index = tecnicoList.indexOfFirst { it.TecnicoId == updatedTecnico.TecnicoId }
        if (index != -1) {
            tecnicoList[index] = updatedTecnico
        }
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        tecnicoList.remove(tecnico)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(nextId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TecnicoViewModel> {
        override fun createFromParcel(parcel: Parcel): TecnicoViewModel {
            return TecnicoViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TecnicoViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
