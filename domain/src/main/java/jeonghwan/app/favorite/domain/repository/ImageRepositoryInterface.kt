package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery


interface ImageRepositoryInterface {
    suspend fun getImage(query: PagingQuery): Result<PagingEntity<ImageEntity>>
}