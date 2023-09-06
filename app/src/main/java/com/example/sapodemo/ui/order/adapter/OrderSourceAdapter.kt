package com.example.sapodemo.ui.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.model.OrderSource

class OrderSourceAdapter: ListAdapter<OrderSource,RecyclerView.ViewHolder>(OrderSourceDiffCallBack) {

    var onClick : ((OrderSource, Int) -> Unit)? = null
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_source_list, parent, false)
        return OrderSourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderSourceViewHolder).bind(getItem(position))
    }

    inner class OrderSourceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val name: TextView = itemView.findViewById(R.id.tvOrderSourceListName)
        private val isSelect: ImageView = itemView.findViewById(R.id.ivOrderSourceListIsSelect)
        private var currentOrderSource = OrderSource()

        init{
            itemView.setOnClickListener{
                currentOrderSource.let {
                    onClick?.invoke(it,adapterPosition)
                }
            }
        }

        fun bind(orderSource: OrderSource){
            currentOrderSource = orderSource
            name.text = currentOrderSource.name
            bindIsSelect()
        }
        private fun bindIsSelect(){
            isSelect.visibility = if(currentOrderSource.isSelect) View.VISIBLE else View.INVISIBLE
        }
    }

    object OrderSourceDiffCallBack : DiffUtil.ItemCallback<OrderSource>() {
        override fun areItemsTheSame(oldItem: OrderSource, newItem: OrderSource): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderSource, newItem: OrderSource): Boolean {
            return oldItem.id == newItem.id && oldItem.isSelect == newItem.isSelect
        }
    }
}