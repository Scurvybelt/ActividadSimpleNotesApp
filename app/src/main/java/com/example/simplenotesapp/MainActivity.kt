package com.example.simplenotesapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity // Importación necesaria
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplenotesapp.Adapters.NoteAdapter
import com.example.simplenotesapp.ViewModels.NotesViewModel
import com.example.simplenotesapp.databinding.MainlayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainlayoutBinding
    // Asumiendo que el paquete del Adaptador es com.example.simplenotesapp.Adapters
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    // ... (onCreate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Configurar la Toolbar como ActionBar de la Activity
        setupToolbar(binding.toolbar)

        // 2. Configurar el RecyclerView
        setupRecyclerView()

        // 3. Observar la lista de notas (ahora NoteEntity)
        notesViewModel.notes.observe(this) { notesList ->
            // Actualiza el adaptador cuando cambian los datos
            noteAdapter.updateNotes(notesList)
            // Actualizar el contador de notas en el subtítulo de la Toolbar
            supportActionBar?.subtitle = "Total: ${notesList.size} notas"
        }

        // 4. Configurar el Floating Action Button (FAB) para agregar notas
        binding.addNoteFab.setOnClickListener {
            // Reemplazamos showAddNoteDialog() por el lanzamiento de la nueva Activity
            startActivity(android.content.Intent(this, AddNoteActivity::class.java))
        }
    }

    /**
     * Establece la Toolbar como el ActionBar de la Activity.
     */
    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Mis Notas"
    }

    /**
     * Infla el menú (res/menu/main_menu.xml) en la Toolbar.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Maneja los clics en los ítems de la Toolbar (Botón Eliminar Todo).
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                showClearNotesConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        // Inicializar el adaptador con una lista vacía y el listener de clic
        // Cambiar a NoteEntity
        noteAdapter = NoteAdapter(emptyList(), notesViewModel) { note ->
            // Acción al hacer clic en una nota: abrir un diálogo para ver/eliminar
            showNoteDetailDialog(note)
        }

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
    }

    // Muestra un diálogo para ver el detalle de la nota
    private fun showNoteDetailDialog(note: com.example.simplenotesapp.Data.NoteEntity) {
        AlertDialog.Builder(this)
            .setTitle(note.title)
            .setMessage(note.content)
            .setPositiveButton("Cerrar", null)
            .setNegativeButton("Eliminar") { dialog, _ ->
                // NoteEntity usa Int como ID, cambiar el type-casting a Int
                notesViewModel.deleteNote(note.id)
                Toast.makeText(this, "Nota eliminada: ${note.title}", Toast.LENGTH_SHORT).show()
            }
            .show()
    }


    // Muestra un diálogo de confirmación para eliminar todas las notas
    private fun showClearNotesConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Todas las Notas")
            .setMessage("¿Estás seguro de que quieres eliminar TODAS las notas? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar Todo") { dialog, _ ->
                notesViewModel.clearAllNotes()
                Toast.makeText(this, "Todas las notas han sido eliminadas.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}


