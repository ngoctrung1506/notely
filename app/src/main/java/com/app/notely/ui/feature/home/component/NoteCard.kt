package com.app.notely.ui.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.app.notely.R
import com.app.notely.core.mock.MockTags
import com.app.notely.core.util.DateUtil
import com.app.notely.domain.model.Note
import com.app.notely.ui.theme.Black

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(note.color.toColorInt())
    val showDeleteDialog = remember { mutableStateOf(false) }
    val swipeOffset = remember { mutableStateOf(0f) }
    val tags =
        remember(note.tags) { note.tags.mapNotNull { name -> MockTags.all.find { it.name == name } } }

    Box(modifier = modifier.fillMaxWidth()) {
        // Delete action background — shown behind the sliding card
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Color.Red.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_button),
                tint = Color.White,
                modifier = Modifier.padding(end = 20.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        var offset = 0f

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes[0]

                            val dragAmount = change.position.x - change.previousPosition.x
                            val verticalAmount = change.position.y - change.previousPosition.y

                            // Only handle if horizontal movement is dominant
                            if (kotlin.math.abs(dragAmount) > kotlin.math.abs(verticalAmount) && kotlin.math.abs(
                                    dragAmount
                                ) > 2
                            ) {
                                offset = (offset + dragAmount).coerceIn(-150f, 0f)
                                swipeOffset.value = offset
                                change.consume()
                            } else if (change.pressed.not()) {
                                break
                            }
                        }

                        if (offset < -100) {
                            showDeleteDialog.value = true
                        }
                        swipeOffset.value = 0f
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (swipeOffset.value == 0f) onClick()
                        }
                    )
                }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { translationX = swipeOffset.value },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (note.content.isNotBlank()) {
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (tags.isNotEmpty()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            tags.forEach { tag ->
                                val tagColor = Color(tag.color.toColorInt())
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = tagColor.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = tag.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tagColor,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    Text(
                        text = DateUtil.formatDate(note.updatedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text(stringResource(R.string.delete_note_title)) },
            text = { Text(stringResource(R.string.delete_note_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(note.id)
                        showDeleteDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun NoteCardPreview() {
    val sample = Note(
        id = 1,
        title = "Sample Note",
        content = "This is a sample note used for previewing the NoteCard UI.",
        color = "#FFEB3B",
        createdAt = 1680000000000,
        updatedAt = 1680000000000,
        tags = listOf("Personal")
    )
    NoteCard(note = sample, onClick = {}, onDelete = {})
}

