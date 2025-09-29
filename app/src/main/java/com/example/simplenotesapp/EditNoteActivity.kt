// com/example/simplenotesapp/EditNoteActivity.kt
package com.example.simplenotesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.simplenotesapp.Data.NoteEntity
import com.example.simplenotesapp.ViewModels.NotesViewModel
import com.google.android.material.appbar.MaterialToolbar

class EditNoteActivity : AppCompatActivity() {

    private val notesViewModel: NotesViewModel by viewModels()
    private var currentNoteId: Int = -1

    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        supportActionBar?.title = "Editar Nota"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Muestra el botón de "Atrás"

        editTitle = findViewById(R.id.editTitle)
        editContent = findViewById(R.id.editContent)
        val saveButton: Button = findViewById(R.id.saveButton)

        // Recibir los datos de la nota desde MainActivity
        currentNoteId = intent.getIntExtra("NOTE_ID", -1)
        if (currentNoteId == -1) {
            Toast.makeText(this, "Error: No se pudo cargar la nota.", Toast.LENGTH_SHORT).show()
            finish() // Cierra si no hay ID
            return
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_edit_note)

        // 2. Establece la Toolbar como la ActionBar de la actividad
        setSupportActionBar(toolbar)

        // 3. Habilita la flecha de "hacia atrás" en la ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Llenar los campos con los datos actuales
        editTitle.setText(intent.getStringExtra("NOTE_TITLE"))
        editContent.setText(intent.getStringExtra("NOTE_CONTENT"))

        saveButton.setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        val title = editTitle.text.toString().trim()
        val content = editContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "El título no puede estar vacío.", Toast.LENGTH_SHORT).show()
            return
        }

        // ---------- INICIO DE LA CORRECCIÓN ----------
        // Creamos el objeto NoteEntity asegurándonos de que tenga el ID correcto.
        // El timestamp se asignará automáticamente con la hora actual gracias al valor por defecto.
        val updatedNote = NoteEntity(id = currentNoteId, title = title, content = content)

        // Llamamos a la función del ViewModel que debe existir para actualizar la nota.
        notesViewModel.updateNote(updatedNote)
        // ---------- FIN DE LA CORRECCIÓN ----------

        Toast.makeText(this, "Nota actualizada.", Toast.LENGTH_SHORT).show()
        finish() // Regresa a MainActivity
    }

    // Maneja el clic en el botón "Atrás" de la barra superior
    override fun onSupportNavigateUp(): Boolean {
        // Simula el comportamiento del botón "Atrás" del dispositivo.
        // Cierra la actividad actual sin guardar cambios.
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
