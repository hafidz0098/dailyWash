package com.raywenderlich.dailywash

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.dailywash.database.Order
import com.raywenderlich.dailywash.database.OrderDatabase
import com.raywenderlich.dailywash.database.OrderUtil
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order.*

abstract class BaseSearchActivity : AppCompatActivity() {

    protected lateinit var orderSearchEngine: OrderSearchEngine
    private val orderAdapter = OrderAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = orderAdapter

        orderSearchEngine = OrderSearchEngine(this)
        val initialLoadDisposable = loadInitialData(this).subscribe()
        compositeDisposable.add(initialLoadDisposable)
    }

    private fun loadInitialData(context: Context): Flowable<List<Long>> {
        return Maybe.fromAction<List<Long>> {
            val database = OrderDatabase.getInstance(context = context).orderDao()
            val orderList = arrayListOf<Order>()
            for (order in OrderUtil.Orders) {
                orderList.add(Order(name = order.name, date = order.date ,type = order.type, price = order.price, weight = order.weight, status = order.status))
            }
            database.insertAll(orderList)

        }.toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show()
                }
                .doOnError {
                    Toast.makeText(context, context.getString(R.string.error_inserting_cheeses), Toast.LENGTH_LONG).show()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        OrderDatabase.destroyInstance()
        compositeDisposable.clear()
    }

    protected fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    protected fun hideProgress() {
        progressBar.visibility = GONE
    }

    protected fun showResult(result: List<Order>) {
        if (result.isEmpty()) {
            Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show()
        }
        orderAdapter.orders = result
    }

}