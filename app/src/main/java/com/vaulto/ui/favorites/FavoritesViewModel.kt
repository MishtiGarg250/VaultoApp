package com.vaulto.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.repository.VaultoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: VaultoRepository
) : ViewModel() {

    val favorites =
        repository.favorites
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun toggleFavorite(item: SavedItem) {

        viewModelScope.launch {

            repository.setFavorite(item, !item.isFavorite)
        }
    }

    fun archive(item: SavedItem) {

        viewModelScope.launch {
            repository.setArchived(item, true)
        }
    }

    fun delete(item: SavedItem) {

        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}
