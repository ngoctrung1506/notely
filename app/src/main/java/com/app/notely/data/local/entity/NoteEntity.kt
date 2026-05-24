package com.app.notely.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val color: String = "#FFFFFF",
    val tags: String = "",
    val pendingSync: Boolean = true,
    val isDeleted: Boolean = false
)