package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "continue_watching")
data class ContinueWatchingItem(
    @PrimaryKey val mediaId: String,
    val title: String,
    val tagline: String,
    val progress: Float, // 0.0 to 1.0
    val lastWatchedTime: Long = System.currentTimeMillis()
)
