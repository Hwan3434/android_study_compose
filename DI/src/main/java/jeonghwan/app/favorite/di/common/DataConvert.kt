package jeonghwan.app.favorite.di.common

import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.MovieEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDateTime2(): LocalDateTime {
    return Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
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
        dateTime = dateTime.toLocalDateTime2()
    )
}


fun MovieDocumentModel.toEntity(): MovieEntity {
    return MovieEntity(
        title = title,
        playTime = playTime,
        thumbnail = thumbnail,
        url = url,
        dateTime = dateTime.toLocalDateTime2(),
        author = author
    )
}
