package com.brightly.kmpdatabasepoc.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.brightly.kmpdatabasepoc.data.entity.ProductEntity
import com.brightly.kmp.room.core.base.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao : BaseDao<ProductEntity> {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE price <= :maxPrice")
    fun getProductsByPrice(maxPrice: Double): Flow<List<ProductEntity>>

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}