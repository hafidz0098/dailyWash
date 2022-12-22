package com.raywenderlich.dailywash.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(Order::class)], version = 1)
abstract class OrderDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao

    companion object {

        private var INSTANCE: OrderDatabase? = null

        fun getInstance(context: Context): OrderDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context,
                            OrderDatabase::class.java, "dailywash.db")
                            .build()
                }
            }
            return INSTANCE as OrderDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}