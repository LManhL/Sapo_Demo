package com.example.sapodemo.ui.order.fragment

import OptionDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.contract.order.OrderContract
import com.example.sapodemo.databinding.FragmentOrderBinding
import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.order.orderpresenter.OrderPresenter
import com.example.sapodemo.presenter.order.viewmodel.OrderViewModel
import com.example.sapodemo.ui.order.adapter.ItemSelectedAdapter
import com.example.sapodemo.ui.order.dialog.CustomKeyBoardDialog
import com.example.sapodemo.ui.order.dialog.ListDialog
import com.example.sapodemo.util.FormatNumberUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OrderFragment : Fragment(), OrderContract.OrderView, MenuProvider {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var presenter: OrderPresenter
    private lateinit var listDialog: ListDialog
    private val model: OrderViewModel by navGraphViewModels(R.id.orderFragment)
    private val itemSelectedAdapter = ItemSelectedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = OrderPresenter(this, model)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        initView()
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_app_bar_menu_order, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.item_menu_display_order_source_list -> {
                listDialog.show()
                true
            }
            else -> false
        }
    }

    override fun onClickMinusItem(productOrder: ProductOrder, position: Int) {
        presenter.handleMinus(productOrder, position)
    }

    override fun onClickAddItem(productOrder: ProductOrder, position: Int) {
        presenter.handleAdd(productOrder, position)
    }

    override fun onClickCancelItem(productOrder: ProductOrder, position: Int) {
        val yesNoDialog = OptionDialog(activity!!)
        yesNoDialog.showYesNoDialog(
            getString(R.string.delete_product),
            getString(R.string.message_confirm_delete_product, productOrder.name),
            object : OptionDialog.YesNoListener {
                override fun onYesClicked() {
                    presenter.handleCancel(productOrder, position)
                }

                override fun onNoClicked() {}
            })
    }

    override fun onClickModifyQuantityItem(productOrder: ProductOrder, position: Int) {
        val customKeyBoardDialog = CustomKeyBoardDialog(
            activity!!,
            FormatNumberUtil.formatNumberFloor(productOrder.quantity)
        )
        customKeyBoardDialog.onClickEnter = { numberString ->
            presenter.handleSubmitQuantity(productOrder, position, numberString)
        }
        customKeyBoardDialog.show()
    }
    override fun createOrder() {
        CoroutineScope(Dispatchers.Main).launch {
            presenter.createOrder()
        }
    }

    override fun onCreateOrderSuccess() {
        val navController = view?.findNavController()
        navController?.navigate(R.id.productListFragment)
        Toast.makeText(activity, getString(R.string.create_order_success), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onFailCreateOrder(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSelectOrderSource(orderSource: OrderSource, position: Int) {
        presenter.handleSelectOrderSource(orderSource, position)
    }

    private fun initView() {
        setUpRecycleView()
        setUpEventListener()
        setUpOrderSourceListDialog()

        model.itemSelectedList.observe(this) {
            binding.tvOrderTotalQuantity.text = presenter.totalQuantityToString()
            binding.tvOrderTotalPrice.text = presenter.totalPriceToString()
            binding.tvOrderTax.text = presenter.totalTaxToString()
            binding.tvOrderProvisional.text = presenter.provisionalToString()
        }
    }

    private fun setUpOrderSourceListDialog() {
        listDialog = ListDialog(activity!!)
        listDialog.orderSourceAdapter.onClick =
            { orderSource, i -> onSelectOrderSource(orderSource, i) }
        model.orderSourceList.observe(this) {
            listDialog.orderSourceAdapter.submitList(it.toList())
        }
    }

    private fun setUpEventListener() {
        binding.apply {
            llOrderAddProduct.setOnClickListener {
                val navController = view?.findNavController()
                navController?.navigate(R.id.action_orderFragment_to_productSelectionFragment)
            }
            edtOrderSearch.setOnClickListener {
                val navController = view?.findNavController()
                navController?.navigate(R.id.action_orderFragment_to_productSelectionFragment)
            }
            btnOrderCreateOrder.setOnClickListener {
                createOrder()
            }
        }
    }

    private fun setUpRecycleView() {
        itemSelectedAdapter.onClickAdd = { productOrder, i -> onClickAddItem(productOrder, i) }
        itemSelectedAdapter.onClickMinus = { productOrder, i -> onClickMinusItem(productOrder, i) }
        itemSelectedAdapter.onClickCancel = { productOrder, i -> onClickCancelItem(productOrder, i) }
        itemSelectedAdapter.onClickChangeQuantity = { productOrder, i -> onClickModifyQuantityItem(productOrder, i) }

        model.itemSelectedList.observe(this) {
            itemSelectedAdapter.submitList(it.toList())
            if (model.itemSelectedHashMap.value.isNullOrEmpty()) {
                binding.llOrderAddProduct.visibility = View.VISIBLE
            }
            else binding.llOrderAddProduct.visibility = View.GONE
        }

        binding.apply {
            rclvOrderProductList.apply {
                adapter = itemSelectedAdapter
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            }
        }
    }
}