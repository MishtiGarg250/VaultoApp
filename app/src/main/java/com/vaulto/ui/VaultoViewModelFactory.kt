package com.vaulto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaulto.data.repository.VaultoRepository
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.save.SaveViewModel

class VaultoViewModelFactory(
    private val repository: VaultoRepository
) : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(
        modelClass: Class<T>
    ):T{
        if(modelClass.isAssignableFrom(HomeViewModel:: class.java)){
            return HomeViewModel(repository) as T
        }

        if(modelClass.isAssignableFrom(SaveViewModel::class.java)){
            return SaveViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}