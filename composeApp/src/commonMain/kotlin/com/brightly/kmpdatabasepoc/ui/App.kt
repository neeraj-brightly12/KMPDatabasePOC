package com.brightly.kmpdatabasepoc.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.data.repository.ProductRepository
import com.brightly.kmpdatabasepoc.data.repository.UserRepository
import com.brightly.kmpdatabasepoc.viewmodel.ProductViewModel
import com.brightly.kmpdatabasepoc.viewmodel.UserViewModel

@Composable
fun App(databaseFactory: DatabaseFactory) {
    val database = remember { databaseFactory.createDatabase() }

    val userRepository = remember { UserRepository(database) }
    val userViewModel: UserViewModel = viewModel { UserViewModel(userRepository) }

    val productRepository = remember { ProductRepository(database) }
    val productViewModel: ProductViewModel = viewModel { ProductViewModel(productRepository) }

    MainScreen(
        userViewModel = userViewModel,
        productViewModel = productViewModel
    )
}