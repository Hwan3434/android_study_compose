package jeonghwan.app.favorite.domain.model

/**
 * image, movie 페이징 처리 공통 결과 모델
 */
data class PagingEntity<T : ContentEntity> (
    var data: List<T> = emptyList(),
    var isEnd: Boolean = false
)