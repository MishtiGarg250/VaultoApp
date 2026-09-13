package com.vaulto.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaulto.data.repository.VaultoRepository
import com.vaulto.data.entity.SavedItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: VaultoRepository
) : ViewModel() {

    val savedItems = repository.savedItems
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val archivedItems = repository.archivedItems
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun toggleFavorite(item: SavedItem) {

        viewModelScope.launch {

            repository.updateItem(
                item.copy(
                    isFavorite = !item.isFavorite
                )
            )
        }
    }

    fun archive(item: SavedItem) {

        viewModelScope.launch {

            repository.updateItem(
                item.copy(
                    isArchived = true
                )
            )
        }
    }

    fun delete(item: SavedItem) {

        viewModelScope.launch {

            repository.deleteItem(item)
        }
    }

    fun restore(item: SavedItem) {
        viewModelScope.launch {
            repository.setArchived(item, false)
        }
    }
}
