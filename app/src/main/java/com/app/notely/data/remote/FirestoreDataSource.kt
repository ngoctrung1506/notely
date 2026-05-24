package com.app.notely.data.remote

import com.app.notely.data.local.entity.NoteEntity
import com.app.notely.data.local.mapper.toFirestoreMap
import com.app.notely.data.local.mapper.toNoteEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val COLLECTION_NOTES = "notes"

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /** Returns the Firestore note for this ID, or null if it doesn't exist. */
    suspend fun getNote(noteId: Long): NoteEntity? {
        val doc = firestore.collection(COLLECTION_NOTES)
            .document(noteId.toString())
            .get()
            .await()
        return if (doc.exists()) doc.toNoteEntity() else null
    }

    /**
     * Returns only notes updated after [sinceTimestamp].
     * Use this for incremental sync to avoid fetching the entire collection.
     */
    suspend fun getNotesUpdatedAfter(sinceTimestamp: Long): List<NoteEntity> {
        return firestore.collection(COLLECTION_NOTES)
            .whereGreaterThan("updatedAt", sinceTimestamp)
            .get()
            .await()
            .documents
            .mapNotNull { doc -> if (doc.exists()) doc.toNoteEntity() else null }
    }

    /** Creates or overwrites a note document in Firestore. */
    suspend fun upsertNote(note: NoteEntity) {
        firestore.collection(COLLECTION_NOTES)
            .document(note.id.toString())
            .set(note.toFirestoreMap())
            .await()
    }

    /** Deletes a note document from Firestore. */
    suspend fun deleteNote(noteId: Long) {
        firestore.collection(COLLECTION_NOTES)
            .document(noteId.toString())
            .delete()
            .await()
    }

}
