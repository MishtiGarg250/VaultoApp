package com.vaulto.ui.save

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vaulto.ui.save.SaveViewModel

@Composable
fun SaveScreen(
    viewModel: SaveViewModel,
    onSaved: () -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var url by remember {
        mutableStateOf("")
    }

    var note by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Save to Vaulto",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Title")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = url,
            onValueChange = {
                url = it
            },
            label = {
                Text("URL")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
            },
            label = {
                Text("Note")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {

                viewModel.save(
                    title = title,
                    url = url,
                    note = note
                )

                onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save to Vaulto")
        }
    }
}