package com.example.sapodemo.presenter.model

import com.example.sapodemo.api.model.product.OptionResponse


class Option() {
    var id: Int? = null
    var name: String? = null
    var values: MutableList<String> = mutableListOf()

    constructor(optionResponse: OptionResponse): this(){
        id = optionResponse.id
        name = optionResponse.name
        optionResponse.values?.let { values.addAll(it.toMutableList()) }
    }
}