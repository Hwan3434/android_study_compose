package jeonghwan.app.favorite.di.impl

import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.di.common.toEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface

class ImageRepositoryImpl(
    private val kakaoDatasource: KakaoDatasource
) : ImageRepositoryInterface {
    override suspend fun getImage(query: PagingQuery): Result<PagingEntity<ImageEntity>> {
        return try {
            val response = kakaoDatasource.requestImage(
                query = query.query,
                sort = query.sort.name,
                page = query.page,
                size = query.size
            )

            // KakaoModel<ImageDocumentModel>의 documents를 도메인 모델로 변환
            val imageEntities = response.documents.map { it.toEntity() }
            Result.success(PagingEntity(
                data = imageEntities,
                isEnd = response.meta.isEnd
            ))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
