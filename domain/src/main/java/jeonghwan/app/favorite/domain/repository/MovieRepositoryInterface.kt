package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.entitymodel.MovieEntity
import jeonghwan.app.favorite.entitymodel.QueryEntity

interface MovieRepositoryInterface {
    suspend fun getMovie(query: QueryEntity): Result<List<MovieEntity>>
}