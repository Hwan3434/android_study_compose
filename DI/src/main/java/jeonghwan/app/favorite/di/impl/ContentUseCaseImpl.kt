package jeonghwan.app.favorite.di.impl

import jeonghwan.app.favorite.domain.model.ContentPagingResult
import jeonghwan.app.favorite.domain.model.ContentQueryEntity
import jeonghwan.app.favorite.domain.model.PagingEntity
import jeonghwan.app.favorite.domain.model.PagingQuery
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ContentUseCaseImpl(
    private val imageRepository: ImageRepositoryInterface,
    private val movieRepository: MovieRepositoryInterface
) : ContentUseCaseInterface {
    override suspend fun getContent(query: ContentQueryEntity): Result<ContentPagingResult> = coroutineScope {
        val imageDeferred = async {
            if (query.isImageEnd) { // 마지막 페이지까지 조회한 경우 빈 리스트 반환
                Result.success(PagingEntity(data = emptyList(), isEnd = true))
            } else {
                imageRepository.getImage(
                    PagingQuery(
                        query = query.query,
                        sort = query.sort,
                        page = query.imagePage,
                        size = query.size
                    )
                )
            }
        }

        val movieDeferred = async {
            if (query.isMovieEnd) {
                Result.success(PagingEntity(data = emptyList(), isEnd = true))
            } else {
                movieRepository.getMovie(
                    PagingQuery(
                        query = query.query,
                        sort = query.sort,
                        page = query.moviePage,
                        size = query.size
                    )
                )
            }
        }

        // 호출 병렬 처리
        val imageResult = imageDeferred.await()
        val movieResult = movieDeferred.await()

        // 두 api 모두 실패한 경우만 실패 처리
        if (imageResult.isFailure && movieResult.isFailure) {
            val combinedMessage = "Image error: ${imageResult.exceptionOrNull()?.message}; " +
                    "Movie error: ${movieResult.exceptionOrNull()?.message}"
            return@coroutineScope Result.failure(Exception(combinedMessage))
        }

        // 하나씩 실패한 경우, 성공한 쪽만 사용
        val imageData = if (imageResult.isSuccess) imageResult.getOrNull()?.data ?: emptyList() else emptyList()
        val movieData = if (movieResult.isSuccess) movieResult.getOrNull()?.data ?: emptyList() else emptyList()

        // 결과 데이터를 결합하여 dateTime 기준 내림차순 정렬
        val sortedList = (imageData + movieData).sortedByDescending { it.dateTime }

        // 이미 종료였거나, 호출 성공시 라스트페이지 결과가 마지막이면 마지막 페이지를 true로 설정
        val isImageLastPage = if (query.isImageEnd) true else if (imageResult.isSuccess) imageResult.getOrNull()?.isEnd ?: false else false
        val isMovieLastPage = if (query.isMovieEnd) true else if (movieResult.isSuccess) movieResult.getOrNull()?.isEnd ?: false else false

        return@coroutineScope Result.success(
            ContentPagingResult(
                data = sortedList,
                isImageLastPage = isImageLastPage,
                isMovieLastPage = isMovieLastPage
            )
        )
    }
}