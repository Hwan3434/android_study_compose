package jeonghwan.app.favorite.di.impl

import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.entitymodel.ContentEntity
import jeonghwan.app.favorite.entitymodel.QueryEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ContentUseCaseImpl(
    private val imageRepository: ImageRepositoryInterface,
    private val movieRepository: MovieRepositoryInterface
) : ContentUseCaseInterface {
    override suspend fun getContent(query: QueryEntity): Result<List<ContentEntity>>  = coroutineScope {
        // getImage와 getMovie를 병렬 처리
        val imageDeferred = async { imageRepository.getImage(query) }
        val movieDeferred = async { movieRepository.getMovie(query) }

        // 각 호출의 결과를 await()로 받고, 실패한 경우 예외 처리
        val imageResult = imageDeferred.await()
        val movieResult = movieDeferred.await()

        // 두 결과 중 하나라도 실패하면 예외 메시지를 합쳐서 반환
        if (imageResult.isFailure || movieResult.isFailure) {
            val imageException = imageResult.exceptionOrNull()
            val movieException = movieResult.exceptionOrNull()
            val combinedMessage = when {
                imageException != null && movieException != null ->
                    "Image error: ${imageException.message}; Movie error: ${movieException.message}"
                imageException != null ->
                    "Image error: ${imageException.message}"
                movieException != null ->
                    "Movie error: ${movieException.message}"
                else -> "Unknown error"
            }
            return@coroutineScope Result.failure(Exception(combinedMessage))
        }

        // 두 결과 모두 성공적인 경우, 데이터를 합치고 dateTime 기준 내림차순 정렬
        val imageEntities = imageResult.getOrNull() ?: emptyList()
        val movieEntities = movieResult.getOrNull() ?: emptyList()
        val sortedList = (imageEntities + movieEntities).sortedByDescending { it.dateTime }
        return@coroutineScope Result.success(sortedList)
    }
}