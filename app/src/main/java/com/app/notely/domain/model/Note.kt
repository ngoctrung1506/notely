package com.app.notely.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val color: String = "#FFFFFF",
    val tags: List<String> = emptyList(),
    val pendingSync: Boolean = true,
    val isDeleted: Boolean = false
)