package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Screen Agent - Templates for common screen layouts
 */
object ScreenAgent {

    @Composable
    fun StandardScreen(
        title: String,
        subtitle: String? = null,
        modifier: Modifier = Modifier,
        scrollable: Boolean = false,
        content: @Composable ColumnScope.() -> Unit
    ) {
        val columnModifier = if (scrollable) {
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        } else {
            modifier
                .fillMaxSize()
                .padding(16.dp)
        }

        Column(modifier = columnModifier) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = if (subtitle != null) 4.dp else 16.dp)
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            content()
        }
    }

    @Composable
    fun FormScreen(
        title: String,
        submitButtonText: String = "Submit",
        onSubmit: () -> Unit,
        isSubmitEnabled: Boolean = true,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    content()
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = isSubmitEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(submitButtonText)
                    }
                }
            }
        }
    }

    @Composable
    fun ListScreen(
        title: String,
        itemCount: Int,
        actionButton: (@Composable () -> Unit)? = null,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "$itemCount items",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                actionButton?.invoke()
            }
            content()
        }
    }

    @Composable
    fun DetailScreen(
        title: String,
        onBackClick: (() -> Unit)? = null,
        actionButtons: (@Composable RowScope.() -> Unit)? = null,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onBackClick != null) {
                        TextButton(onClick = onBackClick) {
                            Text("← Back")
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                if (actionButtons != null) {
                    Row {
                        actionButtons()
                    }
                }
            }
            content()
        }
    }

    @Composable
    fun ErrorScreen(
        title: String = "Something went wrong",
        message: String = "An error occurred. Please try again.",
        buttonText: String = "Retry",
        onRetry: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "⚠️",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onRetry) {
                    Text(buttonText)
                }
            }
        }
    }
}