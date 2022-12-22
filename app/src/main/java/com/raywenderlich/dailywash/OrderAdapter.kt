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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.dailywash.database.Order
import com.raywenderlich.dailywash.database.OrderDatabase
import com.raywenderlich.dailywash.ui.CheckableImageView
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_item.view.*

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var compositeDisposable = CompositeDisposable()

    var orders: List<Order> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = orders.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = orders[position]
        holder.itemView.tv_id_order.text = order.id.toString()
        holder.itemView.textView.text = order.name
        holder.itemView.tv_date.text = order.date
        holder.itemView.tv_jenis.text = order.type
        holder.itemView.tv_harga.text = order.price
        holder.itemView.tv_berat.text = "${order.weight}"
        holder.itemView.tv_status.text = order.status
        holder.itemView.imageFavorite.isChecked = order.favorite == 1

        compositeDisposable.add(Maybe.create<Boolean> { emitter ->
            emitter.setCancellable {
                holder.itemView.imageFavorite.setOnClickListener(null)
            }

            holder.itemView.imageFavorite.setOnClickListener {
                emitter.onSuccess((it as CheckableImageView).isChecked)
            }
        }.toFlowable().onBackpressureLatest()
                .observeOn(Schedulers.io())
                .map { isChecked ->
                    order.favorite = if (!isChecked) 1 else 0
                    val database = OrderDatabase.getInstance(holder.itemView.context).orderDao()
                    database.favoriteOrder(order)
                    order.favorite
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    holder.itemView.imageFavorite.isChecked = it == 1 // 6
                }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}