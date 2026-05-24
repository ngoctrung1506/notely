package com.app.notely.domain.repository

import androidx.paging.PagingData
import com.app.notely.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getPagedNotes(): Flow<PagingData<Note>>
    fun getPagedNotesByUpdatedAt(query: String, columns: Int = 1): Flow<PagingData<Note>>
    fun getPagedNotesByCreatedAt(query: String, columns: Int = 1): Flow<PagingData<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(noteId: Long)
    fun hasPendingSync(): Flow<Boolean>
    suspend fun syncNotes(): Result<Unit>
}