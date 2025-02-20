package jeonghwan.app.favorite.domain.model

/**
 * image, movie 페이징시 공통 쿼리 모델
 */
data class PagingQuery(
    val query: String, // 검색어
    val sort: QuerySort = QuerySort.ACCURACY, // 정렬 방식
    val page: Int, // 이미지 페이지 번호
    val size: Int, // 한 페이지에 보여줄 개수
)