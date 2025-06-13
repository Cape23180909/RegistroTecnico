package ucne.edu.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.database.TareaDb
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MensajeRepository @Inject constructor(
    private val tareaDb: TareaDb
){
    suspend fun saveMensaje(mensaje: MensajeEntity){
        tareaDb.MensajeDao().save(mensaje)
    }

    suspend fun find(id: Int): MensajeEntity?{
        return tareaDb.MensajeDao().find(id)
    }

    suspend fun delete(mensaje: MensajeEntity){
        return tareaDb.MensajeDao().delete(mensaje)
    }

    suspend fun getAll(): Flow<List<MensajeEntity>>{
        return tareaDb.MensajeDao().getAll()
    }
}