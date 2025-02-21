package jeonghwan.app.favorite.data.impl.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jeonghwan.app.favorite.data.datasource.CacheDatasource
import jeonghwan.app.favorite.datamodel.CacheData
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import java.time.LocalDateTime

class CacheDataUtil<T : ContentEntity> {
    suspend fun fetchPagingData(
        key: String,
        currentTime: LocalDateTime,
        cacheDatasource: CacheDatasource,
        typeToken: TypeToken<PagingEntity<T>>,
        networkRequest: suspend () -> PagingEntity<T>
    ): Result<PagingEntity<T>> {
        // 만료된 캐시 삭제
        cacheDatasource.deleteExpired(currentTime)

        // 캐시 히스토리에서 유효 캐시 조회
        cacheDatasource.getValidCache(key, currentTime)?.let { cacheEntity ->
            try {
                val pagingEntity: PagingEntity<T> = Gson().fromJson(
                    cacheEntity.jsonData,
                    typeToken.type
                )
                return Result.success(pagingEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 네트워크에서 데이터 조회
        return try {
            val pagingEntity: PagingEntity<T> = networkRequest()
            val jsonResult = Gson().toJson(pagingEntity)
            cacheDatasource.insert(
                CacheData(
                    key = key,
                    jsonData = jsonResult
                )
            )
            Result.success(pagingEntity)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}