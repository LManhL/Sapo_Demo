package com.example.sapodemo.ui.order.custom

import android.widget.SearchView

interface CustomOnQueryTextChangeListener: SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}