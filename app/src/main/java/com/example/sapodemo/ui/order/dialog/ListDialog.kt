package com.example.sapodemo.ui.order.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapodemo.R
import com.example.sapodemo.ui.order.adapter.OrderSourceListAdapter


class ListDialog(
    context: Context,
) : Dialog(context) {

    var orderSourceListAdapter: OrderSourceListAdapter = OrderSourceListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        this.setContentView(R.layout.layout_dialog_order_source_list)
        this.setCanceledOnTouchOutside(true)
        this.setCancelable(true)

        val recyclerView = this.findViewById<RecyclerView>(R.id.rclvDialogOrderSourceList)
        recyclerView.apply {
            adapter = orderSourceListAdapter
            addItemDecoration(DividerItemDecoration(ContextThemeWrapper(this.context, R.style.Theme_SapoDemo), RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val height = context.resources.displayMetrics.heightPixels
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height/2)
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}