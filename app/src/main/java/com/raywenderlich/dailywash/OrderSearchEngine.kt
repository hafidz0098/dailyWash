package com.raywenderlich.dailywash

import android.content.Context
import android.util.Log
import com.raywenderlich.dailywash.database.Order
import com.raywenderlich.dailywash.database.OrderDatabase

class OrderSearchEngine(private val context: Context) {

    fun search(query: String): List<Order> {
        Thread.sleep(2000)
        Log.d("Searching", "Searching for $query")
        return OrderDatabase.getInstance(context).orderDao().findOrder("%$query%")
    }
}