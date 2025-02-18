package jeonghwan.app.favorite.datamodel

import com.google.gson.annotations.SerializedName


data class MovieDocumentModel(
    @SerializedName("title")
    val title: String,

    @SerializedName("play_time")
    val playTime: Int,

    @SerializedName("thumbnail")
    val thumbnail: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("datetime")
    val dateTime: String,

    @SerializedName("author")
    val author: String
)