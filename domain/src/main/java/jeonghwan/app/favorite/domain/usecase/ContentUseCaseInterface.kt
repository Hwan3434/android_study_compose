package jeonghwan.app.favorite.domain.usecase

import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.QueryEntity

interface ContentUseCaseInterface {
    suspend fun getContent(query: QueryEntity): Result<List<ContentEntity>>
}