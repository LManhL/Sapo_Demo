package com.example.sapodemo.ui.product.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.example.sapodemo.R
import com.example.sapodemo.ui.product.adapter.ProductDetailVariantListAdapter
import com.example.sapodemo.contract.product.VariantDetailContract
import com.example.sapodemo.databinding.FragmentVariantDetailBinding
import com.example.sapodemo.presenter.model.Variant
import com.example.sapodemo.presenter.product.productpresenter.VariantDetailPresenter
import com.example.sapodemo.presenter.product.viewmodel.VariantDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VariantDetailFragment : Fragment(), VariantDetailContract.VariantDetailView {

    companion object {
        const val PRODUCT_ID = "product_id"
        const val VARIANT_ID = "variant_id"
    }

    private lateinit var presenter: VariantDetailContract.VariantDetailPresenter
    private lateinit var binding: FragmentVariantDetailBinding
    private var unitListAdapter = ProductDetailVariantListAdapter()
    private val model: VariantDetailViewModel by viewModels()
    private var productId: Int? = null
    private var variantId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = VariantDetailPresenter(this, model)
        productId = arguments?.getInt(PRODUCT_ID)
        variantId = arguments?.getInt(VARIANT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVariantDetailBinding.inflate(layoutInflater)
        val view = binding.root
        initView()
        return view;
    }

    override fun init() {
        CoroutineScope(Dispatchers.Main).launch {
            if (productId != null && variantId != null) {
                presenter.getVariant(productId!!, variantId!!)
                presenter.getUnitList(productId!!, variantId!!)
            }
        }
    }

    override fun onSuccess(response: String) {
        updateViewInit()
        binding.scrvVariantDetailContainer.visibility = View.VISIBLE
    }

    override fun onFail(response: String) {
        Log.d("API response VariantDetailFragment", response)
        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
        binding.scrvVariantDetailContainer.visibility = View.GONE
    }

    override fun updateViewInit() {
        setUpRecycleView()
        model.let { viewModel ->
            viewModel.variant.observe(this) { variant ->

                if (variant.getImagePath() != null) {
                    binding.ivVariantDetailIllustration.visibility = View.VISIBLE
                    binding.ivVariantDetailAddPicture.visibility = View.GONE
                } else {
                    binding.ivVariantDetailIllustration.visibility = View.GONE
                    binding.ivVariantDetailAddPicture.visibility = View.VISIBLE
                }

                Glide.with(this).load(variant.getImagePath())
                    .placeholder(R.drawable.ic_blank_photo)
                    .fallback(R.drawable.ic_blank_photo)
                    .error(R.drawable.ic_blank_photo)
                    .centerCrop()
                    .into(binding.ivVariantDetailIllustration)

                binding.edtVariantDetailOutputVat.setText(variant.outputVatRateToString())
                binding.edtVariantDetailInputVat.setText(variant.inputVatRateToString())
                binding.edtVariantDetailImportPrice.setText(variant.importPriceToString())
                binding.edtVariantDetailRetailPrice.setText(variant.retailPriceToString())
                binding.edtVariantDetailWholePrice.setText(variant.wholePriceToString())

                binding.tvVariantDetailName.text = variant.nameToString()
                binding.tvVariantDetailSKU.text = variant.skuToString()
                binding.tvVariantDetailBarcode.text = variant.barcodeToString()
                binding.tvProductDetaiWeight.text = variant.weightToString()
                binding.tvVariantDetailUnit.text = variant.unitToString()
                binding.tvVariantDetailSize.text = variant.opt1ToString()
                binding.tvVariantDetailColor.text = variant.opt2ToString()
                binding.tvVariantDetailMaterial.text = variant.opt3ToString()
                binding.tvVariantDetailOnHand.text = variant.onHandToString()
                binding.tvVariantDetailAvailable.text = variant.availableToString()
            }
        }
    }

    private fun initView() {
        binding.scrvVariantDetailContainer.visibility = View.GONE
        if (variantId != null && productId != null) {
            init()
        } else {
            Toast.makeText(activity, "Problem passing parameter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecycleView() {
        binding.rclvVariantDetailUnitList.apply {
            adapter = unitListAdapter
            layoutManager = LinearLayoutManager(activity, VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
        unitListAdapter = ProductDetailVariantListAdapter()
        unitListAdapter.onClick = { variant: Variant -> adapterOnClick(variant) }
        model.unitList.observe(this) { unitList ->
            unitListAdapter.submitList(unitList)
        }
    }

    private fun adapterOnClick(variant: Variant) {
        val bundle = bundleOf()
        variant.id?.apply {
            bundle.putInt(VARIANT_ID, variant.id!!)
            variant.productId?.let { bundle.putInt(PRODUCT_ID, it) }
        }
        val navController = view?.findNavController()
        navController?.navigate(R.id.variantDetailFragment, bundle)
    }
}