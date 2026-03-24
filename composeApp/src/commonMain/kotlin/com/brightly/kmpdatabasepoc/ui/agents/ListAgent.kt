package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * List Agent - Smart list builder with built-in empty states, loading, and item rendering
 */
object ListAgent {

    @Composable
    fun <T> SmartList(
        items: List<T>,
        isLoading: Boolean = false,
        emptyMessage: String = "No items found",
        emptyDescription: String? = "Add your first item to get started",
        loadingMessage: String = "Loading...",
        itemKey: ((T) -> Any)? = null,
        modifier: Modifier = Modifier,
        itemContent: @Composable (T) -> Unit
    ) {
        when {
            isLoading -> {
                LoadingState(loadingMessage, modifier)
            }
            items.isEmpty() -> {
                EmptyState(emptyMessage, emptyDescription, modifier)
            }
            else -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = items,
                        key = itemKey
                    ) { item ->
                        itemContent(item)
                    }
                }
            }
        }
    }

    @Composable
    fun <T> GridList(
        items: List<T>,
        columns: Int = 2,
        isLoading: Boolean = false,
        emptyMessage: String = "No items found",
        modifier: Modifier = Modifier,
        itemContent: @Composable (T) -> Unit
    ) {
        when {
            isLoading -> LoadingState("Loading...", modifier)
            items.isEmpty() -> EmptyState(emptyMessage, null, modifier)
            else -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items.chunked(columns)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    itemContent(item)
                                }
                            }
                            repeat(columns - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingState(message: String, modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(message, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    @Composable
    private fun EmptyState(message: String, description: String?, modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "📭",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                if (description != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}