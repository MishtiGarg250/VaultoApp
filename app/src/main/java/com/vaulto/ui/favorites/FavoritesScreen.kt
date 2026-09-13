package com.vaulto.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.ui.home.SavedItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        TopAppBar(title = { Column { Text("Favorites", style = MaterialTheme.typography.headlineSmall); Text("Your saved essentials", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } })
    }) { padding ->
        if (favorites.isEmpty()) EmptyFavorites(Modifier.padding(padding))
        else LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("${favorites.size} saved ${if (favorites.size == 1) "item" else "items"}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) }
            items(favorites, key = { it.id }) { item -> SavedItemCard(item, { viewModel.toggleFavorite(item) }, { viewModel.archive(item) }, { viewModel.delete(item) }) }
        }
    }
}

@Composable private fun EmptyFavorites(modifier: Modifier) = Box(modifier.fillMaxSize(), Alignment.Center) {
    Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(28.dp).fillMaxWidth()) {
        Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(42.dp))
            Spacer(Modifier.height(14.dp)); Text("Nothing favorited yet", style = MaterialTheme.typography.titleLarge)
            Text("Keep your most useful items one tap away.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
