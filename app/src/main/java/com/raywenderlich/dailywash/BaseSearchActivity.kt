/*
 * Copyright (c) 2017 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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