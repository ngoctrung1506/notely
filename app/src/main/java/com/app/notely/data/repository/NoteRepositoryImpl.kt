package com.app.notely.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.app.notely.data.local.dao.NoteDao
import com.app.notely.data.local.mapper.toNote
import com.app.notely.data.local.mapper.toNoteEntity
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getPagedNotes(): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { noteDao.getPagedNotes() }
    ).flow.map { pagingData ->
        pagingData.map { it.toNote() }
    }

    override fun getPagedNotesByUpdatedAt(query: String): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { noteDao.searchPagedNotesByUpdatedAt(query) }
    ).flow.map { pagingData ->
        pagingData.map { it.toNote() }
    }

    override fun getPagedNotesByCreatedAt(query: String): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { noteDao.searchPagedNotesByCreatedAt(query) }
    ).flow.map { pagingData ->
        pagingData.map { it.toNote() }
    }

    override suspend fun getNoteById(id: Long): Note? =
        noteDao.getNoteById(id)?.toNote()

    override suspend fun saveNote(note: Note): Long =
        noteDao.insertNote(note.toNoteEntity())

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }
}