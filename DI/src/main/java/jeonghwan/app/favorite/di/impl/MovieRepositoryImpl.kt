package jeonghwan.app.favorite.di.impl

import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.di.common.toEntity
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.model.QueryEntity

class MovieRepositoryImpl(
    private val kakaoDatasource: KakaoDatasource
) : MovieRepositoryInterface {
    override suspend fun getMovie(query: QueryEntity): Result<List<MovieEntity>> {
        return try {
            val response = kakaoDatasource.requestMovie(
                query = query.query,
                sort = query.sort.name,
                page = query.page,
                size = query.size
            )
            // KakaoModel<MovieDocumentModel>의 documents를 도메인 모델로 변환
            val movieEntities = response.documents.map { it.toEntity() }
            Result.success(movieEntities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}