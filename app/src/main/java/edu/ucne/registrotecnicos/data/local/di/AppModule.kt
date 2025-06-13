package edu.ucne.registrotecnicos.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.local.database.TareaDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideTecnicoDb(@ApplicationContext applicationContext: Context): TareaDb =
        Room.databaseBuilder(
            applicationContext,
            TareaDb::class.java,
            "Tarea.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTecnicoDao(appDataDb: TareaDb) = appDataDb.TecnicoDao()

    @Provides
    @Singleton
    fun provideTicketDao(appDataDb: TareaDb) = appDataDb.TicketDao()

    @Provides
    @Singleton
    fun provideMensajeDao(appDataDb: TareaDb ) = appDataDb.MensajeDao()
}