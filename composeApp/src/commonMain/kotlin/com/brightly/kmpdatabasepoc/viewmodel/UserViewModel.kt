package com.brightly.kmpdatabasepoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightly.kmpdatabasepoc.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<String>>(emptyList())
    val users: StateFlow<List<String>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .map { list -> list.map { it.name } }
                .collect { userNames ->
                    _users.value = userNames
                  }
        }
    }

    fun addUser(name: String) {

        viewModelScope.launch {

            repository.addUser(name)
        }
    }
}