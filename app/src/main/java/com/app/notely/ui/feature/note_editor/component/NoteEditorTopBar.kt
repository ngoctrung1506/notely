package com.app.notely.ui.feature.note_editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.notely.R
import com.app.notely.ui.feature.note_editor.NoteEditorUiEvent
import com.app.notely.ui.feature.note_editor.NoteEditorUiState

@Composable
fun NoteEditorTopBar(
    uiState: NoteEditorUiState,
    onNavigateBack: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    onEvent: (NoteEditorUiEvent) -> Unit,
    isTablet: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isTablet) MaterialTheme.colorScheme.surface else null

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .let { if (backgroundColor != null) it.background(backgroundColor) else it }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_button),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = if (uiState.id > 0) stringResource(R.string.edit_note_title) else stringResource(R.string.add_note_title),
            style = if (isTablet) {
                MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            } else {
                MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            },
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.weight(1f))
        if (uiState.id > 0) {
            IconButton(onClick = onShowDeleteDialog) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_button),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        DoneButton(
            isLoading = uiState.isLoading,
            enabled = uiState.title.isNotBlank() && uiState.content.isNotBlank(),
            onClick = { onEvent(NoteEditorUiEvent.Save) }
        )
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}
