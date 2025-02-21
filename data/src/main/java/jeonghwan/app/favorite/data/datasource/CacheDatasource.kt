package jeonghwan.app.favorite.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jeonghwan.app.favorite.core.DBCommon
import jeonghwan.app.favorite.datamodel.CacheData
import java.time.LocalDateTime


@Dao
interface CacheDatasource {
    // key와 현재 시간을 기준으로 유효한 캐시 조회 (존재하면 Return)
    @Query("""
        SELECT * FROM ${DBCommon.DATABASE_CACHE_TABLE} 
        WHERE `key` = :key AND expirationTime > :currentTime 
        LIMIT 1
    """)
    suspend fun getValidCache(key: String, currentTime: LocalDateTime): CacheData?


    // 캐시 저장 (동일 키가 있으면 교체)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cache: CacheData)

    // 현재 시간보다 만료된 캐시 데이터 삭제
    @Query("""
        DELETE FROM ${DBCommon.DATABASE_CACHE_TABLE} 
        WHERE expirationTime <= :currentTime
    """)
    suspend fun deleteExpired(currentTime: LocalDateTime)
}