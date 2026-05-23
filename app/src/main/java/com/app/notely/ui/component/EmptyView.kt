package com.app.notely.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    message: String = "No notes yet"
) {
    Box(modifier = modifier.fillMaxSize()) {
        // TODO: Implement empty view UI
        Text(text = message)
    }
}