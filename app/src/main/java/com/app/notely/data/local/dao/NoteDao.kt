package com.app.notely.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.notely.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getPagedNotes(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM notes WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun searchPagedNotesByUpdatedAt(query: String): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM notes WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') AND isDeleted = 0 ORDER BY createdAt DESC")
    fun searchPagedNotesByCreatedAt(query: String): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("SELECT * FROM notes WHERE pendingSync = 1 AND isDeleted = 0")
    suspend fun getPendingSyncNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE (pendingSync = 1 OR isDeleted = 1)")
    suspend fun getPendingSyncNotesIncludingDeleted(): List<NoteEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM notes WHERE (pendingSync = 1 OR isDeleted = 1))")
    fun hasPendingSync(): Flow<Boolean>

}