package com.brightly.kmpdatabasepoc.data.repository

import com.brightly.kmpdatabasepoc.data.dao.ProductDao
import com.brightly.kmpdatabasepoc.data.database.AppDatabase
import com.brightly.kmpdatabasepoc.data.entity.ProductEntity
import com.brightly.kmp.room.core.base.BaseRepository
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    database: AppDatabase
) : BaseRepository<ProductEntity, ProductDao>(database.productDao()) {

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return dao.getAllProducts()
    }

    suspend fun getProductById(id: Int): ProductEntity? {
        return dao.getProductById(id)
    }

    fun getProductsByPrice(maxPrice: Double): Flow<List<ProductEntity>> {
        return dao.getProductsByPrice(maxPrice)
    }

    suspend fun deleteAllProducts() {
        dao.deleteAllProducts()
    }

    // Example of using generalized methods from BaseRepository
    suspend fun addProduct(name: String, price: Double, quantity: Int) {
        val product = ProductEntity(name = name, price = price, quantity = quantity)
        add(product) // Using generalized add() from BaseRepository
    }

    suspend fun updateProductQuantity(product: ProductEntity, newQuantity: Int) {
        val updatedProduct = product.copy(quantity = newQuantity)
        update(updatedProduct) // Using generalized update() from BaseRepository
    }
}