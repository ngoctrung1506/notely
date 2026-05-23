package com.app.notely.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
}