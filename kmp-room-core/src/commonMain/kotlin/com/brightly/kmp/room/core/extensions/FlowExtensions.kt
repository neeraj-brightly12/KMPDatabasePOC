package com.brightly.kmp.room.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Extension functions for Flow to simplify common database operations.
 */

/**
 * Filters out empty lists from the Flow.
 * Useful when you only want to react to non-empty query results.
 */
fun <T> Flow<List<T>>.filterNotEmpty(): Flow<List<T>> =
    filter { it.isNotEmpty() }

/**
 * Maps a Flow of lists to a Flow of their sizes.
 * Useful for counting query results without loading full objects.
 */
fun <T> Flow<List<T>>.mapToSize(): Flow<Int> =
    map { it.size }

/**
 * Maps a Flow of lists to a Flow of the first element, or null if empty.
 */
fun <T> Flow<List<T>>.mapToFirstOrNull(): Flow<T?> =
    map { it.firstOrNull() }