package com.example.simplenotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.simplenotesapp.ViewModels.NotesViewModel
import com.example.simplenotesapp.databinding.ActivityAddNoteBinding // Se asume este nombre de binding

class AddNoteActivity : AppCompatActivity() {

    // Cambiar a activity_add_note.xml si no se llama 'activity_add_note'
    private lateinit var binding: ActivityAddNoteBinding
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asumiendo que generamos el binding para activity_add_note.xml
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSaveButton()
    }

    private fun setupToolbar() {
        // Configurar la Toolbar como ActionBar
        setSupportActionBar(binding.toolbarAddNote)
        // Habilitar el botón de retroceso (Back/Home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Agregar Nota"
    }

    // Manejar el clic en el botón de retroceso de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        finish() // Regresar a la Activity principal
        return true
    }

    private fun setupSaveButton() {
        binding.saveNoteButton.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.titleEditText.text.toString().trim()
        val content = binding.contentEditText.text.toString().trim()

        // Validación: El cuerpo de la nota NO debe estar vacío
        if (content.isBlank()) {
            Toast.makeText(this, "El cuerpo de la nota no puede estar vacío.", Toast.LENGTH_LONG).show()
            return
        }

        // Si es válido, guardar la nota usando el ViewModel
        // El ViewModel ahora maneja la generación del título si es necesario.
        notesViewModel.addNote(title, content)

        Toast.makeText(this, "Nota guardada con éxito.", Toast.LENGTH_SHORT).show()
        finish() // Regresar a la pantalla principal
    }
}