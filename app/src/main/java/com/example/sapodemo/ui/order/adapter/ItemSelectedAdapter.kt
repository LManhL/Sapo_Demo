package com.example.sapodemo.ui.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.model.ProductOrder

class ItemSelectedAdapter: ListAdapter<ProductOrder, RecyclerView.ViewHolder>(
    ProductDifferCallback
) {
    var onClickMinus: ((ProductOrder, Int)->Unit)? = null
    var onClickAdd: ((ProductOrder, Int)->Unit)? = null
    var onClickCancel: ((ProductOrder, Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_order, parent, false)
        return OrderLineItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val productOrder = getItem(position)
        val orderLineItemViewHolder: OrderLineItemViewHolder = holder as OrderLineItemViewHolder
        orderLineItemViewHolder.bind(productOrder)
    }

    inner class OrderLineItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val name: TextView = itemView.findViewById(R.id.tvOrderName)
        private val sku: TextView = itemView.findViewById(R.id.tvOrderSkuCode)
        private val price: TextView = itemView.findViewById(R.id.tvOrderItemPrice)
        private val quantity: TextView = itemView.findViewById(R.id.tvOrderQuantity)
        private val minus: ImageView = itemView.findViewById(R.id.ivOrderMinus)
        private val add: ImageView = itemView.findViewById(R.id.ivOrderAdd)
        private val cancel: ImageView = itemView.findViewById(R.id.ivOrderCancel)
        private val warning: LinearLayout = itemView.findViewById(R.id.llOrderWarning)
        private var currentProductOrder: ProductOrder = ProductOrder()

        init {
            minus.setOnClickListener {
                onClickMinus?.invoke(currentProductOrder, adapterPosition)
            }
            add.setOnClickListener {
                onClickAdd?.invoke(currentProductOrder,adapterPosition)
            }
            cancel.setOnClickListener {
                onClickCancel?.invoke(currentProductOrder,adapterPosition)
            }
        }
        fun bind(productOrder: ProductOrder){
            currentProductOrder = productOrder
            name.text = currentProductOrder.nameToString()
            sku.text = currentProductOrder.skuToString()
            quantity.text = bindQuantity()
            price.text = bindPrice()
            bindWarning()
        }
        private fun bindWarning(){
            val available = currentProductOrder.calculateTotalAvailable()
            if(currentProductOrder.quantity > available){
                warning.visibility = View.VISIBLE
            }
            else warning.visibility = View.GONE
        }
        private fun bindPrice(): String{
            return currentProductOrder.getTotalAvailableToString()
        }
        private fun bindQuantity(): String{
            return currentProductOrder.quantityToString()
        }

    }
    object ProductDifferCallback : DiffUtil.ItemCallback<ProductOrder>() {
        override fun areItemsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
            return oldItem.quantity == newItem.quantity
        }
    }

}