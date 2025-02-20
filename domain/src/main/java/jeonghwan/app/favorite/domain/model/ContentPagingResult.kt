package jeonghwan.app.favorite.domain.model

/**
 * Content 페이징 처리 결과
 * Image와 Movie의 페이징 처리 결과를 담는 클래스
 */
data class ContentPagingResult(
    val data: List<ContentEntity>,
    val isImageLastPage: Boolean = false,
    val isMovieLastPage: Boolean = false
)