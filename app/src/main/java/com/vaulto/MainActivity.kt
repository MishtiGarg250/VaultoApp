package com.vaulto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.campus.vaulto.ui.theme.VaultoTheme
import com.vaulto.data.local.VaultoDatabase
import com.vaulto.data.repository.VaultoRepository
import com.vaulto.ui.VaultoViewModelFactory
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.navigation.VaultoNavigation
import com.vaulto.ui.save.SaveViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        val database =
            VaultoDatabase.getDatabase(applicationContext)

        val repository = VaultoRepository(
            savedItemDao = database.savedItemDao(),
            collectionDao = database.collectionDao()
        )

        val factory =
            VaultoViewModelFactory(repository)

        setContent {

            VaultoTheme {

                val homeViewModel: HomeViewModel =
                    viewModel(factory = factory)

                val saveViewModel: SaveViewModel =
                    viewModel(factory = factory)

                VaultoNavigation(
                    homeViewModel = homeViewModel,
                    saveViewModel = saveViewModel
                )
            }
        }
    }
}