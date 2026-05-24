package com.app.notely.ui.feature.note_editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.notely.R
import com.app.notely.core.util.DateUtil
import com.app.notely.ui.theme.Black

@Composable
fun MetadataRow(createdAt: Long, content: String) {
    val wordCount = content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    val readMinutes = (wordCount / 200).coerceAtLeast(1)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(R.string.created_label)} ${DateUtil.formatDate(createdAt).uppercase()}",
            style = MaterialTheme.typography.labelSmall,
            color = Black
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$readMinutes ${stringResource(R.string.min_read_label)}",
            style = MaterialTheme.typography.labelSmall,
            color = Black
        )
    }
}

