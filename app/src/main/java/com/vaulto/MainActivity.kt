package com.vaulto

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.campus.vaulto.ui.theme.VaultoTheme
import com.campus.vaulto.ui.theme.ThemeMode
import com.vaulto.data.local.VaultoDatabase
import com.vaulto.data.repository.VaultoRepository
import com.vaulto.ui.VaultoViewModelFactory
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.favorites.FavoritesViewModel
import com.vaulto.ui.navigation.VaultoNavigation
import com.vaulto.ui.navigation.SharedContent
import com.vaulto.ui.save.SaveViewModel
import com.vaulto.ui.search.SearchViewModel

class MainActivity : ComponentActivity() {

    private var sharedContent by mutableStateOf<SharedContent?>(null)
    private var themeMode by mutableStateOf(ThemeMode.DARK)

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        sharedContent = SharedContent.fromIntent(intent)
        val preferences = getSharedPreferences("vaulto_preferences", MODE_PRIVATE)
        themeMode = runCatching {
            ThemeMode.valueOf(preferences.getString("theme_mode", ThemeMode.DARK.name).orEmpty())
        }.getOrDefault(ThemeMode.DARK)

        val database =
            VaultoDatabase.getDatabase(applicationContext)

        val repository = VaultoRepository(
            savedItemDao = database.savedItemDao(),
            collectionDao = database.collectionDao()
        )

        val factory =
            VaultoViewModelFactory(repository)

        setContent {

            VaultoTheme(themeMode = themeMode) {

                val homeViewModel: HomeViewModel =
                    viewModel(factory = factory)

                val saveViewModel: SaveViewModel =
                    viewModel(factory = factory)

                val favoritesViewModel: FavoritesViewModel =
                    viewModel(factory = factory)

                val searchViewModel: SearchViewModel =
                    viewModel(factory = factory)

                VaultoNavigation(
                    homeViewModel = homeViewModel,
                    saveViewModel = saveViewModel,
                    favoritesViewModel = favoritesViewModel,
                    searchViewModel = searchViewModel,
                    sharedContent = sharedContent,
                    onSharedContentHandled = { sharedContent = null },
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        themeMode = mode
                        preferences.edit().putString("theme_mode", mode.name).apply()
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        sharedContent = SharedContent.fromIntent(intent)
    }
}
