package com.brightly.kmpdatabasepoc.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brightly.kmpdatabasepoc.ui.components.CustomButton
import com.brightly.kmpdatabasepoc.ui.components.CustomTextField
import com.brightly.kmpdatabasepoc.ui.components.EmptyState
import com.brightly.kmpdatabasepoc.ui.components.ProductCard
import com.brightly.kmpdatabasepoc.viewmodel.ProductViewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Product Management",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Error message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Dismiss")
                    }
                }
            }
        }

        // Add product form
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add New Product",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CustomTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = "Product Name",
                    placeholder = "Enter product name"
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = productPrice,
                    onValueChange = { productPrice = it },
                    label = "Price",
                    placeholder = "0.00",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = productQuantity,
                    onValueChange = { productQuantity = it },
                    label = "Quantity",
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomButton(
                    text = "Add Product",
                    onClick = {
                        val price = productPrice.toDoubleOrNull()
                        val quantity = productQuantity.toIntOrNull()

                        if (productName.isNotBlank() && price != null && quantity != null) {
                            viewModel.addProduct(productName, price, quantity)
                            productName = ""
                            productPrice = ""
                            productQuantity = ""
                        }
                    },
                    enabled = productName.isNotBlank() &&
                             productPrice.toDoubleOrNull() != null &&
                             productQuantity.toIntOrNull() != null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Products header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Products (${products.size})",
                style = MaterialTheme.typography.titleMedium
            )

            if (products.isNotEmpty()) {
                TextButton(onClick = { viewModel.deleteAllProducts() }) {
                    Text("🗑️ Clear All")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Products list
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (products.isEmpty()) {
            EmptyState(
                message = "No products yet",
                description = "Add your first product using the form above"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onDelete = { viewModel.deleteProduct(product) }
                    )
                }
            }
        }
    }
}