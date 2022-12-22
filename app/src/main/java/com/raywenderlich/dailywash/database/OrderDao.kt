package com.raywenderlich.dailywash.database

import androidx.room.*

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE name LIKE :name")
    fun findOrder(name: String): List<Order>

    @Query("SELECT favorite FROM orders WHERE :id LIMIT 1")
    fun isFavorite(id: Long): Int

    @Update
    fun favoriteOrder(order: Order): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(orders: List<Order>): List<Long>
}