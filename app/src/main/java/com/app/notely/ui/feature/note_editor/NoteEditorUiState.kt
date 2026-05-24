package com.app.notely.ui.feature.note_editor

import com.app.notely.domain.model.Tag

data class NoteEditorUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val color: String = "#FFFFFF",
    val createdAt: Long = 0L,
    val tags: List<Tag> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
