package com.example.simplenotesapp.ViewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Note(val id: Long, val content: String)

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("NotesPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Para la lista de notas
    private val _notes = MutableLiveData<List<Note>>(emptyList())
    val notes: LiveData<List<Note>> get() = _notes

    // Para el contador de notas
    private val _notesCount = MutableLiveData<Int>(0)
    val notesCount: LiveData<Int> get() = _notesCount

    private var nextNoteId = 0L // Para generar IDs únicos simples

    init {
        loadNotesFromPreferences()
    }

    fun addNote(noteContent: String) {
        val currentNotes = _notes.value?.toMutableList() ?: mutableListOf()
        val newNote = Note(id = nextNoteId++, content = noteContent)
        currentNotes.add(newNote)
        _notes.value = currentNotes
        _notesCount.value = currentNotes.size
        saveNotesToPreferences()
    }

    fun clearAllNotes() {
        _notes.value = emptyList()
        _notesCount.value = 0
        saveNotesToPreferences()
    }



    private fun saveNotesToPreferences() {
        val notesJson = gson.toJson(_notes.value)
        sharedPreferences.edit().putString("notesList", notesJson).apply()
    }

    private fun loadNotesFromPreferences() {
        val notesJson = sharedPreferences.getString("notesList", null)
        if (notesJson != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            val loadedNotes: List<Note> = gson.fromJson(notesJson, type)
            _notes.value = loadedNotes
            _notesCount.value = loadedNotes.size
            if (loadedNotes.isNotEmpty()) {
                nextNoteId = (loadedNotes.maxOfOrNull { it.id } ?: -1L) + 1
            }
        }
    }



}
