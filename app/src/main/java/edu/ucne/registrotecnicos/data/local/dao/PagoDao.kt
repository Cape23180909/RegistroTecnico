package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entities.PagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoDao {
    @Upsert()
    suspend fun save(pago: List<PagoEntity>)

    @Query(
        """
        SELECT * 
        FROM Pagos
        WHERE PagoId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): PagoEntity?

    @Delete
    suspend fun delete(pago: PagoEntity)

    @Query("SELECT * FROM Pagos")
    fun getAll(): Flow<List<PagoEntity>>
}