package com.vaulto.data.local

import androidx.room.*
import com.vaulto.data.entity.Collection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert
    suspend fun insert(collection: Collection)

    @Update
    suspend fun update(collection: Collection)

    @Delete
    suspend fun delete(collection: Collection)

    @Query("""
        SELECT * FROM collections
        ORDER BY createdAt DESC
    """)
    fun getCollections(): Flow<List<Collection>>
}