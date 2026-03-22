package com.brightly.kmpdatabasepoc.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.brightly.kmpdatabasepoc.`data`.entity.ProductEntity
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ProductDao_Impl(
  __db: RoomDatabase,
) : ProductDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProductEntity: EntityInsertAdapter<ProductEntity>

  private val __deleteAdapterOfProductEntity: EntityDeleteOrUpdateAdapter<ProductEntity>

  private val __updateAdapterOfProductEntity: EntityDeleteOrUpdateAdapter<ProductEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfProductEntity = object : EntityInsertAdapter<ProductEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `products` (`id`,`name`,`price`,`quantity`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ProductEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindDouble(3, entity.price)
        statement.bindLong(4, entity.quantity.toLong())
      }
    }
    this.__deleteAdapterOfProductEntity = object : EntityDeleteOrUpdateAdapter<ProductEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `products` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ProductEntity) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfProductEntity = object : EntityDeleteOrUpdateAdapter<ProductEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `products` SET `id` = ?,`name` = ?,`price` = ?,`quantity` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ProductEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindDouble(3, entity.price)
        statement.bindLong(4, entity.quantity.toLong())
        statement.bindLong(5, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(entity: ProductEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfProductEntity.insert(_connection, entity)
  }

  public override suspend fun insertAll(entities: List<ProductEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfProductEntity.insert(_connection, entities)
  }

  public override suspend fun delete(entity: ProductEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfProductEntity.handle(_connection, entity)
  }

  public override suspend fun deleteAll(entities: List<ProductEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfProductEntity.handleMultiple(_connection, entities)
  }

  public override suspend fun update(entity: ProductEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfProductEntity.handle(_connection, entity)
  }

  public override suspend fun updateAll(entities: List<ProductEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfProductEntity.handleMultiple(_connection, entities)
  }

  public override fun getAllProducts(): Flow<List<ProductEntity>> {
    val _sql: String = "SELECT * FROM products"
    return createFlow(__db, false, arrayOf("products")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _result: MutableList<ProductEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProductEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpQuantity: Int
          _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          _item = ProductEntity(_tmpId,_tmpName,_tmpPrice,_tmpQuantity)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getProductById(id: Int): ProductEntity? {
    val _sql: String = "SELECT * FROM products WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _result: ProductEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpQuantity: Int
          _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          _result = ProductEntity(_tmpId,_tmpName,_tmpPrice,_tmpQuantity)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getProductsByPrice(maxPrice: Double): Flow<List<ProductEntity>> {
    val _sql: String = "SELECT * FROM products WHERE price <= ?"
    return createFlow(__db, false, arrayOf("products")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, maxPrice)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _result: MutableList<ProductEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProductEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpQuantity: Int
          _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          _item = ProductEntity(_tmpId,_tmpName,_tmpPrice,_tmpQuantity)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllProducts() {
    val _sql: String = "DELETE FROM products"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
