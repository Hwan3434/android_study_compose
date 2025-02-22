package jeonghwan.app.favorite.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.common.nowFLong
import jeonghwan.app.favorite.common.DBCommon

/*
 * 즐겨찾기 데이터
 */
@Entity(tableName = DBCommon.DATABASE_FAVORITE_TABLE)
data class FavoriteData (
    @PrimaryKey(autoGenerate = false)
    val thumbnail: String,
    val dateTime: Long,
    val createAt: Long = nowFLong(),
)