package com.vaulto.data.local

import androidx.room.*
import com.vaulto.data.entity.SavedItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {

    @Insert
    suspend fun insert(item: SavedItem)

    @Update
    suspend fun update(item: SavedItem)

    @Delete
    suspend fun delete(item: SavedItem)

    @Query("""
        SELECT * FROM saved_items
        WHERE isArchived = 0
        ORDER BY createdAt DESC
    """)
    fun getActiveItems(): Flow<List<SavedItem>>

    @Query("""
        SELECT * FROM saved_items
        WHERE isFavorite = 1
        AND isArchived = 0
        ORDER BY createdAt DESC
    """)
    fun getFavorites(): Flow<List<SavedItem>>

    @Query("""
        SELECT * FROM saved_items
        WHERE title LIKE '%' || :query || '%'
        OR url LIKE '%' || :query || '%'
        OR note LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): Flow<List<SavedItem>>
}