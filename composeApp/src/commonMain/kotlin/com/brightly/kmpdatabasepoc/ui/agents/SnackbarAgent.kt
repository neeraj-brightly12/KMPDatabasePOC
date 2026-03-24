package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Snackbar Agent - Easy snackbar management
 */
class SnackbarAgent(
    private val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun showSuccess(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        show("✅ $message", actionLabel, onAction)
    }

    fun showError(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        show("❌ $message", actionLabel, onAction, SnackbarDuration.Long)
    }

    fun showInfo(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        show("ℹ️ $message", actionLabel, onAction)
    }

    fun showWarning(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        show("⚠️ $message", actionLabel, onAction, SnackbarDuration.Long)
    }

    fun show(
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
            if (result == SnackbarResult.ActionPerformed && onAction != null) {
                onAction()
            }
        }
    }

    fun dismiss() {
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}

@Composable
fun rememberSnackbarAgent(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope()
): Pair<SnackbarAgent, SnackbarHostState> {
    val agent = remember(snackbarHostState, scope) {
        SnackbarAgent(snackbarHostState, scope)
    }
    return Pair(agent, snackbarHostState)
}