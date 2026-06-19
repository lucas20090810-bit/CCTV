package com.example.data

import kotlinx.coroutines.flow.Flow

class WatchlistRepository(private val dao: WatchlistDao) {
    val watchlistFlow: Flow<List<WatchlistItem>> = dao.getWatchlistFlow()

    suspend fun addToWatchlist(item: WatchlistItem) {
        dao.insertItem(item)
    }

    suspend fun removeFromWatchlist(mediaId: String) {
        dao.deleteItem(mediaId)
    }

    suspend fun isBookmarked(mediaId: String): Boolean {
        return dao.getItemById(mediaId) != null
    }
}
