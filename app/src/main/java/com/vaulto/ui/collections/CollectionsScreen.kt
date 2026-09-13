package com.vaulto.ui.collections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.ui.save.SaveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(viewModel: SaveViewModel) {
    val collections by viewModel.collections.collectAsStateWithLifecycle(initialValue = emptyList())
    var showCreate by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Collections") }) }, floatingActionButton = { ExtendedFloatingActionButton(onClick = { showCreate = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("New collection") }) }) { padding ->
        if (collections.isEmpty()) Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Folder, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp)); Spacer(Modifier.height(12.dp)); Text("Organize your vault", style = MaterialTheme.typography.titleLarge); Text("Create a collection for shared links and notes.", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
        else LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { items(collections, key = { it.id }) { collection -> ElevatedCard(Modifier.fillMaxWidth()) { Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Folder, null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(16.dp)); Text(collection.name, style = MaterialTheme.typography.titleMedium) } } } }
    }
    if (showCreate) CreateCollectionDialog(onDismiss = { showCreate = false }, onCreate = { viewModel.createCollection(it); showCreate = false })
}

@Composable
private fun CreateCollectionDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("New collection") }, text = { OutlinedTextField(name, { name = it }, label = { Text("Collection name") }, singleLine = true) }, confirmButton = { TextButton(onClick = { onCreate(name) }, enabled = name.isNotBlank()) { Text("Create") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
