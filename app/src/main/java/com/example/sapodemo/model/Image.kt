package com.example.sapodemo.model

import com.example.sapodemo.api.model.product.ImageProductResponse
import com.example.sapodemo.api.model.product.ImageVariantResponse

class Image() {
    var fullPath: String? = null
    var id: Int? = null

    constructor(imageVariant: ImageVariantResponse): this(){
        fullPath = imageVariant.fullPath
        id = imageVariant.id
    }
    constructor(imageProductResponse: ImageProductResponse): this(){
        fullPath = imageProductResponse.fullPath
        id = imageProductResponse.id
    }
}