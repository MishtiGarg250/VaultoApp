package com.vaulto.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vaulto.ui.collections.CollectionsScreen
import com.vaulto.ui.favorites.FavoritesScreen

import com.vaulto.ui.home.HomeScreen
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.save.SaveScreen
import com.vaulto.ui.save.SaveViewModel
import com.vaulto.ui.settings.SettingsScreen

@Composable
fun VaultoNavigation(
    homeViewModel: HomeViewModel,
    saveViewModel: SaveViewModel
) {

    val navController = rememberNavController()

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    Scaffold(

        bottomBar = {

            if (currentRoute != "save") {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
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
                        }
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
                        }
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
                        }
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
                        }
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
                    }
                )
            }

            composable("collections") {

                CollectionsScreen()
            }

            composable("favorites") {

                FavoritesScreen()
            }

            composable("settings") {

                SettingsScreen()
            }

            composable("save") {

                SaveScreen(
                    viewModel = saveViewModel,
                    onSaved = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}