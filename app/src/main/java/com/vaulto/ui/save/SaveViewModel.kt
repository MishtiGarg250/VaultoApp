package com.vaulto.ui.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.repository.VaultoRepository
import kotlinx.coroutines.launch

class SaveViewModel(
    private val repository: VaultoRepository
) : ViewModel() {

    fun save(
        title: String,
        url: String,
        note: String
    ) {

        if (title.isBlank()) return

        viewModelScope.launch {

            val item = SavedItem(
                title = title,
                url = url.ifBlank { null },
                note = note.ifBlank { null }
            )

            repository.saveItem(item)
        }
    }
}