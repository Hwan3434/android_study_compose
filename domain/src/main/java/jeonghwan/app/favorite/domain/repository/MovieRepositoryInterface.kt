package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.model.QueryEntity

interface MovieRepositoryInterface {
    suspend fun getMovie(query: QueryEntity): Result<List<MovieEntity>>
}