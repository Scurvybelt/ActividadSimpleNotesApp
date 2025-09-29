package com.example.simplenotesapp

import android.content.Intent // <- IMPORTACIÓN AÑADIDA
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplenotesapp.Adapters.NoteAdapter
import com.example.simplenotesapp.Data.NoteEntity // <- He añadido esta importación para que el código sea más limpio
import com.example.simplenotesapp.ViewModels.NotesViewModel
import com.example.simplenotesapp.databinding.MainlayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainlayoutBinding
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

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
            startActivity(Intent(this, AddNoteActivity::class.java))
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

    // ---------- INICIO DE LA MODIFICACIÓN ----------
    private fun setupRecyclerView() {
        // Inicializar el adaptador pasando los dos listeners:
        // 1. onNoteClick para el clic simple.
        // 2. onNoteDoubleClick para el doble clic.
        noteAdapter = NoteAdapter(
            emptyList(),
            notesViewModel,
            onNoteClick = { note ->
                // Acción al hacer CLIC SIMPLE: abrir el diálogo de detalle/eliminar.
                showNoteDetailDialog(note)
            },
            onNoteDoubleClick = { note ->
                // Acción al hacer DOBLE CLIC: lanzar la actividad de edición.
                val intent = Intent(this, EditNoteActivity::class.java).apply {
                    // Adjuntamos los datos de la nota que queremos editar.
                    putExtra("NOTE_ID", note.id)
                    putExtra("NOTE_TITLE", note.title)
                    putExtra("NOTE_CONTENT", note.content)
                }
                // Iniciamos la actividad de edición.
                startActivity(intent)
            }
        )

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
    }
    // ---------- FIN DE LA MODIFICACIÓN ----------

    // Muestra un diálogo para ver el detalle de la nota
    private fun showNoteDetailDialog(note: NoteEntity) {
        AlertDialog.Builder(this)
            .setTitle(note.title)
            .setMessage(note.content)
            .setPositiveButton("Cerrar", null)
            .setNegativeButton("Eliminar") { dialog, _ ->
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
