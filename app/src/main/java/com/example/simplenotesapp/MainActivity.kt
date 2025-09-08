package com.example.simplenotesapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.simplenotesapp.databinding.MainlayoutBinding
import com.example.simplenotesapp.ViewModels.NotesViewModel
class MainActivity : ComponentActivity() {
    private lateinit var binding: MainlayoutBinding
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        notesViewModel.notes.observe(this) { notesList ->
            // Actualiza tu UI con la lista de notas
            binding.notesListTextView.text = notesList.joinToString(separator = "\n\n") { "- ${it.content}" }
        }

        notesViewModel.notesCount.observe(this) { count ->
            // Actualiza tu UI con el contador de notas
            binding.notesCountTextView.text = "Notas: $count"
        }

        // Configurar el listener para el botón de guardar nota
        binding.saveNoteButton.setOnClickListener {
            val newNoteContent = binding.noteEditText.text.toString()
            if (newNoteContent.isNotBlank()) {
                notesViewModel.addNote(newNoteContent)
                binding.noteEditText.text.clear()
            }
        }


        binding.clearNotesButton.setOnClickListener {
            notesViewModel.clearAllNotes()
        }

    }
}

