package com.brightly.kmpdatabasepoc.ui.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brightly.kmpdatabasepoc.ui.agents.*

/**
 * Examples of how to use UI Agents
 */

// Example 1: Using FormAgent
@Composable
fun FormAgentExample() {
    val formAgent = rememberFormAgent()

    // Configure form fields
    LaunchedEffect(Unit) {
        formAgent.addTextField(
            key = "name",
            label = "Product Name",
            validator = FormValidators.combine(
                FormValidators.required(),
                FormValidators.minLength(3)
            )
        )
        formAgent.addTextField(
            key = "price",
            label = "Price",
            keyboardType = KeyboardType.Decimal,
            validator = FormValidators.positiveNumber()
        )
        formAgent.addTextField(
            key = "quantity",
            label = "Quantity",
            keyboardType = KeyboardType.Number,
            validator = FormValidators.integer()
        )
    }

    ScreenAgent.FormScreen(
        title = "Add Product",
        submitButtonText = "Save Product",
        onSubmit = {
            if (formAgent.validate()) {
                val name = formAgent.getValue("name")
                val price = formAgent.getValue("price").toDouble()
                val quantity = formAgent.getValue("quantity").toInt()
                // Handle save
                formAgent.clear()
            }
        }
    ) {
        formAgent.RenderForm()
    }
}

// Example 2: Using ListAgent
@Composable
fun ListAgentExample() {
    val items = remember { listOf("Item 1", "Item 2", "Item 3") }
    val isLoading = remember { mutableStateOf(false) }

    ScreenAgent.ListScreen(
        title = "My Items",
        itemCount = items.size,
        actionButton = {
            Button(onClick = { /* Add item */ }) {
                Text("+ Add")
            }
        }
    ) {
        ListAgent.SmartList(
            items = items,
            isLoading = isLoading.value,
            emptyMessage = "No items yet",
            emptyDescription = "Add your first item to get started"
        ) { item ->
            CardAgent.InfoCard(
                title = item,
                subtitle = "Description",
                icon = "📦",
                actionText = "View",
                onAction = { /* View item */ }
            )
        }
    }
}

// Example 3: Using DialogAgent
@Composable
fun DialogAgentExample() {
    val showConfirm = rememberDialogState()
    val showInput = rememberDialogState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { showConfirm.value = true }) {
            Text("Show Confirm Dialog")
        }

        Button(onClick = { showInput.value = true }) {
            Text("Show Input Dialog")
        }
    }

    DialogAgent.ConfirmDialog(
        show = showConfirm.value,
        title = "Confirm Action",
        message = "Are you sure?",
        onConfirm = {
            // Handle confirm
            showConfirm.value = false
        },
        onDismiss = { showConfirm.value = false }
    )

    DialogAgent.InputDialog(
        show = showInput.value,
        title = "Enter Name",
        label = "Name",
        onConfirm = { name ->
            // Handle input
            showInput.value = false
        },
        onDismiss = { showInput.value = false }
    )
}

// Example 4: Using CardAgent
@Composable
fun CardAgentExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardAgent.InfoCard(
            title = "John Doe",
            subtitle = "john@example.com",
            icon = "👤",
            actionText = "Edit",
            onAction = { /* Handle edit */ }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardAgent.StatCard(
                value = "25",
                label = "Products",
                icon = "📦",
                modifier = Modifier.weight(1f)
            )
            CardAgent.StatCard(
                value = "12",
                label = "Users",
                icon = "👥",
                modifier = Modifier.weight(1f)
            )
        }

        CardAgent.ActionCard(
            title = "Welcome!",
            description = "Get started by adding your first product",
            buttonText = "Add Product",
            onButtonClick = { /* Handle click */ }
        )
    }
}

// Example 5: Using SnackbarAgent
@Composable
fun SnackbarAgentExample() {
    val (snackbarAgent, snackbarHostState) = rememberSnackbarAgent()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { snackbarAgent.showSuccess("Operation successful!") }) {
                Text("Show Success")
            }
            Button(onClick = { snackbarAgent.showError("Something went wrong!") }) {
                Text("Show Error")
            }
            Button(onClick = { snackbarAgent.showInfo("Here's some info") }) {
                Text("Show Info")
            }
            Button(onClick = {
                snackbarAgent.showWarning(
                    "This action is permanent",
                    actionLabel = "Undo",
                    onAction = { /* Handle undo */ }
                )
            }) {
                Text("Show Warning with Action")
            }
        }
    }
}

// Example 6: Using NavigationAgent
enum class AppScreen { HOME, PROFILE, SETTINGS }

@Composable
fun NavigationAgentExample() {
    val navAgent = rememberNavigationAgent(AppScreen.HOME)

    when (navAgent.currentScreen.value) {
        AppScreen.HOME -> {
            ScreenAgent.StandardScreen(title = "Home") {
                Button(onClick = { navAgent.navigateTo(AppScreen.PROFILE) }) {
                    Text("Go to Profile")
                }
            }
        }
        AppScreen.PROFILE -> {
            ScreenAgent.DetailScreen(
                title = "Profile",
                onBackClick = { navAgent.navigateBack() }
            ) {
                Text("Profile content")
            }
        }
        AppScreen.SETTINGS -> {
            ScreenAgent.StandardScreen(title = "Settings") {
                Text("Settings content")
            }
        }
    }
}