package com.vaulto.ui.save

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveScreen(viewModel: SaveViewModel, onSaved: () -> Unit, initialTitle: String = "", initialUrl: String = "", initialNote: String = "", shareId: Long = 0L) {
    var title by rememberSaveable(shareId) { mutableStateOf(initialTitle) }
    var titleEdited by rememberSaveable(shareId) { mutableStateOf(false) }
    var url by rememberSaveable(shareId) { mutableStateOf(initialUrl) }
    var note by rememberSaveable(shareId) { mutableStateOf(initialNote) }
    var selectedCollectionId by rememberSaveable(shareId) { mutableStateOf<Long?>(null) }
    var collectionMenuOpen by remember { mutableStateOf(false) }
    var newCollectionDialog by remember { mutableStateOf(false) }
    val collections by viewModel.collections.collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedName = collections.firstOrNull { it.id == selectedCollectionId }?.name ?: "No collection"

    LaunchedEffect(initialUrl, shareId) {
        viewModel.loadSharedTitle(initialUrl) { fetchedTitle ->
            if (!titleEdited) title = fetchedTitle
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text(if (initialUrl.isNotBlank()) "Save shared item" else "New note") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (initialUrl.isNotBlank()) Text("Shared link detected — review and save it to your vault.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(title, { title = it; titleEdited = true }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(url, { url = it }, label = { Text("URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            ExposedDropdownMenuBox(expanded = collectionMenuOpen, onExpandedChange = { collectionMenuOpen = it }) {
                OutlinedTextField(selectedName, {}, readOnly = true, label = { Text("Collection") }, leadingIcon = { Icon(Icons.Default.Folder, null) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(collectionMenuOpen) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = collectionMenuOpen, onDismissRequest = { collectionMenuOpen = false }) {
                    DropdownMenuItem(text = { Text("No collection") }, onClick = { selectedCollectionId = null; collectionMenuOpen = false })
                    collections.forEach { collection -> DropdownMenuItem(text = { Text(collection.name) }, onClick = { selectedCollectionId = collection.id; collectionMenuOpen = false }) }
                    HorizontalDivider()
                    DropdownMenuItem(text = { Row { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("New collection") } }, onClick = { collectionMenuOpen = false; newCollectionDialog = true })
                }
            }
            OutlinedTextField(note, { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
            Spacer(Modifier.weight(1f))
            Button(onClick = { viewModel.save(title, url, note, selectedCollectionId); onSaved() }, enabled = title.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Save to Vaulto") }
            Spacer(Modifier.height(12.dp))
        }
    }
    if (newCollectionDialog) NewCollectionDialog(onDismiss = { newCollectionDialog = false }, onCreate = { viewModel.createCollection(it); newCollectionDialog = false })
}

@Composable private fun NewCollectionDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("New collection") }, text = { OutlinedTextField(name, { name = it }, label = { Text("Collection name") }, singleLine = true) }, confirmButton = { TextButton(onClick = { onCreate(name) }, enabled = name.isNotBlank()) { Text("Create") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
