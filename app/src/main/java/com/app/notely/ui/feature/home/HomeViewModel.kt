package com.app.notely.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortBy {
    UpdatedDate,
    CreatedDate
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow(SortBy.UpdatedDate)
    val sortBy: StateFlow<SortBy> = _sortBy.asStateFlow()

    val pagedNotes: Flow<PagingData<Note>> = combine(
        _searchQuery.debounce(200L),
        _sortBy
    ) { query, sort -> query to sort }
        .distinctUntilChanged()
        .flatMapLatest { (query, sort) ->
            when (sort) {
                SortBy.UpdatedDate -> noteRepository.getPagedNotesByUpdatedAt(query)
                SortBy.CreatedDate -> noteRepository.getPagedNotesByCreatedAt(query)
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSortByChanged(sort: SortBy) {
        _sortBy.value = sort
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }
}

