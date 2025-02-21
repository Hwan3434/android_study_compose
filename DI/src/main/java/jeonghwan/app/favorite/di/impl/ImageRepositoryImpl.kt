package jeonghwan.app.favorite.di.impl

import com.google.gson.reflect.TypeToken
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.core.database.datasource.CacheDatasource
import jeonghwan.app.favorite.di.common.CacheDataUtil
import jeonghwan.app.favorite.di.common.toEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import java.time.LocalDateTime
import java.time.ZoneId

class ImageRepositoryImpl(
    private val kakaoDatasource: KakaoDatasource,
    private val cacheDatasource: CacheDatasource
) : ImageRepositoryInterface {

    // PagingQuery를 기반으로 캐시 키를 생성하는 함수
    private fun getCacheKey(query: PagingQuery): String {
        return "image_${query.query}_${query.sort}_${query.page}_${query.size}"
    }

    // 현재 시간을 반환하는 함수
    private fun getCurrentLocalDateTime(): LocalDateTime {
        return LocalDateTime.now(ZoneId.systemDefault())
    }

    override suspend fun getImage(query: PagingQuery): Result<PagingEntity<ImageEntity>> {
        val key = getCacheKey(query)
        val currentTime = getCurrentLocalDateTime()
        val util = CacheDataUtil<ImageEntity>()

        return util.fetchPagingData(
            key = key,
            currentTime = currentTime,
            cacheDatasource = cacheDatasource,
            typeToken = object : TypeToken<PagingEntity<ImageEntity>>() {},
            networkRequest = {
                val response = kakaoDatasource.requestImage(
                    query = query.query,
                    sort = query.sort.name,
                    page = query.page,
                    size = query.size
                )
                // KakaoModel<MovieDocumentModel>의 documents를 도메인 모델(MovieEntity)로 변환
                val movieEntities = response.documents.map { it.toEntity() }
                PagingEntity(
                    data = movieEntities,
                    isEnd = response.meta.isEnd
                )
            }
        )
    }
}
