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
        UPDATE saved_items
        SET isFavorite = :isFavorite
        WHERE id = :id
    """)
    suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    )

    @Query("""
        UPDATE saved_items
        SET isArchived = :isArchived
        WHERE id = :id
    """)
    suspend fun updateArchived(
        id: Long,
        isArchived: Boolean
    )

    @Query("""
        SELECT * FROM saved_items
        WHERE isArchived = 0
        ORDER BY createdAt DESC
    """)
    fun getActiveItems(): Flow<List<SavedItem>>

    @Query("""
        SELECT * FROM saved_items
        WHERE isFavorite = 1 AND isArchived = 0
        ORDER BY createdAt DESC
    """)
    fun getFavorites(): Flow<List<SavedItem>>

    @Query("""
        SELECT * FROM saved_items
        WHERE isArchived = 1
        ORDER BY createdAt DESC
    """)
    fun getArchivedItems(): Flow<List<SavedItem>>

    @Query("""
        SELECT saved_items.* FROM saved_items
        LEFT JOIN collections ON collections.id = saved_items.collectionId
        WHERE (
            :query = '' OR
            saved_items.title LIKE '%' || :query || '%' COLLATE NOCASE OR
            saved_items.url LIKE '%' || :query || '%' COLLATE NOCASE OR
            saved_items.note LIKE '%' || :query || '%' COLLATE NOCASE OR
            collections.name LIKE '%' || :query || '%' COLLATE NOCASE
        )
        AND (
            :filter = 'ALL' OR
            (:filter = 'FAVORITES' AND saved_items.isFavorite = 1) OR
            (:filter = 'ARCHIVED' AND saved_items.isArchived = 1)
        )
        ORDER BY saved_items.createdAt DESC
    """)
    fun search(query: String, filter: String): Flow<List<SavedItem>>

    @Query("SELECT * FROM saved_items WHERE id = :id LIMIT 1")
    fun getItem(id: Long): Flow<SavedItem?>
}
