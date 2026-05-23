package com.app.notely.ui.feature.list_note.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.notely.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: (Note) -> Unit = {},
    onDelete: (Long) -> Unit = {}
) {
    Box(modifier = modifier.padding(8.dp)) {
        // TODO: Implement note item UI
        Text(text = note.title)
    }
}