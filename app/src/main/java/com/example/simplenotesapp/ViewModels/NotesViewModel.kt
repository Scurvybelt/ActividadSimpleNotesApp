package com.example.simplenotesapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.Data.NoteDatabase
import com.example.simplenotesapp.Data.NoteEntity // Importar la nueva entidad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// NOTA: La clase 'Note' debe ser eliminada o movida, pero para mantener la compatibilidad
// con el adaptador y MainActivity, puedes crear un alias o una clase auxiliar si es necesario.
// O, más simple, adaptar el adaptador y MainActivity para usar NoteEntity.
// *Asumiendo que se adaptará el resto del código para usar NoteEntity*

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    // Referencia al DAO
    private val noteDao = NoteDatabase.getDatabase(application).noteDao()

    // LiveData directamente desde Room
    val notes: LiveData<List<NoteEntity>> = noteDao.getAllNotes()

    // El contador de notas se puede obtener del tamaño de 'notes' en la Activity
    // Eliminamos _notesCount si ya no es necesario

    private val _notesCount = MutableLiveData<Int>(0)
    val notesCount: LiveData<Int> get() = _notesCount

    init {
        // Observar la lista de notas de Room para actualizar el contador
        notes.observeForever { list ->
            _notesCount.value = list.size
        }
    }

    // Función modificada para guardar en Room
    fun addNote(title: String, noteContent: String) {
        // Validación y generación de título movidas al ViewModel para el guardado
        val finalTitle = if (title.isBlank()) {
            noteContent.substringBefore('\n').take(30).trim().ifBlank { "Nueva Nota" }
        } else {
            title
        }

        val timestamp = System.currentTimeMillis()
        val newNote = NoteEntity(
            title = finalTitle,
            content = noteContent,
            timestamp = timestamp
        )

        // Usar Coroutine para la operación de base de datos asíncrona
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(newNote)
        }
    }

    // Nueva función para eliminar una nota por su ID
    fun deleteNote(noteId: Int) { // Cambiar a Int si NoteEntity usa Int como ID
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteById(noteId)
        }
    }

    fun clearAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteAll()
        }
    }

    // Función auxiliar para formatear el timestamp (se mantiene)
    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        // Si tienes un repositorio:
        noteDao.update(note)
        // Si no, llama directamente al DAO:
        // noteDao.update(note)
    }
}