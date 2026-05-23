package com.app.notely.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.app.notely.ui.feature.list_note.ListNoteScreen

fun NavGraphBuilder.noteNavGraph(navController: NavHostController) {
    composable(Screen.NoteList.route) {
        ListNoteScreen(navController)
    }
}