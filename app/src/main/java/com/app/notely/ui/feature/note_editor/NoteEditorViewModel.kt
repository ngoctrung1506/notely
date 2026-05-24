package com.app.notely.ui.feature.note_editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.notely.core.util.DateUtil
import com.app.notely.core.mock.MockTags
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val NOTE_COLORS = listOf(
    "#B2EBF2", "#E1BEE7", "#FFCCBC", "#C8E6C9",
    "#B3E5FC", "#FFF9C4", "#F8BBD0", "#DCEDC8",
    "#FFE0B2", "#E8EAF6", "#F3E5F5", "#E0F2F1"
)

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Long = savedStateHandle.get<Long>("noteId") ?: 0L
    private val isEditMode get() = noteId > 0L

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    init {
        if (isEditMode) {
            loadNote(noteId)
        } else {
            _uiState.update { it.copy(color = NOTE_COLORS.random()) }
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val note = noteRepository.getNoteById(id)
            if (note != null) {
                _uiState.update {
                    it.copy(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                        color = note.color,
                        createdAt = note.createdAt,
                        tags = note.tags.mapNotNull { name -> MockTags.all.find { it.name == name } },
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Note not found") }
            }
        }
    }

    fun onEvent(event: NoteEditorUiEvent) {
        when (event) {
            is NoteEditorUiEvent.TitleChanged -> _uiState.update { it.copy(title = event.title) }
            is NoteEditorUiEvent.ContentChanged -> _uiState.update { it.copy(content = event.content) }
            is NoteEditorUiEvent.TagToggled -> {
                val current = _uiState.value.tags
                val newTags = if (current.any { it.id == event.tag.id }) {
                    current.filterNot { it.id == event.tag.id }
                } else {
                    current + event.tag
                }
                _uiState.update { it.copy(tags = newTags) }
            }
            is NoteEditorUiEvent.Save -> saveNote()
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    private fun saveNote() {
        val state = _uiState.value
        if (state.title.isBlank() && state.content.isBlank()) return

        viewModelScope.launch {
            val now = DateUtil.getCurrentTime()
            if (isEditMode) {
                noteRepository.updateNote(
                    Note(
                        id = state.id,
                        title = state.title,
                        content = state.content,
                        color = state.color,
                        createdAt = state.createdAt,
                        updatedAt = now,
                        tags = state.tags.map { it.name }
                    )
                )
            } else {
                noteRepository.saveNote(
                    Note(
                        title = state.title,
                        content = state.content,
                        color = state.color,
                        createdAt = now,
                        updatedAt = now,
                        tags = state.tags.map { it.name }
                    )
                )
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
