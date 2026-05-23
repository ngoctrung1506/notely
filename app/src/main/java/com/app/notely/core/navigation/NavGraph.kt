package com.app.notely.core.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.app.notely.ui.feature.home.HomeScreen


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

}