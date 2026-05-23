package com.app.notely

import com.app.notely.domain.repository.NoteRepository
import io.mockk.mockk
import org.junit.Test

class ListNoteViewModelTest {
    private val noteRepository = mockk<NoteRepository>()

    @Test
    fun `test note list loading`() {
        // TODO: Implement test
    }
}