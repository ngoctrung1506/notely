package com.app.notely.domain.repository

import androidx.paging.PagingData
import com.app.notely.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getPagedNotes(): Flow<PagingData<Note>>

}