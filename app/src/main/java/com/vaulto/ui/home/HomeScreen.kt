package com.vaulto.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaulto.data.entity.SavedItem
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddClick: () -> Unit
) {

    val items by viewModel.savedItems
        .collectAsStateWithLifecycle()

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Vaulto")
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = onAddClick
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }

    ) { padding ->

        if (items.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text(
                    text = "Your vault is empty.\nSave something!",
                    modifier = Modifier.padding(24.dp)
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(items) { item ->

                    SavedItemCard(
                        item = item,
                        onFavorite = {
                            viewModel.toggleFavorite(item)
                        },
                        onArchive = {
                            viewModel.archive(item)
                        },
                        onDelete = {
                            viewModel.delete(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedItemCard(
    item: SavedItem,
    onFavorite: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            item.url?.let {

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            item.note?.let {

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(it)
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                IconButton(
                    onClick = onFavorite
                ) {

                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite"
                    )
                }

                TextButton(
                    onClick = onArchive
                ) {
                    Text("Archive")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Delete")
                }
            }
        }
    }
}