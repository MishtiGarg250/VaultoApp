package com.vaulto.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.data.entity.SavedItem
import java.text.DateFormat
import java.util.Date

@Composable
fun HomeScreen(viewModel: HomeViewModel, onAddClick: () -> Unit, onArchiveClick: () -> Unit, onSearchClick: () -> Unit) {
    val items by viewModel.savedItems.collectAsStateWithLifecycle()
    Scaffold(containerColor = MaterialTheme.colorScheme.background, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = onAddClick, icon = { Icon(Icons.Default.Add, null) }, text = { Text("New note") }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(18.dp))
    }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                VaultHeader(
                    count = items.size,
                    onSearchClick = onSearchClick,
                    onArchiveClick = onArchiveClick
                )
            }
            if (items.isEmpty()) item { EmptyVault() }
            else {
                item { Text("Latest saves", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp)) }
                items(items, key = { it.id }) { item -> SavedItemCard(item, { viewModel.toggleFavorite(item) }, { viewModel.archive(item) }, { viewModel.delete(item) }) }
            }
        }
    }
}

@Composable private fun VaultHeader(count: Int, onSearchClick: () -> Unit, onArchiveClick: () -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Vaulto", style = MaterialTheme.typography.headlineLarge)
                Text("Your private space for useful things", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onArchiveClick, modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surface)) { Icon(Icons.Default.Archive, "Archived notes") }
        }
        Spacer(Modifier.height(22.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().clickable(onClick = onSearchClick),
            readOnly = true,
            singleLine = true,
            placeholder = { Text("Search your vault") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = { Icon(Icons.Default.Tune, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
            )
        )
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(onClick = onSearchClick, label = { Text("Search all $count items") }, leadingIcon = { Icon(Icons.Default.Search, null, Modifier.size(16.dp)) }, border = null)
            AssistChip(onClick = onArchiveClick, label = { Text("Archived") }, leadingIcon = { Icon(Icons.Default.Archive, null, Modifier.size(16.dp)) }, border = null)
        }
    }
}

@Composable private fun EmptyVault() {
    Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(26.dp), horizontalAlignment = Alignment.Start) {
            Icon(Icons.Default.NorthEast, null, modifier = Modifier.size(30.dp))
            Spacer(Modifier.height(24.dp)); Text("Start building your vault", style = MaterialTheme.typography.titleLarge)
            Text("Share a link from any app, or create your first note.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

@Composable
fun SavedItemCard(item: SavedItem, onFavorite: () -> Unit, onArchive: () -> Unit, onDelete: () -> Unit, archived: Boolean = false, onRestore: (() -> Unit)? = null, collectionName: String? = null, onClick: (() -> Unit)? = null) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    item.url?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 6.dp)) }
                }
                IconButton(onClick = onFavorite, modifier = Modifier.size(38.dp)) { Icon(if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, if (item.isFavorite) "Remove from favorites" else "Add to favorites", tint = if (item.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            item.note?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 12.dp)) }
            Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                collectionName?.let { AssistChip(onClick = {}, label = { Text(it) }, border = null, modifier = Modifier.height(28.dp)) }
                Text(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(item.createdAt)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                if (archived) TextButton(onClick = { onRestore?.invoke() }) { Text("Restore") }
                else TextButton(onClick = onArchive) { Icon(Icons.Default.Archive, null, Modifier.size(17.dp)); Spacer(Modifier.width(5.dp)); Text("Archive") }
                TextButton(onClick = { showDeleteDialog = true }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Icon(Icons.Default.DeleteOutline, null, Modifier.size(17.dp)); Spacer(Modifier.width(5.dp)); Text("Delete") }
            }
        }
    }
    if (showDeleteDialog) AlertDialog(onDismissRequest = { showDeleteDialog = false }, title = { Text("Delete this note?") }, text = { Text("${item.title} will be permanently deleted. This cannot be undone.") }, confirmButton = { TextButton(onClick = { showDeleteDialog = false; onDelete() }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") } }, dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } })
}
