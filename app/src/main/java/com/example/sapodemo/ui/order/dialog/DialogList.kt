package com.example.sapodemo.ui.order.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.ui.order.adapter.OrderSourceAdapter


class DialogList(
    context: Context,
) : Dialog(context) {

    var orderSourceAdapter: OrderSourceAdapter = OrderSourceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_dialog_order_source_list, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rclvDialogOrderSourceList)
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            adapter = orderSourceAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }
}