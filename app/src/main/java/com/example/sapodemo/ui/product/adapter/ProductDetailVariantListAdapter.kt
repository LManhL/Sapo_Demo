package com.example.sapodemo.ui.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapodemo.R
import com.example.sapodemo.presenter.model.Variant

class ProductDetailVariantListAdapter: ListAdapter<Variant, RecyclerView.ViewHolder>(
    VariantDifferCallback
) {
    companion object{
        const val VIEW_TYPE_NO_PACKET = 0
        const val VIEW_TYPE_HAVE_PACKET = 1
    }
    var onClick : ((Variant) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragment_product_detail_multiple_variants, parent, false)
        return ProductDetailVariantListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val variant = getItem(position)
        val variantListViewHolder: ProductDetailVariantListViewHolder = holder as ProductDetailVariantListViewHolder
        variantListViewHolder.bind(variant)
        if(getItemViewType(position) == VIEW_TYPE_HAVE_PACKET){
            variantListViewHolder.bindSubArrowHavePacket()
        }
        else variantListViewHolder.bindSubArrowNoPacket()
    }

    override fun getItemViewType(position: Int): Int {
        val variant = getItem(position)
        if(variant?.packsize == true){
            return VIEW_TYPE_HAVE_PACKET
        }
        return VIEW_TYPE_NO_PACKET
    }
    inner class ProductDetailVariantListViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val imageView: ImageView = itemView.findViewById(R.id.ivProductDetailMultipleVariantsIllustration)
        private val name : TextView = itemView.findViewById(R.id.tvProductDetailMultipleVariantsName)
        private val SKU : TextView = itemView.findViewById(R.id.tvProductDetailMultipleVariantsSKU)
        private val available : TextView = itemView.findViewById(R.id.tvProductDetailMultipleVariantsAvailable)
        private val cost : TextView = itemView.findViewById(R.id.tvProductDetailMultipleVariantsPrice)
        private val onHand: TextView = itemView.findViewById(R.id.tvProductDetailMultipleVariantsOnHand)
        private val subArrow: ImageView = itemView.findViewById(R.id.ivProductDetailMultipleVariantsSubArrow)
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
            name.text = variant.name
            available.text = bindAvailable(available.context)
            SKU.text = bindSKU(SKU.context)
            cost.text = bindRetailPrice()
            onHand.text = bindOnHand(onHand.context)
        }
        fun bindSubArrowNoPacket(){
            subArrow.visibility = View.GONE
        }
        fun bindSubArrowHavePacket(){
            subArrow.visibility = View.VISIBLE
        }
        private fun bindImage(){
            Glide.with(itemView).load(currentVariant.getImagePath())
                .placeholder(R.drawable.ic_blank_photo)
                .fallback(R.drawable.ic_blank_photo)
                .error(R.drawable.ic_blank_photo)
                .centerCrop()
                .into(imageView)
        }
        private fun bindAvailable(context: Context): String{
            val availableString = context?.getString(R.string.available)
            return "$availableString: ${currentVariant.availableToString()}"
        }
        private fun bindSKU(context: Context): String{
            val skuString = context?.getString(R.string.sku)
            return "$skuString: ${currentVariant.skuToString()}"
        }
        private fun bindRetailPrice(): String{
            return currentVariant.retailPriceToString()
        }
        private fun bindOnHand(context: Context): String{
            val onHandString = context.getString(R.string.on_hand)
            return "$onHandString: ${currentVariant.onHandToString()}"
        }
    }
    object VariantDifferCallback : DiffUtil.ItemCallback<Variant>() {
        override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }
}