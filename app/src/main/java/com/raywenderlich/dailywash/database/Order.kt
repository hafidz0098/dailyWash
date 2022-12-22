package com.raywenderlich.dailywash.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "orders", indices = [Index(value = ["name"], unique = true)])
data class Order(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                 @ColumnInfo(name = "name") var name: String,
                 @ColumnInfo(name = "date") var date: String,
                 @ColumnInfo(name = "type") var type: String,
                 @ColumnInfo(name = "price") var price: String,
                 @ColumnInfo(name = "weight") var weight: Double,
                 @ColumnInfo(name = "status") var status: String,
                 @ColumnInfo(name = "favorite") var favorite: Int = 0)
