package com.app.notely.domain.model

sealed class SyncStatus {
    data object Idle : SyncStatus()
    data object PendingSync : SyncStatus()
    data object Syncing : SyncStatus()
    data class Success(val lastSyncedAt: Long) : SyncStatus()
    data class Error(val message: String) : SyncStatus()
    data object NetworkOffline : SyncStatus()
}
