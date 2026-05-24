package com.app.notely.ui.feature.note_editor.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.app.notely.R
import com.app.notely.core.mock.MockTags
import com.app.notely.domain.model.Tag

@Composable
fun TagSelectorRow(
    selectedTags: List<Tag>,
    onTagToggle: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    var pickerVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Selected tags + "Add tag" button in a scrollable row
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Sell,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            selectedTags.forEach { tag ->
                val tagColor = Color(tag.color.toColorInt())
                InputChip(
                    selected = true,
                    onClick = { onTagToggle(tag) },
                    label = { Text("#${tag.name}") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.tag_remove, tag.name),
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        selectedContainerColor = tagColor.copy(alpha = 0.15f),
                        selectedLabelColor = tagColor,
                        selectedTrailingIconColor = tagColor
                    )
                )
            }

            TextButton(
                onClick = { pickerVisible = !pickerVisible },
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.tags_add_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // Expandable tag picker
        AnimatedVisibility(visible = pickerVisible) {
            Column {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MockTags.all.forEach { tag ->
                        val isSelected = selectedTags.any { it.id == tag.id }
                        val tagColor = Color(tag.color.toColorInt())
                        FilterChip(
                            selected = isSelected,
                            onClick = { onTagToggle(tag) },
                            label = { Text(tag.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = tagColor.copy(alpha = 0.15f),
                                selectedLabelColor = tagColor
                            )
                        )
                    }
                }
            }
        }
    }
}

  