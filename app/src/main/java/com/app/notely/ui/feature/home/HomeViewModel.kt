package com.app.notely.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.notely.core.network.ConnectivityService
import com.app.notely.data.local.preferences.SyncPreferences
import com.app.notely.domain.model.Note
import com.app.notely.domain.model.SyncStatus
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortBy {
    UpdatedDate,
    CreatedDate
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val syncPreferences: SyncPreferences,
    connectivityService: ConnectivityService
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow(SortBy.UpdatedDate)
    val sortBy: StateFlow<SortBy> = _sortBy.asStateFlow()

    private val _columns = MutableStateFlow(1)

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    val pagedNotes: Flow<PagingData<Note>> = combine(
        _searchQuery.debounce(200L),
        _sortBy,
        _columns
    ) { query, sort, cols -> Triple(query, sort, cols) }
        .distinctUntilChanged()
        .flatMapLatest { (query, sort, cols) ->
            when (sort) {
                SortBy.UpdatedDate -> noteRepository.getPagedNotesByUpdatedAt(query, cols)
                SortBy.CreatedDate -> noteRepository.getPagedNotesByCreatedAt(query, cols)
            }
        }
        .cachedIn(viewModelScope)

    init {
        // Observe connectivity
        connectivityService.observeConnectivity()
            .onEach { isOnline ->
                _isOnline.value = isOnline
                if (!isOnline && _syncStatus.value !is SyncStatus.Syncing) {
                    _syncStatus.value = SyncStatus.NetworkOffline
                } else if (isOnline && _syncStatus.value is SyncStatus.NetworkOffline) {
                    // Back online - clear offline status, let pending sync observer take over
                    _syncStatus.value = SyncStatus.Idle
                }
            }
            .launchIn(viewModelScope)

        // Reflect pending-sync state reactively
        noteRepository.hasPendingSync()
            .onEach { hasPending ->
                if (_syncStatus.value !is SyncStatus.Syncing &&
                    _syncStatus.value !is SyncStatus.NetworkOffline
                ) {
                    if (hasPending) {
                        _syncStatus.value = SyncStatus.PendingSync
                    } else {
                        val lastSyncedAt = syncPreferences.getLastSyncedAt()
                        _syncStatus.value = if (lastSyncedAt > 0) SyncStatus.Success(lastSyncedAt) else SyncStatus.Idle
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSortByChanged(sort: SortBy) {
        _sortBy.value = sort
    }

    fun setColumns(columns: Int) {
        _columns.value = columns
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun triggerSync() {
        if (_syncStatus.value is SyncStatus.Syncing) return
        if (!_isOnline.value) return

        viewModelScope.launch {
            _syncStatus.value = SyncStatus.Syncing
            val result = noteRepository.syncNotes()
            _syncStatus.value = if (result.isSuccess) {
                val lastSyncedAt = syncPreferences.getLastSyncedAt()
                SyncStatus.Success(lastSyncedAt)
            } else {
                SyncStatus.Error(result.exceptionOrNull()?.message ?: "Sync failed")
            }
        }
    }
}


