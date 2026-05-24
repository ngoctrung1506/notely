package com.app.notely.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sync_prefs")

@Singleton
class SyncPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val lastSyncedAtKey = longPreferencesKey("last_synced_at")

    suspend fun getLastSyncedAt(): Long =
        context.dataStore.data.first()[lastSyncedAtKey] ?: 0L

    suspend fun setLastSyncedAt(timestamp: Long) {
        context.dataStore.edit { it[lastSyncedAtKey] = timestamp }
    }
}
