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

    val archivedItems = savedItemDao.getArchivedItems()

    suspend fun saveItem(item: SavedItem) {
        savedItemDao.insert(item)
    }

    suspend fun updateItem(item: SavedItem) {
        savedItemDao.update(item)
    }

    suspend fun deleteItem(item: SavedItem) {
        savedItemDao.delete(item)
    }

    suspend fun setFavorite(item: SavedItem, isFavorite: Boolean) {
        savedItemDao.updateFavorite(item.id, isFavorite)
    }

    suspend fun setArchived(item: SavedItem, isArchived: Boolean) {
        savedItemDao.updateArchived(item.id, isArchived)
    }

    fun searchItems(query: String, filter: String) =
        savedItemDao.search(query, filter)

    fun getItem(id: Long) = savedItemDao.getItem(id)


    // -------------------------
    // Collections
    // -------------------------

    val collections = collectionDao.getCollections()

    suspend fun createCollection(collection: Collection) {
        collectionDao.insert(collection)
    }
}
