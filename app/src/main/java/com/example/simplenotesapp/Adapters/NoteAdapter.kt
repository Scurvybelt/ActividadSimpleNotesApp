package com.example.simplenotesapp.Adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.semantics.setText
// import androidx.compose.ui.semantics.text // <- LÍNEA ELIMINADA y nos aseguramos de que no haya otras de compose.ui
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotesapp.Data.NoteEntity
import com.example.simplenotesapp.ViewModels.NotesViewModel
import com.example.simplenotesapp.databinding.NoteItemBinding

class NoteAdapter(
    private var notes: List<NoteEntity>,
    private val viewModel: NotesViewModel,
    private val onNoteClick: (NoteEntity) -> Unit,
    private val onNoteDoubleClick: (NoteEntity) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var isDoubleClick = false
    private val doubleClickThreshold: Long = 300 // Umbral de 300ms

    inner class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        // ---------- INICIO DE LA CORRECCIÓN ----------
        // Usamos .setText() explícitamente para evitar la ambigüedad con Jetpack Compose.
        holder.binding.noteTitleTextView.setText(note.title)
        holder.binding.noteDateTextView.setText(viewModel.formatTimestamp(note.timestamp))
        // ---------- FIN DE LA CORRECCIÓN ----------

        // Lógica de clic para diferenciar entre clic simple y doble clic
        holder.itemView.setOnClickListener {
            if (!isDoubleClick) {
                isDoubleClick = true // Marcamos que ha ocurrido un primer clic.

                // Esperamos `doubleClickThreshold` milisegundos.
                handler.postDelayed({
                    if (isDoubleClick) {
                        // Si después del retraso, la bandera no ha cambiado, fue un CLIC SIMPLE.
                        onNoteClick(note)
                        isDoubleClick = false // Reseteamos la bandera.
                    }
                }, doubleClickThreshold)
            } else {
                // Si la bandera ya era `true`, es un DOBLE CLIC.
                isDoubleClick = false // Reseteamos la bandera.
                handler.removeCallbacksAndMessages(null) // Cancelamos el clic simple que estaba en espera.
                onNoteDoubleClick(note) // Ejecutamos la acción de DOBLE CLIC.
            }
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<NoteEntity>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
