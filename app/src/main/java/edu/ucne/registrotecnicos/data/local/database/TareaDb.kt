package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.LaboratorioDao
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.PagoDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entities.LaboratorioEntity
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entities.PagoEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
        MensajeEntity::class,
        LaboratorioEntity::class,
        PagoEntity::class
    ],
    version = 12,
    exportSchema = false
)
abstract class TareaDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun TicketDao(): TicketDao
    abstract fun MensajeDao(): MensajeDao
    abstract fun LaboratorioDao(): LaboratorioDao
    abstract fun PagoDao(): PagoDao
}