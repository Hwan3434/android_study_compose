package jeonghwan.app.favorite.di.common

import jeonghwan.app.favorite.core.database.emtity.FavoriteData
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.MovieEntity
import java.time.LocalDateTime
import java.time.ZoneId

fun String.toKotlinLocalDateTime(): LocalDateTime {
    return LocalDateTime.now(ZoneId.systemDefault())
}

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
        dateTime = dateTime.toKotlinLocalDateTime()
    )
}


fun MovieDocumentModel.toEntity(): MovieEntity {
    return MovieEntity(
        title = title,
        playTime = playTime,
        thumbnail = thumbnail,
        url = url,
        dateTime = dateTime.toKotlinLocalDateTime(),
        author = author
    )
}
