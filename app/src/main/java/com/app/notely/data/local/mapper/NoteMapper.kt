package com.app.notely.data.local.mapper

import com.app.notely.data.local.entity.NoteEntity
import com.app.notely.domain.model.Note

fun NoteEntity.toNote(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    color = color
)

fun Note.toNoteEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    color = color
)