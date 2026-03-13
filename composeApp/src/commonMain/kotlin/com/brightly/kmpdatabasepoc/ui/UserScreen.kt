package com.brightly.kmpdatabasepoc.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightly.kmpdatabasepoc.viewmodel.UserViewModel

@Composable
fun UserScreen(viewModel: UserViewModel) {

    val users by viewModel.users.collectAsState()

    var text by remember { mutableStateOf("") }

    Column(Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.addUser(text)
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add User")
        }

        Spacer(Modifier.height(20.dp))

        Text("Users (${users.size}):", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(10.dp))

        users.forEach { userName ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = userName,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}