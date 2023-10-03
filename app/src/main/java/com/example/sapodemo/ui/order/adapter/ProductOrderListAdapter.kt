package com.example.sapodemo.ui.order.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapodemo.R
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.model.ProductPrototype

class ProductOrderListAdapter: ListAdapter<ProductOrder, RecyclerView.ViewHolder>(ProductOrderDifferCallback
) {
    companion object{
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_VARIANT = 1
    }

    var onClick : ((ProductOrder, Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).id == ProductPrototype.ID_LOADING) VIEW_TYPE_LOADING
        else VIEW_TYPE_VARIANT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_VARIANT){
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_order_list, parent, false)
            ProductSelectionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading,parent,false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == VIEW_TYPE_VARIANT){
            (holder as ProductSelectionViewHolder).bind(getItem(position))
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(holder.itemViewType == VIEW_TYPE_VARIANT){
            if(payloads.isEmpty()){
                super.onBindViewHolder(holder, position, payloads)
            }
            else{
                val bundle = payloads[0] as Bundle
                for(key: String in bundle.keySet()){
                    if(key == ProductOrderDifferCallback.QUANTITY_PAYLOAD) {
                        (holder as ProductSelectionViewHolder).apply {
                            setCurrentValue(getItem(position))
                            bindQuantity()
                        }
                    }
                }
            }
        }
    }


    inner class ProductSelectionViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val imageView: ImageView = itemView.findViewById(R.id.ivProductOrderListIllustration)
        private val name : TextView = itemView.findViewById(R.id.tvProductOrderListName)
        private val SKU : TextView = itemView.findViewById(R.id.tvProductOrderListSKUCode)
        private val available : TextView = itemView.findViewById(R.id.tvProductOrderListAvailable)
        private val cost : TextView = itemView.findViewById(R.id.tvProductOrderListCost)
        private val quantity: TextView = itemView.findViewById(R.id.tvProductOrderListQuantity)
        private val blinkAnimation = AnimationUtils.loadAnimation(itemView.context, R.anim.blink)
        private var currentProductOrder = ProductOrder()

        init{
            itemView.setOnClickListener{
                it.startAnimation(blinkAnimation)
                onClick?.invoke(currentProductOrder,adapterPosition)
            }
        }

        fun setCurrentValue(productOrder: ProductOrder){
            currentProductOrder = productOrder
        }

        fun bind(productOrder: ProductOrder){
            setCurrentValue(productOrder)
            Glide.with(itemView).load(currentProductOrder.getImagePath())
                .placeholder(R.drawable.ic_blank_photo)
                .fallback(R.drawable.ic_blank_photo)
                .error(R.drawable.ic_blank_photo)
                .centerCrop()
                .into(imageView)

            name.text = currentProductOrder.name;
            available.text = bindAvailable()
            SKU.text = bindSKU(SKU.context)
            cost.text = bindRetailPrice()
            bindQuantity()
        }
        fun bindQuantity(){
            if(currentProductOrder.quantity == 0.0){
                quantity.visibility = View.GONE
            }
            else {
                quantity.visibility = View.VISIBLE
                quantity.text = currentProductOrder.quantityToString()
            }
        }
        private fun bindAvailable(): String{
            return currentProductOrder.getTotalAvailableToString()
        }
        private fun bindSKU(context: Context): String{
            val skuString = context.getString(R.string.sku)
            return "$skuString: ${currentProductOrder.skuToString()}"
        }
        private fun bindRetailPrice():String{
            return currentProductOrder.retailPriceToString()
        }
    }
    class LoadingViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val progressBar: ProgressBar = itemView.findViewById(R.id.pgrbLoading)
    }
}