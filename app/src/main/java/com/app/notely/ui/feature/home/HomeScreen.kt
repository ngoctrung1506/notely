package com.app.notely.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.app.notely.R
import com.app.notely.core.navigation.Screen
import com.app.notely.domain.model.Note
import com.app.notely.domain.model.SyncStatus
import com.app.notely.ui.component.EmptyView
import com.app.notely.ui.feature.home.component.HomeDrawerContent
import com.app.notely.ui.feature.home.component.HomeTopAppBar
import com.app.notely.ui.feature.home.component.NoteCard
import com.app.notely.ui.feature.home.component.SyncStatusBar
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

enum class HomeNavItem { Home, Tags }


@Composable
fun HomeScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.pagedNotes.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val columns = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium -> 2
        else -> 3
    }
    val isCompact = windowWidthSizeClass == WindowWidthSizeClass.Compact
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LaunchedEffect(columns) { viewModel.setColumns(columns) }
    val scope = rememberCoroutineScope()
    var selectedNavItem by remember { mutableStateOf(HomeNavItem.Home) }

    if (isCompact) {
        ModalNavigationDrawer(
            drawerState = drawerState, drawerContent = {
                ModalDrawerSheet {
                    HomeDrawerContent(
                        selectedItem = selectedNavItem, onItemSelected = { item ->
                            selectedNavItem = item
                            scope.launch { drawerState.close() }
                            if (item == HomeNavItem.Tags) {
                                navController.navigate(Screen.Tags.route)
                            }
                        })
                }
            }) {
            HomeMainContent(
                columns = columns,
                lazyPagingItems = lazyPagingItems,
                showMenuIcon = true,
                onMenuClick = { scope.launch { drawerState.open() } },
                onCreateNote = { navController.navigate(Screen.NoteEditor.createRoute()) },
                onNoteClick = { noteId ->
                    navController.navigate(
                        Screen.NoteEditor.createRoute(
                            noteId
                        )
                    )
                },
                onDeleteNote = { viewModel.deleteNote(it) },
                searchQuery = searchQuery,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                sortBy = sortBy,
                onSortByChanged = { viewModel.onSortByChanged(it) },
                syncStatus = syncStatus,
                isOnline = isOnline,
                onSyncClick = { viewModel.triggerSync() })
        }
    } else {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(modifier = Modifier.width(280.dp)) {
                    HomeDrawerContent(
                        selectedItem = selectedNavItem, onItemSelected = { item ->
                            selectedNavItem = item
                            if (item == HomeNavItem.Tags) {
                                navController.navigate(Screen.Tags.route)
                            }
                        })
                }
            }) {
            HomeMainContent(
                columns = columns,
                lazyPagingItems = lazyPagingItems,
                showMenuIcon = false,
                onMenuClick = {},
                onCreateNote = { navController.navigate(Screen.NoteEditor.createRoute()) },
                onNoteClick = { noteId ->
                    navController.navigate(
                        Screen.NoteEditor.createRoute(
                            noteId
                        )
                    )
                },
                onDeleteNote = { viewModel.deleteNote(it) },
                searchQuery = searchQuery,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                sortBy = sortBy,
                onSortByChanged = { viewModel.onSortByChanged(it) },
                syncStatus = syncStatus,
                isOnline = isOnline,
                onSyncClick = { viewModel.triggerSync() })
        }
    }
}


@Composable
private fun HomeMainContent(
    columns: Int,
    lazyPagingItems: LazyPagingItems<Note>,
    showMenuIcon: Boolean,
    onMenuClick: () -> Unit,
    onCreateNote: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    sortBy: SortBy,
    onSortByChanged: (SortBy) -> Unit,
    syncStatus: SyncStatus,
    isOnline: Boolean,
    onSyncClick: () -> Unit
) {
    Scaffold(topBar = {
        HomeTopAppBar(
            showMenuIcon = showMenuIcon,
            onMenuClick = onMenuClick,
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            sortBy = sortBy,
            onSortByChanged = onSortByChanged,
            syncStatus = syncStatus,
            isOnline = isOnline,
            onSyncClick = onSyncClick
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = onCreateNote,
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.home_new_note_label)
            )
        }
    }) { paddingValues ->
        val isEmptyAndLoaded =
            lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.refresh is LoadState.NotLoading

        if (isEmptyAndLoaded) {
            EmptyView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                message = stringResource(R.string.home_empty_message)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SyncStatusBar(syncStatus = syncStatus)
                }

                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id }) { index ->
                    lazyPagingItems[index]?.let { note ->
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) },
                            onDelete = { onDeleteNote(note.id) })
                    }
                }

                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val sampleNotes = listOf(
        Note(
            id = 1,
            title = "Shopping list",
            content = "Eggs, Milk, Bread",
            color = "#FFF9C4",
            createdAt = 1680000000000,
            updatedAt = 1680000000000,
            tags = listOf("Personal")
        ), Note(
            id = 2,
            title = "Ideas",
            content = "App idea: minimal notes",
            color = "#E0F7FA",
            createdAt = 1680000000000,
            updatedAt = 1680000000000,
            tags = emptyList()
        )
    )

    val pagingData = PagingData.from(sampleNotes)
    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

    HomeMainContent(
        columns = 1,
        lazyPagingItems = lazyPagingItems,
        showMenuIcon = true,
        onMenuClick = {},
        onCreateNote = {},
        onNoteClick = { _ -> },
        onDeleteNote = { _ -> },
        searchQuery = "",
        onSearchQueryChanged = {},
        sortBy = SortBy.UpdatedDate,
        onSortByChanged = {},
        syncStatus = SyncStatus.Idle,
        isOnline = true,
        onSyncClick = {})
}

