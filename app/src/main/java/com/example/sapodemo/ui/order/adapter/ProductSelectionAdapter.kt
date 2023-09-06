package com.example.sapodemo.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapodemo.R
import com.example.sapodemo.model.ProductOrder
import com.example.sapodemo.model.ProductPrototype

class ProductSelectionAdapter: ListAdapter<ProductOrder, RecyclerView.ViewHolder>(ProductDifferCallback
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
                .inflate(R.layout.item_fragment_product_selection, parent, false)
            ProductSelectionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading,parent,false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val productOrder = getItem(position)
        if(holder.itemViewType == VIEW_TYPE_VARIANT){
            val productSelectionViewHolder: ProductSelectionAdapter.ProductSelectionViewHolder = holder as ProductSelectionViewHolder
            productSelectionViewHolder.bind(productOrder)
        }
    }


    inner class ProductSelectionViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val imageView: ImageView = itemView.findViewById(R.id.ivProductSelectionIllustration)
        private val name : TextView = itemView.findViewById(R.id.tvProductSelectionName)
        private val SKU : TextView = itemView.findViewById(R.id.tvProductSelectionSKUCode)
        private val available : TextView = itemView.findViewById(R.id.tvProductSelectionAvailable)
        private val cost : TextView = itemView.findViewById(R.id.tvProductSelectionCost)
        private val quantity: TextView = itemView.findViewById(R.id.tvProductSelectionQuantity)
        private var currentProductOrder = ProductOrder()

        init{
            itemView.setOnClickListener{
                currentProductOrder.let {
                    onClick?.invoke(it,adapterPosition)
                }
            }
        }

        fun bind(productOrder: ProductOrder){
            currentProductOrder = productOrder
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
        private fun bindQuantity(){
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
            val skuString = context?.getString(R.string.sku)
            return "$skuString: ${currentProductOrder.skuToString()}"
        }
        private fun bindRetailPrice():String{
            return currentProductOrder.retailPriceToString()
        }
    }
    class LoadingViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val progressBar: ProgressBar = itemView.findViewById(R.id.pgrbLoading)
    }
    object ProductDifferCallback : DiffUtil.ItemCallback<ProductOrder>() {
        override fun areItemsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductOrder, newItem: ProductOrder): Boolean {
            return oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
        }
    }
}