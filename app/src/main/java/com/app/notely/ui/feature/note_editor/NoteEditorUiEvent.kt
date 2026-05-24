package com.app.notely.ui.feature.note_editor

import com.app.notely.domain.model.Tag

sealed class NoteEditorUiEvent {
    data class TitleChanged(val title: String) : NoteEditorUiEvent()
    data class ContentChanged(val content: String) : NoteEditorUiEvent()
    data class TagToggled(val tag: Tag) : NoteEditorUiEvent()
    object Save : NoteEditorUiEvent()
}
