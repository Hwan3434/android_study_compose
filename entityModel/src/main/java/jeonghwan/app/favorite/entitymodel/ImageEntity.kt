package jeonghwan.app.favorite.entitymodel

data class ImageEntity(
    val collection: String,
    val thumbnail: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    val dateTime: String
) : ContentEntity {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }

    override fun getDateTime(): Pair<String, String> {
        return formatDateTimeHigh(dateTime)
    }
}
