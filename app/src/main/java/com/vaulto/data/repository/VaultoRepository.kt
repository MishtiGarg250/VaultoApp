package com.vaulto.data.repository

import com.vaulto.data.entity.Collection
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.local.CollectionDao
import com.vaulto.data.local.SavedItemDao

class VaultoRepository(
    private val savedItemDao: SavedItemDao,
    private val collectionDao: CollectionDao
) {

    // -------------------------
    // Saved Items
    // -------------------------

    val savedItems = savedItemDao.getActiveItems()

    val favorites = savedItemDao.getFavorites()

    suspend fun saveItem(item: SavedItem) {
        savedItemDao.insert(item)
    }

    suspend fun updateItem(item: SavedItem) {
        savedItemDao.update(item)
    }

    suspend fun deleteItem(item: SavedItem) {
        savedItemDao.delete(item)
    }

    fun searchItems(query: String) =
        savedItemDao.search(query)


    // -------------------------
    // Collections
    // -------------------------

    val collections = collectionDao.getCollections()

    suspend fun createCollection(collection: Collection) {
        collectionDao.insert(collection)
    }
}