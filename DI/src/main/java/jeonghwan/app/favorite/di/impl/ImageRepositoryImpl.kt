package jeonghwan.app.favorite.di.impl

import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.di.common.toEntity
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.QueryEntity

class ImageRepositoryImpl(
    private val kakaoDatasource: KakaoDatasource
) : ImageRepositoryInterface {
    override suspend fun getImage(query: QueryEntity): Result<List<ImageEntity>> {
        return try {
            val response = kakaoDatasource.requestImage(
                query = query.query,
                sort = query.sort.name,
                page = query.page,
                size = query.size
            )
            // KakaoModel<ImageDocumentModel>의 documents를 도메인 모델로 변환
            val imageEntities = response.documents.map { it.toEntity() }
            Result.success(imageEntities)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
