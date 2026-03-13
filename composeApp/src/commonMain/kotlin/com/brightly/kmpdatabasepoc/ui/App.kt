package com.brightly.kmpdatabasepoc.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.data.repository.UserRepository
import com.brightly.kmpdatabasepoc.viewmodel.UserViewModel

@Composable
fun App(databaseFactory: DatabaseFactory) {
    val database = remember { databaseFactory.createDatabase() }
    val repository = remember { UserRepository(database) }
    val viewModel: UserViewModel = viewModel { UserViewModel(repository) }
    UserScreen(viewModel)
}