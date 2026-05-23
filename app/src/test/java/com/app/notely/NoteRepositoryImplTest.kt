package com.app.notely

import com.app.notely.data.local.dao.NoteDao
import com.app.notely.data.repository.NoteRepositoryImpl
import io.mockk.mockk
import org.junit.Test

class NoteRepositoryImplTest {
    private val noteDao = mockk<NoteDao>()
    private val repository = NoteRepositoryImpl(noteDao)

    @Test
    fun `test get all notes`() {
        // TODO: Implement test
    }
}