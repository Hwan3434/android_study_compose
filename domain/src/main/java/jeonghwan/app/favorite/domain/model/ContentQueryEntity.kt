package jeonghwan.app.favorite.domain.model

/**
 * Content Usecase 에서 사용하는 쿼리 정보
 */
data class ContentQueryEntity(
    val query: String, // 검색어
    val sort: QuerySort = QuerySort.ACCURACY, // 정렬 방식
    val imagePage: Int, // 이미지 페이지 번호
    val moviePage: Int, // 동영상 페이지 번호
    val size: Int, // 한 페이지에 보여줄 개수
    val isImageEnd: Boolean = false, // 이미지 검색 완료 상태
    val isMovieEnd: Boolean = false // 동영상 검색 완료 상태
)
