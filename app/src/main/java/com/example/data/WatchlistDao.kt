package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_items ORDER BY addedTime DESC")
    fun getWatchlistFlow(): Flow<List<WatchlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WatchlistItem)

    @Query("DELETE FROM watchlist_items WHERE mediaId = :mediaId")
    suspend fun deleteItem(mediaId: String)

    @Query("SELECT * FROM watchlist_items WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getItemById(mediaId: String): WatchlistItem?
}
