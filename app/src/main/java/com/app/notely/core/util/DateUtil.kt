package com.app.notely.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    fun getCurrentTime(): Long = System.currentTimeMillis()

    fun formatDate(timestamp: Long): String = dateFormat.format(Date(timestamp))

    fun getRelativeTime(timestamp: Long): String {
        val currentTime = getCurrentTime()
        val diffInMillis = currentTime - timestamp

        return when {
            diffInMillis < 60 * 1000 -> "Just now"
            diffInMillis < 60 * 60 * 1000 -> "${diffInMillis / (60 * 1000)} minutes ago"
            diffInMillis < 24 * 60 * 60 * 1000 -> "${diffInMillis / (60 * 60 * 1000)} hours ago"
            else -> "${diffInMillis / (24 * 60 * 60 * 1000)} days ago"
        }
    }
}