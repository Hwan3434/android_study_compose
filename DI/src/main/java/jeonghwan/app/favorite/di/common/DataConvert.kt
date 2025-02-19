package jeonghwan.app.favorite.di.common

import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.entitymodel.ImageEntity
import jeonghwan.app.favorite.entitymodel.MovieEntity
import kotlinx.datetime.toLocalDateTime

fun ImageDocumentModel.toEntity(): ImageEntity {
    return ImageEntity(
        collection = collection,
        thumbnail = thumbnailUrl,
        imageUrl = imageUrl,
        width = width,
        height = height,
        displaySiteName = displaySiteName,
        docUrl = docUrl,
        dateTime = dateTime.toLocalDateTime()
    )
}


fun MovieDocumentModel.toEntity(): MovieEntity {
    return MovieEntity(
        title = title,
        playTime = playTime,
        thumbnail = thumbnail,
        url = url,
        dateTime = dateTime.toLocalDateTime(),
        author = author
    )
}
