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