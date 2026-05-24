package com.app.notely.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
    object NoteEditor : Screen("note_editor") {
        const val ARG_NOTE_ID = "noteId"
        const val ROUTE_WITH_ARGS = "note_editor?noteId={noteId}"
        fun createRoute(noteId: Long = 0L) = "note_editor?noteId=$noteId"
    }
    object Tags : Screen("tags")
}