package com.app.notely.core.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.notely.ui.feature.note_editor.NoteEditorScreen
import com.app.notely.ui.feature.home.HomeScreen
import com.app.notely.core.navigation.Screen


fun NavGraphBuilder.noteNavGraph(
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    composable(Screen.Home.route) {
        HomeScreen(
            windowWidthSizeClass = windowWidthSizeClass,
            navController = navController
        )
    }

    composable(
        route = Screen.NoteEditor.ROUTE_WITH_ARGS,
        arguments = listOf(
            navArgument(Screen.NoteEditor.ARG_NOTE_ID) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        NoteEditorScreen(
            windowWidthSizeClass = windowWidthSizeClass,
            onNavigateBack = { navController.popBackStack() }
        )
    }

}