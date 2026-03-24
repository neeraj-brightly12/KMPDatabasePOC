package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dialog Agent - Smart dialog manager for confirmations, inputs, and alerts
 */
object DialogAgent {

    @Composable
    fun ConfirmDialog(
        show: Boolean,
        title: String,
        message: String,
        confirmText: String = "Confirm",
        dismissText: String = "Cancel",
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        if (show) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(title) },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text(confirmText)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(dismissText)
                    }
                }
            )
        }
    }

    @Composable
    fun InputDialog(
        show: Boolean,
        title: String,
        label: String,
        initialValue: String = "",
        confirmText: String = "Save",
        dismissText: String = "Cancel",
        onConfirm: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        if (show) {
            var text by remember { mutableStateOf(initialValue) }

            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(title) },
                text = {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text(label) },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { onConfirm(text) },
                        enabled = text.isNotBlank()
                    ) {
                        Text(confirmText)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(dismissText)
                    }
                }
            )
        }
    }

    @Composable
    fun AlertDialog(
        show: Boolean,
        title: String,
        message: String,
        buttonText: String = "OK",
        onDismiss: () -> Unit
    ) {
        if (show) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(title) },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text(buttonText)
                    }
                }
            )
        }
    }

    @Composable
    fun DeleteConfirmDialog(
        show: Boolean,
        itemName: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        ConfirmDialog(
            show = show,
            title = "Delete $itemName?",
            message = "Are you sure you want to delete this $itemName? This action cannot be undone.",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

/**
 * Composable function to manage dialog state easily
 */
@Composable
fun rememberDialogState(): MutableState<Boolean> {
    return remember { mutableStateOf(false) }
}