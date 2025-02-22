package jeonghwan.app.favorite.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.common.createExpirationLocalDateTime
import jeonghwan.app.favorite.common.DBCommon
import jeonghwan.app.favorite.common.DBCommon.Companion.CACHE_EXPIRATION_MINUTES
import java.time.LocalDateTime

/**
 * 네트워크 검색 결과 캐시데이터
 */
@Entity(tableName = DBCommon.DATABASE_CACHE_TABLE)
data class CacheData (
    @PrimaryKey val key: String,
    val jsonData: String,
    val expirationTime: LocalDateTime = createExpirationLocalDateTime(CACHE_EXPIRATION_MINUTES),
)
