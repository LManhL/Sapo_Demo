package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageProductResponse(
    @Json(name = "created_on")
    val createdOn: String?,
    @Json(name = "file_name")
    val fileName: String?,
    @Json(name = "full_path")
    val fullPath: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "modified_on")
    val modifiedOn: String?,
    @Json(name = "path")
    val path: String?,
    @Json(name = "position")
    val position: Int?,
    @Json(name = "size")
    val size: Int?
)