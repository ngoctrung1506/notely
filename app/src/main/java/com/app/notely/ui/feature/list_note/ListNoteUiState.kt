package com.app.notely.ui.feature.list_note

import com.app.notely.domain.model.Note

data class ListNoteUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)