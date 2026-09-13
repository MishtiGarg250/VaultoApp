package com.vaulto.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.ui.search.SearchViewModel
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(id: Long, viewModel: SearchViewModel, onBack: () -> Unit) {
    val item by viewModel.item(id).collectAsStateWithLifecycle(initialValue = null)
    val context = LocalContext.current
    var showDelete by remember { mutableStateOf(false) }
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = { TopAppBar(navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }, title = { Text("Saved item") }) }) { padding ->
        val savedItem = item
        if (savedItem == null) Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { Text("This item is no longer available.") }
        else Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text(savedItem.title, style = MaterialTheme.typography.headlineSmall, maxLines = 3, overflow = TextOverflow.Ellipsis)
            savedItem.url?.let { url -> Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceVariant) { Text(url, Modifier.padding(14.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary) } }
            savedItem.note?.takeIf { it.isNotBlank() }?.let { Column { Text("NOTE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary); Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 6.dp)) } }
            Text("Saved ${DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(savedItem.createdAt))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.weight(1f))
            savedItem.url?.let { url -> Button(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.OpenInNew, null); Spacer(Modifier.width(8.dp)); Text("Open link") } }
            OutlinedButton(onClick = { viewModel.toggleFavorite(savedItem) }, modifier = Modifier.fillMaxWidth()) { Icon(if (savedItem.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, null); Spacer(Modifier.width(8.dp)); Text(if (savedItem.isFavorite) "Remove from favorites" else "Add to favorites") }
            OutlinedButton(onClick = { viewModel.setArchived(savedItem, !savedItem.isArchived) }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Archive, null); Spacer(Modifier.width(8.dp)); Text(if (savedItem.isArchived) "Restore from archive" else "Archive item") }
            TextButton(onClick = { showDelete = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Icon(Icons.Default.DeleteOutline, null); Spacer(Modifier.width(8.dp)); Text("Delete item") }
        }
    }
    if (showDelete && item != null) AlertDialog(onDismissRequest = { showDelete = false }, title = { Text("Delete this item?") }, text = { Text("This action cannot be undone.") }, confirmButton = { TextButton(onClick = { viewModel.delete(item!!); showDelete = false; onBack() }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") } }, dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } })
}
