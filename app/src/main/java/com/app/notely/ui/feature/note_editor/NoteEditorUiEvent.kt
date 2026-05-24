package com.app.notely.ui.feature.note_editor

sealed class NoteEditorUiEvent {
    data class TitleChanged(val title: String) : NoteEditorUiEvent()
    data class ContentChanged(val content: String) : NoteEditorUiEvent()
    object Save : NoteEditorUiEvent()
}
