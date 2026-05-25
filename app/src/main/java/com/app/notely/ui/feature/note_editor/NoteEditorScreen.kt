package com.app.notely.ui.feature.note_editor

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.notely.R
import com.app.notely.ui.feature.note_editor.component.MobileNoteEditorLayout
import com.app.notely.ui.feature.note_editor.component.TabletNoteEditorLayout

@Composable
fun NoteEditorScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    onNavigateBack: () -> Unit,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val (showDeleteDialog, setShowDeleteDialog) = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved, uiState.isDeleted) {
        if (uiState.isSaved || uiState.isDeleted) onNavigateBack()
    }

    val cardColor = try {
        Color(uiState.color.toColorInt())
    } catch (e: IllegalArgumentException) {
        MaterialTheme.colorScheme.surface
    }

    val isTablet = windowWidthSizeClass != WindowWidthSizeClass.Compact

    if (isTablet) {
        TabletNoteEditorLayout(
            uiState = uiState,
            cardColor = cardColor,
            onEvent = viewModel::onEvent,
            onNavigateBack = onNavigateBack,
            onShowDeleteDialog = { setShowDeleteDialog(true) }
        )
    } else {
        MobileNoteEditorLayout(
            uiState = uiState,
            cardColor = cardColor,
            onEvent = viewModel::onEvent,
            onNavigateBack = onNavigateBack,
            onShowDeleteDialog = { setShowDeleteDialog(true) }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteDialog(false) },
            title = { Text(stringResource(R.string.delete_note_title)) },
            text = { Text(stringResource(R.string.delete_note_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteNote(uiState.id)
                        setShowDeleteDialog(false)
                    }
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { setShowDeleteDialog(false) }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MobileNoteEditorPreview() {
    val sampleState = NoteEditorUiState(
        id = 1,
        title = "Preview Title",
        content = "This is sample content used for previewing the editor screen on mobile.",
        color = "#E0F7FA",
        createdAt = 1680000000000,
        tags = emptyList(),
        isLoading = false
    )
    val cardColor = try {
        Color(sampleState.color.toColorInt())
    } catch (e: IllegalArgumentException) {
        MaterialTheme.colorScheme.surface
    }

    MobileNoteEditorLayout(
        uiState = sampleState,
        cardColor = cardColor,
        onEvent = {},
        onNavigateBack = {},
        onShowDeleteDialog = {}
    )
}

@Preview(showSystemUi = true, device = Devices.TABLET)
@Composable
fun TabletNoteEditorPreview() {
    val sampleState = NoteEditorUiState(
        id = 1,
        title = "Preview Title",
        content = "This is sample content used for previewing the editor screen on tablet.",
        color = "#E0F7FA",
        createdAt = 1680000000000,
        tags = emptyList(),
        isLoading = false
    )
    val cardColor = try {
        Color(sampleState.color.toColorInt())
    } catch (e: IllegalArgumentException) {
        MaterialTheme.colorScheme.surface
    }

    TabletNoteEditorLayout(
        uiState = sampleState,
        cardColor = cardColor,
        onEvent = {},
        onNavigateBack = {},
        onShowDeleteDialog = {}
    )
}



