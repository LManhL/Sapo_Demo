package com.example.sapodemo.ui.product.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.ui.product.adapter.ImageListAdapter
import com.example.sapodemo.contract.product.ProductDetailContract
import com.example.sapodemo.databinding.FragmentProductDetailBinding
import com.example.sapodemo.presenter.model.Image
import com.example.sapodemo.presenter.model.Variant
import com.example.sapodemo.presenter.product.productpresenter.ProductDetailPresenter
import com.example.sapodemo.presenter.product.viewmodel.ProductDetailViewModel
import com.example.sapodemo.ui.product.adapter.ProductDetailVariantListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment(), ProductDetailContract.ProductDetailView {

    companion object {
        const val PRODUCT_ID = "product_id"
        const val VARIANT_ID = "variant_id"
    }

    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var productDetailPresenter: ProductDetailContract.ProductDetailPresenter
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var variantListAdapter: ProductDetailVariantListAdapter
    private val imageList: MutableList<Image> = mutableListOf()
    private val model: ProductDetailViewModel by viewModels()
    private var id: Int? = null
    private var isMultipleVariant: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailPresenter = ProductDetailPresenter(this, model)
        id = arguments?.getInt(ProductListFragment.PRODUCT_ID)
        isMultipleVariant = arguments?.getBoolean(ProductListFragment.IS_MULTIPLE_VARIANT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun init(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            productDetailPresenter.getProduct(id)
        }
    }

    override fun onSuccess(response: String) {
        Log.d("API response ProductDetailFragment", response)
        setUpView()
        binding.scrvProductDetailContainer.visibility = View.VISIBLE
    }

    override fun onFail(response: String) {
        Log.d("API response ProductDetailFragment", response)
        binding.scrvProductDetailContainer.visibility = View.GONE
        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
    }

    override fun setUpView() {
        setUpImageAdapter()
        setupClickListeners()
        binding.apply {
            llProductDetailSKUBarcodeWeightUnitContainer.visibility = if(isMultipleVariant == true) View.GONE else View.VISIBLE
            crdProductDetailInventoryContainer.visibility = if(isMultipleVariant == true ) View.GONE else View.VISIBLE
            crdProductDetailPriceContainer.visibility = if(isMultipleVariant == true) View.GONE else View.VISIBLE
            crdProductDetailVariantListContainer.visibility = if(isMultipleVariant == true) View.VISIBLE else View.GONE
        }
        if(isMultipleVariant == true){
            setUpViewMultipleVariant()
        }
        else{
            setUpViewSingleVariant()
        }
    }
    private fun initView(){
        binding.scrvProductDetailContainer.visibility = View.GONE
        if(id != null){
            init(id!!)
        }
        else{
            Toast.makeText(activity,"Problem passing parameter",Toast.LENGTH_SHORT).show()
        }
    }
    private fun setUpViewSingleVariant(){
        model.product.observe(this) { product ->
            val variant = product.variants[0]
            val onHand = "${getString(R.string.on_hand)}: ${variant.onHandToString()}"
            val available = "${getString(R.string.available)}: ${variant.availableToString()}"
            val sellable = if(variant.isSellable()) getString(R.string.sellable) else getString(R.string.not_sellable)
            val active = if(variant.isActive()) getString(R.string.active) else getString(R.string.inactive)
            binding.apply {

                edtProductDetailOutputVat.setText(variant.outputVatRateToString())
                edtProductDetailInputVat.setText(variant.inputVatRateToString())
                edtProductDetailRetailPrice.setText(variant.retailPriceToString())
                edtProductDetailWholePrice.setText(variant.wholePriceToString())
                edtProductDetailImportPrice.setText(variant.importPriceToString())
                edtProductDetailDescription.setText(variant.descriptionToString())

                tvProductDetailOnHand.text = onHand
                tvProductDetailAvailable.text = available
                tvProductDetailSellable.text = sellable
                tvProductDetailStatus.text = active
                tvProductDetaiWeight.text = variant.weightToString()
                tvProductDetailName.text = variant.nameToString()
                tvProductDetailSKU.text = variant.skuToString()
                tvProductDetailBarcode.text = variant.barcodeToString()
                tvProductDetailUnit.text = variant.unitToString()

            }
        }
    }
    private fun setUpViewMultipleVariant(){
        variantListAdapter = ProductDetailVariantListAdapter()
        variantListAdapter.onClick = {variant -> adapterOnClick(variant)}

        binding.rclvProductDetailVariantList.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context,RecyclerView.VERTICAL))
            adapter = variantListAdapter
        }
        model.product.observe(this) { product ->
            val active = if(product.isActive()) getString(R.string.active) else getString(R.string.inactive)
            binding.apply {
                tvProductDetailName.text = product.nameToString()
                variantListAdapter.submitList(product.variants.toList())
                tvProductDetailStatus.text = active
            }
        }
    }
    private fun setupClickListeners() {
        binding.rlProductDetailAdditionalInfor.setOnClickListener {
            val additionalInfoVisible = binding.llProductDetailAdditionalInfor.isVisible
            binding.llProductDetailAdditionalInfor.visibility = if (additionalInfoVisible) View.GONE else View.VISIBLE
            binding.ivProductDetailArrowUp.visibility = if (additionalInfoVisible) View.GONE else View.VISIBLE
            binding.ivProductDetailArrowDown.visibility = if (additionalInfoVisible) View.VISIBLE else View.GONE
        }
    }
    private fun adapterOnClick(variant: Variant){
        val bundle = bundleOf()
        variant.id?.apply {
            bundle.putInt(VARIANT_ID, variant.id!!)
            arguments?.let { bundle.putInt(PRODUCT_ID, id!!)}
        }
        val navController = view?.findNavController()
        navController?.navigate(R.id.action_productDetailFragment_to_variantDetailFragment,bundle)
    }
    private fun setUpImageAdapter(){
        binding.rclvProductDetailImageList.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            imageListAdapter = ImageListAdapter(imageList)
            adapter = imageListAdapter
        }
        model.product.observe(this) { product ->
            binding.apply {
                product?.images?.let { imageList.addAll(it) }
                imageListAdapter.notifyDataSetChanged()
            }
        }
    }
}
