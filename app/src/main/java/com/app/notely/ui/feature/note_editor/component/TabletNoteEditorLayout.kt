package com.app.notely.ui.feature.note_editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.notely.R
import com.app.notely.ui.feature.note_editor.NoteEditorUiEvent
import com.app.notely.ui.feature.note_editor.NoteEditorUiState
import com.app.notely.ui.theme.Black


@Composable
fun TabletNoteEditorLayout(
    uiState: NoteEditorUiState,
    cardColor: Color,
    onEvent: (NoteEditorUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onShowDeleteDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top bar
        NoteEditorTopBar(
            uiState = uiState,
            onNavigateBack = onNavigateBack,
            onShowDeleteDialog = onShowDeleteDialog,
            onEvent = onEvent,
            isTablet = true,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )

        // Content area — constrained max width like design (800dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .width(800.dp)
                    .align(Alignment.TopCenter)
                    .background(cardColor, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 32.dp, vertical = 32.dp)
            ) {
                if (uiState.createdAt > 0L) {
                    MetadataRow(createdAt = uiState.createdAt, content = uiState.content)
                    Spacer(Modifier.height(24.dp))
                }

                NoteEditorField(
                    value = uiState.title,
                    onValueChange = { onEvent(NoteEditorUiEvent.TitleChanged(it)) },
                    placeholder = stringResource(R.string.note_title_placeholder),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        letterSpacing = (-0.72).sp,
                        color = Black
                    )
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = 0.5.dp
                )
                Spacer(Modifier.height(12.dp))

                TagSelectorRow(
                    selectedTags = uiState.tags,
                    onTagToggle = { onEvent(NoteEditorUiEvent.TagToggled(it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = 0.5.dp
                )
                Spacer(Modifier.height(16.dp))

                NoteEditorField(
                    value = uiState.content,
                    onValueChange = { onEvent(NoteEditorUiEvent.ContentChanged(it)) },
                    placeholder = stringResource(R.string.note_content_placeholder),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp, lineHeight = 28.sp, color = Black
                    ),
                    minLines = 18
                )
            }
        }
    }
}
