package com.app.notely.ui.feature.note_editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.notely.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.notely.ui.feature.note_editor.component.DoneButton
import com.app.notely.ui.feature.note_editor.component.MetadataRow
import com.app.notely.ui.feature.note_editor.component.NoteEditorField
import com.app.notely.ui.theme.Black

@Composable
fun NoteEditorScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    onNavigateBack: () -> Unit,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
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
            onNavigateBack = onNavigateBack
        )
    } else {
        MobileNoteEditorLayout(
            uiState = uiState,
            cardColor = cardColor,
            onEvent = viewModel::onEvent,
            onNavigateBack = onNavigateBack
        )
    }
}


// ─── Mobile Layout ───────────────────────────────────────────────────────────

@Composable
private fun MobileNoteEditorLayout(
    uiState: NoteEditorUiState,
    cardColor: Color,
    onEvent: (NoteEditorUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
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
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.weight(1f))
            DoneButton(
                isLoading = uiState.isLoading,
                enabled = uiState.title.isNotBlank() && uiState.content.isNotBlank(),
                onClick = { onEvent(NoteEditorUiEvent.Save) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Scrollable editor area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(cardColor)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .imePadding()
        ) {
            // Metadata row
            if (uiState.createdAt > 0L) {
                MetadataRow(createdAt = uiState.createdAt, content = uiState.content)
                Spacer(Modifier.height(24.dp))
            }

            // Title
            NoteEditorField(
                value = uiState.title,
                onValueChange = { onEvent(NoteEditorUiEvent.TitleChanged(it)) },
                placeholder = stringResource(R.string.note_title_placeholder),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    letterSpacing = (-0.5).sp,
                    color = Black
                )
            )

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 0.5.dp
            )
            Spacer(Modifier.height(16.dp))

            // Body
            NoteEditorField(
                value = uiState.content,
                onValueChange = { onEvent(NoteEditorUiEvent.ContentChanged(it)) },
                placeholder = stringResource(R.string.note_content_mobile_placeholder),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Black
                ),
                minLines = 12
            )
        }
    }
}

// ─── Tablet Layout ────────────────────────────────────────────────────────────

@Composable
private fun TabletNoteEditorLayout(
    uiState: NoteEditorUiState,
    cardColor: Color,
    onEvent: (NoteEditorUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (uiState.id > 0) stringResource(R.string.edit_note_title) else stringResource(R.string.add_note_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.weight(1f))
            DoneButton(
                isLoading = uiState.isLoading,
                enabled = uiState.title.isNotBlank() && uiState.content.isNotBlank(),
                onClick = { onEvent(NoteEditorUiEvent.Save) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

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
                Spacer(Modifier.height(16.dp))

                NoteEditorField(
                    value = uiState.content,
                    onValueChange = { onEvent(NoteEditorUiEvent.ContentChanged(it)) },
                    placeholder = stringResource(R.string.note_content_placeholder),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        color = Black
                    ),
                    minLines = 18
                )
            }
        }
    }
}

