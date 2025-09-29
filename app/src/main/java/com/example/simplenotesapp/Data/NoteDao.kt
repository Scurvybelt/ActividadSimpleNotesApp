package com.example.simplenotesapp.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface NoteDao {
    // Obtiene todas las notas ordenadas por timestamp descendente, envueltas en LiveData
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity)

    // Eliminar una nota por su ID
    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    // Eliminar todas las notas
    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}