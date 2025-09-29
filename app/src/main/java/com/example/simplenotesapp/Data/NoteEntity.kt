package com.example.simplenotesapp.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// El nombre de la tabla será 'notes'
@Entity(tableName = "notes")
data class NoteEntity(
    // La clave primaria con autogeneración de ID
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)