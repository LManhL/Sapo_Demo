package com.example.sapodemo.ui.product.fragment

import android.os.Bundle
import android.text.Editable
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
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.ProductPrototype
import com.example.sapodemo.presenter.model.Variant
import com.example.sapodemo.presenter.product.productpresenter.ProductListPresenter
import com.example.sapodemo.ui.product.custom.CustomTextWatcher
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
    private var isLoadMore: Boolean = MetadataModel.ENABLE_LOAD_MORE
    private var isFocusSearch: Boolean = false
    private var currentType = ProductPrototype.PRODUCT_TYPE
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
        initView()
        return view;
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_app_bar_menu_product_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
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
    override fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean) {
        Log.d("API response ProductListFragment", response)
        isLoadMore = enableLoadMore
        if (res == API_RESULT.ERROR) Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
    }
    override fun swipeRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            productListPresenter.swipeRefresh()
        }
    }
    override fun updateViewSwipeRefresh() {
        binding.swprfsProductList.isRefreshing = false
    }
    private fun initView() {
        setUpViewModel()
        setUpRecycleView()
        setUpEventListener()
        if (model.products.value.isNullOrEmpty()) init()
    }

    private fun setUpRecycleView() {
        productListAdapter.onClick = { it->
            onClickItemAdapter(it)
        }
        binding.apply {
            rclvProductList.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = productListAdapter
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val count = model.products.value?.size
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

    private fun onClickItemAdapter(productPrototype: ProductPrototype){
        if(currentType == ProductPrototype.PRODUCT_TYPE){
            onClickItemProductList(productPrototype as Product)
        }
        else{
            onClickItemVariantList(productPrototype as Variant)
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
        model.products.observe(this) { productList ->
            productListAdapter.submitList(productList.toList())
        }
        if (currentType == ProductPrototype.PRODUCT_TYPE) {
            model.total.observe(this) { total ->
                val totalCountText = "$total ${getString(R.string.product)}"
                binding.tvProductListTotalCount.text = totalCountText
            }
        } else {
            model.total.observe(this) { total ->
                val totalCountText = "$total ${getString(R.string.variant)}"
                binding.tvProductListTotalCount.text = totalCountText
            }
        }
    }


    private fun handleClickMenu() {
        currentType = if (currentType == ProductPrototype.PRODUCT_TYPE) ProductPrototype.VARIANT_TYPE
                      else ProductPrototype.PRODUCT_TYPE
        isLoadMore = MetadataModel.ENABLE_LOAD_MORE
        init()
        setUpViewModel()
    }
}