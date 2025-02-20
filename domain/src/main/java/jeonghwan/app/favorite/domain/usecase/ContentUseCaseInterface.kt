package jeonghwan.app.favorite.domain.usecase

import jeonghwan.app.favorite.domain.model.ContentPagingResult
import jeonghwan.app.favorite.domain.model.ContentQueryEntity

interface ContentUseCaseInterface {
    suspend fun getContent(query: ContentQueryEntity): Result<ContentPagingResult>
}