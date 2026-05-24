package com.app.notely.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.app.notely.data.local.dao.NoteDao
import com.app.notely.data.local.mapper.toNote
import com.app.notely.data.local.mapper.toNoteEntity
import com.app.notely.data.local.preferences.SyncPreferences
import com.app.notely.data.remote.FirestoreDataSource
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val syncPreferences: SyncPreferences
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
        noteDao.insertNote(note.copy(pendingSync = true).toNoteEntity())

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.copy(pendingSync = true).toNoteEntity())
    }

    override suspend fun deleteNote(noteId: Long) {
        // Soft delete: mark as deleted and pending sync instead of removing
        val note = noteDao.getNoteById(noteId)
        if (note != null) {
            noteDao.updateNote(note.copy(isDeleted = true, pendingSync = true))
        }
    }

    override fun hasPendingSync(): Flow<Boolean> = noteDao.hasPendingSync()

    /**
     * Two-pass sync:
     * Pass 1 — Push: for every locally pending note, push to Firestore (last-write-wins on updatedAt).
     *          Deleted notes are pushed as deletes and then hard-deleted locally.
     * Pass 2 — Pull: fetch all remote notes and upsert any that are newer than (or absent from) local.
     */
    override suspend fun syncNotes(): Result<Unit> = runCatching {
        // ── Pass 1: push local pending changes ──────────────────────────────
        val pendingNotes = noteDao.getPendingSyncNotesIncludingDeleted()
        for (localEntity in pendingNotes) {
            when {
                localEntity.isDeleted -> {
                    firestoreDataSource.deleteNote(localEntity.id)
                    noteDao.deleteNote(localEntity.id)
                }
                else -> {
                    val remoteEntity = runCatching { firestoreDataSource.getNote(localEntity.id) }.getOrNull()
                    if (remoteEntity == null || localEntity.updatedAt >= remoteEntity.updatedAt) {
                        firestoreDataSource.upsertNote(localEntity)
                        noteDao.updateNote(localEntity.copy(pendingSync = false))
                    } else {
                        // Remote is newer even though local had a pending flag — take remote
                        noteDao.updateNote(remoteEntity.copy(pendingSync = false))
                    }
                }
            }
        }

        // ── Pass 2: pull remote notes updated since last sync ────────────────
        // Only fetches notes with updatedAt > lastSyncedAt — scales to any collection size.
        val lastSyncedAt = syncPreferences.getLastSyncedAt()
        val remoteNotes = firestoreDataSource.getNotesUpdatedAfter(lastSyncedAt)
        for (remoteEntity in remoteNotes) {
            val localEntity = noteDao.getNoteById(remoteEntity.id)
            when {
                // Note doesn't exist locally at all — insert it
                localEntity == null -> noteDao.insertNote(remoteEntity.copy(pendingSync = false))
                // Remote is strictly newer and local has no pending changes — overwrite local
                remoteEntity.updatedAt > localEntity.updatedAt && !localEntity.pendingSync ->
                    noteDao.updateNote(remoteEntity.copy(pendingSync = false))
            }
        }

        // Persist the sync timestamp so the next sync only fetches newer changes
        syncPreferences.setLastSyncedAt(System.currentTimeMillis())
    }
}
