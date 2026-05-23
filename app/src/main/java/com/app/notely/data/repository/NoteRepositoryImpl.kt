package com.app.notely.data.repository

import com.app.notely.data.local.dao.NoteDao
import com.app.notely.data.local.mapper.toNote
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { entities ->
            entities.map { it.toNote() }
        }


}