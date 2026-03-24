package com.brightly.kmpdatabasepoc.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightly.kmpdatabasepoc.ui.components.CustomButton
import com.brightly.kmpdatabasepoc.ui.components.CustomTextField
import com.brightly.kmpdatabasepoc.ui.components.EmptyState
import com.brightly.kmpdatabasepoc.ui.components.UserCard
import com.brightly.kmpdatabasepoc.viewmodel.UserViewModel

@Composable
fun UserScreen(viewModel: UserViewModel) {
    val users by viewModel.users.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "User Management",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add New User",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CustomTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "User Name",
                    placeholder = "Enter user name"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomButton(
                    text = "Add User",
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.addUser(text)
                            text = ""
                        }
                    },
                    enabled = text.isNotBlank()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Users (${users.size})",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (users.isEmpty()) {
            EmptyState(
                message = "No users yet",
                description = "Add your first user using the form above"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { userName ->
                    UserCard(userName = userName)
                }
            }
        }
    }
}