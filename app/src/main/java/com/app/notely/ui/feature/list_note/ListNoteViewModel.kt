package com.app.notely.ui.feature.list_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.notely.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListNoteUiState())
    val uiState: StateFlow<ListNoteUiState> = _uiState

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            // TODO: Load notes from repository
        }
    }

    fun onEvent(event: ListNoteUiEvent) {
        when (event) {
            is ListNoteUiEvent.RefreshNotes -> loadNotes()
            is ListNoteUiEvent.DeleteNote -> deleteNote(event.noteId)
        }
    }

    private fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            // TODO: Delete note from repository
        }
    }
}