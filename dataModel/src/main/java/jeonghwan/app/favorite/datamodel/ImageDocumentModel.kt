package jeonghwan.app.favorite.datamodel

import com.google.gson.annotations.SerializedName

data class ImageDocumentModel(
    @SerializedName("collection")
    val collection: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("display_sitename")
    val displaySiteName: String,

    @SerializedName("doc_url")
    val docUrl: String,

    @SerializedName("datetime")
    val dateTime: String
)