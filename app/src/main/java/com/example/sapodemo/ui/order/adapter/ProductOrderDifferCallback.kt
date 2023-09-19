package com.example.sapodemo.ui.order.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.sapodemo.presenter.model.ProductOrder

object ProductOrderDifferCallback: DiffUtil.ItemCallback<ProductOrder>() {

    const val QUANTITY_PAYLOAD = "QUANTITY_PAYLOAD"

    override fun areItemsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
        return oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
    }

    override fun getChangePayload(oldItem: ProductOrder, newItem: ProductOrder): Any {
        val bundle = Bundle()
        if(oldItem.quantity != newItem.quantity){
            bundle.putDouble(QUANTITY_PAYLOAD,newItem.quantity)
        }
        return bundle
    }
}