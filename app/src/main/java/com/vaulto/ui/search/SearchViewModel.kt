package com.vaulto.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.repository.VaultoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SearchFilter { ALL, FAVORITES, ARCHIVED }

class SearchViewModel(private val repository: VaultoRepository) : ViewModel() {
    val query = MutableStateFlow("")
    val filter = MutableStateFlow(SearchFilter.ALL)
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches

    val results = combine(query, filter) { text, selectedFilter ->
        text.trim() to selectedFilter.name
    }.flatMapLatest { (text, selectedFilter) ->
        repository.searchItems(text, selectedFilter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun updateQuery(value: String) { query.value = value }
    fun setFilter(value: SearchFilter) { filter.value = value }

    fun rememberSearch() {
        val value = query.value.trim()
        if (value.isBlank()) return
        _recentSearches.value = (listOf(value) + _recentSearches.value.filterNot { it.equals(value, true) }).take(5)
    }

    fun item(id: Long) = repository.getItem(id)
    fun toggleFavorite(item: SavedItem) = viewModelScope.launch { repository.setFavorite(item, !item.isFavorite) }
    fun setArchived(item: SavedItem, archived: Boolean) = viewModelScope.launch { repository.setArchived(item, archived) }
    fun delete(item: SavedItem) = viewModelScope.launch { repository.deleteItem(item) }
}
