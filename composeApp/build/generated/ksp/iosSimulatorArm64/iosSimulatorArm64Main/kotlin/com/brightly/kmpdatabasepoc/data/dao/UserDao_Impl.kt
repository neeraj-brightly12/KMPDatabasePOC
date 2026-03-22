package com.brightly.kmpdatabasepoc.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.brightly.kmpdatabasepoc.`data`.entity.UserEntity
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
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserEntity: EntityInsertAdapter<UserEntity>

  private val __deleteAdapterOfUserEntity: EntityDeleteOrUpdateAdapter<UserEntity>

  private val __updateAdapterOfUserEntity: EntityDeleteOrUpdateAdapter<UserEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserEntity = object : EntityInsertAdapter<UserEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `users` (`id`,`name`) VALUES (nullif(?, 0),?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
      }
    }
    this.__deleteAdapterOfUserEntity = object : EntityDeleteOrUpdateAdapter<UserEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `users` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UserEntity) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfUserEntity = object : EntityDeleteOrUpdateAdapter<UserEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `users` SET `id` = ?,`name` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UserEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(entity: UserEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfUserEntity.insert(_connection, entity)
  }

  public override suspend fun insertAll(entities: List<UserEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserEntity.insert(_connection, entities)
  }

  public override suspend fun delete(entity: UserEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfUserEntity.handle(_connection, entity)
  }

  public override suspend fun deleteAll(entities: List<UserEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfUserEntity.handleMultiple(_connection, entities)
  }

  public override suspend fun update(entity: UserEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfUserEntity.handle(_connection, entity)
  }

  public override suspend fun updateAll(entities: List<UserEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfUserEntity.handleMultiple(_connection, entities)
  }

  public override fun getUsers(): Flow<List<UserEntity>> {
    val _sql: String = "SELECT * FROM users"
    return createFlow(__db, false, arrayOf("users")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<UserEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: UserEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          _item = UserEntity(_tmpId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUserById(id: Int): UserEntity? {
    val _sql: String = "SELECT * FROM users WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: UserEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          _result = UserEntity(_tmpId,_tmpName)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllUsers() {
    val _sql: String = "DELETE FROM users"
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
