package jeonghwan.app.favorite.domain.usecase

import jeonghwan.app.favorite.entitymodel.ContentEntity
import jeonghwan.app.favorite.entitymodel.QueryEntity

interface ContentUseCaseInterface {
    suspend fun getContent(query: QueryEntity): Result<List<ContentEntity>>
}