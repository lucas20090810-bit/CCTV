package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_items")
data class WatchlistItem(
    @PrimaryKey val mediaId: String,
    val title: String,
    val tagline: String,
    val rating: String,
    val year: String,
    val duration: String,
    val genre: String,
    val isMovie: Boolean,
    val addedTime: Long = System.currentTimeMillis()
)
