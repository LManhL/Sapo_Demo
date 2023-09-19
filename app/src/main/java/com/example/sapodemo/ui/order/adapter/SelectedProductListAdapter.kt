package com.example.sapodemo.ui.order.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.presenter.model.ProductOrder

class SelectedProductListAdapter : ListAdapter<ProductOrder, RecyclerView.ViewHolder>(
    ProductOrderDifferCallback
) {

    var onClickMinus: ((ProductOrder, Int) -> Unit)? = null
    var onClickAdd: ((ProductOrder, Int) -> Unit)? = null
    var onClickCancel: ((ProductOrder, Int) -> Unit)? = null
    var onClickChangeQuantity: ((ProductOrder, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_selected_product_list, parent, false)
        return OrderLineItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val productOrder = getItem(position)
        val orderLineItemViewHolder: OrderLineItemViewHolder = holder as OrderLineItemViewHolder
        orderLineItemViewHolder.bind(productOrder)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            for (key: String in bundle.keySet()) {
                if (key == ProductOrderDifferCallback.QUANTITY_PAYLOAD) {
                    (holder as OrderLineItemViewHolder).apply {
                        setCurrentValue(getItem(position))
                        bindQuantity()
                        bindWarning()
                    }
                }
            }
        }
    }

    inner class OrderLineItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tvSelectedProductListName)
        private val sku: TextView = itemView.findViewById(R.id.tvSelectedProductListSkuCode)
        private val price: TextView = itemView.findViewById(R.id.tvSelectedProductListItemPrice)
        private val quantity: TextView = itemView.findViewById(R.id.tvSelectedProductListQuantity)
        private val minus: ImageView = itemView.findViewById(R.id.ivSelectedProductListMinus)
        private val add: ImageView = itemView.findViewById(R.id.ivSelectedProductListAdd)
        private val cancel: ImageView = itemView.findViewById(R.id.ivSelectedProductListCancel)
        private val warning: LinearLayout = itemView.findViewById(R.id.llSelectedProductListWarning)
        private var currentProductOrder: ProductOrder = ProductOrder()

        init {
            minus.setOnClickListener {
                onClickMinus?.invoke(currentProductOrder, adapterPosition)
            }
            add.setOnClickListener {
                onClickAdd?.invoke(currentProductOrder, adapterPosition)
            }
            cancel.setOnClickListener {
                onClickCancel?.invoke(currentProductOrder, adapterPosition)
            }
            quantity.setOnClickListener {
                onClickChangeQuantity?.invoke(currentProductOrder, adapterPosition)
            }

        }

        fun setCurrentValue(productOrder: ProductOrder){
            currentProductOrder = productOrder
        }

        fun bind(productOrder: ProductOrder) {
            setCurrentValue(productOrder)
            name.text = currentProductOrder.nameToString()
            sku.text = currentProductOrder.skuToString()
            bindQuantity()
            bindPrice()
            bindWarning()
        }
        fun bindQuantity() {
            quantity.text = currentProductOrder.truncateQuantityToString()
        }

        fun bindWarning() {
            val available = currentProductOrder.calculateTotalAvailable()
            if (currentProductOrder.quantity > available) {
                warning.visibility = View.VISIBLE
            } else warning.visibility = View.GONE
        }

        private fun bindPrice() {
            price.text = currentProductOrder.retailPriceToString()
        }

    }

}