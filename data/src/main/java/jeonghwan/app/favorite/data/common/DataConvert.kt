package jeonghwan.app.favorite.data.common

import jeonghwan.app.favorite.datamodel.FavoriteData
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.MovieEntity
import java.time.LocalDateTime
import java.time.ZoneId

fun convertLocalDateTimeToLong(dateTime: LocalDateTime): Long {
    return try {
        dateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
    } catch (e: Exception) {
        // 변환에 실패하면 현재 시각을 타임스탬프로 변환하여 반환합니다.
        LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
    }
}

fun String.toLong(): Long {
    return convertLocalDateTimeToLong(LocalDateTime.now(ZoneId.systemDefault()))
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
        dateTime = dateTime.toLong()
    )
}


fun MovieDocumentModel.toEntity(): MovieEntity {
    return MovieEntity(
        title = title,
        playTime = playTime,
        thumbnail = thumbnail,
        url = url,
        dateTime = dateTime.toLong(),
        author = author
    )
}
