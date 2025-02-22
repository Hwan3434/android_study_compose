package jeonghwan.app.favorite.data.common

import jeonghwan.app.favorite.common.toFLong
import jeonghwan.app.favorite.datamodel.FavoriteData
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.MovieEntity

fun FavoriteData.toEntity(): FavoriteEntity {
    return FavoriteEntity(
        thumbnail = this.thumbnail,
        dateTime = this.dateTime
    )
}

fun ImageDocumentModel.toEntity(): ImageEntity {
    return ImageEntity(
        collection = collection,
        thumbnail = thumbnailUrl,
        imageUrl = imageUrl,
        width = width,
        height = height,
        displaySiteName = displaySiteName,
        docUrl = docUrl,
        dateTime = dateTime.toFLong()
    )
}


fun MovieDocumentModel.toEntity(): MovieEntity {
    return MovieEntity(
        title = title,
        playTime = playTime,
        thumbnail = thumbnail,
        url = url,
        dateTime = dateTime.toFLong(),
        author = author
    )
}
