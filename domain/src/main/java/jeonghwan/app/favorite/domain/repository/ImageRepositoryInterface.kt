package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.QueryEntity

interface ImageRepositoryInterface {
    suspend fun getImage(query: QueryEntity): Result<List<ImageEntity>>
}