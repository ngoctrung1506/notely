package com.app.notely.data.local.mapper

import com.app.notely.data.local.entity.NoteEntity
import com.app.notely.domain.model.Note
import com.google.firebase.firestore.DocumentSnapshot

fun NoteEntity.toNote(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    color = color,
    tags = if (tags.isBlank()) emptyList() else tags.split(","),
    pendingSync = pendingSync,
    isDeleted = isDeleted
)

fun Note.toNoteEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    color = color,
    tags = tags.joinToString(","),
    pendingSync = pendingSync,
    isDeleted = isDeleted
)


fun NoteEntity.toFirestoreMap(): Map<String, Any> = mapOf(
    "id" to id,
    "title" to title,
    "content" to content,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
    "color" to color,
    "tags" to tags
)

fun DocumentSnapshot.toNoteEntity(): NoteEntity = NoteEntity(
    id = getLong("id") ?: 0L,
    title = getString("title") ?: "",
    content = getString("content") ?: "",
    createdAt = getLong("createdAt") ?: 0L,
    updatedAt = getLong("updatedAt") ?: 0L,
    color = getString("color") ?: "#FFFFFF",
    tags = getString("tags") ?: "",
    pendingSync = false,
    isDeleted = false
)
