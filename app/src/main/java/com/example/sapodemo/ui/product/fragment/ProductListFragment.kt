package com.example.sapodemo.ui.product.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.presenter.product.viewmodel.ProductListViewModel
import com.example.sapodemo.R
import com.example.sapodemo.ui.product.adapter.ProductListAdapter
import com.example.sapodemo.api.config.API_RESULT
import com.example.sapodemo.contract.product.ProductListContract
import com.example.sapodemo.databinding.FragmentProductListBinding
import com.example.sapodemo.model.MetadataModel
import com.example.sapodemo.model.Product
import com.example.sapodemo.model.ProductPrototype
import com.example.sapodemo.model.Variant
import com.example.sapodemo.presenter.product.productpresenter.ProductListPresenter
import kotlinx.coroutines.*

class ProductListFragment : Fragment(), ProductListContract.ProductListView, MenuProvider {

    companion object {
        const val PRODUCT_ID = "product_id"
        const val VARIANT_ID = "variant_id"
        const val IS_MULTIPLE_VARIANT = "is_multiple_variant"
    }

    private lateinit var binding: FragmentProductListBinding
    private lateinit var productListPresenter: ProductListPresenter
    private val model: ProductListViewModel by viewModels()
    private var productListAdapter: ProductListAdapter = ProductListAdapter()
    private var variantListAdapter: ProductListAdapter = ProductListAdapter()
    private var isLoadMore: Boolean = MetadataModel.ENABLE_LOAD_MORE
    private var isFocusSearch: Boolean = false
    private var currentType = ProductPrototype.PRODUCT_TYPE
    private var currentScrollPositionProduct = 0
    private var currentScrollPositionVariant = 0
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productListPresenter = ProductListPresenter(this, model)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(layoutInflater)
        val view = binding.root
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setUpAdapter()
        setUpViewModel()
        initView()
        initData()
        return view;
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_app_bar_menu_product_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.item_menu_display_custom -> {
                true
            }
            R.id.item_menu_change_type -> {
                handleClickMenu()
                true
            }
            else -> false
        }
    }

    override fun init() {
        binding.swprfsProductList.isRefreshing = true
        CoroutineScope(Dispatchers.Main).launch {
            productListPresenter.init(binding.edtProductListSearch.text.toString(), currentType)
        }
    }

    override fun updateViewInit(res: API_RESULT, response: String, enableLoadMore: Boolean) {
        Log.d("API response ProductListFragment", response)
        isLoadMore = enableLoadMore
        binding.swprfsProductList.isRefreshing = false
        when (res) {
            API_RESULT.EMPTYLIST -> {
                binding.tvProductListEmptyMessage.visibility = View.VISIBLE
            }
            API_RESULT.ERROR -> {
                binding.tvProductListEmptyMessage.visibility = View.GONE
                Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
            }
            API_RESULT.SUCCESS -> {
                binding.tvProductListEmptyMessage.visibility = View.GONE
            }
        }
    }

    override fun loadMore() {
        isLoadMore = MetadataModel.DISABLE_LOAD_MORE
        binding.rclvProductList.smoothScrollToPosition(
            (binding.rclvProductList.adapter?.itemCount ?: 0)
        )
        CoroutineScope(Dispatchers.Main).launch {
            productListPresenter.loadMoreData(
                binding.edtProductListSearch.text.toString(),
                currentType
            )
        }
    }

    override fun swipeRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            productListPresenter.swipeRefresh()
        }
    }

    override fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean) {
        Log.d("API response ProductListFragment", response)
        isLoadMore = enableLoadMore
        if (res == API_RESULT.ERROR) Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
    }

    override fun updateViewSwipeRefresh() {
        binding.swprfsProductList.isRefreshing = false
    }
    private fun initView() {
        setUpRecycleView()
        setUpEventListener()
    }

    private fun setUpRecycleView() {
        binding.apply {
            rclvProductList.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = if (currentType == ProductPrototype.PRODUCT_TYPE) productListAdapter else variantListAdapter
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val count =
                            if (currentType == ProductPrototype.PRODUCT_TYPE) model.products.value?.size
                            else model.variants.value?.size
                        if (count != null) {
                            if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == count - 1
                                && isLoadMore == MetadataModel.ENABLE_LOAD_MORE
                            ) {
                                loadMore()
                            }
                        }
                    }
                })
            }
        }
    }
    private fun setUpEventListener() {
        binding.apply {
            swprfsProductList.setOnRefreshListener {
                swprfsProductList.isRefreshing = true
                swipeRefresh()
            }
            edtProductListSearch.setOnFocusChangeListener { _, hasFocus ->
                isFocusSearch = hasFocus
            }
            edtProductListSearch.addTextChangedListener(object : CustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (isFocusSearch) {
                        job?.cancel()
                        job = CoroutineScope(Dispatchers.Main).launch {
                            delay(500)
                            init()
                        }
                    }
                }
            })
        }
    }

    private fun setUpAdapter() {
        model.products.observe(this) { productList ->
            productListAdapter.submitList(productList as List<ProductPrototype>?)
        }
        model.variants.observe(this) { variantList ->
            variantListAdapter.submitList(variantList as List<ProductPrototype>?)
        }
        productListAdapter.onClick = { it ->
            onClickItemProductList(it as Product)
        }
        variantListAdapter.onClick = { it ->
            onClickItemVariantList(it as Variant)
        }
    }
    private fun onClickItemVariantList(variant: Variant) {
        val bundle = bundleOf()
        variant.id?.apply {
            bundle.putInt(VARIANT_ID, variant.id!!)
            variant.productId?.let { bundle.putInt(PRODUCT_ID, it) }
        }
        val navController = view?.findNavController()
        navController?.navigate(R.id.action_productListFragment_to_variantDetailFragment, bundle)
    }

    private fun onClickItemProductList(product: Product) {
        val bundle = bundleOf()
        var isMultipleVariant: Boolean
        product.id?.apply {
            isMultipleVariant = product.isMultipleVariant()
            bundle.putInt(PRODUCT_ID, product.id!!)
            bundle.putBoolean(IS_MULTIPLE_VARIANT, isMultipleVariant)
        }
        val navController = view?.findNavController()
        navController?.navigate(R.id.action_productListFragment_to_productDetailFragment, bundle)
    }

    private fun setUpViewModel() {
        if (currentType == ProductPrototype.PRODUCT_TYPE) {
            model.totalProductList.observe(this) { total ->
                val totalCountText = "$total ${getString(R.string.product)}"
                binding.tvProductListTotalCount.text = totalCountText
            }
        } else {
            model.totalVariantList.observe(this) { total ->
                val totalCountText = "$total ${getString(R.string.variant)}"
                binding.tvProductListTotalCount.text = totalCountText
            }
        }
    }


    private fun handleClickMenu() {
        val layoutManager = binding.rclvProductList.layoutManager as LinearLayoutManager
        if (currentType == ProductPrototype.PRODUCT_TYPE) {
            currentScrollPositionProduct = layoutManager.findFirstVisibleItemPosition()
            currentType = ProductPrototype.VARIANT_TYPE
            binding.rclvProductList.adapter = variantListAdapter
            if (model.variants.value.isNullOrEmpty()) init()
            binding.rclvProductList.layoutManager?.scrollToPosition(currentScrollPositionVariant)
        } else {
            currentScrollPositionVariant = layoutManager.findFirstVisibleItemPosition()
            currentType = ProductPrototype.PRODUCT_TYPE
            binding.rclvProductList.adapter = productListAdapter
            if (model.products.value.isNullOrEmpty()) init()
            binding.rclvProductList.layoutManager?.scrollToPosition(currentScrollPositionProduct)
        }
        isLoadMore = MetadataModel.ENABLE_LOAD_MORE
        setUpViewModel()
    }
    private fun initData() {
        if ((currentType == ProductPrototype.PRODUCT_TYPE && model.products.value.isNullOrEmpty())
            || (currentType == ProductPrototype.VARIANT_TYPE && model.variants.value.isNullOrEmpty())
        ) {
            init()
        }
    }

    interface CustomTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }
}