package com.example.sapodemo.ui.order.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.data.network.config.API_RESULT
import com.example.sapodemo.databinding.FragmentProductSelectionBinding
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.order.ProductSelectionPresenter
import com.example.sapodemo.contract.order.ProductSelectionContract
import com.example.sapodemo.data.manager.AppDataManager
import com.example.sapodemo.presenter.order.OrderViewModel
import com.example.sapodemo.presenter.order.ProductSelectionViewModel
import com.example.sapodemo.ui.order.adapter.ProductOrderListAdapter
import com.example.sapodemo.ui.order.custom.CustomOnQueryTextChangeListener
import kotlinx.coroutines.*

class ProductSelectionFragment : Fragment(), ProductSelectionContract.ProductSelectionView {

    private lateinit var binding: FragmentProductSelectionBinding
    private lateinit var productSelectionPresenter: ProductSelectionPresenter
    private val productSelectionViewModel: ProductSelectionViewModel by viewModels()
    private val orderViewModel: OrderViewModel by navGraphViewModels(R.id.orderFragment)
    private val productOrderListAdapter = ProductOrderListAdapter()
    private var isLoadMore: Boolean = MetadataModel.ENABLE_LOAD_MORE
    private var isMultipleSelection: Boolean = false
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productSelectionPresenter = ProductSelectionPresenter(
            this,
            productSelectionViewModel,
            AppDataManager(context!!),
            orderViewModel
        )
        isMultipleSelection = productSelectionPresenter.getSharePrefSelectionType()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductSelectionBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun initData() {
        binding.swprfsProductSelection.isRefreshing = true
        CoroutineScope(Dispatchers.Main).launch {
            productSelectionPresenter.init(binding.sevProductSelection.query.toString())
        }
    }

    override fun loadMore() {
        binding.rclvProductSelection.smoothScrollToPosition(
            (binding.rclvProductSelection.adapter?.itemCount ?: 0)
        )
        isLoadMore = MetadataModel.DISABLE_LOAD_MORE
        CoroutineScope(Dispatchers.Main).launch {
            productSelectionPresenter.loadMoreData(binding.sevProductSelection.query.toString())
        }
    }

    override fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean) {
        Log.d("API response ProductSelectionFragment", response)
        isLoadMore = enableLoadMore
        if (res == API_RESULT.ERROR) Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
    }

    override fun updateViewInit(res: API_RESULT, response: String, enableLoadMore: Boolean) {
        Log.d("API response ProductSelectionFragment", response)
        binding.swprfsProductSelection.isRefreshing = false
        isLoadMore = enableLoadMore
        when (res) {
            API_RESULT.EMPTYLIST -> binding.tvProductSelectionEmptyMessage.visibility = View.VISIBLE
            API_RESULT.ERROR -> {
                binding.tvProductSelectionEmptyMessage.visibility = View.VISIBLE
                Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
            }
            API_RESULT.SUCCESS -> binding.tvProductSelectionEmptyMessage.visibility = View.GONE
        }
    }

    override fun select(productOrder: ProductOrder, position: Int) {
        if (isMultipleSelection) {
            productSelectionPresenter.select(productOrder, position)
        } else {
            productSelectionPresenter.select(productOrder, position)
            submit()
        }
    }

    override fun reselect() {
        productSelectionPresenter.reselect()
    }

    override fun submit() {
        productSelectionPresenter.submit()
        val navController = view?.findNavController()
        navController?.popBackStack()
    }

    private fun initView() {
        setUpEventListener()
        setUpRecycleView()
        if (productSelectionViewModel.productOrderList.value.isNullOrEmpty()) initData()
    }

    private fun setUpRecycleView() {
        productOrderListAdapter.onClick = { product, position ->
            select(product, position)
        }
        productSelectionViewModel.productOrderList.observe(this) { list ->
            productOrderListAdapter.submitList(list.toList())
        }
        binding.apply {
            rclvProductSelection.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = productOrderListAdapter
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val count = productSelectionViewModel.productOrderList.value?.size
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
            sevProductSelection.setOnQueryTextListener(object : CustomOnQueryTextChangeListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    job?.cancel()
                    job = CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        initData()
                    }
                    return false
                }
            })
            swprfsProductSelection.setOnRefreshListener {
                binding.swprfsProductSelection.isRefreshing = false
            }
            tglBtnProductSelectionMultipleSelection.isChecked = isMultipleSelection
            tglBtnProductSelectionMultipleSelection.setOnCheckedChangeListener { _, isChecked ->
                isMultipleSelection = isChecked
                productSelectionPresenter.setSharePrefSelectionType(isMultipleSelection)
            }
            btnProductSelectionSubmit.setOnClickListener {
                submit()
            }
            btnProductSelectionReselect.setOnClickListener {
                reselect()
            }
        }
    }
}