package com.app.notely.ui.feature.list_note

sealed class ListNoteUiEvent {
    object RefreshNotes : ListNoteUiEvent()
    data class DeleteNote(val noteId: Long) : ListNoteUiEvent()
}