package com.brightly.kmpdatabasepoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightly.kmpdatabasepoc.data.entity.ProductEntity
import com.brightly.kmpdatabasepoc.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllProducts().collect { productList ->
                    _products.value = productList
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProduct(name: String, price: Double, quantity: Int) {
        viewModelScope.launch {
            try {
                repository.addProduct(name, price, quantity)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add product: ${e.message}"
            }
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                repository.update(product)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update product: ${e.message}"
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                repository.delete(product)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete product: ${e.message}"
            }
        }
    }

    fun deleteAllProducts() {
        viewModelScope.launch {
            try {
                repository.deleteAllProducts()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete all products: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}