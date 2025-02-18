package jeonghwan.app.favorite.entitymodel

data class MovieEntity(
    val title: String,
    val playTime: Int,
    val thumbnail: String,
    val url: String,
    val dateTime: String,
    val author: String
) : ContentEntity {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }

    override fun getDateTime(): Pair<String, String> {
        return formatDateTimeHigh(dateTime)
    }
}