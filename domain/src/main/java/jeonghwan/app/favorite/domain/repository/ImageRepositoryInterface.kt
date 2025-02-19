package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.entitymodel.ImageEntity
import jeonghwan.app.favorite.entitymodel.QueryEntity

interface ImageRepositoryInterface {
    suspend fun getImage(query: QueryEntity): List<ImageEntity>
}