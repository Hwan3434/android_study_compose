package jeonghwan.app.favorite.data.impl.repository

import com.google.gson.reflect.TypeToken
import jeonghwan.app.favorite.common.nowFLocalDateTime
import jeonghwan.app.favorite.data.datasource.CacheDatasource
import jeonghwan.app.favorite.data.common.toEntity
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface

class MovieRepositoryImpl(
    private val kakaoDatasource: KakaoDatasource,
    private val cacheDatasource: CacheDatasource
) : MovieRepositoryInterface {

    // PagingQuery를 기반으로 캐시 키를 생성하는 함수
    private fun getCacheKey(query: PagingQuery): String {
        return "movie_${query.query}_${query.sort}_${query.page}_${query.size}"
    }

    override suspend fun getMovie(query: PagingQuery): Result<PagingEntity<MovieEntity>> {
        val key = getCacheKey(query)
        val currentTime = nowFLocalDateTime()
        val util = CacheDataUtil<MovieEntity>()

        return util.fetchPagingData(
            key = key,
            currentTime = currentTime,
            cacheDatasource = cacheDatasource,
            typeToken = object : TypeToken<PagingEntity<MovieEntity>>() {},
            networkRequest = {
                val response = kakaoDatasource.requestMovie(
                    query = query.query,
                    sort = query.sort.name,
                    page = query.page,
                    size = query.size
                )
                // KakaoModel<MovieDocumentModel>의 documents를 도메인 모델(MovieEntity)로 변환
                val movieEntities = response.documents.map {
                    it.toEntity()
                }
                PagingEntity(
                    data = movieEntities,
                    isEnd = response.meta.isEnd
                )
            }
        )
    }
}