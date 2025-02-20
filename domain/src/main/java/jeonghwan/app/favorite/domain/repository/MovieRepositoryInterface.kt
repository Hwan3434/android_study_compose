package jeonghwan.app.favorite.domain.repository

import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery

interface MovieRepositoryInterface {
    suspend fun getMovie(query: PagingQuery): Result<PagingEntity<MovieEntity>>
}