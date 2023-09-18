package com.example.sapodemo.data.manager

import com.example.sapodemo.data.network.services.OrderApiService
import com.example.sapodemo.data.network.services.ProductApiService
import com.example.sapodemo.data.network.services.VariantApiService
import com.example.sapodemo.data.sharepref.PreferencesHelper

interface DataManager: PreferencesHelper, OrderApiService, ProductApiService, VariantApiService{

}