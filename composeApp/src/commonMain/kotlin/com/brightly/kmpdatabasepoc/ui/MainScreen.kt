package com.brightly.kmpdatabasepoc.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import com.brightly.kmpdatabasepoc.viewmodel.ProductViewModel
import com.brightly.kmpdatabasepoc.viewmodel.UserViewModel

enum class Screen {
    USERS,
    PRODUCTS
}

@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel
) {
    var selectedScreen by remember { mutableStateOf(Screen.USERS) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Text(
                            text = "👤",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    label = { Text("Users") },
                    selected = selectedScreen == Screen.USERS,
                    onClick = { selectedScreen = Screen.USERS }
                )
                NavigationBarItem(
                    icon = {
                        Text(
                            text = "🛒",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    label = { Text("Products") },
                    selected = selectedScreen == Screen.PRODUCTS,
                    onClick = { selectedScreen = Screen.PRODUCTS }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.USERS -> UserScreen(userViewModel)
                Screen.PRODUCTS -> ProductScreen(productViewModel)
            }
        }
    }
}