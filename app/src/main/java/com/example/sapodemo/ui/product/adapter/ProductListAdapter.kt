package com.example.sapodemo.ui.product.adapter

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
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.ProductPrototype
import com.example.sapodemo.presenter.model.Variant

class ProductListAdapter() : ListAdapter<ProductPrototype,RecyclerView.ViewHolder>(
    ProductDifferCallback
) {
    companion object{
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_PRODUCT = 1
        const val VIEW_TYPE_VARIANT = 2
    }
    var onClick : ((ProductPrototype) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PRODUCT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_fragment_product_list, parent, false)
                ProductListViewHolder(view)
            }
            VIEW_TYPE_VARIANT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_fragment_variant_list, parent, false)
                 VariantListViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading,parent,false)
                 LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val productPrototype = getItem(position)
        if(holder.itemViewType == VIEW_TYPE_PRODUCT){
            val productListViewHolder: ProductListViewHolder = holder as ProductListViewHolder
            productListViewHolder.bind(productPrototype as Product)
        }
        else if(holder.itemViewType == VIEW_TYPE_VARIANT){
            val variantListViewHolder: VariantListViewHolder = holder as VariantListViewHolder
            variantListViewHolder.bind(productPrototype as Variant)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).id == ProductPrototype.ID_LOADING) VIEW_TYPE_LOADING
        else if(getItem(position) is Product){
            VIEW_TYPE_PRODUCT
        } else {
            VIEW_TYPE_VARIANT
        }
    }
    inner class ProductListViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private var imageView: ImageView = itemView.findViewById(R.id.ivProductListIllustration);
        private var name : TextView = itemView.findViewById(R.id.tvProductListName);
        private var versionCount : TextView = itemView.findViewById(R.id.tvProductListVersions);
        private var available : TextView = itemView.findViewById(R.id.tvProductListAvailable);
        private var currentProduct: Product = Product()

        init{
            itemView.setOnClickListener{
                currentProduct.let {
                    onClick?.invoke(it)
                }
            }
        }

        fun bind(product: Product){
            currentProduct = product
            name.text = product.name;
            available.text = bindAvailable(available.context)
            versionCount.text = bindVersionCount(versionCount.context)
            bindImage(imageView.context)
        }
        private fun bindVersionCount(context: Context): String{
            val versionCountString = context.getString(R.string.count_version)
            return "$versionCountString: ${currentProduct.getVersionCountToString()}"
        }
        private fun bindImage(context: Context){
            Glide.with(itemView).load(currentProduct.getImagePath())
                .placeholder(R.drawable.ic_blank_photo)
                .fallback(R.drawable.ic_blank_photo)
                .error(R.drawable.ic_blank_photo)
                .centerCrop()
                .into(imageView)
        }
        private fun bindAvailable(context: Context): String{
            val availableString = context.getString(R.string.available)
            return "$availableString: ${currentProduct.getVersionCountToString()}"
        }

    }
    inner class VariantListViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val imageView: ImageView = itemView.findViewById(R.id.ivVariantListIllustration)
        private val name : TextView = itemView.findViewById(R.id.tvVariantListName)
        private val SKU : TextView = itemView.findViewById(R.id.tvVariantListSKUCode)
        private val available : TextView = itemView.findViewById(R.id.tvVariantListAvailable)
        private val cost : TextView = itemView.findViewById(R.id.tvVariantListCost)
        private var currentVariant: Variant = Variant()

        init{
            itemView.setOnClickListener{
                currentVariant.let {
                    onClick?.invoke(it)
                }
            }
        }

        fun bind(variant: Variant){
            currentVariant = variant
            bindImage()
            name.text = variant.name;
            available.text = bindAvailable()
            SKU.text = bindSKU(SKU.context)
            cost.text = bindRetailPrice()
        }
        private fun bindImage(){
            Glide.with(itemView).load(currentVariant.getImagePath())
                .placeholder(R.drawable.ic_blank_photo)
                .fallback(R.drawable.ic_blank_photo)
                .error(R.drawable.ic_blank_photo)
                .centerCrop()
                .into(imageView)
        }
        private fun bindAvailable(): String{
            return currentVariant.getTotalAvailableToString()
        }
        private fun bindSKU(context: Context): String{
            val skuString = context.getString(R.string.sku)
            return "$skuString: ${currentVariant.skuToString()}"
        }
        private fun bindRetailPrice():String{
            return currentVariant.retailPriceToString()
        }
    }
    class LoadingViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val progressBar: ProgressBar = itemView.findViewById(R.id.pgrbLoading)
    }
    object ProductDifferCallback : DiffUtil.ItemCallback<ProductPrototype>() {
        override fun areItemsTheSame(oldItem: ProductPrototype, newItem: ProductPrototype): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductPrototype, newItem: ProductPrototype): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }
}