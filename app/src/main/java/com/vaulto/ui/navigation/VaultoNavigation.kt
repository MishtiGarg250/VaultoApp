package com.vaulto.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vaulto.ui.collections.CollectionsScreen
import com.vaulto.ui.favorites.FavoritesScreen
import com.vaulto.ui.favorites.FavoritesViewModel

import com.vaulto.ui.home.HomeScreen
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.save.SaveScreen
import com.vaulto.ui.save.SaveViewModel
import com.vaulto.ui.search.SearchScreen
import com.vaulto.ui.search.SearchViewModel
import com.vaulto.ui.detail.ItemDetailsScreen
import com.vaulto.ui.settings.SettingsScreen
import com.campus.vaulto.ui.theme.ThemeMode

@Composable
fun VaultoNavigation(
    homeViewModel: HomeViewModel,
    saveViewModel: SaveViewModel,
    favoritesViewModel: FavoritesViewModel,
    searchViewModel: SearchViewModel,
    sharedContent: SharedContent?,
    onSharedContentHandled: () -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {

    val navController = rememberNavController()

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    LaunchedEffect(sharedContent?.id) {
        if (sharedContent != null) navController.navigate("save") { launchSingleTop = true }
    }

    Scaffold(

        bottomBar = {

            if (currentRoute != "save" && currentRoute != "archive" && currentRoute != "search" && currentRoute != "detail/{itemId}") {

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {

                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = {
                            if (currentRoute != "home") {
                                val returnedToHome = navController.popBackStack(
                                    route = "home",
                                    inclusive = false
                                )
                                if (!returnedToHome) {
                                    navController.navigate("home") {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text("Home")
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == "collections",
                        onClick = {
                            navController.navigate("collections") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Folder,
                                contentDescription = "Collections"
                            )
                        },
                        label = {
                            Text("Collections")
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == "favorites",
                        onClick = {
                            navController.navigate("favorites") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorites"
                            )
                        },
                        label = {
                            Text("Favorites")
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = {
                            navController.navigate("settings") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        },
                        label = {
                            Text("Settings")
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("home") {

                HomeScreen(
                    viewModel = homeViewModel,
                    onAddClick = {
                        navController.navigate("save")
                    },
                    onArchiveClick = { navController.navigate("archive") },
                    onSearchClick = { navController.navigate("search") }
                )
            }

            composable("search") {
                SearchScreen(
                    viewModel = searchViewModel,
                    onBack = { navController.popBackStack() },
                    onItemClick = { id -> navController.navigate("detail/$id") }
                )
            }

            composable("detail/{itemId}") { entry ->
                val id = entry.arguments?.getString("itemId")?.toLongOrNull()
                if (id != null) ItemDetailsScreen(
                    id = id,
                    viewModel = searchViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("collections") {

                CollectionsScreen(viewModel = saveViewModel)
            }

            composable("favorites") {

                FavoritesScreen(viewModel = favoritesViewModel)
            }

            composable("archive") {
                com.vaulto.ui.archive.ArchiveScreen(
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("settings") {

                SettingsScreen(
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange
                )
            }

            composable("save") {

                SaveScreen(
                    viewModel = saveViewModel,
                    onSaved = {
                        onSharedContentHandled()
                        navController.popBackStack()
                    },
                    initialTitle = sharedContent?.title.orEmpty(),
                    initialUrl = sharedContent?.url.orEmpty(),
                    initialNote = sharedContent?.note.orEmpty(),
                    shareId = sharedContent?.id ?: 0L
                )
            }
        }
    }
}
