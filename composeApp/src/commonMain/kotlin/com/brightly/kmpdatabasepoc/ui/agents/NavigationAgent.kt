package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/**
 * Navigation Agent - Simple navigation management
 */
class NavigationAgent<T>(private val initialScreen: T) {
    private val _currentScreen = mutableStateOf(initialScreen)
    val currentScreen: State<T> = _currentScreen

    private val backStack = mutableListOf<T>()

    fun navigateTo(screen: T) {
        backStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        return if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeLast()
            true
        } else {
            false
        }
    }

    fun canGoBack(): Boolean = backStack.isNotEmpty()

    fun clearBackStack() {
        backStack.clear()
    }

    fun replace(screen: T) {
        _currentScreen.value = screen
    }
}

@Composable
fun <T> rememberNavigationAgent(initialScreen: T): NavigationAgent<T> {
    return remember { NavigationAgent(initialScreen) }
}

/**
 * Tab Navigation Agent - For bottom navigation and tab bars
 */
class TabNavigationAgent<T>(
    private val tabs: List<TabItem<T>>
) {
    private val _selectedTab = mutableStateOf(tabs.first().screen)
    val selectedTab: State<T> = _selectedTab

    data class TabItem<T>(
        val screen: T,
        val label: String,
        val icon: String
    )

    fun selectTab(screen: T) {
        _selectedTab.value = screen
    }

    @Composable
    fun BottomNavigationBar() {
        NavigationBar {
            tabs.forEach { tab ->
                NavigationBarItem(
                    icon = {
                        Text(
                            text = tab.icon,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    label = { Text(tab.label) },
                    selected = _selectedTab.value == tab.screen,
                    onClick = { selectTab(tab.screen) }
                )
            }
        }
    }
}

@Composable
fun <T> rememberTabNavigationAgent(
    tabs: List<TabNavigationAgent.TabItem<T>>
): TabNavigationAgent<T> {
    return remember { TabNavigationAgent(tabs) }
}