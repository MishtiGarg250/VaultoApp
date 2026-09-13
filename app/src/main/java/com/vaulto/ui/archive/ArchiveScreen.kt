package com.vaulto.ui.archive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.ui.home.HomeViewModel
import com.vaulto.ui.home.SavedItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(viewModel: HomeViewModel, onBack: () -> Unit) {
    val archived by viewModel.archivedItems.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("Archived") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        if (archived.isEmpty()) Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Archive, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp)); Spacer(Modifier.height(12.dp)); Text("No archived notes", style = MaterialTheme.typography.titleMedium); Text("Archived notes stay here until you restore them.", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
        else LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(archived, key = { it.id }) { item -> SavedItemCard(item, { viewModel.toggleFavorite(item) }, {}, { viewModel.delete(item) }, archived = true, onRestore = { viewModel.restore(item) }) } }
    }
}
