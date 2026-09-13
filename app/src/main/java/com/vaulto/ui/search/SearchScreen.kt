package com.vaulto.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.ui.home.SavedItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, onBack: () -> Unit, onItemClick: (Long) -> Unit) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        TopAppBar(navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }, title = { Text("Search") })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::updateQuery,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Search your vault") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { if (query.isNotBlank()) IconButton(onClick = { viewModel.updateQuery("") }) { Icon(Icons.Default.Close, "Clear search") } },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SearchFilter.entries.forEach { choice -> FilterChip(selected = filter == choice, onClick = { viewModel.setFilter(choice) }, label = { Text(choice.label) }) }
            }
            Spacer(Modifier.height(12.dp))
            when {
                query.isBlank() -> SearchStart(recentSearches, onRecentClick = viewModel::updateQuery)
                results.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text("No saved items match your search.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                else -> LazyColumn(contentPadding = PaddingValues(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { Text("${results.size} ${if (results.size == 1) "result" else "results"}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) }
                    items(results, key = { it.id }) { item ->
                        SavedItemCard(item, { viewModel.toggleFavorite(item) }, { viewModel.setArchived(item, true) }, { viewModel.delete(item) }, archived = item.isArchived, onRestore = { viewModel.setArchived(item, false) }, onClick = { viewModel.rememberSearch(); focusManager.clearFocus(); onItemClick(item.id) })
                    }
                }
            }
        }
    }
}

private val SearchFilter.label: String get() = when (this) { SearchFilter.ALL -> "All"; SearchFilter.FAVORITES -> "Favorites"; SearchFilter.ARCHIVED -> "Archived" }

@Composable private fun SearchStart(recentSearches: List<String>, onRecentClick: (String) -> Unit) {
    Column(Modifier.padding(top = 16.dp)) {
        Text(if (recentSearches.isEmpty()) "Search everything you saved" else "Recent searches", style = MaterialTheme.typography.titleMedium)
        if (recentSearches.isEmpty()) Text("Titles, links, notes, and collection names are searched locally on your device.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
        else recentSearches.forEach { query -> TextButton(onClick = { onRecentClick(query) }) { Icon(Icons.Default.Search, null, Modifier.size(18.dp)); Spacer(Modifier.width(10.dp)); Text(query) } }
    }
}
