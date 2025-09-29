package com.example.simplenotesapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView // <<-- ASEGURARSE DE ESTA IMPORTACIÓN
import com.example.simplenotesapp.R
import com.example.simplenotesapp.Data.NoteEntity // <<-- USAR NoteEntity
import com.example.simplenotesapp.ViewModels.NotesViewModel
// import androidx.cardview.widget.CardView // Ya no es necesario importar si solo se usa itemView

class NoteAdapter(
    // Cambiar el tipo de datos de la lista a NoteEntity
    private var notes: List<NoteEntity>,
    private val viewModel: NotesViewModel,
    // Cambiar el tipo de dato en el callback a NoteEntity
    private val onNoteClick: (NoteEntity) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() { // <<-- ASEGURARSE DE HEREDAR CORRECTAMENTE

    // La clase ViewHolder debe heredar de RecyclerView.ViewHolder
    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.noteTitleTextView)
        val dateTextView: TextView = view.findViewById(R.id.noteDateTextView)
        // No necesitamos la referencia CardView si solo usamos itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        // El error 'Unresolved reference 'NoteViewHolder'.' es corregido al asegurar la clase interna y la herencia
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.titleTextView.text = note.title
        // Usar la función del ViewModel para formatear el timestamp
        holder.dateTextView.text = viewModel.formatTimestamp(note.timestamp)

        // Configurar el listener de clic en todo el ítem
        holder.itemView.setOnClickListener {
            onNoteClick(note)
        }
    }

    override fun getItemCount() = notes.size

    fun updateNotes(newNotes: List<NoteEntity>) {
        // Asegurarse de que el orden sea descendente, tal como Room lo devuelve.
        this.notes = newNotes
        notifyDataSetChanged()
    }
}